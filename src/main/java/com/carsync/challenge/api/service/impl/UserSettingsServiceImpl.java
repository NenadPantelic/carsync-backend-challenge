package com.carsync.challenge.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.auth.AuthInterceptor;
import com.carsync.challenge.api.auth.JwtTokenProvider;
import com.carsync.challenge.api.auth.UserContext;
import com.carsync.challenge.api.dao.UserRepository;
import com.carsync.challenge.api.dto.request.ChangePasswordDTO;
import com.carsync.challenge.api.exception.PasswordDoesNotMatchException;
import com.carsync.challenge.api.exception.UnauthorizedAccessException;
import com.carsync.challenge.api.model.User;
import com.carsync.challenge.api.service.UserSettingsService;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@Service
public class UserSettingsServiceImpl implements UserSettingsService {

	@Autowired
	private UserRepository _userRepository;

	@Autowired
	private JwtTokenProvider _tokenProvider;

	@Autowired
	private PasswordEncoder _passwordEncoder;

	@Override
	public void changePassword(ChangePasswordDTO passwordData) {
		UserContext userContext = AuthInterceptor.context.get();
		if (userContext == null) {
			throw unauthorized();
		}
		Long userId = userContext.getUserId();
		User user = getUserRepository().findById(userId).orElseThrow(() -> unauthorized());
		if (!getPasswordEncoder().matches(passwordData.getOldPassword(), user.getPassword())) {
			throw new PasswordDoesNotMatchException("Wrong current password! Try again!");
		}
		user.setPassword(getPasswordEncoder().encode(passwordData.getNewPassword()));
		getUserRepository().save(user);
	}

	private UnauthorizedAccessException unauthorized() {
		return new UnauthorizedAccessException("You're not authorized to access this resource!");
	}

}
