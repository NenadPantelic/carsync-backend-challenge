package com.carsync.challenge.api.service;

import com.carsync.challenge.api.dto.request.LoginDTO;
import com.carsync.challenge.api.dto.response.AuthenticatedDTO;

public interface AuthService {
	
	public AuthenticatedDTO login(LoginDTO loginData);

}
