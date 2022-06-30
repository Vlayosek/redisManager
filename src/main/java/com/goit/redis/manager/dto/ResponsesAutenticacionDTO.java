package com.goit.redis.manager.dto;

import lombok.Data;

@Data
public class ResponsesAutenticacionDTO {
	private Integer code;
	private Boolean success;
	private String message;
}
