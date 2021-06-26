package com.carsync.challenge.api.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthException extends AuthenticationException {
	private static final long serialVersionUID = 3332960242637578807L;

	public InvalidJwtAuthException(String message) {
		super(message);
	}
}
