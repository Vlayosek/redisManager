package com.goit.redis.manager.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goit.redis.manager.bo.INotGruposBO;
import com.goit.redis.manager.dto.CriteriosFiltrado;
import com.goit.redis.manager.dto.RequestGrupoDTO;
import com.goit.redis.manager.dto.ResponseOk;
import com.goit.redis.manager.dto.UsuarioLogin;
import com.goit.redis.manager.exceptions.BOException;
import com.goit.redis.manager.exceptions.CustomExceptionHandler;
import com.goit.redis.manager.util.MensajesUtil;

@RestController
@RequestMapping("/grupos")
public class NotGruposApi {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotGruposApi.class);
	
	@Autowired
	private INotGruposBO objINotGruposBO;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> postGrupos(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestGrupoDTO objRequestGrupoDTO)
		throws BOException {
		try {
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotGruposBO.postGrupos(objRequestGrupoDTO,usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario())
					), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	
	}
	
	@RequestMapping(value = "/grupo-segmento",method = RequestMethod.POST)
	public ResponseEntity<?> postGrupoSegmento(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody CriteriosFiltrado objCriteriosFiltrado,
			@RequestParam(value = "idGrupo", required = false) String strIdGrupo)
		throws BOException {
		try {
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotGruposBO.postGrupoSegmento(objCriteriosFiltrado,usuarioLogin,strIdGrupo)
					), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	
	}
	
	@RequestMapping(value = "/getGroup", method = RequestMethod.GET)
	public ResponseEntity<?> getGroupsById(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idGrupo", required = false) Integer intIdGrupo
	// @RequestParam(value = "typeGrupo", required = false) String strTypeGrupo
	) throws BOException {
		try {

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotGruposBO.getGrupo(intIdGrupo)), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}

	@RequestMapping(value = "/getGroups", method = RequestMethod.GET)
	public ResponseEntity<?> getGroups(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "tipoCanal", required = false) String strTipoCanal) throws BOException {
		try {
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok",
							MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotGruposBO.getGrupos(strTipoCanal, usuarioLogin)), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new
					CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}
	
	@RequestMapping(value = "/getDestinatarioXGrupos", method = RequestMethod.GET)
	public ResponseEntity<?> getDestinatariosXGrupos(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idGrupo", 	required = false) Integer intIdGrupo
					) throws BOException {
		try {

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok",
							MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotGruposBO.getGruposDestinatarios(intIdGrupo)), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}
	
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> putGrupo(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestParam(value = "idGrupo", required = false) Integer intIdGrupo,
			@RequestBody RequestGrupoDTO objRequestGrupoDTO)
		throws BOException {
		try {
			logger.info("Peticion entrante de Actualizacion de Grupo....");
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotGruposBO.putGrupos(objRequestGrupoDTO,usuarioLogin,intIdGrupo)
					), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteGrupo(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestParam(value = "idGrupo", required = false) Integer intIdGrupo)
		throws BOException {
		try {
			logger.info("Peticion entrante de Eliminación de Grupo....");
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotGruposBO.deleteGrupo(intIdGrupo, usuarioLogin)
					), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	
	}
	
	@RequestMapping(value = "/cambiar-tipo",method = RequestMethod.PUT)
	public ResponseEntity<?> cambiarTipo(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestParam(value = "idGrupo", required = false) Integer intIdGrupo)
		throws BOException {
		try {
			logger.info("Peticion entrante de Eliminación de Grupo....");
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotGruposBO.cambiarTipo(intIdGrupo, usuarioLogin)
					), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	
	}
	
	
	@RequestMapping(value = "/grupo-destinatarios", method = RequestMethod.GET)
	public ResponseEntity<?> allGruposDestinatarios(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "tipoGrupo", 	required = false) String strTipoGrupo
					) throws BOException {
		try {
			logger.info("Peticion entrante de obtencion Grupos con destinatarios....");
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok",
							MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotGruposBO.allGruposDestinatarios(strTipoGrupo,usuarioLogin)), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}

}
