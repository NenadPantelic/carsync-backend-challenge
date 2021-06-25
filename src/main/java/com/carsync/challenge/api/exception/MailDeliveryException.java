package com.carsync.challenge.api.exception;

public class MailDeliveryException extends RuntimeException {
	private static final long serialVersionUID = -7819297628962078971L;

	public MailDeliveryException(String message) {
		super(message);
	}
}
