package com.goit.redis.manager.dto;

import java.util.List;

import lombok.Data;

@Data
public class CriteriosFiltrado {

	private String nombreSegmento;
	private String objetivoSegmento;
	private String canal;
	private List<CamposFiltradosDTO> camposFiltrados;

	
}
