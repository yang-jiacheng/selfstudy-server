package com.lxy.system.service;


import com.lxy.common.domain.R;
import com.lxy.system.po.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.*;
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

        for (int i = 0; i < keys.size();  i += batchSize) {
            List<String> batchKeys = keys.subList(i,  Math.min(i  + batchSize, keys.size()));
            Map<String, T> batchMap = batchKeys.stream()
                    .collect(Collectors.toMap(k  -> k, map::get));

            redisTemplate.executePipelined((RedisCallback<Object>)  connection -> {
                for (Map.Entry<String, T> entry : batchMap.entrySet())  {
                    byte[] keyBytes = keySerializer.serialize(entry.getKey());
                    byte[] valueBytes = valueSerializer.serialize(entry.getValue());
                    connection.set(keyBytes,  valueBytes);
                    if (timeout != null && timeout > 0) {
                        long timeoutSeconds = timeUnit.toSeconds(timeout);
                        connection.expire(keyBytes,  timeoutSeconds);
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


    /**
     * Redisson 分布式锁
     *
     * @param lockKey      锁的 key
     * @param waitTime     尝试获取锁的最大等待时间
     * @param leaseTime    锁自动释放时间
     * @param timeUnit     时间单位
     * @param action       要执行的业务逻辑
     * @param <T>          返回类型
     * @return             执行结果
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
        }catch (Exception e){
            log.error("操作失败", e);
        }finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }
}
