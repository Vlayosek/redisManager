package com.goit.redis.manager.bo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.goit.helper.FechasHelper;
import com.goit.helper.enums.Estado;
import com.goit.helper.enums.FormatoFecha;
import com.goit.redis.manager.bo.INotGruposBO;
import com.goit.redis.manager.dao.NotGruposDAO;
import com.goit.redis.manager.dao.NotGruposDestinatariosDAO;
import com.goit.redis.manager.dto.CamposFiltradosDTO;
import com.goit.redis.manager.dto.CriteriosFiltrado;
import com.goit.redis.manager.dto.DestinatariosDTO;
import com.goit.redis.manager.dto.GetGruposDestinatariosDTO;
import com.goit.redis.manager.dto.GruposAndDestinatariosDTO;
import com.goit.redis.manager.dto.GruposDTO;
import com.goit.redis.manager.dto.GruposDestinatarios;
import com.goit.redis.manager.dto.GruposDestinatariosDTO;
import com.goit.redis.manager.dto.ObtenerGrupoDTO;
import com.goit.redis.manager.dto.RequestGrupoDTO;
import com.goit.redis.manager.dto.UsuarioLogin;
import com.goit.redis.manager.enums.TiposCanalEnum;
import com.goit.redis.manager.enums.TiposDatoEnum;
import com.goit.redis.manager.exceptions.BOException;
import com.goit.redis.manager.model.NotDestinatarios;
import com.goit.redis.manager.model.NotGrupos;
import com.goit.redis.manager.model.NotGruposDestinatarios;
import com.goit.redis.manager.util.ValidacionUtil;

@Service
public class NotGruposBOImpl implements INotGruposBO{

	@Autowired
	private NotGruposDAO objNotGruposDAO;
	
	@Autowired
	private NotGruposDestinatariosDAO objNotGruposDestinatariosDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class}) 
	public Map<String,Object> postGrupos(RequestGrupoDTO objRequestGrupoDTO, String idUsuario, String idEmpresa, String usuario)
			throws BOException {
		
		//Valida campos requeridos
		ValidacionUtil.validarCampoRequeridoBO(objRequestGrupoDTO.getNombre(),"not.campo.nombre");
		ValidacionUtil.validarCampoRequeridoBO(objRequestGrupoDTO.getDescripcion(),"not.campo.descripcion");
		//ValidacionUtil.validarCampoRequeridoBO(objRequestGrupoDTO.getTipo(),"not.campo.tipo");
		ValidacionUtil.validarCampoRequeridoBO(objRequestGrupoDTO.getCanal(),"not.campo.canal");
		
		
		//Se define canalaes
		String[] strCanales = { TiposCanalEnum.CORREO.getName(),TiposCanalEnum.MENSAJERIA.getName(), TiposCanalEnum.TODOS.getName() };
		// VALIDA TIPO 
		if (!Arrays.stream(strCanales).anyMatch(StringUtils.upperCase(objRequestGrupoDTO.getCanal())::equals)) 
			throw new BOException("not.warn.paramNoValidCanal");
		
		//Se define tipo
		String[] strTipo = { TiposDatoEnum.GRUPO.getName(),TiposDatoEnum.ESTRACTO.getName()};
		
		// VALIDA TIPO 
		if (!Arrays.stream(strTipo).anyMatch(StringUtils.upperCase(objRequestGrupoDTO.getTipo())::equals)) 
			throw new BOException("not.warn.paramNoValidTipo");
		
		//Setea los datos a persistir
		NotGrupos objNotGrupos=new NotGrupos();	
		objNotGrupos.setFechaCreacion(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objNotGrupos.setUsuarioCreacion(usuario.toUpperCase());
		objNotGrupos.setNombre(objRequestGrupoDTO.getNombre().toUpperCase());
		objNotGrupos.setDescripcion(objRequestGrupoDTO.getDescripcion().toUpperCase());
		objNotGrupos.setEstado(Estado.ACTIVO.getName());
		objNotGrupos.setTipo(TiposDatoEnum.GRUPO.getName());
		objNotGrupos.setCanal(objRequestGrupoDTO.getCanal().toUpperCase());
		objNotGrupos.setIdEmpresa(idEmpresa);
		objNotGrupos.setIdUsuario(idUsuario);
		
		//Valida que la cadena este separado con el caracter ,
		if (!ObjectUtils.isEmpty(objRequestGrupoDTO.getCamposPersonalizados())) {
			objNotGrupos.setCamposPersonalizados(objRequestGrupoDTO.getCamposPersonalizados()
					.toString()
					.replace("[","")
					.replace("]","")
					.replace(" ", "")
					.toUpperCase());	
		}
		
		//Inserta
		objNotGruposDAO.persist(objNotGrupos);
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("idGrupo", objNotGrupos.getIdGrupo());
		
		return map;
	}

	@Override
	public Map<String, Object> getGrupo(Integer intIdGrupo) throws BOException {

		//Valida campos requeridos
		ValidacionUtil.validarCampoRequeridoBO(intIdGrupo,"not.campo.idGrupo");
		
		// Busca el grupo
		Optional<NotGrupos> objNotGrupos = objNotGruposDAO.findYValidar(intIdGrupo);
		
		ObtenerGrupoDTO objObtenerGrupoDTO = new ObtenerGrupoDTO();
		objObtenerGrupoDTO.setIdGrupo(objNotGrupos.get().getIdGrupo());
		objObtenerGrupoDTO.setNombre(objNotGrupos.get().getNombre());
		objObtenerGrupoDTO.setDescripcion(objNotGrupos.get().getDescripcion());
		objObtenerGrupoDTO.setTipo(objNotGrupos.get().getTipo());
		objObtenerGrupoDTO.setEstado(objNotGrupos.get().getEstado());
		objObtenerGrupoDTO.setCamposPersonalizados(objNotGrupos.get().getCamposPersonalizados());	
		
		Map<String, Object> mapResult = new HashMap<>();
		
		mapResult.put("row", objObtenerGrupoDTO);
				
		return mapResult;
	}

	@Override
	public Map<String, Object> getGrupos(String strTipoCanal,UsuarioLogin usuarioLogin) throws BOException {
		
		
		ValidacionUtil.validarCampoRequeridoBO(strTipoCanal,"not.campo.canal");
		
		
		//Se define canalaes
		String[] strCanales = { TiposCanalEnum.CORREO.getName(),TiposCanalEnum.MENSAJERIA.getName()};
		// VALIDA TIPO 
		if (!Arrays.stream(strCanales).anyMatch(StringUtils.upperCase(strTipoCanal)::equals)) 
			throw new BOException("not.warn.paramNoValidCanal");
		
		List<String> lCanales = new ArrayList<String>();
		lCanales.add(TiposCanalEnum.TODOS.getName());
		lCanales.add(strTipoCanal);

		List<GruposDTO> lsConsultarGrupos = objNotGruposDAO.consultarGrupos(lCanales, usuarioLogin.getIdUsuario(), usuarioLogin.getIdEmpresa());
		
		Map<String,Object> mapResult = new HashMap<String,Object>();

		mapResult.put("totalRows", lsConsultarGrupos.size());
		mapResult.put("row", lsConsultarGrupos);

		return mapResult;

	}

	@Override
	public Map<String, Object> getGruposDestinatarios(Integer intIdGrupo) throws BOException {
		// TODO Auto-generated method stub
		 System.out.println(".......==>> "+intIdGrupo);
		// Busca el grupo
		Optional<NotGrupos> objNotGrupos = objNotGruposDAO.findYValidar(intIdGrupo);
		
		List<NotDestinatarios> lsGruposDestinatarios = objNotGruposDAO.consultarGruposDestinatarios(intIdGrupo);
		
		GetGruposDestinatariosDTO objGetGruposDestinatariosDTO = new GetGruposDestinatariosDTO();
		
		objGetGruposDestinatariosDTO.setLsNotDestinatarios(lsGruposDestinatarios);
		objGetGruposDestinatariosDTO.setNotGrupos(objNotGrupos.get());
		objGetGruposDestinatariosDTO.setEstado(objNotGrupos.get().getEstado());
		
				
		Map<String,Object> mapResult = new HashMap<String,Object>();
		
		mapResult.put("row", objGetGruposDestinatariosDTO);
		return mapResult;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
	public Map<String, Object> postGrupoSegmento(CriteriosFiltrado objCriteriosFiltrado,
			UsuarioLogin usuarioLogin, String idGrupo) throws BOException {

		//Valida campos requeridos
		ValidacionUtil.validarCampoRequeridoBO(idGrupo,"not.campo.idGrupo");
		ValidacionUtil.validarCampoRequeridoBO(objCriteriosFiltrado.getNombreSegmento(),"not.campo.nomSegmento");
		ValidacionUtil.validarCampoRequeridoBO(objCriteriosFiltrado.getObjetivoSegmento(),"not.campo.objSegmento");
		
		Optional<NotGrupos> optNotGrupos = null;
		List<GruposDestinatariosDTO> lsGetGruposDestinatariosDTO= null;
		List<NotDestinatarios> lsGruposDestinatarios = new ArrayList<NotDestinatarios>();
		List<CamposFiltradosDTO> lsCamposFiltrados = objCriteriosFiltrado.getCamposFiltrados();
		
		Long existeGrupo = null;
		
		// VALIDA que lleguen los criterios de Filtrados
		if (objCriteriosFiltrado.getCamposFiltrados().size() == 0) 
			throw new BOException("not.warn.paramCriteriosBusqueda");
		
		//Valido los Criterios de Filtrado que sean requeridos
		for (CamposFiltradosDTO forCamposFiltradosDTO : objCriteriosFiltrado.getCamposFiltrados()) {
			ValidacionUtil.validarCampoRequeridoBO(forCamposFiltradosDTO.getCampo(),"not.campo.filtCampo");
			ValidacionUtil.validarCampoRequeridoBO(forCamposFiltradosDTO.getCriterio(),"not.campo.filtCriterio");
			ValidacionUtil.validarCampoRequeridoBO(forCamposFiltradosDTO.getValorCriterio(),"not.campo.filtValor");
		}

		String[] strExt = idGrupo.toUpperCase().split(",");
		Integer __idGrupo = null;
		
		for (String string : strExt) {
			existeGrupo = objNotGruposDAO.existeGrupo(Integer.parseInt(string));
			
			if(!(existeGrupo > 0)) {
				throw new BOException("not.warn.grupoNoExiste", new Object[] { string });
			}
			
			__idGrupo = Integer.parseInt(string);
			//lsGetGruposDestinatariosDTO = objNotGruposDestinatariosDAO.getDestinatariosXGrupo_v2(__idGrupo);
			lsGruposDestinatarios.addAll(objNotGruposDAO.consultarGruposDestinatarios_v2(__idGrupo,lsCamposFiltrados));
			
		}
		
		//Setea los datos a persistir
		NotGrupos objNotGrupos=new NotGrupos();	
		objNotGrupos.setFechaCreacion(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objNotGrupos.setUsuarioCreacion(usuarioLogin.getUsuario().toUpperCase());
		objNotGrupos.setNombre(objCriteriosFiltrado.getNombreSegmento().toUpperCase());
		objNotGrupos.setDescripcion(objCriteriosFiltrado.getObjetivoSegmento().toUpperCase());
		objNotGrupos.setEstado(Estado.ACTIVO.getName());
		objNotGrupos.setTipo(TiposDatoEnum.ESTRACTO.getName());
		objNotGrupos.setCanal(objCriteriosFiltrado.getCanal().toUpperCase());
		objNotGrupos.setIdEmpresa(usuarioLogin.getIdEmpresa());
		objNotGrupos.setIdUsuario(usuarioLogin.getIdUsuario());
		
		objNotGruposDAO.persist(objNotGrupos);
		
		NotGruposDestinatarios objNotGruposDestinatarios = null;
		
		for (NotDestinatarios string : lsGruposDestinatarios) {
			objNotGruposDestinatarios = new NotGruposDestinatarios();
			objNotGruposDestinatarios.setNotGrupos(objNotGrupos);
			objNotGruposDestinatarios.setNotDestinatarios(string);
			objNotGruposDestinatarios.setEstado(Estado.ACTIVO.getName());
			objNotGruposDestinatariosDAO.persist(objNotGruposDestinatarios);
		}
				
	  Map<String,Object> mapResult = new HashMap<String,Object>();
	  mapResult.put("cont", lsGruposDestinatarios.size()); 
	  mapResult.put("row",lsGruposDestinatarios);
		 

		return mapResult;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
	public Map<String, Object> putGrupos(RequestGrupoDTO objRequestGrupoDTO, UsuarioLogin usuario, Integer intIdGrupo) throws BOException {
		//Valida campos requeridos
		ValidacionUtil.validarCampoRequeridoBO(intIdGrupo,"not.campo.idGrupo");
		
		Optional<NotGrupos> optNotGrupos = objNotGruposDAO.findYValidar(intIdGrupo);

		//Valida campos requeridos
		ValidacionUtil.validarCampoRequeridoBO(objRequestGrupoDTO.getNombre(),"not.campo.nombre");
		ValidacionUtil.validarCampoRequeridoBO(objRequestGrupoDTO.getDescripcion(),"not.campo.descripcion");
		ValidacionUtil.validarCampoRequeridoBO(objRequestGrupoDTO.getTipo(),"not.campo.tipo");
		ValidacionUtil.validarCampoRequeridoBO(objRequestGrupoDTO.getCanal(),"not.campo.canal");
		
		
		//Se define canalaes
		String[] strCanales = { TiposCanalEnum.CORREO.getName(),TiposCanalEnum.MENSAJERIA.getName(), TiposCanalEnum.TODOS.getName() };
		// VALIDA TIPO 
		if (!Arrays.stream(strCanales).anyMatch(StringUtils.upperCase(objRequestGrupoDTO.getCanal())::equals)) 
			throw new BOException("not.warn.paramNoValidCanal");
		
		//Se define tipo
		String[] strTipo = { TiposDatoEnum.GRUPO.getName(),TiposDatoEnum.ESTRACTO.getName()};
		
		// VALIDA TIPO 
		if (!Arrays.stream(strTipo).anyMatch(StringUtils.upperCase(objRequestGrupoDTO.getTipo())::equals)) 
			throw new BOException("not.warn.paramNoValidTipo");
		
		optNotGrupos.get().setFechActualiza(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		optNotGrupos.get().setUsuarioActualiza(usuario.getUsuario());
		optNotGrupos.get().setNombre(objRequestGrupoDTO.getNombre());
		optNotGrupos.get().setDescripcion(objRequestGrupoDTO.getDescripcion());
		optNotGrupos.get().setTipo(objRequestGrupoDTO.getTipo());
		optNotGrupos.get().setCanal(objRequestGrupoDTO.getCanal());
		optNotGrupos.get().setIdEmpresa(usuario.getIdEmpresa());
		optNotGrupos.get().setIdUsuario(usuario.getIdUsuario());
		
		//Valida que la cadena este separado con el caracter ,
		if (!ObjectUtils.isEmpty(objRequestGrupoDTO.getCamposPersonalizados())) {
			optNotGrupos.get().setCamposPersonalizados(objRequestGrupoDTO.getCamposPersonalizados()
					.toString()
					.replace("[","")
					.replace("]","")
					.replace(" ", "")
					.toUpperCase());	
		}
		
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("idGrupo", optNotGrupos.get().getIdGrupo());
		
		return map;
				
				
	}

	@Override
	public Map<String, Object> allGruposDestinatarios(String strTipoGrupo, UsuarioLogin usuarioLogin) throws BOException {
		
		ValidacionUtil.validarCampoRequeridoBO(strTipoGrupo,"not.campo.typeGrupo");
		
		//Se define tipo
		String[] strTipo = { TiposDatoEnum.GRUPO.getName(),TiposDatoEnum.ESTRACTO.getName()};
		
		// VALIDA TIPO 
		if (!Arrays.stream(strTipo).anyMatch(StringUtils.upperCase(strTipoGrupo)::equals)) 
			throw new BOException("not.warn.paramNoValidTipo");
		
		//Obtengo los grupos por el tipo
		List<ObtenerGrupoDTO> lsObtenerGrupoDTO = objNotGruposDAO.findGroup(strTipoGrupo, usuarioLogin.getIdUsuario(), usuarioLogin.getIdEmpresa());
		
		//Creacion Variables
		List<GruposAndDestinatariosDTO> lsGruposAndDestinatariosDTO =  new ArrayList<GruposAndDestinatariosDTO>();
		GruposAndDestinatariosDTO objGruposAndDestinatariosDTO = null;
		List<DestinatariosDTO> lsDestinatariosDTO = null;

		//Recorro el listado de grupos para obtener los destinatarios por grupo
		for (ObtenerGrupoDTO listabtenerGrupoDTO : lsObtenerGrupoDTO) {
			lsDestinatariosDTO = new ArrayList<DestinatariosDTO>();
			lsDestinatariosDTO = objNotGruposDAO.allGruposDestinatarios(listabtenerGrupoDTO.getIdGrupo());

			objGruposAndDestinatariosDTO = new GruposAndDestinatariosDTO();
			objGruposAndDestinatariosDTO.setNombre(listabtenerGrupoDTO.getNombre());
			objGruposAndDestinatariosDTO.setCantidadDestinatarios(listabtenerGrupoDTO.getCantidadDestinatarios() != null ? listabtenerGrupoDTO.getCantidadDestinatarios() : null);
			objGruposAndDestinatariosDTO.setFechaCreacion(FechasHelper.dateToString(listabtenerGrupoDTO.getFechaCreacion(), FormatoFecha.YYYY_MM_DD_HH_MM_SS) );
			objGruposAndDestinatariosDTO.setListaDestinatarios(lsDestinatariosDTO);
			
			lsGruposAndDestinatariosDTO.add(objGruposAndDestinatariosDTO);
		}

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("row", lsGruposAndDestinatariosDTO);
		
		return map;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
	public Map<String, Object> deleteGrupo(Integer intIdGrupo, UsuarioLogin usuarioLogin) throws BOException {
		// TODO Auto-generated method stub
		//Valida campos requeridos
		ValidacionUtil.validarCampoRequeridoBO(intIdGrupo,"not.campo.idGrupo");
		
		NotGrupos objNotGrupos = objNotGruposDAO.findBy(intIdGrupo, usuarioLogin.getIdUsuario(), usuarioLogin.getIdEmpresa());
		
		objNotGrupos.setEstado(Estado.INACTIVO.getName());
		objNotGrupos.setFechaInactiva(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objNotGrupos.setUsuarioInactiva(usuarioLogin.getUsuario());
		
		objNotGruposDAO.update(objNotGrupos);
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("idGrupo", objNotGrupos);
		
		return map;
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
	public Map<String, Object> cambiarTipo(Integer intIdGrupo, UsuarioLogin usuarioLogin) throws BOException {
		// TODO Auto-generated method stub
		//Valida campos requeridos
		ValidacionUtil.validarCampoRequeridoBO(intIdGrupo,"not.campo.idGrupo");
		
		NotGrupos objNotGrupos = objNotGruposDAO.findBy(intIdGrupo, usuarioLogin.getIdUsuario(), usuarioLogin.getIdEmpresa());
		
		objNotGrupos.setTipo(TiposDatoEnum.GRUPO.getName());
		objNotGrupos.setFechActualiza(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objNotGrupos.setUsuarioActualiza(usuarioLogin.getUsuario());
		
		objNotGruposDAO.update(objNotGrupos);
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("idGrupo", objNotGrupos);
		
		return map;
	}

}
