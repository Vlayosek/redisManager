package com.goit.redis.manager.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RequestDestinatariosDTO {

	//private Integer idDestinatario;//Actualizacion
	private String primerNombre;//Requerido
	private String primerApellido;//Requerido
	private String correo;//Requerido
	private String genero;//Requerido
	private String telefonoMovil;
	private String fechaNacimiento;//Requerido
	private String pais;
	private List<CamposPersonalizadosDTO> camposPersonalizados;
}
