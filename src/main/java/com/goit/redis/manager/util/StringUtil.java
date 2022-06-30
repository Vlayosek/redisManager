package com.goit.redis.manager.util;

import com.goit.redis.manager.exceptions.BOException;


public class StringUtil {
	
	/**
	 * Permite descomponer una cadena de cararteres separado por un caracter en
	 * especifico
	 * 
	 * @author Bryan Zamora
	 * @param strCadena
	 * @param strCaracter
	 * @return parts
	 */
	public static String[] descomponerCadenaSeparadoPorCaracter(String strCadena, String strCaracter) throws BOException {
		String[] parts = null;
		try {
			parts = strCadena.split(strCaracter);
		} catch (Exception e) {
			throw new BOException("con.error.descomponerCadena");
		}
		return parts;
	}
}
