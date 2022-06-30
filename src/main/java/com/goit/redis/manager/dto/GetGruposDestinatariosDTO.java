package com.goit.redis.manager.dto;

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
public class GetGruposDestinatariosDTO {
	
	//private Integer idGrupoDestinatario;
	private NotGrupos notGrupos;
	private String estado;
	private List<NotDestinatarios> lsNotDestinatarios;
    
}
