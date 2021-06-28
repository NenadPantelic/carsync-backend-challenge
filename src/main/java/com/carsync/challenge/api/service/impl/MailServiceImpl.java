package com.carsync.challenge.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.exception.MessageDeliveryException;
import com.carsync.challenge.api.model.utils.Email;
import com.carsync.challenge.api.model.utils.Message;
import com.carsync.challenge.api.service.MessageService;
import com.carsync.challenge.api.utils.Messages;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("mailService")
public class MailServiceImpl implements MessageService {

	private final String _apiEndpoint;
	private final String _apiKey;
	private final String _mailTemplateFilePath;

	public MailServiceImpl(@Value("${mail.api-endpoint}") String apiEndpoint, @Value("${mail.api-key}") String apiKey,
			@Value("${mail.template-file-path}") String emailTemplateFile) {
		_apiEndpoint = apiEndpoint;
		_apiKey = apiKey;
		_mailTemplateFilePath = emailTemplateFile;
	}

	private String getApiEndpoint() {
		return _apiEndpoint;
	}

	private String getApiKey() {
		return _apiKey;
	}

	private String getMailTemplateFilePath() {
		return _mailTemplateFilePath;
	}

	@Override
	public String readMessageTemplate() throws IOException {
		File resource = new ClassPathResource(getMailTemplateFilePath()).getFile();
		return new String(Files.readAllBytes(resource.toPath()));
	}

	public void sendMessage(Message email) {
		String emailContent = createMessageContent(email.getContent());
		try {
			log.info("Preparing mail sending...");
			HttpResponse<JsonNode> request = Unirest.post(getApiEndpoint()).basicAuth("api", getApiKey())
					.field("from", email.getFrom()).field("to", email.getTo())
					.field("subject", ((Email) email).getSubject()).field("html", emailContent).asJson();
			if (request.getStatus() != 200) {
				throw new MessageDeliveryException(String
						.format("An error occured during the email delivery. Cause: %s", request.getStatusText()));
			}
			log.info("Mail sent successfully!");
		} catch (UnirestException e) {
			throw new MessageDeliveryException(String.format(Messages.MAIL_DELIVERY_ERROR_MESSAGE));
		}
	}

}
