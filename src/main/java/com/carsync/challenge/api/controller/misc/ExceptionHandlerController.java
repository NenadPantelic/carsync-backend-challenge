package com.carsync.challenge.api.controller.misc;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.carsync.challenge.api.exception.APIErrorResponse;
import com.carsync.challenge.api.exception.InvalidVerificationTokenException;
import com.carsync.challenge.api.exception.MailDeliveryException;

@ControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(MailDeliveryException.class)
	public final ResponseEntity<APIErrorResponse> handleMailDeliveryException(Exception ex, WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(InvalidVerificationTokenException.class)
	public final ResponseEntity<APIErrorResponse> handleInvalidVerificationTokenException(Exception ex,
			WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	private ResponseEntity<APIErrorResponse> createExceptionResponseEntity(WebRequest request, Exception ex,
			HttpStatus statusCode) {
		APIErrorResponse errorResponse = new APIErrorResponse(Instant.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorResponse, statusCode);
	}

}
