package com.carsync.challenge.api.service;

import com.carsync.challenge.api.dto.request.AuthRequestDTO;
import com.carsync.challenge.api.dto.request.VerificationRequestDTO;

public interface AuthRequestService {
	public void createAuthRequest(AuthRequestDTO authReqData);

	public void verifyAuthRequest(VerificationRequestDTO verificationData);

}
