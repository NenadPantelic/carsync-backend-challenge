package com.carsync.challenge.api.service.impl;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.dao.UserRepository;
import com.carsync.challenge.api.dao.VerificationTokenRepository;
import com.carsync.challenge.api.dto.request.SignupDTO;
import com.carsync.challenge.api.dto.request.VerifyAccountDTO;
import com.carsync.challenge.api.exception.InvalidVerificationTokenException;
import com.carsync.challenge.api.model.User;
import com.carsync.challenge.api.model.VerificationToken;
import com.carsync.challenge.api.model.utils.Email;
import com.carsync.challenge.api.service.MailService;
import com.carsync.challenge.api.service.SignupService;
import com.carsync.challenge.api.utils.TimestampUtils;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@Service
public class SignupServiceImpl implements SignupService {

	@Autowired
	private VerificationTokenRepository _verificationTokenRepository;

	@Autowired
	private UserRepository _userRepository;

	@Autowired
	private MailService _mailService;

	@Value("${verification-token.time-offset-in-minutes}")
	private long _expirationOffsetInMinutes;

	@Value("${mail.sender}")
	private String _mailSender;

	@Value("${mail.subject}")
	private String _mailSubject;

	@Transactional
	@Override
	public void signup(SignupDTO signupData) {
		VerificationToken token = VerificationToken.builder().email(signupData.getEmail())
				.token(UUID.randomUUID().toString())
				.expirationTime(TimestampUtils.getExpirationTime(getExpirationOffsetInMinutes())).build();
		getVerificationTokenRepository().save(token);
		getMailService().sendMessage(
				new Email(getMailSender(), token.getEmail(), getMailSubject(), token.getToken()));
	}

	@Transactional
	@Override
	public void verifyAccount(VerifyAccountDTO verifyAccountData) {
		String token = verifyAccountData.getVerificationToken();
		VerificationToken verificationToken = getVerificationTokenRepository().findBy_token(token)
				.orElseThrow(() -> new InvalidVerificationTokenException("The verification token is not valid!"));
		String email = verifyAccountData.getEmail();
		checkTokenValidity(verificationToken, email);
		createUser(verifyAccountData);
		getVerificationTokenRepository().deleteBy_email(email);
		// TODO: return login credentials
		// TODO: hash the password

	}

	private void checkTokenValidity(VerificationToken verificationToken, String email) {
		if (!verificationToken.getEmail().equals(email)) {
			throw new InvalidVerificationTokenException(
					"The email you provided doesn't match your secret verification code! Please, check that again or reclaim new token!");
		}
		if (!TimestampUtils.isExpirationTimeValid(verificationToken.getExpirationTime())) {
			throw new InvalidVerificationTokenException(
					"The token you provided is expired. Use another one or reclaim new token!");
		}
	}

	private User createUser(VerifyAccountDTO accountData) {
		User user = User.builder().email(accountData.getEmail()).password(accountData.getPassword()).build();
		return getUserRepository().save(user);
	}

}