package com.carsync.challenge.api.service;

import com.carsync.challenge.api.dto.request.ChangePasswordDTO;

public interface UserSettingsService {
	public void changePassword(ChangePasswordDTO passwordData);

}
