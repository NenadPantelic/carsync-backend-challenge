package com.carsync.challenge.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.carsync.challenge.api.dto.request.SignupDTO;
import com.carsync.challenge.api.service.AuthService;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

	@Autowired
	private AuthService _authService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("signup")
	public void signup(@Valid @RequestBody SignupDTO signupData) {
		getAuthService().signup(signupData);
	}

}
