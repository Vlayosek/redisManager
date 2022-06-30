package com.goit.redis.manager.util;

import java.io.File;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.util.PropertiesUtil;

public class PropertiesRedis extends LoggerOBJ{
	
	private PropertiesUtil config;
	private URL url;
	
	@PostConstruct
	public void cargarPropierties() {
		try {
			
			File archivoProperties = new File("");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	 public String obtenerPropiedad(String clave){
	        return config.getStringProperty(clave,"EOF");
	    }
	    
	    public int getInt(String valor,int porDefecto){
	        return config.getIntegerProperty(valor,porDefecto);
	    }
	    
	    public String getString(String valor,String porDefecto){
	        return config.getStringProperty(valor, porDefecto);
	    }

}
