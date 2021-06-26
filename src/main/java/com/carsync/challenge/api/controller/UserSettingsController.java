package com.carsync.challenge.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.carsync.challenge.api.dto.request.ChangePasswordDTO;
import com.carsync.challenge.api.service.UserSettingsService;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@RestController
@RequestMapping("/api/v1/my-profile/settings")
public class UserSettingsController {

	@Autowired
	private UserSettingsService _userSettingsService;

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping
	public void changePassword(@Valid @RequestBody ChangePasswordDTO passwordData) {
		getUserSettingsService().changePassword(passwordData);

	}

}
