package com.lxy.common.redis.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import com.lxy.common.security.bo.StatelessAdmin;
import com.lxy.common.security.bo.StatelessUser;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.util.JsonUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 封装的一个通常redis缓存处理方法 参考: https://www.jianshu.com/p/7bf5dc61ca06
 */
@Service
public class CommonRedisServiceImpl implements CommonRedisService {

	private final static Logger logger = LoggerFactory.getLogger(CommonRedisServiceImpl.class);

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	private ValueOperations<String, Object> valueOperations;
	private HashOperations<String, String, Object> hashOperations;



	@PostConstruct
	public void init() {
		this.valueOperations = redisTemplate.opsForValue();
		this.hashOperations = redisTemplate.opsForHash();
	}


	@Override
	public boolean existKey(String key) {
		boolean exist = redisTemplate.hasKey(key);
		return exist;
	}

	@Override
	public long getKeyExpired(String key, TimeUnit timeUnit) {
		if (timeUnit == null) {
			timeUnit = TimeUnit.SECONDS;
		}
		boolean exist = this.existKey(key);
		if (!exist) {
			return 0;
		}
		long expire = this.redisTemplate.getExpire(key, timeUnit);

		return expire;
	}


	@Override
	public boolean insertString(String key, String value, long timeout, TimeUnit timeUnit) {
		if (timeout > 0L) {
			if (timeUnit == null) {
				timeUnit = TimeUnit.SECONDS;
			}

			valueOperations.set(key, value, timeout, timeUnit);
		} else {
			valueOperations.set(key, value);
		}

		return true;
	}


	@Override
	public void saveBatch(Map<String, Object> map) {
		if(MapUtil.isNotEmpty(map)){
			valueOperations.multiSet(map);
		}
	}

	@Override
	public void insertBatchString(Map<String, String> map, Integer seconds) {
		stringRedisTemplate.executePipelined((RedisCallback<String>) connection -> {
			// 使用 StringRedisConnection 接口的 set(String, String) 方法，不再需要类型转换
			StringRedisConnection stringConn = (StringRedisConnection) connection;
			map.forEach((key, value) -> {
				stringConn.set(key, value); // 直接使用 String key 和 String value
				if (seconds != null && seconds > 0) {
					stringConn.expire(key, seconds); // expire 方法仍然可以使用 String key
				}
			});
			return null;
		});
	}


	@Override
	public String getString(String key) {
		Object object = valueOperations.get(key);
		if (object == null) {
			return null;
		}
		String value = object.toString();
		return value;
	}

	@Override
	public void deleteKey(String key) {
		this.redisTemplate.delete(key);
	}

	@Override
	public Set<String> search(String pattern) {
		Set<String> keySet = this.redisTemplate.keys(pattern);

		return keySet;
	}

	@Override
	public void updateKeyExpired(String key, long second) {
		this.redisTemplate.expire(key, second, TimeUnit.SECONDS);
	}

	@Override
	public void insertHash(String key, Map<String, Object> data) {
		hashOperations.putAll(key,data);
	}

	@Override
	public void insertHashValue(String key, String hashKey, String value) {
		hashOperations.put(key, hashKey, value);
	}

	@Override
	public <TYPE> void insertHashValue(String key, String hashKey, String value,Class<TYPE> type) {
		hashOperations.put(key, hashKey, value);
	}

	@Override
	public <T> T getHashValue(String key, String hashKey, Class<T> type) {
		HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
		String value = hashOps.get(key, hashKey);
		try {
			return JsonUtil.getTypeObj(value, type);
		} catch (Exception e) {
			logger.error("Failed to deserialize hash value for key {}: {}", key, e.getMessage());
			return null;
		}
	}

	@Override
	public <T> T getHashValue(String key, String hashKey) {
		T value = null;
		HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
		value = hashOperations.get(key, hashKey);
		return value;
	}

	@Override
	public Map<String, String> getHashEntries(String key) {
		HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
		Map<String, String> map = hashOperations.entries(key);
		return map;
	}

	@Override
	public <TYPE> Map<String, TYPE> getHashEntries(String key,Class<TYPE> type) {
		HashOperations<String, String, TYPE> hashOperations = redisTemplate.opsForHash();
		Map<String, TYPE> map = hashOperations.entries(key);
		return map;
	}

	@Override
	public void deleteHashKey(String key, String... hashKeys) {
		for (String hashKey : hashKeys) {
			hashOperations.delete(key, hashKey);
		}
	}

	@Override
	public void deleteHashKey(String key, String hashKey) {
		hashOperations.delete(key, hashKey);
	}

	@Override
	public boolean existHashKey(String key, String hashKey) {
		boolean exist = hashOperations.hasKey(key, hashKey);
		return exist;
	}

	@Override
	public Set<String> scanKey(String pattern) {
		Set<String> keySet = new HashSet<>();
		try (RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
             Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).build())) {
			while (cursor.hasNext()) {
				keySet.add(new String(cursor.next(), StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			logger.error("Scan keys error: {}", e.getMessage(), e);
		}
		return keySet;
	}


	@Override
	public void deleteKeys(Set<String> keys) {
		this.redisTemplate.delete(keys);
	}

	@Override
	public Set<String> keys(String pattern) {
		return this.redisTemplate.keys(pattern);
	}

	@Override
	public List<Object> getValues(Collection<String> collections) {
		ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
		return operations.multiGet(collections);
	}

	@Override
	public void setValues(Map<String,Object> map) {
		ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
		operations.multiSet(map);
	}

	@Override
	public void loginStatusToRedis(String key, StatelessAdmin statelessAdmin, int endDay) {
		Date now = new Date();
		statelessAdmin.setLoginTime(now);

		Date end = DateUtil.offsetDay(now, endDay);
		//设置过期时间
		statelessAdmin.setEndTime(end);

		List<StatelessAdmin> loginList = JSON.parseArray(this.getString(key), StatelessAdmin.class);
		if (CollUtil.isEmpty(loginList)){
			loginList = Collections.singletonList(statelessAdmin);
		}else {
			loginList.add(statelessAdmin);
		}
		this.insertString(key,JsonUtil.toJson(loginList),-1L, TimeUnit.SECONDS);
	}

	@Override
	public void loginStatusToRedisUser(String key, StatelessUser statelessUser, int endDay) {
		Date now = new Date();
		statelessUser.setLoginTime(now);

		Date end = DateUtil.offsetDay(now, endDay);
		//设置过期时间
		statelessUser.setEndTime(end);

		List<StatelessUser> loginList = JSON.parseArray(this.getString(key), StatelessUser.class);
		if (CollUtil.isEmpty(loginList)){
			loginList = Collections.singletonList(statelessUser);
		}else {
			loginList.add(statelessUser);
		}
		this.insertString(key,JsonUtil.toJson(loginList),-1L, TimeUnit.SECONDS);
	}

	@Override
	public StatelessAdmin controlLoginNum(String key, Integer onlineNum, int endDay, String token) {
		Date now = new Date();

		StatelessAdmin loginStatus = null;
		if (this.existKey(key)){
			List<StatelessAdmin> loginList = JSON.parseArray(this.getString(key), StatelessAdmin.class);
			if (loginList !=null && loginList.size() > 0){
				//删除过期的jwt
				loginList.removeIf(o -> DateUtil.compare(now, o.getEndTime()) > 0);
				//按过期时间正序
				loginList.sort(Comparator.comparing(StatelessAdmin::getEndTime));
				int size = loginList.size();
				if (size > onlineNum){
					loginList.remove(0);
				}
				for (StatelessAdmin status : loginList) {
					String accessToken = status.getToken();
					if (token.equals(accessToken)){
						Date end = DateUtil.offsetDay(now, endDay);
						status.setEndTime(end);
						loginStatus = status;
						break;
					}
				}
				this.insertString(key,JsonUtil.toJson(loginList),-1L, TimeUnit.SECONDS);
			}
		}
		return loginStatus;
	}

	@Override
	public StatelessUser controlLoginNumUser(String key, Integer onlineNum, int endDay, String token) {
		Date now = new Date();

		StatelessUser loginStatus = null;
		if (this.existKey(key)){
			List<StatelessUser> loginList = JSON.parseArray(this.getString(key), StatelessUser.class);
			if (loginList !=null && loginList.size() > 0){
				//删除过期的jwt
				loginList.removeIf(o -> DateUtil.compare(now, o.getEndTime()) > 0);
				//按过期时间正序
				loginList.sort(Comparator.comparing(StatelessUser::getEndTime));
				int size = loginList.size();
				if (size > onlineNum){
					loginList.remove(0);
				}
				for (StatelessUser status : loginList) {
					String accessToken = status.getToken();
					if (token.equals(accessToken)){
						Date end = DateUtil.offsetDay(now, endDay);
						status.setEndTime(end);
						loginStatus = status;
						break;
					}
				}
				this.insertString(key,JsonUtil.toJson(loginList),-1L, TimeUnit.SECONDS);
			}
		}
		return loginStatus;
	}

	@Override
	public void removeInRedisUser(String key, String token) {
		List<StatelessUser> loginList = JSON.parseArray(this.getString(key), StatelessUser.class);
		if (loginList !=null && loginList.size() > 0){
			loginList.removeIf(o -> o.getToken().equals(token));
			this.insertString(key, JsonUtil.toJson(loginList),-1L, TimeUnit.SECONDS);
		}
	}

	@Override
	public void removeInRedis(String key, String token) {
		List<StatelessAdmin> loginList = JSON.parseArray(this.getString(key), StatelessAdmin.class);
		if (loginList !=null && loginList.size() > 0){
			loginList.removeIf(o -> o.getToken().equals(token));
			this.insertString(key, JsonUtil.toJson(loginList),-1L, TimeUnit.SECONDS);
		}
	}


}
