package com.goit.redis.manager.enums;

public enum ParametrosRedisEnum {

	JWT_SECRET("JWT_SECRET"),
	GOIT_SEGURIDAD_API("GOIT_SEGURIDAD_API"),
	REDIS_GROUP("GRP_DESTINATARIO");
	

	private String name;

	ParametrosRedisEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
