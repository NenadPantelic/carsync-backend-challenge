package com.carsync.challenge.api.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.carsync.challenge.api.dao.UserRepository;
import com.carsync.challenge.api.model.User;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@Component
public class DbSeedUtils {
	@Autowired
	private UserRepository _userRepository;

	@Autowired
	private PasswordEncoder _passwordEncoder;

	public void addUser() {
		getUserRepository().save(
				User.builder().email("test@test.com").password(getPasswordEncoder().encode("password123")).build());
	}

}
