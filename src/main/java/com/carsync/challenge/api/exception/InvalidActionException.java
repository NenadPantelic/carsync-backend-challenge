package com.carsync.challenge.api.exception;

public class InvalidActionException extends RuntimeException {
	private static final long serialVersionUID = 2574249925802006710L;

	public InvalidActionException(String message) {
		super(message);
	}
}
