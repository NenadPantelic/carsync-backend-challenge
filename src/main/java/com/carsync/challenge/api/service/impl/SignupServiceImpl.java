package com.carsync.challenge.api.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.dao.UserRepository;
import com.carsync.challenge.api.dao.VerificationTokenRepository;
import com.carsync.challenge.api.dto.request.AuthRequestDTO;
import com.carsync.challenge.api.dto.request.SignupDTO;
import com.carsync.challenge.api.dto.request.VerificationRequestDTO;
import com.carsync.challenge.api.dto.request.VerifyAccountDTO;
import com.carsync.challenge.api.exception.InvalidVerificationTokenException;
import com.carsync.challenge.api.model.User;
import com.carsync.challenge.api.model.VerificationToken;
import com.carsync.challenge.api.model.utils.Email;
import com.carsync.challenge.api.model.utils.TokenType;
import com.carsync.challenge.api.service.AuthRequestService;
import com.carsync.challenge.api.service.LoginService;
import com.carsync.challenge.api.service.MessageService;
import com.carsync.challenge.api.utils.FactoryUtils;
import com.carsync.challenge.api.utils.TimestampUtils;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Accessors(prefix = "_")
@Getter
@Slf4j
@Service("signupService")
public class SignupServiceImpl implements AuthRequestService {

	@Autowired
	private VerificationTokenRepository _verificationTokenRepository;

	@Autowired
	private UserRepository _userRepository;

	@Autowired
	private LoginService _authService;

	@Autowired
	@Qualifier("mailService")
	private MessageService _mailService;

	@Autowired
	private PasswordEncoder _passwordEncoder;

	@Value("${verification-token.time-offset-in-minutes}")
	private long _expirationOffsetInMinutes;

	@Value("${mail.sender}")
	private String _mailSender;

	@Value("${mail.subject}")
	private String _mailSubject;

	@Transactional
	@Override
	public void createAuthRequest(AuthRequestDTO signupData) {
		VerificationToken verificationToken = (VerificationToken) FactoryUtils.createToken(
				TokenType.SIGNUP_VERIFICATION, ((SignupDTO) signupData).getEmail(), getExpirationOffsetInMinutes());
		getVerificationTokenRepository().save(verificationToken);
		log.info("Verification token created!");
		getMailService().sendMessage(new Email(getMailSender(), verificationToken.getEmail(), getMailSubject(),
				verificationToken.getToken()));
		log.info("Verification token successfully delivered!");
	}

	@Transactional
	@Override
	public void verifyAuthRequest(VerificationRequestDTO verificationData) {
		String token = verificationData.getVerificationToken();
		VerificationToken verificationToken = getVerificationTokenRepository().findBy_token(token)
				.orElseThrow(() -> new InvalidVerificationTokenException("The verification token is not valid!"));
		String email = ((VerifyAccountDTO) verificationData).getEmail();
		String password = ((VerifyAccountDTO) verificationData).getPassword();
		checkTokenValidity(verificationToken, email);
		log.info("Signup token matching confirmed!");
		createUser(email, password);
		getVerificationTokenRepository().deleteBy_email(email);
		log.info("New user registered - {}!", email);
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

	private User createUser(String email, String password) {
		User user = User.builder().email(email).password(getPasswordEncoder().encode(password)).build();
		return getUserRepository().save(user);
	}

}
