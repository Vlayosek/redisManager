package com.goit.redis.manager.enums;


public enum FormatoFechaEnum {

	YYYY_MM_DD("yyyy-MM-dd"),
	YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd hh:mm:ss");

	private String name;

	FormatoFechaEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
