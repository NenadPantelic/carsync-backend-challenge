package com.carsync.challenge.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.auth.JwtTokenProvider;
import com.carsync.challenge.api.dao.UserRepository;
import com.carsync.challenge.api.dto.request.ChangePasswordDTO;
import com.carsync.challenge.api.exception.PasswordDoesNotMatchException;
import com.carsync.challenge.api.model.User;
import com.carsync.challenge.api.service.UserSettingsService;
import com.carsync.challenge.api.utils.AuthUtils;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Accessors(prefix = "_")
@Getter
@Slf4j
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
		Long userId = AuthUtils.fetchUserId();
		User user = getUserRepository().findById(userId).orElseThrow(() -> AuthUtils.unauthorized());
		if (!getPasswordEncoder().matches(passwordData.getOldPassword(), user.getPassword())) {
			throw new PasswordDoesNotMatchException("Wrong current password! Try again!");
		}
		user.setPassword(getPasswordEncoder().encode(passwordData.getNewPassword()));
		getUserRepository().save(user);
		log.info("Password successfully changed for user = {}!", userId);
	}

}
