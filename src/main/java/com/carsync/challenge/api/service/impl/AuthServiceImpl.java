package com.carsync.challenge.api.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.dao.VerificationTokenRepository;
import com.carsync.challenge.api.dto.request.SignupDTO;
import com.carsync.challenge.api.model.VerificationToken;
import com.carsync.challenge.api.service.AuthService;
import com.carsync.challenge.api.utils.TimestampUtils;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private VerificationTokenRepository _verificationTokenRepository;

	@Value("${verification-token.time-offset-in-minutes}")
	private long _expirationOffsetInMinutes;

	@Override
	public void signup(SignupDTO signupData) {
		VerificationToken token = VerificationToken.builder().email(signupData.getEmail())
				.verificationToken(UUID.randomUUID().toString())
				.expirationTime(TimestampUtils.getExpirationTime(getExpirationOffsetInMinutes())).build();
		getVerificationTokenRepository().save(token);
		
		// TODO: send an email with the token via Mailgun
	}

}
