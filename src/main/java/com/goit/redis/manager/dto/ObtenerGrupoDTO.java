package com.goit.redis.manager.dto;

import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ObtenerGrupoDTO {
	
	private Integer idGrupo;
	private String nombre; 
	private String descripcion; 
	private String tipo;  
	private String estado;
	private String camposPersonalizados;
	private BigInteger cantidadDestinatarios;
	private Date fechaCreacion;
	
	

}
