package com.carsync.challenge.api.utils;

import java.util.UUID;

import com.carsync.challenge.api.model.AuthToken;
import com.carsync.challenge.api.model.TwoFAToken;
import com.carsync.challenge.api.model.VerificationToken;
import com.carsync.challenge.api.model.utils.TokenType;

public class FactoryUtils {

	public static AuthToken createToken(TokenType tokenType, String contact, long timeOffset) {

		if (tokenType.equals(TokenType.SIGNUP_VERIFICATION)) {
			VerificationToken verificationToken = VerificationToken.builder().email(contact)
					.token(UUID.randomUUID().toString()).build();
			verificationToken.setExpirationTime(TimestampUtils.getExpirationTime(timeOffset));
			return verificationToken;
		} else if (tokenType.equals(TokenType.TWO_FA)) {
			TwoFAToken twoFAToken = TwoFAToken.builder().phoneNo(contact).token(AuthUtils.generateRandomCode(6))
					.build();
			twoFAToken.setExpirationTime(TimestampUtils.getExpirationTime(timeOffset));
			return twoFAToken;
		} else {
			return null;
		}
	}

}
