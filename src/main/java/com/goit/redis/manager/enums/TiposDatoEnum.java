package com.goit.redis.manager.enums;

public enum TiposDatoEnum {
	
	GRUPO("G"),
	ESTRACTO("E");

	private String name;

	TiposDatoEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
