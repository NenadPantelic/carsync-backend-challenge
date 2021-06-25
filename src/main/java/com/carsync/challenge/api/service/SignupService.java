package com.carsync.challenge.api.service;

import com.carsync.challenge.api.dto.request.SignupDTO;
import com.carsync.challenge.api.dto.request.VerifyAccountDTO;

public interface SignupService {

	public void signup(SignupDTO signupData);
	public void verifyAccount(VerifyAccountDTO verifyAccountData);

}
