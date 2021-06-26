package com.carsync.challenge.api.controller.misc;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.carsync.challenge.api.exception.APIErrorResponse;
import com.carsync.challenge.api.exception.InvalidJwtAuthException;
import com.carsync.challenge.api.exception.InvalidVerificationTokenException;
import com.carsync.challenge.api.exception.MailDeliveryException;
import com.carsync.challenge.api.utils.Messages;

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

	@ExceptionHandler(InvalidJwtAuthException.class)
	public final ResponseEntity<APIErrorResponse> handleInvalidJwtAuthenticationException(Exception ex,
			WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InsufficientAuthenticationException.class)
	public final ResponseEntity<APIErrorResponse> handleInsufficientAuthenticationException(Exception ex,
			WebRequest request) {
		APIErrorResponse exceptionResponse = new APIErrorResponse(Instant.now(), Messages.INVALID_JWT_OR_NON_AUTH,
				request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<APIErrorResponse> usernameNotFoundExceptions(Exception ex, WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public final ResponseEntity<APIErrorResponse> badCredentialsException(Exception ex, WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.UNAUTHORIZED);
	}

	private ResponseEntity<APIErrorResponse> createExceptionResponseEntity(WebRequest request, Exception ex,
			HttpStatus statusCode) {
		APIErrorResponse errorResponse = new APIErrorResponse(Instant.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorResponse, statusCode);
	}

}
