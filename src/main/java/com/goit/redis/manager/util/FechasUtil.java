package com.goit.redis.manager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.goit.redis.manager.enums.FormatoFechaEnum;

public class FechasUtil {

	/**
	 * Valida si el formato de una fecha en String es correcto.
	 * 
	 * @author Bryan Zamora
	 * @param strDate      Fecha en String
	 * @param formatoFecha Tipo de formato de fecha a evaluar.
	 * @return
	 */
	public static boolean formatoFechaValido(String strDate, FormatoFechaEnum formatoFecha) {
		if (strDate.trim().equals("")) {
			return false;
		}
		SimpleDateFormat sdfrmt = new SimpleDateFormat(formatoFecha.getName());
		sdfrmt.setLenient(false);
		try {
			sdfrmt.parse(strDate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Valida si la fecha indicada es futura respecto a otra fecha.
	 * 
	 * @author Vladimir Kozisck.
	 * @param fechaFin fecha en Date
	 * @param fechaIni fecha en Date
	 * @return
	 */
	public static boolean esFechaFutura(Date fechaFin, Date fechaIni) {
		return fechaFin.before(fechaIni);
	}

	/**
	 * Convierte una fecha string a tipo de dato java.util.Date
	 * 
	 * @author Bryan Zamora
	 * @param strFecha
	 * @return
	 */
	public static Date stringToDate(String strFecha, FormatoFechaEnum formatoFecha) {
		if (!formatoFechaValido(strFecha, formatoFecha)) {
			throw new RuntimeException("La fecha no cumple con el formato indicado.");
		}
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(formatoFecha.getName());
			return formatter.parse(strFecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Convierte una fecha Date a String
	 * 
	 * @author Bryan Zamora
	 * @param datFecha
	 * @param formatoFecha
	 * @return
	 */
	public static String dateToString(Date datFecha, FormatoFechaEnum formatoFecha) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatoFecha.getName());
		return formatter.format(datFecha);
	}
	

	/**
	 * Suma los minutos a la fecha
	 * 
	 * @author Bryan Zamora
	 * @param date
	 * @param strMinutos
	 * @return
	 */
	public static Date sumarMinutosFecha(Date date,String strMinutos) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE,new Integer(strMinutos));
		return calendar.getTime();
	}
	
}
