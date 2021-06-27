package com.carsync.challenge.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carsync.challenge.api.dto.request.LoginDTO;
import com.carsync.challenge.api.dto.request.TwoFALoginDTO;
import com.carsync.challenge.api.dto.response.AuthResultDTO;
import com.carsync.challenge.api.service.LoginService;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

	@Autowired
	private LoginService _loginService;

	@PostMapping
	public AuthResultDTO login(@Valid @RequestBody final LoginDTO loginData) {
		return getLoginService().login(loginData);
	}

	
	@PostMapping("/two-fa")
	public AuthResultDTO loginWithTwoFA(@Valid @RequestBody final TwoFALoginDTO loginData) {
		return getLoginService().loginWithTwoFA(loginData);
	}
}
