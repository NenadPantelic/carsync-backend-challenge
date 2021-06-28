package com.carsync.challenge.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.carsync.challenge.api.dto.request.LoginDTO;
import com.carsync.challenge.api.dto.request.SignupDTO;
import com.carsync.challenge.api.dto.request.VerifyAccountDTO;
import com.carsync.challenge.api.dto.response.AuthResultDTO;
import com.carsync.challenge.api.service.AuthRequestService;
import com.carsync.challenge.api.service.LoginService;

import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@RestController
@RequestMapping("/api/v1/signup")
public class SignupController {

	@Autowired
	@Qualifier("signupService")
	private AuthRequestService _signupService;

	@Autowired
	private LoginService _loginService;

	@ApiOperation(value = "Make signup request - with email")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public void signup(@Valid @RequestBody final SignupDTO signupData) {
		getSignupService().createAuthRequest(signupData);
	}

	@ApiOperation(value = "Verify signup request (register) - with secret verification code")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/verify-account")
	public AuthResultDTO signup(@Valid @RequestBody final VerifyAccountDTO verifyAccountData) {
		getSignupService().verifyAuthRequest(verifyAccountData);
		return getLoginService().login(new LoginDTO(verifyAccountData.getEmail(), verifyAccountData.getPassword()));
	}

}
