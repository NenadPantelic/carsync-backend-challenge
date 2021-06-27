package com.carsync.challenge.api.utils;

import org.apache.commons.text.RandomStringGenerator;

import com.carsync.challenge.api.auth.AuthInterceptor;
import com.carsync.challenge.api.auth.UserContext;
import com.carsync.challenge.api.exception.UnauthorizedAccessException;

public class AuthUtils {

	private static final String AUTH_PREFIX = "Bearer ";

	public static String extractTokenFromHeader(String headerValue) {
		if (headerValue != null && headerValue.startsWith(AUTH_PREFIX)) {
			return headerValue.substring(AUTH_PREFIX.length(), headerValue.length());
		}
		return null;
	}

	public static String generateRandomCode(int length) {
		RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange('0', 'z')
				.filteredBy(Character::isDigit).build();
		return pwdGenerator.generate(length);
	}

	public static Long fetchUserIdFromToken() {
		UserContext userContext = AuthInterceptor.context.get();
		if (userContext == null) {
			throw unauthorized();
		}
		return userContext.getUserId();

	}

	public static UnauthorizedAccessException unauthorized() {
		return new UnauthorizedAccessException("You're not authorized to access this resource!");
	}

}
