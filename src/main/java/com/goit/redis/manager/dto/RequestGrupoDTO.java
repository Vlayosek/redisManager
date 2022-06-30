package com.goit.redis.manager.dto;

import java.util.List;

import lombok.Data;

@Data
public class RequestGrupoDTO {
	private Integer idGrupo;
	private String nombre;
	private String descripcion;
	private String tipo;
	private String canal;
	private List<String> camposPersonalizados;
}
