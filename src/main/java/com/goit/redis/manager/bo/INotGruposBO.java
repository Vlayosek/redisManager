package com.goit.redis.manager.bo;

import java.util.Map;

import com.goit.redis.manager.dto.CriteriosFiltrado;
import com.goit.redis.manager.dto.RequestGrupoDTO;
import com.goit.redis.manager.dto.UsuarioLogin;
import com.goit.redis.manager.exceptions.BOException;

public interface INotGruposBO {

	
	public Map<String,Object> postGrupoSegmento(CriteriosFiltrado objCriteriosFiltrado, UsuarioLogin usuarioLogin, String idGrupo) throws BOException;
	
	public Map<String,Object> postGrupos(RequestGrupoDTO objRequestGrupoDTO, String idUsuario, String idEmpresa, String usuario) throws BOException;

	public Map<String, Object> getGrupo(Integer intIdGrupo) throws BOException;
	
	public Map<String, Object> getGrupos(String strTipoCanal,UsuarioLogin usuarioLogin) throws BOException;
	
	public Map<String, Object> getGruposDestinatarios(Integer intIdGrupo) throws BOException;
	
	public Map<String,Object> putGrupos(RequestGrupoDTO objRequestGrupoDTO, UsuarioLogin usuario, Integer intIdGrupo) throws BOException;
	
	public Map<String, Object> allGruposDestinatarios(String strTipoGrupo,UsuarioLogin usuarioLogin) throws BOException;
	
	public Map<String, Object> deleteGrupo(Integer intIdGrupo,UsuarioLogin usuarioLogin) throws BOException;
	
	public Map<String, Object> cambiarTipo(Integer intIdGrupo,UsuarioLogin usuarioLogin) throws BOException;
	

		
}
