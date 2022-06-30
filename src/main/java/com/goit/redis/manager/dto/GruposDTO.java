package com.goit.redis.manager.dto;

import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GruposDTO {
	private Integer idGrupo; 
	private BigInteger cantidadDestinatarios;
	private String descripcion; 
	private String estado;  
	private String fechaCreacion; 
	private String nombre; 
	private String tipo; 
	private String canal; 
}
