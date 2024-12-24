package com.lxy.common.redis.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lxy.common.domain.StatelessAdmin;
import com.lxy.common.domain.StatelessUser;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 封装的一个通常redis缓存处理方法 参考: https://www.jianshu.com/p/7bf5dc61ca06
 */
@Service
public class CommonRedisServiceImpl implements CommonRedisService {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean existKey(String key) {
		boolean exist = this.redisTemplate.hasKey(key);
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
	public boolean insertString(String key, String value, int timeout, TimeUnit timeUnit) {
		ValueOperations<String, Object> valueOperations = this.redisTemplate.opsForValue();
		if (timeout > 0) {
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
	public boolean insertString(String key, String value, long timeout, TimeUnit timeUnit) {
		ValueOperations<String, Object> valueOperations = this.redisTemplate.opsForValue();
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
			ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
			valueOperations.multiSet(map);
		}
	}

	@Override
	public void insertBatchString(final Map<String,String> map,Integer seconds){
		stringRedisTemplate.executePipelined(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				StringRedisConnection stringRedisConn = (StringRedisConnection)connection;

				for (Map.Entry<String, String> entry : map.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					stringRedisConn.set(key.getBytes(StandardCharsets.UTF_8),value.getBytes(StandardCharsets.UTF_8));
					if (seconds != null && seconds != -1){
						//过期时间单位秒
						stringRedisConn.expire(key.getBytes(),seconds.longValue());
					}

				}
				return null;
			}
		});
	}


	@Override
	public String getString(String key) {
		ValueOperations<String, Object> valueOperations = this.redisTemplate.opsForValue();
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
		// TODO Auto-generated method stub
		this.redisTemplate.expire(key, second, TimeUnit.SECONDS);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertHash(String key, Object data) {
		// TODO Auto-generated method stub
		HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
		if (data instanceof Map) { // 如果data的数据类型不是map
			Map<String, String> map = (Map<String, String>) data;
			hashOperations.putAll(key, map);
		} else {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL); // 在序列化后的数据里保存对象的类型信息，Final
			objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // 序列化空值失败时不抛异常
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 反序列化不存在的字段失败时不抛异常
			HashMapper<Object, String, Object> jacksonMapper = new Jackson2HashMapper(objectMapper, false); // 不展平
			Map<String, Object> mappedHash = jacksonMapper.toHash(data);
			hashOperations.putAll(key, mappedHash);
		}

	}

	@Override
	public void insertHashValue(String key, String hashKey, String value) {
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		this.redisTemplate.setHashValueSerializer(stringRedisSerializer);
		HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
		hashOperations.put(key, hashKey, value);
	}

	@Override
	public <TYPE> void insertHashValue(String key, String hashKey, String value,Class<TYPE> type) {
		Jackson2JsonRedisSerializer<TYPE> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<TYPE>(type);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE); // 在序列化后的数据里保存对象的类信息
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // 序列化空值失败时不抛异常
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 反序列化不存在的字段失败时不抛异常
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
		this.redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

		HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
		hashOperations.put(key, hashKey, value);
	}

	@Override
	public <T> T getHashValue(String key, String hashKey, Class<T> classType) {
		T value = null;
		/*
		 * 这里存在一个问题，Jackson无法反序列化不以引号开始的字符串，因此我们约定hash数据都必须用jackson序列化后再存储
		 * if(classType.getName().equals("java.lang.String")) { StringRedisSerializer
		 * stringRedisSerializer = new StringRedisSerializer();
		 * this.redisTemplate.setHashValueSerializer(stringRedisSerializer);
		 * HashOperations<String, String, T> hashOperations =
		 * redisTemplate.opsForHash(); value = hashOperations.get(key, hashKey); }else {
		 * 
		 * 
		 * }
		 */
		Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<T>(classType);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE); // 在序列化后的数据里保存对象的类信息
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // 序列化空值失败时不抛异常
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 反序列化不存在的字段失败时不抛异常
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
		this.redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
		value = hashOperations.get(key, hashKey);
		return value;

	}

	@Override
	public <T> T getHashValue(String key, String hashKey) {
		T value = null;
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		this.redisTemplate.setHashValueSerializer(stringRedisSerializer);
		HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
		value = hashOperations.get(key, hashKey);
		return value;
	}
	
	@Override
	public Map<String, String> getHashEntries(String key) {
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		this.redisTemplate.setHashValueSerializer(stringRedisSerializer);
		HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
		Map<String, String> map = hashOperations.entries(key);
		return map;
	}

	@Override
	public <TYPE> Map<String, TYPE> getHashEntries(String key,Class<TYPE> type) {
		Jackson2JsonRedisSerializer<TYPE> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<TYPE>(type);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE); // 在序列化后的数据里保存对象的类信息
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // 序列化空值失败时不抛异常
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 反序列化不存在的字段失败时不抛异常
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
		this.redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

		HashOperations<String, String, TYPE> hashOperations = redisTemplate.opsForHash();
		Map<String, TYPE> map = hashOperations.entries(key);
		return map;
	}
	
	@Override
	public void deleteHashKey(String key, String... hashKeys) {
		HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
		for (String hashKey : hashKeys) {
			hashOperations.delete(key, hashKey);
		}
	}
	
	@Override
	public void deleteHashKey(String key, String hashKey) {
		HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
		hashOperations.delete(key, hashKey);
	}
	
	@Override
	public boolean existHashKey(String key, String hashKey) {
		HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
		boolean exist = hashOperations.hasKey(key, hashKey);
		return exist;
	}

	@Override
	public Set<String> scanKey(String patern) {
		//注意ScanOptions的count并不能设置返回的数量,游标只能挨个遍历直到返回所有
		Set<String> keySet = new HashSet<String>();		//redis的scan可能会返回重复的对象，因此用Set保证不重复
		RedisConnection redisConnection = this.redisTemplate.getConnectionFactory().getConnection();
		try(Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions().match(patern).build())){
			while (cursor.hasNext()) {
				byte[] data = cursor.next();		//这里遍历得到的是key数据
				String key = new String(data, "UTF-8");
				//System.out.println(key);
				keySet.add(key);
		    }
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
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

		List<StatelessAdmin> loginList = JSONObject.parseArray(this.getString(key), StatelessAdmin.class);
		if (CollUtil.isEmpty(loginList)){
			loginList = Collections.singletonList(statelessAdmin);
		}else {
			loginList.add(statelessAdmin);
		}
		this.insertString(key,JsonUtil.toJson(loginList),-1, TimeUnit.SECONDS);
	}

	@Override
	public void loginStatusToRedisUser(String key, StatelessUser statelessUser, int endDay) {
		Date now = new Date();
		statelessUser.setLoginTime(now);

		Date end = DateUtil.offsetDay(now, endDay);
		//设置过期时间
		statelessUser.setEndTime(end);

		List<StatelessUser> loginList = JSONObject.parseArray(this.getString(key), StatelessUser.class);
		if (CollUtil.isEmpty(loginList)){
			loginList = Collections.singletonList(statelessUser);
		}else {
			loginList.add(statelessUser);
		}
		this.insertString(key,JsonUtil.toJson(loginList),-1, TimeUnit.SECONDS);
	}

	@Override
	public StatelessAdmin controlLoginNum(String key, Integer onlineNum, int endDay, String token) {
		Date now = new Date();

		StatelessAdmin loginStatus = null;
		if (this.existKey(key)){
			List<StatelessAdmin> loginList = JSONObject.parseArray(this.getString(key), StatelessAdmin.class);
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
				this.insertString(key,JsonUtil.toJson(loginList),-1, TimeUnit.SECONDS);
			}
		}
		return loginStatus;
	}

	@Override
	public StatelessUser controlLoginNumUser(String key, Integer onlineNum, int endDay, String token) {
		Date now = new Date();

		StatelessUser loginStatus = null;
		if (this.existKey(key)){
			List<StatelessUser> loginList = JSONObject.parseArray(this.getString(key), StatelessUser.class);
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
				this.insertString(key,JsonUtil.toJson(loginList),-1, TimeUnit.SECONDS);
			}
		}
		return loginStatus;
	}

	@Override
	public void removeInRedisUser(String key, String token) {
		List<StatelessUser> loginList = JSONObject.parseArray(this.getString(key), StatelessUser.class);
		if (loginList !=null && loginList.size() > 0){
			loginList.removeIf(o -> o.getToken().equals(token));
			this.insertString(key, JsonUtil.toJson(loginList),-1, TimeUnit.SECONDS);
		}
	}

	@Override
	public void removeInRedis(String key, String token) {
		List<StatelessAdmin> loginList = JSONObject.parseArray(this.getString(key), StatelessAdmin.class);
		if (loginList !=null && loginList.size() > 0){
			loginList.removeIf(o -> o.getToken().equals(token));
			this.insertString(key, JsonUtil.toJson(loginList),-1, TimeUnit.SECONDS);
		}
	}


}
