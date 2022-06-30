package com.goit.redis.manager.enums;

public enum ParametrosEnum {
	
	JWT_SECRET("JWT_SECRET"),
	GOIT_SEGURIDAD_API("http://desa.goitsa.me:8989/goit-security-api");
	//TODO: se debe cambiar para obtenerse de properties o tabla de parametros
	private String name;

	ParametrosEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
