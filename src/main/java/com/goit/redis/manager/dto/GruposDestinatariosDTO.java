package com.goit.redis.manager.dto;

import com.goit.redis.manager.model.NotDestinatarios;
import com.goit.redis.manager.model.NotGrupos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GruposDestinatariosDTO {
    private Integer idGrupoDestinatario;
    private NotGrupos notGrupos;
    private NotDestinatarios notDestinatarios;
    private String estado;

}
