package com.goit.redis.manager.enums;


public enum AuthenticacionEnum {
	
	BEARER {
		@Override
		public String toString() {
			return "Bearer";
		}
	}, 
	BASIC {
		@Override
		public String toString() {
			return "Basic";
		}
	},
	REFRESH {
		@Override
		public String toString() {
			return "refreshToken";
		}
	};
	
}