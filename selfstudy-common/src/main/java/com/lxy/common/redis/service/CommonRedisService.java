package com.lxy.common.redis.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lxy.common.domain.StatelessAdmin;
import com.lxy.common.domain.StatelessUser;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户使用的JWT凭据的操作Service
 * */
public interface CommonRedisService {
	
	/**
	 * 新增一个String类型的Value
	 * @param timeout 有效时长,
	 * @param timeUnit 有效时长的单位，请用秒
	 * */
	boolean insertString(String key,String Value,int timeout,TimeUnit timeUnit);

	boolean insertString(String key, String value, long timeout, TimeUnit timeUnit);


	/**
	 * 批量插入 过期时间永久
	 * @param map map
	 */
	void saveBatch(Map<String,Object> map);

	/**
	 * 批量插入string
	 * @param map key为redis的key ， value为对应数据
	 * @param seconds -1为永久，单位秒
	 */
	void insertBatchString(final Map<String,String> map,Integer seconds);

	/**
	 * 用key获取String值
	 * */
	String getString(String key);
	
	
	/**
	 * 获取key的过期时间
	 * */
	long getKeyExpired(String key,TimeUnit timeUnit);
	
	/**
	 * 根据表达式搜索所有key
	 * */
	Set<String> search(String pattern);
	
	/**
	 * key是否存在
	 * */
	boolean existKey(String key);
	
	/**
	 * 删除Redis中的key和值
	 * */
	void deleteKey(String key);
	
	/**
	 *	批量删除键和值
	 */
	void deleteKeys(Set<String> keys);
	
	/**
	 * 更新key的过期时间
	 * @param key redis的key
	 * @param second 新的有效时间长度，单位秒
	 * */
	void updateKeyExpired(String key,long second);
	
	/**
	 * 插入Hash数据,将POJO对象数据直接插入
	 * */
	void insertHash(String key,Object data);
	
	/**
	 * 根据key和hashKey删除hash中的特定键，可批量删除
	 * */
	void deleteHashKey(String key, String... hashKeys);

	void deleteHashKey(String key, String hashKey);
	
	/**
	 * 根据key和hashKey取值(特殊情况)
	 * */
	<T> T getHashValue(String key, String hashKey);
	
	/**
	 * 根据key和hashKey取值
	 * */
	<T> T getHashValue(String key, String hashKey,Class<T> classType);

	/**
	 * 根据key获得整个hash结构体,我们约定只能获取key和value都是string的map
	 * */
	Map<String, String> getHashEntries(String key);

	<TYPE> Map<String, TYPE> getHashEntries(String key,Class<TYPE> type);
	
	/**
	 * 判断某个hash中对应的hashKey是否存在
	 * */
	boolean existHashKey(String key, String hashKey);

	/**
	 * 插入单个hashkey的值
	 * */
	void insertHashValue(String key, String hashKey, String value);

	<TYPE> void insertHashValue(String key, String hashKey, String value,Class<TYPE> type);
	/**
	 * 根据搜索表达式检索出符合要求的所有key
	 * */
	Set<String> scanKey(String patern);
	
	
	/**
	 * 批量获取key  正式服禁用
	 */
	@Deprecated
	Set<String> keys(String pattern);
	
	/**
	 * 批量获取值
	 */
	List<Object> getValues(Collection<String> collections);
	
	/**
	 * 插入map
	 */
	void setValues(Map<String,Object> map);

	/**
	 * 登录状态持久化到redis
	 * @param statelessAdmin 管理员信息
	 * @param endDay 过期时长单位天
	 */
	void loginStatusToRedis(String key, StatelessAdmin statelessAdmin, int endDay);

	/**
	 * 登录状态持久化到redis
	 * @param statelessUser 管理员信息
	 * @param endDay 过期时长单位天
	 */
	void loginStatusToRedisUser(String key, StatelessUser statelessUser, int endDay);

	/**
	 * 控制管理员一个账号在线数
	 * @param onlineNum 一个号在线数
	 * @param token jwt
	 */
	StatelessAdmin controlLoginNum(String key, Integer onlineNum,int endDay, String token);

	/**
	 * 控制一个账号在线数，JWT续签
	 * @param onlineNum 一个号在线数
	 * @param token jwt
	 */
	StatelessUser controlLoginNumUser(String key, Integer onlineNum, int endDay, String token);

	void removeInRedisUser(String key,String token);

	void removeInRedis(String key,String token);

}
