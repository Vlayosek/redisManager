package com.goit.redis.manager.util;

import org.apache.commons.lang3.ObjectUtils;

import com.goit.redis.manager.exceptions.BOException;

public class ValidacionUtil {

	private ValidacionUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Valida campo requerido 
	 * @param <T>
	 * @param objCampoRequerido
	 * @param strNombreCampo
	 * @throws BOException
	 */
	public static <T> void validarCampoRequeridoBO(T objCampoRequerido, String strNombreCampo) throws BOException {
	 	
		if (ObjectUtils.isEmpty(objCampoRequerido)) 
			throw new BOException("not.warn.campoObligatorio", new Object[] { strNombreCampo });
		
	}
	
	/**
	 * Valida canal requerido 
	 * @param <T>
	 * @param objCampoRequerido
	 * @param strNombreCampo
	 * @throws BOException
	 */
	public static <T> void validarCanalRequeridoBO(T objCampoRequerido, String strNombreCampo) throws BOException {
	 	
		if (ObjectUtils.isEmpty(strNombreCampo)) 
			throw new BOException("not.warn.headerObligatorio", new Object[] { objCampoRequerido });
	}
}
