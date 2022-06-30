package com.goit.redis.manager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralParametrosGeneralesDTO {
	
	private Integer idParametroGeneral;
    private String descripcion;
    private String estado;
    private String nombre;
    private String tipoValor;
    private String valor;

}
