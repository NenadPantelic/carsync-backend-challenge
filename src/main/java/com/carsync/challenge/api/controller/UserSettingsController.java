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

import com.carsync.challenge.api.dto.request.ChangePasswordDTO;
import com.carsync.challenge.api.dto.request.TwoFADTO;
import com.carsync.challenge.api.dto.request.VerificationRequestDTO;
import com.carsync.challenge.api.service.AuthRequestService;
import com.carsync.challenge.api.service.UserSettingsService;

import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@RestController
@RequestMapping("/api/v1/my-profile/settings")
public class UserSettingsController {

	@Autowired
	private UserSettingsService _userSettingsService;

	@Autowired
	@Qualifier("twoFAService")
	private AuthRequestService _twoFAService;

	@ApiOperation(value = "Change password")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping
	public void changePassword(@Valid @RequestBody ChangePasswordDTO passwordData) {
		getUserSettingsService().changePassword(passwordData);
	}

	@ApiOperation(value = "Make a request for 2FA verification")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/two-fa")
	public void enableTwoFA(@Valid @RequestBody TwoFADTO twoFAData) {
		getTwoFAService().createAuthRequest(twoFAData);
	}

	@ApiOperation(value = "Confirm (enable) 2FA")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping("/two-fa/enable")
	public void enableTwoFA(@Valid @RequestBody VerificationRequestDTO verificationData) {
		getTwoFAService().verifyAuthRequest(verificationData);
	}

	@ApiOperation(value = "Disable 2FA")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping("/two-fa/disable")
	public void disableTwoFA() {
		getTwoFAService().disable();
	}

}
