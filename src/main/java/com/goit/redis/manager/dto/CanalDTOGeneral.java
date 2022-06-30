package com.goit.redis.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CanalDTOGeneral {
	
	private String id;
	private String nombre;
	private String estado;
	private String fechaDesde;
	private String fechaHasta;

}
