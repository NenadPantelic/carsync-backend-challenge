package com.carsync.challenge.api.service.impl;

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
import com.carsync.challenge.api.dto.response.AuthenticatedDTO;
import com.carsync.challenge.api.model.User;
import com.carsync.challenge.api.service.AuthService;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Accessors(prefix = "_")
@Getter
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository _userRepository;

	@Autowired
	private AuthenticationManager _authenticationManager;

	@Autowired
	private JwtTokenProvider _tokenProvider;
	@Autowired
	private PasswordEncoder _passwordEncoder;

	@Override
	public AuthenticatedDTO login(LoginDTO loginData) {

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
				String token = getTokenProvider().createToken(user.getId(), email);
				return new AuthenticatedDTO(token);
			} else {
				throw new BadCredentialsException("Invalid email/password supplied!");
			}

		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid email/password supplied!");
		}

	}


}
