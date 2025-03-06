package com.lxy.common.service;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis操作
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/03/06 14:55
 */

@Service
public class RedisService {

    private final static Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
     * @author jiacheng yang.
     * @since 2025/03/06 14:58
     * @param key   缓存的键值
     * @param value 缓存的值
     * @param timeout 超时时间 -1表示永久
     * @param timeUnit 超时时间单位
     */
    public <T> void setObject(final String key, final T value,final Long timeout, final TimeUnit timeUnit) {
        if (timeout == null || timeout <= 0) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        }
        logger.info("缓存对象成功, key:{}", key);
    }

    /**
     * 缓存List类型数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     */
    public <T> void setList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        logger.info("缓存List类型数据成功, key:{}", key);
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
        logger.info("缓存Hash类型数据成功, key:{}, hKey:{}", key, hKey);
    }

    /**
     * 批量缓存
     * @author jiacheng yang.
     * @since 2025/03/06 15:22
     * @param timeout 超时时间 -1表示永久
     */
    public void setStringBatch(final Map<String, String> map, final Long timeout) {
        int batchSize = 1000; // 每批次处理 1000 个 Key
        List<String> keys = new ArrayList<>(map.keySet());

        for (int i = 0; i < keys.size(); i += batchSize) {
            List<String> batchKeys = keys.subList(i, Math.min(i + batchSize, keys.size()));
            Map<String, String> batchMap = batchKeys.stream()
                    .collect(Collectors.toMap(k -> k, map::get));

            stringRedisTemplate.executePipelined((RedisCallback<String>) connection -> {
                StringRedisConnection stringConn = (StringRedisConnection) connection;
                batchMap.forEach((key, value) -> {
                    stringConn.set(key, value);
                    if (timeout != null && timeout > 0) {
                        stringConn.expire(key, timeout);
                    }
                });
                return null;
            });
        }
        logger.info("批量缓存成功");
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
    public <T> T getHashValue(final String key, final String hKey,Class<T> clazz) {
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

}
