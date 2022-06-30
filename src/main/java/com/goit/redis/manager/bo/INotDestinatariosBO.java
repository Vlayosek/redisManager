package com.goit.redis.manager.bo;

import java.util.List;
import java.util.Map;

import com.goit.redis.manager.dto.RequestDestinatariosDTO;
import com.goit.redis.manager.dto.UsuarioLogin;
import com.goit.redis.manager.exceptions.BOException;

public interface INotDestinatariosBO {

	/**
	 * @Autor Vladimir Kozisck
	 * @param lsRequestDestinatariosDTO
	 * @param intIdGrupo
	 * @param idUsuario
	 * @param idEmpresa
	 * @param usuario
	 * @throws BOException
	 */
	Map<String, Object> saveDestinatarios(List<RequestDestinatariosDTO> lsRequestDestinatariosDTO,Integer intIdGrupo ,String idUsuario,
			String idEmpresa, String usuario) throws BOException;

	/**
	 *
	 * @Autor Vladimir Kozisck
	 * @param strIdEntidadRedis
	 * @return
	 * @throws BOException
	 */
	Map<String, Object> getDestinatarios(String stridDestinatario) throws BOException;

	/**
	 * Obtener un destinatario con sus campos adicionales
	 * 
	 * @author Vladimir Kozisck
	 * @param stridDestinatario
	 * @return
	 * @throws BOException
	 */
	Map<String, Object> getDestinatariosGroup(String stridDestinatario) throws BOException;

	
	/**
	 * Actualiza un DEstinatario
	 * 
	 * @author Vladimir Kozisck
	 * @param lsRequestDestinatariosDTO
	 * @param idDestinatario
	 * @param usuario
	 * @return
	 * @throws BOException
	 */
	Map<String, Object> updateDestinatarios(RequestDestinatariosDTO lsRequestDestinatariosDTO,
			String idDestinatario, UsuarioLogin usuario) throws BOException;
	
	
	/**
	 * Eliminar un destinatario
	 * 
	 * @author Vladimir Kozisck
	 * @param idDestinatario
	 * @param usuario
	 * @return
	 * @throws BOException
	 */
	Map<String, Object> deleteDestinatarios(String idDestinatario, UsuarioLogin usuario) throws BOException;
	

}
