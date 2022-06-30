package com.goit.redis.manager.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.goit.helper.FechasHelper;
import com.goit.helper.enums.Estado;
import com.goit.helper.enums.FormatoFecha;
import com.goit.redis.manager.bo.INotDestinatariosBO;
import com.goit.redis.manager.dao.NotDestinatariosDAO;
import com.goit.redis.manager.dao.NotGruposDAO;
import com.goit.redis.manager.dao.NotGruposDestinatariosDAO;
import com.goit.redis.manager.dao.redis.RedisDAO;
import com.goit.redis.manager.dto.CamposPersonalizadosDTO;
import com.goit.redis.manager.dto.RequestDestinatariosDTO;
import com.goit.redis.manager.dto.UsuarioLogin;
import com.goit.redis.manager.enums.ParametrosRedisEnum;
import com.goit.redis.manager.exceptions.BOException;
import com.goit.redis.manager.model.NotDestinatarios;
import com.goit.redis.manager.model.NotGrupos;
import com.goit.redis.manager.model.NotGruposDestinatarios;
import com.goit.redis.manager.util.ValidacionUtil;

@Service
public class NotDestinatariosBOImpl implements INotDestinatariosBO {

	@Autowired
	private NotDestinatariosDAO objNotDestinatariosDAO;

	@Autowired
	private NotGruposDAO objNotGruposDAO;

	@Autowired
	private RedisDAO objRedisDAO;

	@Autowired
	private NotGruposDestinatariosDAO objNotGruposDestinatariosDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String,Object> saveDestinatarios(List<RequestDestinatariosDTO> lsRequestDestinatariosDTO, Integer intIdGrupo,
			String idUsuario, String idEmpresa, String usuario) throws BOException {
		
		
		//dependiendo el canal son lso campos requeridos
		//Validar el canal de que exista

		// Valida Campo requerido id Grupo
		ValidacionUtil.validarCampoRequeridoBO(intIdGrupo, "not.campo.idGrupo");
		
		// Valida Campo requerido canal
		//ValidacionUtil.validarCampoRequeridoBO("Canal", "not.campo.idGrupo");

		// Busca el grupo
		Optional<NotGrupos> objNotGrupos = objNotGruposDAO.findYValidar(intIdGrupo);
		NotGrupos notGrupos = objNotGrupos.get();

		NotDestinatarios objDestinatarios =  null;
		NotGruposDestinatarios objNotGruposDestinatarios = null;
		String uuid = null;
		
		String uuidRedis = null;
		Map<String,String> entity = null;
		List<CamposPersonalizadosDTO> lsCamposPersonalizadosDTO = new ArrayList<CamposPersonalizadosDTO>();

		for (RequestDestinatariosDTO objRequestDestinatariosDTO : lsRequestDestinatariosDTO) {

			lsCamposPersonalizadosDTO.addAll(objRequestDestinatariosDTO.getCamposPersonalizados());
			uuid = UUID.randomUUID().toString();

			objNotGruposDestinatarios = new NotGruposDestinatarios();
			objDestinatarios =  new NotDestinatarios();

			objDestinatarios.setIdDestinatario(uuid);
			
			if (objNotGrupos.get().getCanal().equalsIgnoreCase("C")){
				
				ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getCorreo(), "not.campo.correo");
			} else if(objNotGrupos.get().getCanal().equalsIgnoreCase("M")) {
				ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getTelefonoMovil(), "not.campo.telefonoMovil");
			} else {
				ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getCorreo(), "not.campo.correo");
				ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getTelefonoMovil(), "not.campo.telefonoMovil");
				//ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getTelefonoMovil(), "not.campo.canalesTodos");
			}
			
			//objDestinatarios.setCorreo(objRequestDestinatariosDTO.getCorreo());
			objDestinatarios.setCorreo(objRequestDestinatariosDTO.getCorreo() != null ? objRequestDestinatariosDTO.getCorreo().toUpperCase() : "");
			objDestinatarios.setTelefonoMovil(objRequestDestinatariosDTO.getTelefonoMovil() != null ? objRequestDestinatariosDTO.getTelefonoMovil() : "");
			objDestinatarios.setPrimerNombre(objRequestDestinatariosDTO.getPrimerNombre() != null ? objRequestDestinatariosDTO.getPrimerNombre().toUpperCase() : "");
			objDestinatarios.setPrimerApellido(objRequestDestinatariosDTO.getPrimerApellido()!= null ? objRequestDestinatariosDTO.getPrimerApellido().toUpperCase() : "");
			objDestinatarios.setGenero(objRequestDestinatariosDTO.getGenero() != null ? objRequestDestinatariosDTO.getGenero().toUpperCase() : "");
			objDestinatarios.setFechaNacimiento(objRequestDestinatariosDTO.getFechaNacimiento() != null ? objRequestDestinatariosDTO.getFechaNacimiento() : "");
			objDestinatarios.setPais(objRequestDestinatariosDTO.getPais() != null ? objRequestDestinatariosDTO.getPais().toUpperCase() : "");
			objDestinatarios.setFechaCreacion(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD_HH_MM_SS));
			objDestinatarios.setUsuarioCreacion(usuario.toUpperCase());
			objDestinatarios.setEstado(Estado.ACTIVO.getName());
			objDestinatarios.setIdUsuario(idUsuario);
			objDestinatarios.setIdEmpresa(idEmpresa);
			//objDestinatarios.setIdGrupoDestinatario(3);

			objNotGruposDestinatarios.setNotGrupos(notGrupos);
			objNotGruposDestinatarios.setNotDestinatarios(objDestinatarios);
			objNotGruposDestinatarios.setEstado(Estado.ACTIVO.getName());
			objNotGruposDestinatariosDAO.persist(objNotGruposDestinatarios);
					
			if(!ObjectUtils.isEmpty(objRequestDestinatariosDTO.getCamposPersonalizados())) {
				entity = new HashMap<>();
				uuidRedis = UUID.randomUUID().toString();
				
				for (CamposPersonalizadosDTO camposPersonalizadosDTO : lsCamposPersonalizadosDTO) {
					entity.put(camposPersonalizadosDTO.campo, camposPersonalizadosDTO.valor);
				}
				objDestinatarios.setIdEntidadRedis(uuidRedis);
			}

			objRedisDAO.put(ParametrosRedisEnum.REDIS_GROUP,uuidRedis,entity);

			objNotDestinatariosDAO.persist(objDestinatarios);
		}

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("idGrupo", objNotGrupos.get().getIdGrupo());

		return map;
	}

	@Override
	public Map<String, Object> getDestinatarios(String stridDestinatario) throws BOException {
		// Valida Campo requerido id Destinatario
		ValidacionUtil.validarCampoRequeridoBO(stridDestinatario, "not.campo.idDestinatario");

		Optional<NotDestinatarios> opt = objNotDestinatariosDAO.findYValidar(stridDestinatario);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("row", opt);

		return map;
	}

	@Override
	public Map<String, Object> getDestinatariosGroup(String stridDestinatario) throws BOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> updateDestinatarios(RequestDestinatariosDTO objRequestDestinatariosDTO,
			String idDestinatario, UsuarioLogin usuario) throws BOException {


		// Valida Campo requerido id Grupo
		ValidacionUtil.validarCampoRequeridoBO(idDestinatario, "not.campo.idDestinatario");
		
		Optional<NotDestinatarios> optNotDestinatarios = objNotDestinatariosDAO.findYValidar(idDestinatario);
		
		//Valida campos requeridos
		ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getPrimerNombre(),"not.campo.primerNombre");
		ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getPrimerApellido(),"not.campo.primerApellido");
		ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getGenero(),"not.campo.genero");
		ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getCorreo(),"not.campo.correo");
		ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getTelefonoMovil(),"not.campo.telefonoMovil");
		ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getFechaNacimiento(),"not.campo.fechaNacimiento");
		ValidacionUtil.validarCampoRequeridoBO(objRequestDestinatariosDTO.getPais(),"not.campo.pais");
		
		optNotDestinatarios.get().setFechActualiza(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		optNotDestinatarios.get().setUsuarioActualiza(usuario.getUsuario());
		optNotDestinatarios.get().setCorreo(objRequestDestinatariosDTO.getCorreo() != null ? objRequestDestinatariosDTO.getCorreo().toUpperCase() : "");
		optNotDestinatarios.get().setPrimerNombre(objRequestDestinatariosDTO.getPrimerNombre() != null ? objRequestDestinatariosDTO.getPrimerNombre().toUpperCase() : "");
		optNotDestinatarios.get().setPrimerApellido(objRequestDestinatariosDTO.getPrimerApellido()!= null ? objRequestDestinatariosDTO.getPrimerApellido().toUpperCase() : "");
		optNotDestinatarios.get().setGenero(objRequestDestinatariosDTO.getGenero()!= null ? objRequestDestinatariosDTO.getGenero().toUpperCase() : "");
		optNotDestinatarios.get().setTelefonoMovil(objRequestDestinatariosDTO.getTelefonoMovil()!= null ? objRequestDestinatariosDTO.getTelefonoMovil(): "");
		optNotDestinatarios.get().setFechaNacimiento(objRequestDestinatariosDTO.getFechaNacimiento()!= null ? objRequestDestinatariosDTO.getFechaNacimiento() : "");
		optNotDestinatarios.get().setPais(objRequestDestinatariosDTO.getPais()!= null ? objRequestDestinatariosDTO.getPais(): "");
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("idDestinatario", optNotDestinatarios.get().getIdDestinatario());
		
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
	public Map<String, Object> deleteDestinatarios(String idDestinatario, UsuarioLogin usuario) throws BOException {
		// TODO Auto-generated method stub
		
		// Valida Campo requerido id Grupo
		ValidacionUtil.validarCampoRequeridoBO(idDestinatario, "not.campo.idDestinatario");
		
		NotDestinatarios objNotDestinatarios = objNotDestinatariosDAO.findBy(idDestinatario, usuario.getIdUsuario(), usuario.getIdEmpresa());
		
		objNotDestinatarios.setEstado(Estado.INACTIVO.getName());
		objNotDestinatarios.setFechaInactiva(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objNotDestinatarios.setUsuarioInactiva(usuario.getUsuario());
		
		objNotDestinatariosDAO.update(objNotDestinatarios);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("idDestinatario", objNotDestinatarios);
		
		return map;
		
		/*
		 * Optional<NotDestinatarios> optNotDestinatarios =
		 * objNotDestinatariosDAO.findYValidar(idDestinatario);
		 * 
		 * optNotDestinatarios.get().setEstado(Estado.INACTIVO.getName());
		 * optNotDestinatarios.get().setFechaInactiva(FechasHelper.dateToString(new
		 * Date(), FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		 * optNotDestinatarios.get().setUsuarioInactiva(usuario.getUsuario());
		 * 
		 * objNotDestinatariosDAO.update(optNotDestinatarios.get());
		 * 
		 * Map<String,Object> map=new HashMap<String,Object>();
		 * map.put("idDestinatario", optNotDestinatarios.get().getIdDestinatario());
		 * 
		 * return map;
		 */
		

	}



}
