package com.goit.redis.manager.dto;

import lombok.Data;

@Data
public class CamposFiltradosDTO {
	private String campo;
	private String criterio;
	private String valorCriterio;
}
