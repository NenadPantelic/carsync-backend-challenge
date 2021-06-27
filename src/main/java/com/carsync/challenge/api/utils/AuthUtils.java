package com.carsync.challenge.api.utils;

import org.apache.commons.text.RandomStringGenerator;

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

}
