package com.carsync.challenge.api.controller.misc;

import java.time.Instant;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.carsync.challenge.api.exception.APIErrorResponse;
import com.carsync.challenge.api.exception.InvalidActionException;
import com.carsync.challenge.api.exception.InvalidJwtAuthException;
import com.carsync.challenge.api.exception.InvalidVerificationTokenException;
import com.carsync.challenge.api.exception.MessageDeliveryException;
import com.carsync.challenge.api.exception.PasswordDoesNotMatchException;
import com.carsync.challenge.api.exception.UnauthorizedAccessException;
import com.carsync.challenge.api.utils.Messages;

@ControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(MessageDeliveryException.class)
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
	public final ResponseEntity<APIErrorResponse> handleUsernameNotFoundExceptions(Exception ex, WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public final ResponseEntity<APIErrorResponse> handleBadCredentialsException(Exception ex, WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(PasswordDoesNotMatchException.class)
	public final ResponseEntity<APIErrorResponse> handleNonMatchingPasswordException(Exception ex, WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	public final ResponseEntity<APIErrorResponse> handleUnauthorizedAccessException(Exception ex, WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidActionException.class)
	public final ResponseEntity<APIErrorResponse> handleInvalidActionException(Exception ex, WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public final ResponseEntity<APIErrorResponse> handleMethodArgumentNotValidException(Exception ex,
			WebRequest request) {
		return createExceptionResponseEntity(request, ex, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<APIErrorResponse> handleIntegrityViolationException(DataIntegrityViolationException ex,
			WebRequest request) {
		Throwable exception = ex.getRootCause();
		String message = exception.getMessage();
		if (message.contains("Detail")) {
			message = message.split("Detail:")[1];
		}
		if(message.contains("Unique index or primary key violation:")){
			int startIndex = message.indexOf("(");
			int lastIndex = message.indexOf(")");
			message = message.substring(startIndex+1, lastIndex) + " value must be unique.";
		}
		APIErrorResponse exceptionResponse = new APIErrorResponse(Instant.now(), message,
				request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<APIErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		StringBuilder builder = new StringBuilder();
		ex.getConstraintViolations().forEach(constraint -> {
			String message = constraint.getMessage();
			if (message.contains("Detail:")) {
				message = message.split("detail: ")[1];
			}
			builder.append(message);
		});
		APIErrorResponse exceptionResponse = new APIErrorResponse(Instant.now(), builder.toString(),
				request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<APIErrorResponse> createExceptionResponseEntity(WebRequest request, Exception ex,
			HttpStatus statusCode) {
		APIErrorResponse errorResponse = new APIErrorResponse(Instant.now(), createResponseMessage(ex),
				request.getDescription(false));
		createResponseMessage(ex);
		return new ResponseEntity<>(errorResponse, statusCode);
	}

	private String createResponseMessage(Exception e) {
		if (e instanceof MethodArgumentNotValidException) {
			String originalMessage = e.getMessage();
			String[] messageComponents = originalMessage.split("default message");
			String responseMessage = messageComponents[messageComponents.length - 1];
			return responseMessage.replaceAll("[\\[\\](){}]", "");
		} else {
			return e.getMessage();
		}
	}

}
