package com.goit.redis.manager.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redisCore")
public class CoreRoot extends Application{

	@Override
	public Set<Class<?>> getClasses(){
		HashSet h = new HashSet<Class<?>>();
		h.add(NotDestinatariosApi.class);
		h.add(NotGruposApi.class);
		return h;
		
	}
	
}
