package com.goit.redis.manager.enums;

public enum TiposCanalEnum {
	
	CORREO("C"),
	MENSAJERIA("M"),
	TODOS("T");

	private String name;

	TiposCanalEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
