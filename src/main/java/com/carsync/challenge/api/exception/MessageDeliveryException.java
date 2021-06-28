package com.carsync.challenge.api.exception;

public class MessageDeliveryException extends RuntimeException {
	private static final long serialVersionUID = -7819297628962078971L;

	public MessageDeliveryException(String message) {
		super(message);
	}
}
