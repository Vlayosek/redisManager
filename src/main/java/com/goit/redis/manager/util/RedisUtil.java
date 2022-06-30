package com.goit.redis.manager.util;

import java.util.Date;

public class RedisUtil {
	
	PropertiesRedis propsRedis;
	
	private int maxLength;
    private String characters;
    private boolean charged = false; 
    
    public String getUniqueID() {
    	
    	StringBuilder finalID = null;
    	finalID = new StringBuilder();
    	
    	try {
    		if(!charged) {
    			maxLength = propsRedis.getInt("redis.core.id.maxlength", 10);
    			characters = propsRedis.getString("redis.core.id.chararray", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    			charged = true;
    		}
    		while(finalID.toString().length() < maxLength) {
    			int position = getRandom(0,characters.length()-1);
    			finalID.append(characters.charAt(position));
    		}
			
		} catch (Exception e) {
			// TODO: handle exception
			finalID.append(getUniqueId(1000, 5000));
		}
    	
		return finalID.toString();
    	
    }
    
    private int getRandom(int init,int finish){
        return (int) (Math.random() * finish) + init;
    }
    
    public String getUniqueId(int init,int finish){
        String id="";
        Date date;
        try{
           date = new Date();
           id = (Long.toHexString(date.getTime())+Integer.toHexString(getRandom(init,finish)));
           //Obtiene un hexadecimal unico uniendo la fecha en expresion unix, junto con un numero
           //aleatorio de 1000 a 5000 , y lo devuelve como cadena de texto
        }catch(Exception ex){
           ex.printStackTrace(System.err);
        }
        return id;
    }

}
