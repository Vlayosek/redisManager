package com.goit.redis.manager.dao;

import java.util.Map;

import com.goit.redis.manager.exceptions.BOException;
import com.goit.redis.manager.util.RedisTool;

import redis.clients.jedis.Jedis;

public class CoreDAO extends RedisTool{
	
	public Jedis getJedisConnection() {
		return new Jedis("127.0.0.1", 6379, 5000);
	}
	
	public String PutEntityBulk(String uniqueID, Map<String,String> entity,Jedis p_jedis) throws BOException{
		Jedis jedis = null;
		try {
			if(p_jedis != null){
                jedis = p_jedis;
            }else{
                jedis = getJedisConnection();
            }
            //despues se guarda lo nuevo
            if(uniqueID.length() <=1){
                uniqueID = redisUtil.getUniqueID();
            }
            jedis.hmset(uniqueID, entity);
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(p_jedis == null){
                jedis.close();
			}
		}
	
		return uniqueID ;
	}
}
