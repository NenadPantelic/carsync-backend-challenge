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
import com.carsync.challenge.api.dto.request.VerifyAccountDTO;
import com.carsync.challenge.api.service.SignupService;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@RestController
@RequestMapping("/api/v1/signup")
public class SignupController {

	@Autowired
	private SignupService _authService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public void signup(@Valid @RequestBody final SignupDTO signupData) {
		getAuthService().signup(signupData);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/verify-account")
	public void signup(@Valid @RequestBody final VerifyAccountDTO verifyAccountData) {
		getAuthService().verifyAccount(verifyAccountData);
	}

}
