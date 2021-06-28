package com.carsync.challenge.api.exception;

public class UnauthorizedAccessException extends RuntimeException {

	private static final long serialVersionUID = 3398418389542468808L;

	public UnauthorizedAccessException(String message) {
		super(message);
	}

}
