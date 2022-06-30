package com.goit.redis.manager.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public abstract class LoggerOBJ {
	
	public static final Logger logger = LogManager.getLogger("com.goit.redis.manager.*");
	
	public void push(String key, String value) {
		ThreadContext.put(key, value);
	}
	
	public void remove() {
		ThreadContext.remove("redisCoreId");
		ThreadContext.remove("ip");
	}

}
