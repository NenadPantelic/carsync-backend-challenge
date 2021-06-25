package com.carsync.challenge.api.service;

import com.carsync.challenge.api.model.utils.Email;

public interface MailService {
	public void sendMessage(Email email);

}
