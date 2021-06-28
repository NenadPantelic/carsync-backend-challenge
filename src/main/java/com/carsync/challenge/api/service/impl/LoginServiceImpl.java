package com.carsync.challenge.api.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.auth.JwtTokenProvider;
import com.carsync.challenge.api.dao.UserRepository;
import com.carsync.challenge.api.dto.request.LoginDTO;
import com.carsync.challenge.api.dto.request.TwoFALoginDTO;
import com.carsync.challenge.api.dto.response.AuthResultDTO;
import com.carsync.challenge.api.model.User;
import com.carsync.challenge.api.model.utils.UserTwoFA;
import com.carsync.challenge.api.service.LoginService;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Accessors(prefix = "_")
@Getter
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TwoFAServiceImpl _twoFAService;

	@Autowired
	private UserRepository _userRepository;

	@Autowired
	private AuthenticationManager _authenticationManager;

	@Autowired
	private JwtTokenProvider _tokenProvider;
	@Autowired
	private PasswordEncoder _passwordEncoder;

	@Transactional
	@Override
	public AuthResultDTO login(LoginDTO loginData) {

		try {
			String email = loginData.getEmail();
			String password = loginData.getPassword();
			log.info("Authentication in progress.....");

			getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(email, password));
			log.info("Fetching a user with the email = " + email);
			User user = getUserRepository().findBy_email(email)
					.orElseThrow(() -> new BadCredentialsException("Invalid email/password supplied!"));
			String expectedPassword = user.getPassword();
			if (getPasswordEncoder().matches(password, expectedPassword)) {
				return resolveLogin(user);
			} else {
				throw new BadCredentialsException("Invalid email/password supplied!");
			}

		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid email/password supplied!");
		}

	}

	@Transactional
	@Override
	public AuthResultDTO loginWithTwoFA(TwoFALoginDTO loginData) {
		String twoFAToken = loginData.getTwoFAToken();
		UserTwoFA user2FA = getTwoFAService().performTwoFA(twoFAToken);
		return generateLoginToken(user2FA.getUser().getId(), user2FA.getUser().getPhoneNo());

	}

	private AuthResultDTO generateLoginToken(long userId, String contact) {
		String token = getTokenProvider().createToken(userId, contact);
		log.info("Generating login token...");
		return new AuthResultDTO(token, false);

	}

	private AuthResultDTO resolveLogin(User user) {
		if (user.getTwoFAEnabled()) {
			getTwoFAService().setTwoFA(user.getId(), user.getPhoneNo());
			return new AuthResultDTO(null, true);
		} else {
			return generateLoginToken(user.getId(), user.getEmail());
		}
	}

}
