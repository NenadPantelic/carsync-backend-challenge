package com.carsync.challenge.api.service;

import com.carsync.challenge.api.dto.request.SignupDTO;
import com.carsync.challenge.api.dto.request.VerifyAccountDTO;
import com.carsync.challenge.api.dto.response.AuthResultDTO;

public interface SignupService {

	public void signup(SignupDTO signupData);
	public AuthResultDTO verifyAccount(VerifyAccountDTO verifyAccountData);

}
