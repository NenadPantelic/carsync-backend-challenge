package com.carsync.challenge.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.exception.MessageDeliveryException;
import com.carsync.challenge.api.model.utils.Message;
import com.carsync.challenge.api.service.MessageService;
import com.carsync.challenge.api.utils.Messages;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Accessors(prefix = "_")
@Data
@Slf4j
@Service("smsService")
public class SMSServiceImpl implements MessageService {

	private final String _accountSID;
	private final String _authToken;
	private final String _smsTemplateFilePath;

	public SMSServiceImpl(@Value("${twilio.account-sid}") String accountSID,
			@Value("${twilio.auth-token}") String authToken,
			@Value("${sms.template-file-path}") String smsTemplateFilePath) {
		_accountSID = accountSID;
		_authToken = authToken;
		_smsTemplateFilePath = smsTemplateFilePath;
		Twilio.init(getAccountSID(), getAuthToken());
	}

	@Override
	public void sendMessage(Message smsMessage) {
		if (isPhoneNumberValid(smsMessage.getFrom())) {
			try {
				PhoneNumber to = new PhoneNumber(smsMessage.getTo());
				PhoneNumber from = new PhoneNumber(smsMessage.getFrom());
				String message = createMessageContent(smsMessage.getContent());

				MessageCreator creator = getMessageCreator(to, from, message);
				creator.create();
				log.info("Sending sms to {}", to);
			} catch (ApiException e) {
				throw new MessageDeliveryException(Messages.SMS_DELIVERY_ERROR_MESSAGE);
			}

		} else {
			throw new IllegalArgumentException("Phone number [" + smsMessage.getTo() + "] is not a valid number");
		}
	}

	@Override
	public String readMessageTemplate() throws IOException {
		File resource = new ClassPathResource(getSmsTemplateFilePath()).getFile();
		return new String(Files.readAllBytes(resource.toPath()));
	}

	private boolean isPhoneNumberValid(String phoneNumber) {
		// TODO: Implement phone number validator
		return true;
	}

	private MessageCreator getMessageCreator(PhoneNumber to, PhoneNumber from, String message) {
		return com.twilio.rest.api.v2010.account.Message.creator(to, from, message);

	}

}
