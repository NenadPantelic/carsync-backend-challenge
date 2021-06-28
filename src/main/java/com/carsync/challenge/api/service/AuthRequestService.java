package com.carsync.challenge.api.service;

import com.carsync.challenge.api.dto.request.AuthRequestDTO;
import com.carsync.challenge.api.dto.request.VerificationRequestDTO;

public interface AuthRequestService {
	public void createAuthRequest(AuthRequestDTO authReqData);

	public void verifyAuthRequest(VerificationRequestDTO verificationData);

	// TODO: recode this -> it violates Interface Segregation principle
	public default void disable() {
		throw new UnsupportedOperationException("This functionality is not supported yet!");
	}

}
