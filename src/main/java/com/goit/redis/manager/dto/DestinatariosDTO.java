package com.goit.redis.manager.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DestinatariosDTO {
	
//	private Integer idDestinatario;
//	private String correo;
//	private String primerNombre;
//	private String primerApellido;
//	private String genero;
//	private String fechaNacimiento;
//	private String pais;
//	private String idEntidadRedis;
//	private String estado;
//	private Date fechaCrea;
//	private Date fechaActualiza;
//	private Date fechaInactiva;
//	private String usuarioCrea;
//	private String usuarioActualiza;
//	private String usuarioInactiva;
//	private Integer idGrupoDestinatario;
//	private String idUsuario;
//	private String idEmpresa;
//	private List<CamposPersonalizadosDTO> lsCamposPersonalizados;
	
	private String correo;
	private String telefonoMovil;
	private String primerNombre;
	private String primerApellido;
	private String fechaNacimiento;
	private String fechaCreacion;
	

}
