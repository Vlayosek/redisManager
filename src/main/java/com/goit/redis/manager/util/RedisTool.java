package com.goit.redis.manager.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class RedisTool extends LoggerOBJ{
	
	public static PropertiesRedis propsRedis;
	public static RedisUtil redisUtil;
	
	public static String getErrorByStack(Exception ex) {
		String totalError = "";
        StringWriter errors = null;
        try{
            errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            totalError = errors.toString();
        }catch(Exception internalError){
            totalError = ex.toString();
            internalError.printStackTrace();
        }
        return totalError;
	}

}
