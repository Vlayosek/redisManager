package com.goit.redis.manager.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.goit.redis.manager.model.NotDestinatarios;
import com.goit.redis.manager.model.NotGrupos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GruposAndDestinatariosDTO {
	
	private String nombre;
	private BigInteger cantidadDestinatarios;
	private String fechaCreacion;
	private List<DestinatariosDTO> listaDestinatarios;

}
