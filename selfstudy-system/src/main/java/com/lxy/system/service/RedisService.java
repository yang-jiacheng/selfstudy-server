package com.lxy.system.service;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Redis操作
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/03/06 14:55
 */

@Slf4j
@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 扫描 Redis 中匹配的 key
     *
     * @param pattern 通配符，如 "user:*"
     * @param count   每次扫描数量建议值，非限制
     * @return 所有匹配的 key 列表
     */
    public Set<String> scanKeys(String pattern, Integer count) {
        Set<String> keys = new HashSet<>();

        redisTemplate.execute((RedisCallback<Void>) connection -> {
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

            // 构建 ScanOptions，只在 count 不为空时设置
            ScanOptions.ScanOptionsBuilder builder = ScanOptions.scanOptions().match(pattern);
            if (count != null) {
                builder.count(count);
            }
            ScanOptions options = builder.build();

            try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
                while (cursor.hasNext()) {
                    String key = stringRedisSerializer.deserialize(cursor.next());
                    if (key != null) {
                        keys.add(key);
                    }
                }
            } catch (Exception e) {
                log.error("scanKeys 异常: pattern={}, count={}", pattern, count, e);
            }

            return null;
        });

        return keys;
    }


    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 缓存基本的对象
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  超时时间 -1表示永久
     * @param timeUnit 超时时间单位
     * @author jiacheng yang.
     * @since 2025/03/06 14:58
     */
    public <T> void setObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        if (timeout == null || timeout <= 0) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        }
        log.info("缓存对象成功, key:{}, value:{}", key, value);
    }

    /**
     * 缓存List类型数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     */
    public <T> void setList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        log.info("缓存List类型数据成功, key:{}", key);
    }

    /**
     * 存入Hash数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setHashValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
        log.info("缓存Hash类型数据成功, key:{}, hKey:{}, value:{}", key, hKey, value);
    }

    @SuppressWarnings("all")
    public <T> void setObjectBatch(final Map<String, T> map, final Long timeout, final TimeUnit timeUnit) {
        int batchSize = 1000; // 每批次处理 1000 个 Key
        List<String> keys = new ArrayList<>(map.keySet());
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

        for (int i = 0; i < keys.size(); i += batchSize) {
            List<String> batchKeys = keys.subList(i, Math.min(i + batchSize, keys.size()));
            Map<String, T> batchMap = batchKeys.stream()
                    .collect(Collectors.toMap(k -> k, map::get));

            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (Map.Entry<String, T> entry : batchMap.entrySet()) {
                    byte[] keyBytes = keySerializer.serialize(entry.getKey());
                    byte[] valueBytes = valueSerializer.serialize(entry.getValue());
                    connection.set(keyBytes, valueBytes);
                    if (timeout != null && timeout > 0) {
                        long timeoutSeconds = timeUnit.toSeconds(timeout);
                        connection.expire(keyBytes, timeoutSeconds);
                    }
                }
                return null;
            });
        }
        log.info(" 批量缓存对象成功");
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key, final TimeUnit timeUnit) {
        return Optional.of(redisTemplate.getExpire(key, timeUnit)).orElse(0L);
    }

    /**
     * 获取缓存的基本对象
     */
    public <T> T getObject(final String key, Class<T> clazz) {
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        Object value = operation.get(key);
        return clazz.cast(value);
    }


    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(final String key) {
        return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 获取Hash数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getHashValue(final String key, final String hKey, Class<T> clazz) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return clazz.cast(opsForHash.get(key, hKey));
    }

    /**
     * 获取多个Hash数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getHashValueBatch(final String key, final Collection<String> hKeys, Class<T> clazz) {
        List<Object> rawList = redisTemplate.opsForHash().multiGet(key, new ArrayList<>(hKeys));
        return rawList.stream()
                .filter(Objects::nonNull)  // 过滤掉 null 值，避免转换错误
                .map(clazz::cast)           // 强制转换为 T 类型
                .collect(Collectors.toList());
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存的键值
     */
    public void deleteKey(final String key) {
        this.redisTemplate.delete(key);
    }

    /**
     * 删除多个对象
     *
     * @param keys 缓存的键值
     */
    public void deleteKeys(final Collection<String> keys) {
        this.redisTemplate.delete(keys);
    }


    /**
     * Redisson 分布式锁
     *
     * @param lockKey   锁的 key
     * @param waitTime  尝试获取锁的最大等待时间
     * @param leaseTime 锁自动释放时间
     * @param timeUnit  时间单位
     * @param action    要执行的业务逻辑
     * @param <T>       返回类型
     * @return 执行结果
     */
    public <T> T tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit, Supplier<T> action) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (isLocked) {
                return action.get();
            } else {
                log.error("系统繁忙，请稍后重试");
                return null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("操作被中断", e);
        } catch (Exception e) {
            log.error("操作失败", e);
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }

    /**
     * 添加元素到有序集合
     *
     * @param key   缓存的键值
     * @param value 要添加的值
     * @param score 分数
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public void addToSortedSet(final String key, final Object value, final double score) {
        redisTemplate.opsForZSet().add(key, value, score);
        log.info("添加元素到有序集合成功, key:{}, value:{}, score:{}", key, value, score);
    }

    /**
     * 批量添加元素到有序集合
     *
     * @param key    缓存的键值
     * @param tuples 元素和分数的集合
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public void addToSortedSetBatch(final String key, final Set<ZSetOperations.TypedTuple<Object>> tuples) {
        redisTemplate.opsForZSet().add(key, tuples);
        log.info("批量添加元素到有序集合成功, key:{}, size:{}", key, tuples.size());
    }

    /**
     * 获取有序集合指定范围的元素（按分数从低到高）
     *
     * @param key   缓存的键值
     * @param start 开始位置
     * @param end   结束位置
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getSortedSetRange(final String key, final long start, final long end) {
        return (Set<T>) redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取有序集合指定范围的元素（按分数从高到低）
     *
     * @param key   缓存的键值
     * @param start 开始位置
     * @param end   结束位置
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getSortedSetReverseRange(final String key, final long start, final long end) {
        return (Set<T>) redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取有序集合指定分数范围的元素
     *
     * @param key 缓存的键值
     * @param min 最小分数
     * @param max 最大分数
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getSortedSetRangeByScore(final String key, final double min, final double max) {
        return (Set<T>) redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 获取有序集合指定分数范围的元素（带分数）
     *
     * @param key 缓存的键值
     * @param min 最小分数
     * @param max 最大分数
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Set<ZSetOperations.TypedTuple<Object>> getSortedSetRangeByScoreWithScores(final String key, final double min, final double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * 获取有序集合元素的分数
     *
     * @param key   缓存的键值
     * @param value 元素值
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Double getSortedSetScore(final String key, final Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 获取有序集合元素的排名（从小到大）
     *
     * @param key   缓存的键值
     * @param value 元素值
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Long getSortedSetRank(final String key, final Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 获取有序集合元素的排名（从大到小）
     *
     * @param key   缓存的键值
     * @param value 元素值
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Long getSortedSetReverseRank(final String key, final Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取有序集合的元素数量
     *
     * @param key 缓存的键值
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Long getSortedSetSize(final String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取有序集合指定分数范围内的元素数量
     *
     * @param key 缓存的键值
     * @param min 最小分数
     * @param max 最大分数
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Long getSortedSetCount(final String key, final double min, final double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 删除有序集合中的元素
     *
     * @param key    缓存的键值
     * @param values 要删除的元素
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Long removeFromSortedSet(final String key, final Object... values) {
        Long count = redisTemplate.opsForZSet().remove(key, values);
        log.info("删除有序集合元素成功, key:{}, count:{}", key, count);
        return count;
    }

    /**
     * 删除有序集合指定排名范围的元素
     *
     * @param key   缓存的键值
     * @param start 开始位置
     * @param end   结束位置
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Long removeFromSortedSetByRange(final String key, final long start, final long end) {
        Long count = redisTemplate.opsForZSet().removeRange(key, start, end);
        log.info("删除有序集合指定排名范围元素成功, key:{}, start:{}, end:{}, count:{}", key, start, end, count);
        return count;
    }

    /**
     * 删除有序集合指定分数范围的元素
     *
     * @param key 缓存的键值
     * @param min 最小分数
     * @param max 最大分数
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Long removeFromSortedSetByScore(final String key, final double min, final double max) {
        Long count = redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        log.info("删除有序集合指定分数范围元素成功, key:{}, min:{}, max:{}, count:{}", key, min, max, count);
        return count;
    }

    /**
     * 增加有序集合元素的分数
     *
     * @param key   缓存的键值
     * @param value 元素值
     * @param delta 增加的分数
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Double incrementSortedSetScore(final String key, final Object value, final double delta) {
        Double newScore = redisTemplate.opsForZSet().incrementScore(key, value, delta);
        log.info("增加有序集合元素分数成功, key:{}, value:{}, delta:{}, newScore:{}", key, value, delta, newScore);
        return newScore;
    }

    /**
     * 有序集合交集操作
     *
     * @param key      目标键
     * @param otherKey 其他键
     * @param destKey  结果存储键
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Long intersectSortedSet(final String key, final String otherKey, final String destKey) {
        Long count = redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
        log.info("有序集合交集操作成功, key:{}, otherKey:{}, destKey:{}, count:{}", key, otherKey, destKey, count);
        return count;
    }

    /**
     * 有序集合并集操作
     *
     * @param key      目标键
     * @param otherKey 其他键
     * @param destKey  结果存储键
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public Long unionSortedSet(final String key, final String otherKey, final String destKey) {
        Long count = redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
        log.info("有序集合并集操作成功, key:{}, otherKey:{}, destKey:{}, count:{}", key, otherKey, destKey, count);
        return count;
    }

    /**
     * 判断有序集合是否包含指定元素
     *
     * @param key   缓存的键值
     * @param value 元素值
     * @author jiacheng yang.
     * @since 2025/01/21 16:00
     */
    public boolean isSortedSetMember(final String key, final Object value) {
        Double score = redisTemplate.opsForZSet().score(key, value);
        return score != null;
    }
}
