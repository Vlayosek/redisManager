package com.goit.redis.manager.util;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goit.redis.manager.dto.ResponsesAutenticacionDTO;
import com.goit.redis.manager.enums.ParametrosEnum;
import com.goit.redis.manager.exceptions.BOException;
import com.goit.redis.manager.exceptions.RestClientException;
import com.goit.redis.manager.exceptions.UnauthorizedException;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Service
public class ServiciosUtil {
	
	// Variables para Tiempo de Conexion
	private static Integer READ_TIME_OUT = 30000; // 30 segundos
	private static Integer CONNECT_TIME_OUT = 5000; // 5 Segundos
	
	@Autowired
	private Gson gson;
	
	/**
	 * Valida las credenciales de un usuario.
	 * 
	 * @author Bryan Zamora
	 * @param strAuthorization
	 * @param strLanguage
	 * @param strCanal
	 * @return
	 * @throws RestClientException
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws BOException
	 * @throws UnauthorizedException 
	 */
	public ResponsesAutenticacionDTO validarToken(
			String strAuthorization, 
			String strLanguage,
			String strCanal)
			throws RestClientException, JsonMappingException, JsonProcessingException, BOException, UnauthorizedException {
		
		ObjectMapper objectMapper = new ObjectMapper();

//		// Consulta base url
		//TODO: se debe cambiar para obtenerse de properties o tabla de parametros
		String strBaseUrl = ParametrosEnum.GOIT_SEGURIDAD_API.getName();

		//String strBaseUrl="http://localhost:8080/seguridad/";
		
		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		client.setReadTimeout(READ_TIME_OUT);
		client.setConnectTimeout(CONNECT_TIME_OUT);
		//TODO: se debe cambiar para obtenerse de properties o tabla de parametros
		WebResource webResource = client.resource(strBaseUrl + "/v1/autenticacion/validarToken");
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.header("content-type", MediaType.APPLICATION_JSON)
				.header("Accept-Language", strLanguage)
				.header("Authorization",strAuthorization)
				.header("canal",strCanal)
				.post(ClientResponse.class);

		String strResponseEntity = response.getEntity(String.class);
		System.out.println(strResponseEntity);
		JsonNode jsonNode = objectMapper.readTree(strResponseEntity);
		ResponsesAutenticacionDTO objResponsesAutenticacionDTO;
		if (response.getStatus() == 200) {
			objResponsesAutenticacionDTO = gson.fromJson(strResponseEntity, ResponsesAutenticacionDTO.class);
			return objResponsesAutenticacionDTO;
		} else if (response.getStatus() == 400) {
			System.out.println("400");
			throw new BOException(jsonNode.get("message").asText());
		} else if (response.getStatus() == 401) {
			System.out.println("401");
			throw new UnauthorizedException(jsonNode.get("message").asText(),
					jsonNode.get("errorData") != null ? jsonNode.get("errorData") : new Object[] {});
		} else {
			System.out.println("40*");
			throw new RestClientException(jsonNode.get("message").asText(),
					jsonNode.get("errorData") != null ? jsonNode.get("errorData") : new Object[] {});
		}

	}
}
