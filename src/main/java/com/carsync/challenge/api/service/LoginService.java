package com.carsync.challenge.api.service;

import javax.validation.Valid;

import com.carsync.challenge.api.dto.request.LoginDTO;
import com.carsync.challenge.api.dto.request.TwoFALoginDTO;
import com.carsync.challenge.api.dto.response.AuthResultDTO;

public interface LoginService {

	public AuthResultDTO login(LoginDTO loginData);

	public AuthResultDTO loginWithTwoFA(@Valid TwoFALoginDTO loginData);

}
