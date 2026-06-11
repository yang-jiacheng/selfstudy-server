package com.lxy.common.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 高性能 Bean 属性拷贝工具。
 * <p>
 * 核心优化策略：
 * <ol>
 * <li>使用 CopyPlan 预编译 getter/setter 映射，避免运行时反射开销</li>
 * <li>使用 MethodHandle 替代 Method.invoke，提高执行性能</li>
 * <li>使用缓存机制避免重复 Bean 内省</li>
 * <li>支持 primitive 与 wrapper 自动兼容</li>
 * <li>通过 ignoreProperties 实现灵活字段控制</li>
 * <li>构建了类似轻量 MapStruct 的运行时映射引擎</li>
 * <li>注意：本工具执行浅拷贝，引用类型属性与源对象共享同一实例。</li>
 * </ol>
 * 兼容 JDK 8 ~ 21。
 * </p>
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2026/4/15 19:31
 */

public final class BeanUtil {

    /**
     * (sourceClass, targetClass) → CopyPlan，Bean→Bean 拷贝缓存
     */
    private static final Map<CopyKey, CopyPlan> PLAN_CACHE = new ConcurrentHashMap<>();

    /**
     * targetClass → MapCopyPlan，Map→Bean 拷贝缓存；Map 的 key 对应目标 Bean 属性名
     */
    private static final Map<Class<?>, MapCopyPlan> MAP_PLAN_CACHE = new ConcurrentHashMap<>();

    /**
     * convertValue 无法转换时的哨兵返回值，用于与 null 区分（null 本身是合法赋值结果）
     */
    private static final Object UNCONVERTIBLE = new Object();

    // ======================== 缓存 ========================
    /**
     * targetClass → MethodHandle(构造器)
     */
    private static final Map<Class<?>, MethodHandle> CTOR_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER;

    // ======================== 基本类型 ↔ 包装类型映射 ========================

    static {
        Map<Class<?>, Class<?>> m = new HashMap<>(16);
        m.put(boolean.class, Boolean.class);
        m.put(byte.class, Byte.class);
        m.put(char.class, Character.class);
        m.put(short.class, Short.class);
        m.put(int.class, Integer.class);
        m.put(long.class, Long.class);
        m.put(float.class, Float.class);
        m.put(double.class, Double.class);
        PRIMITIVE_TO_WRAPPER = Collections.unmodifiableMap(m);
    }

    private BeanUtil() {}

    // ======================== Public API ========================

    /**
     * 按照源对象属性创建目标 Class 实例并拷贝属性值，可选忽略指定属性。
     *
     * @param source 源 Bean 对象
     * @param tClass 目标 Class
     * @param ignoreProperties 不拷贝的属性名列表
     * @param <T> 目标类型
     * @return 目标对象实例；source 为 null 时返回 null
     */
    public static <T> T copyProperties(Object source, Class<T> tClass, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        T target = newInstance(tClass);
        CopyPlan plan = resolvePlan(source.getClass(), tClass);
        plan.execute(source, target, ignoreProperties);
        return target;
    }

    /**
     * 将源 Bean 集合批量转换为目标 Class 集合，可选忽略指定属性。
     *
     * @param sourceList 源 Bean 集合
     * @param tClass 目标 Class
     * @param ignoreProperties 不拷贝的属性名列表
     * @param <T> 目标类型
     * @return 目标对象列表；sourceList 为空时返回空列表
     */
    public static <T> List<T> copyToList(Collection<?> sourceList, Class<T> tClass, String... ignoreProperties) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(sourceList.size());
        CopyPlan plan = null;
        Class<?> lastSourceClass = null;
        for (Object source : sourceList) {
            if (source == null) {
                continue;
            }
            Class<?> srcClass = source.getClass();
            if (srcClass != lastSourceClass) {
                plan = resolvePlan(srcClass, tClass);
                lastSourceClass = srcClass;
            }
            T target = newInstance(tClass);
            plan.execute(source, target, ignoreProperties);
            result.add(target);
        }
        return result;
    }

    /**
     * 将 Map 转换为目标 Class 实例，Map 的 key 与目标 Bean 的属性名对应，可选忽略指定属性。
     * <p>
     * 类型不兼容或 Map 中 key 在目标 Bean 无对应属性时，该字段静默跳过，不抛出异常。 null 值仅在目标属性为非基本类型时赋值，基本类型属性保持默认值。
     * </p>
     *
     * @param map 源 Map，key 为属性名，value 为属性值
     * @param tClass 目标 Bean Class
     * @param ignoreProperties 不拷贝的属性名列表
     * @param <T> 目标类型
     * @return 目标对象实例；map 为 null 时返回 null
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> tClass, String... ignoreProperties) {
        if (map == null) {
            return null;
        }
        T target = newInstance(tClass);
        // 复用缓存计划，避免重复内省目标类
        MapCopyPlan plan = resolveMapPlan(tClass);
        plan.execute(map, target, ignoreProperties);
        return target;
    }

    /**
     * 将 Map 集合批量转换为目标 Class 列表，Map 的 key 与目标 Bean 的属性名对应，可选忽略指定属性。
     * <p>
     * 集合中的 null 元素会被跳过。
     * </p>
     *
     * @param mapList 源 Map 集合
     * @param tClass 目标 Bean Class
     * @param ignoreProperties 不拷贝的属性名列表
     * @param <T> 目标类型
     * @return 目标对象列表；mapList 为空时返回空列表
     */
    public static <T> List<T> mapToList(Collection<Map<String, Object>> mapList, Class<T> tClass,
        String... ignoreProperties) {
        if (mapList == null || mapList.isEmpty()) {
            return Collections.emptyList();
        }
        // 目标类结构固定，同一 tClass 复用同一 MapCopyPlan
        MapCopyPlan plan = resolveMapPlan(tClass);
        List<T> result = new ArrayList<>(mapList.size());
        for (Map<String, Object> map : mapList) {
            if (map == null) {
                continue;
            }
            T target = newInstance(tClass);
            plan.execute(map, target, ignoreProperties);
            result.add(target);
        }
        return result;
    }

    // ======================== CopyPlan 解析 ========================

    private static CopyPlan resolvePlan(Class<?> sourceClass, Class<?> targetClass) {
        CopyKey key = new CopyKey(sourceClass, targetClass);
        CopyPlan plan = PLAN_CACHE.get(key);
        if (plan != null) {
            return plan;
        }
        plan = buildPlan(sourceClass, targetClass);
        PLAN_CACHE.put(key, plan);
        return plan;
    }

    private static CopyPlan buildPlan(Class<?> sourceClass, Class<?> targetClass) {
        try {
            PropertyDescriptor[] sourcePds = Introspector.getBeanInfo(sourceClass).getPropertyDescriptors();
            PropertyDescriptor[] targetPds = Introspector.getBeanInfo(targetClass).getPropertyDescriptors();

            Map<String, Method> sourceGetters = new HashMap<>(sourcePds.length);
            for (PropertyDescriptor pd : sourcePds) {
                Method readMethod = pd.getReadMethod();
                if (readMethod != null && !"class".equals(pd.getName())) {
                    sourceGetters.put(pd.getName(), readMethod);
                }
            }

            MethodHandles.Lookup lookup = MethodHandles.lookup();
            List<PropPair> pairs = new ArrayList<>();

            for (PropertyDescriptor targetPd : targetPds) {
                String name = targetPd.getName();
                if ("class".equals(name)) {
                    continue;
                }
                Method writeMethod = targetPd.getWriteMethod();
                if (writeMethod == null) {
                    continue;
                }
                Method readMethod = sourceGetters.get(name);
                if (readMethod == null) {
                    continue;
                }
                Class<?> sourceType = readMethod.getReturnType();
                Class<?> targetType = writeMethod.getParameterTypes()[0];
                if (!isTypeAssignable(targetType, sourceType)) {
                    continue;
                }

                readMethod.setAccessible(true);
                writeMethod.setAccessible(true);

                MethodHandle getter = lookup.unreflect(readMethod);
                MethodHandle setter = lookup.unreflect(writeMethod);
                pairs.add(new PropPair(name, getter, setter));
            }

            Introspector.flushFromCaches(sourceClass);
            Introspector.flushFromCaches(targetClass);

            return new CopyPlan(pairs.toArray(new PropPair[0]));
        } catch (Exception e) {
            throw new BeanCopyException(
                "Failed to build CopyPlan: " + sourceClass.getName() + " -> " + targetClass.getName(), e);
        }
    }

    // ======================== MapCopyPlan 解析 ========================

    private static MapCopyPlan resolveMapPlan(Class<?> targetClass) {
        MapCopyPlan plan = MAP_PLAN_CACHE.get(targetClass);
        if (plan != null) {
            return plan;
        }
        plan = buildMapPlan(targetClass);
        MAP_PLAN_CACHE.put(targetClass, plan);
        return plan;
    }

    private static MapCopyPlan buildMapPlan(Class<?> targetClass) {
        try {
            PropertyDescriptor[] targetPds = Introspector.getBeanInfo(targetClass).getPropertyDescriptors();
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            // 预分配合理容量，减少 rehash
            Map<String, MethodHandle> setters = new HashMap<>(targetPds.length * 2);
            Map<String, Class<?>> setterTypes = new HashMap<>(targetPds.length * 2);
            for (PropertyDescriptor pd : targetPds) {
                String name = pd.getName();
                if ("class".equals(name)) {
                    continue;
                }
                Method writeMethod = pd.getWriteMethod();
                if (writeMethod == null) {
                    // 只读属性，跳过
                    continue;
                }
                writeMethod.setAccessible(true);
                setters.put(name, lookup.unreflect(writeMethod));
                setterTypes.put(name, writeMethod.getParameterTypes()[0]);
            }
            Introspector.flushFromCaches(targetClass);
            return new MapCopyPlan(setters, setterTypes);
        } catch (Exception e) {
            throw new BeanCopyException("Failed to build MapCopyPlan: " + targetClass.getName(), e);
        }
    }

    // ======================== 类型兼容判断 ========================

    private static boolean isTypeAssignable(Class<?> target, Class<?> source) {
        if (target.isAssignableFrom(source)) {
            return true;
        }
        Class<?> targetWrapped = target.isPrimitive() ? PRIMITIVE_TO_WRAPPER.get(target) : target;
        Class<?> sourceWrapped = source.isPrimitive() ? PRIMITIVE_TO_WRAPPER.get(source) : source;
        return targetWrapped != null && sourceWrapped != null && targetWrapped.isAssignableFrom(sourceWrapped);
    }

    /**
     * Map→Bean 场景的常见类型转换。
     * <p>
     * 支持的转换方向：
     * <ul>
     * <li>任意类型 → String：Timestamp/Date/LocalDateTime/LocalDate 格式化，BigDecimal 使用 toPlainString，其余 Number 调
     * toString</li>
     * <li>String → BigDecimal / Integer / Long / Double / Boolean / LocalDateTime / LocalDate</li>
     * <li>Number 互转：BigInteger/Integer/Long/Short/Byte/Float/Double/BigDecimal 之间宽化或窄化</li>
     * <li>Timestamp/Date → LocalDateTime</li>
     * </ul>
     * 若无已知转换路径，返回 {@link #UNCONVERTIBLE} 哨兵，调用方应静默跳过。
     * </p>
     *
     * @param value 源值，非 null
     * @param targetType 目标属性类型
     * @return 转换后的值，或 {@link #UNCONVERTIBLE} 表示无法转换
     */
    private static Object convertValue(Object value, Class<?> targetType) {
        // ── 任意类型 → String（导出 VO 字段为 String 的典型场景）──
        if (targetType == String.class) {
            // Timestamp 是 Date 的子类，必须先判断，否则会走 Date 分支丢失毫秒精度
            if (value instanceof Timestamp) {
                return ((Timestamp)value).toLocalDateTime().format(DateCusUtil.DATE_TIME_FORMATTER);
            }
            if (value instanceof Date) {
                // java.util.Date（含 java.sql.Date）统一转 LocalDateTime 再格式化
                return LocalDateTime.ofInstant(((Date)value).toInstant(), DateCusUtil.SYSTEM_ZONE)
                    .format(DateCusUtil.DATE_TIME_FORMATTER);
            }
            if (value instanceof LocalDateTime) {
                return ((LocalDateTime)value).format(DateCusUtil.DATE_TIME_FORMATTER);
            }
            if (value instanceof LocalDate) {
                return ((LocalDate)value).format(DateCusUtil.DATE_FORMATTER);
            }
            if (value instanceof BigDecimal) {
                // toPlainString 避免科学计数法，如 1.2E+7 → "12000000"
                return ((BigDecimal)value).toPlainString();
            }
            if (value instanceof Number || value instanceof String || value instanceof Boolean) {
                return value.toString();
            }
            return UNCONVERTIBLE;
        }

        // ── String → 常见目标类型 ──
        if (value instanceof String str) {
            // 空字符串不做转换，避免 NumberFormatException 或歧义
            if (str.isEmpty()) {
                return UNCONVERTIBLE;
            }
            try {
                if (targetType == BigDecimal.class) {
                    return new BigDecimal(str);
                }
                if (targetType == Long.class || targetType == long.class) {
                    return Long.valueOf(str);
                }
                if (targetType == Integer.class || targetType == int.class) {
                    return Integer.valueOf(str);
                }
                if (targetType == Double.class || targetType == double.class) {
                    return Double.valueOf(str);
                }
                if (targetType == Boolean.class || targetType == boolean.class) {
                    return Boolean.valueOf(str);
                }
                if (targetType == LocalDateTime.class) {
                    return LocalDateTime.parse(str, DateCusUtil.DATE_TIME_FORMATTER);
                }
                if (targetType == LocalDate.class) {
                    return LocalDate.parse(str, DateCusUtil.DATE_FORMATTER);
                }
            } catch (Exception e) {
                // 字符串格式与目标类型不匹配（如 "abc" → Integer），静默跳过
                return UNCONVERTIBLE;
            }
            return UNCONVERTIBLE;
        }

        // ── Number 互转（BigInteger/BigDecimal/Integer/Long/Short/Byte/Float/Double）──
        if (value instanceof Number num) {
            if (targetType == Long.class || targetType == long.class) {
                return num.longValue();
            }
            if (targetType == Integer.class || targetType == int.class) {
                return num.intValue();
            }
            if (targetType == Double.class || targetType == double.class) {
                return num.doubleValue();
            }
            if (targetType == Float.class || targetType == float.class) {
                return num.floatValue();
            }
            if (targetType == Short.class || targetType == short.class) {
                return num.shortValue();
            }
            if (targetType == Byte.class || targetType == byte.class) {
                return num.byteValue();
            }
            if (targetType == BigDecimal.class) {
                // BigInteger 有专用构造器，精度更准确
                if (value instanceof BigInteger) {
                    return new BigDecimal((BigInteger)value);
                }
                return new BigDecimal(num.toString());
            }
            if (targetType == BigInteger.class) {
                if (value instanceof BigDecimal) {
                    // toBigInteger 直接截断小数部分
                    return ((BigDecimal)value).toBigInteger();
                }
                return BigInteger.valueOf(num.longValue());
            }
        }

        // ── Timestamp/Date → LocalDateTime ──
        if (targetType == LocalDateTime.class) {
            // Timestamp 优先（子类先判断）
            if (value instanceof Timestamp) {
                return ((Timestamp)value).toLocalDateTime();
            }
            if (value instanceof Date) {
                return LocalDateTime.ofInstant(((Date)value).toInstant(), DateCusUtil.SYSTEM_ZONE);
            }
        }

        // 无已知转换路径
        return UNCONVERTIBLE;
    }

    // ======================== 构造器 ========================

    private static <T> T newInstance(Class<T> clazz) {
        try {
            MethodHandle ctor = CTOR_CACHE.get(clazz);
            if (ctor == null) {
                ctor = resolveConstructor(clazz);
                CTOR_CACHE.put(clazz, ctor);
            }
            return (T)ctor.invoke();
        } catch (Throwable e) {
            throw new BeanCopyException("Cannot instantiate " + clazz.getName(), e);
        }
    }

    private static MethodHandle resolveConstructor(Class<?> clazz) {
        try {
            Constructor<?> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            return MethodHandles.lookup().unreflectConstructor(ctor);
        } catch (Exception e) {
            throw new BeanCopyException("No accessible default constructor: " + clazz.getName(), e);
        }
    }

    // ======================== 内部类 ========================

    private static Set<String> toSet(String[] array) {
        Set<String> set = new HashSet<>(array.length * 2);
        set.addAll(Arrays.asList(array));
        return set;
    }

    /**
     * Map→Bean 预编译拷贝计划，存储目标 Bean 所有可写属性的 setter MethodHandle 及参数类型。
     * <p>
     * 通过属性名 key 匹配 Map 中的 value，类型不兼容或 key 无对应属性时静默跳过。
     * </p>
     *
     * @param setters     属性名 → setter MethodHandle
     * @param setterTypes 属性名 → setter 参数类型，用于执行前类型兼容判断
     */
        private record MapCopyPlan(Map<String, MethodHandle> setters, Map<String, Class<?>> setterTypes) {

        void execute(Map<String, Object> source, Object target, String... ignoreProperties) {
                try {
                    Set<String> ignoreSet = (ignoreProperties != null && ignoreProperties.length > 0)
                            ? toSet(ignoreProperties) : Collections.emptySet();
                    for (Map.Entry<String, Object> entry : source.entrySet()) {
                        String key = entry.getKey();
                        // 忽略属性列表中的 key 直接跳过
                        if (ignoreSet.contains(key)) {
                            continue;
                        }
                        MethodHandle setter = setters.get(key);
                        if (setter == null) {
                            // 目标 Bean 中无此属性，跳过
                            continue;
                        }
                        Object value = entry.getValue();
                        Class<?> targetType = setterTypes.get(key);
                        if (value == null) {
                            // 基本类型不能接受 null，跳过；引用类型正常赋 null
                            if (!targetType.isPrimitive()) {
                                setter.invoke(target, (Object) null);
                            }
                            continue;
                        }
                        if (isTypeAssignable(targetType, value.getClass())) {
                            // 类型直接兼容，无需转换
                            setter.invoke(target, value);
                        } else {
                            // 类型不直接兼容，尝试 convertValue 做常见隐式转换（如 Timestamp→String、BigInteger→Long）
                            Object converted = convertValue(value, targetType);
                            if (converted != UNCONVERTIBLE) {
                                setter.invoke(target, converted);
                            }
                            // converted == UNCONVERTIBLE 说明无已知转换路径，静默跳过
                        }
                    }
                } catch (Throwable e) {
                    throw new BeanCopyException("MapCopyPlan execution failed", e);
                }
            }
        }

    /**
         * 预编译的属性拷贝计划，包含所有匹配的 getter-setter 对。
         */
        private record CopyPlan(PropPair[] pairs) {

        void execute(Object source, Object target, String... ignoreProperties) {
                try {
                    if (ignoreProperties == null || ignoreProperties.length == 0) {
                        for (PropPair pair : pairs) {
                            Object value = pair.getter.invoke(source);
                            pair.setter.invoke(target, value);
                        }
                    } else {
                        Set<String> ignoreSet = toSet(ignoreProperties);
                        for (PropPair pair : pairs) {
                            if (ignoreSet.contains(pair.name)) {
                                continue;
                            }
                            Object value = pair.getter.invoke(source);
                            pair.setter.invoke(target, value);
                        }
                    }
                } catch (Throwable e) {
                    throw new BeanCopyException("CopyPlan execution failed", e);
                }
            }
        }

    /**
         * 单个属性映射：源 getter → 目标 setter。
         */
        private record PropPair(String name, MethodHandle getter, MethodHandle setter) {
    }

    /**
         * CopyPlan 缓存 Key，基于 (sourceClass, targetClass) 组合。
         */
        private record CopyKey(Class<?> sourceClass, Class<?> targetClass) {

        @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof CopyKey that)) {
                    return false;
                }
                return sourceClass == that.sourceClass && targetClass == that.targetClass;
            }

    }

    // ======================== 工具方法 ========================

    /**
     * BeanUtil 专用运行时异常。
     */
    public static class BeanCopyException extends RuntimeException {
        public BeanCopyException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
