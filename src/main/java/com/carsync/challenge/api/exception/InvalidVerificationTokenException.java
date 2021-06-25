package com.carsync.challenge.api.exception;

public class InvalidVerificationTokenException extends RuntimeException {

	private static final long serialVersionUID = 510240190843567093L;

	public InvalidVerificationTokenException(String message) {
		super(message);
	}

}
