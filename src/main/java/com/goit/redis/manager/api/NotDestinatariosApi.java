package com.goit.redis.manager.api;

import java.util.List;

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

import com.goit.redis.manager.bo.INotDestinatariosBO;
import com.goit.redis.manager.dto.RequestDestinatariosDTO;
import com.goit.redis.manager.dto.ResponseOk;
import com.goit.redis.manager.dto.UsuarioLogin;
import com.goit.redis.manager.exceptions.BOException;
import com.goit.redis.manager.exceptions.CustomExceptionHandler;
import com.goit.redis.manager.util.MensajesUtil;

@RestController
@RequestMapping("/destinatario")
public class NotDestinatariosApi {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotDestinatariosApi.class);
	
	@Autowired
	private INotDestinatariosBO objINotDestinatariosBO;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> postDestinatario(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idGrupo", required = false) Integer intIdGrupo,
			@RequestBody List<RequestDestinatariosDTO> lsRequestDestinatariosDTO)
			throws BOException {
		try {
			
			System.out.println(".....llega: "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			UsuarioLogin usuarioLogin = (UsuarioLogin)
					SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotDestinatariosBO.saveDestinatarios(lsRequestDestinatariosDTO, intIdGrupo, usuarioLogin.getIdUsuario(), usuarioLogin.getIdEmpresa(), usuarioLogin.getUsuario())), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}
	
	
	@RequestMapping(value = "/getDestinatariosXId", method = RequestMethod.GET)
	public ResponseEntity<?> getDestinatariosXId(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(name = "idDestinatario",required=false) String stridDestinatario)
			throws BOException {

		try {

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotDestinatariosBO.getDestinatarios(stridDestinatario)), HttpStatus.OK);
		} catch (BOException be){
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}
	
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> updateDestinatario(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idDestinatario", required = false) String strIdDestinatario,
			@RequestBody RequestDestinatariosDTO lsRequestDestinatariosDTO)
			throws BOException {
		try {
			
			System.out.println(".....llega: "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			UsuarioLogin usuarioLogin = (UsuarioLogin)
					SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotDestinatariosBO.updateDestinatarios(lsRequestDestinatariosDTO, strIdDestinatario, usuarioLogin)), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteDestinatario(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idDestinatario", required = false) String strIdDestinatario)
			throws BOException {
		try {
			
			System.out.println(".....llega: "+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			UsuarioLogin usuarioLogin = (UsuarioLogin)
					SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotDestinatariosBO.deleteDestinatarios(strIdDestinatario, usuarioLogin)), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}

}
