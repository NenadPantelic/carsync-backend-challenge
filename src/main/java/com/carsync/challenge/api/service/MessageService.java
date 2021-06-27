package com.carsync.challenge.api.service;

import java.io.IOException;

import com.carsync.challenge.api.model.utils.Message;

public interface MessageService {
	public void sendMessage(Message message);

	public String readMessageTemplate() throws IOException;

	public default String createMessageContent(String content) {
		try {
			String messageTemplate = readMessageTemplate();
			return messageTemplate.replace("[$VERIFICATION_CODE$]", content);
		} catch (IOException e) {
			return String.format("Your Login360 verification code: %s", content);
		}
	}

}
