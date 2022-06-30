package com.goit.redis.manager.dao.redis;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.goit.redis.manager.enums.ParametrosRedisEnum;

@Service
public class RedisDAO {
	
	@Autowired 
	RedisTemplate<String,Object> redisTemplate;
	
	public void post(ParametrosRedisEnum parametrosRedisEnum,String strKey,String strRefreshToken) {
		redisTemplate.opsForHash().put(parametrosRedisEnum.name(),strKey,strRefreshToken);
	}
	
	public void delete(ParametrosRedisEnum parametrosRedisEnum,String strKey) {
		redisTemplate.opsForHash().delete(parametrosRedisEnum.name(),strKey);
	}
	
	public Object find(ParametrosRedisEnum parametrosRedisEnum,String strKey) {
		return redisTemplate.opsForHash().get(parametrosRedisEnum.name(),strKey);
	}
	
	public Object findAll(ParametrosRedisEnum parametrosRedisEnum,String strKey) {
		return redisTemplate.opsForHash().entries(parametrosRedisEnum.name());
	}

	public void put(ParametrosRedisEnum parametrosRedisEnum, String strKey, Map<String, String> entity) {
		redisTemplate.opsForHash().put(parametrosRedisEnum.name(),strKey,entity);
	}
}
