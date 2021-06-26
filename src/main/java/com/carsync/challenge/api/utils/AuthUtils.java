package com.carsync.challenge.api.utils;

public class AuthUtils {

	private static final String AUTH_PREFIX = "Bearer ";

	public static String extractTokenFromHeader(String headerValue) {
		if (headerValue != null && headerValue.startsWith(AUTH_PREFIX)) {
			return headerValue.substring(AUTH_PREFIX.length(), headerValue.length());
		}
		return null;
	}

}
