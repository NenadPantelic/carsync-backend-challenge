package com.carsync.challenge.api.exception;

public class PasswordDoesNotMatchException extends RuntimeException {

	private static final long serialVersionUID = -53709313253580409L;

	public PasswordDoesNotMatchException(String message) {
		super(message);
	}

}
