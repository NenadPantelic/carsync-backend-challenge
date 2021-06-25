package com.carsync.challenge.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.exception.MailDeliveryException;
import com.carsync.challenge.api.model.utils.Email;
import com.carsync.challenge.api.service.MailService;
import com.carsync.challenge.api.utils.Messages;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class MailServiceImpl implements MailService {

	private final String _apiEndpoint;
	private final String _apiKey;

	@Value("${mail.template-file-path}")
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

	private String readEmailTemplate() throws IOException {
		File resource = new ClassPathResource(getMailTemplateFilePath()).getFile();
		return new String(Files.readAllBytes(resource.toPath()));
	}

	private String createVerificationEmailContent(String verificationCode) {
		try {
			String emailTemplate = readEmailTemplate();
			return emailTemplate.replace("[$VERIFICATION_CODE$]", verificationCode);
		} catch (IOException e) {
			return String.format("<strong>Verification code: %s</strong>", verificationCode);
		}

	}

	public void sendMessage(Email email) {
		String emailContent = createVerificationEmailContent(email.getContent());
		try {
			HttpResponse<JsonNode> request = Unirest.post(getApiEndpoint()).basicAuth("api", getApiKey())
					.field("from", email.getFrom()).field("to", email.getTo()).field("subject", email.getSubject())
					.field("html", emailContent).asJson();
			if (request.getStatus() != 200) {
				throw new MailDeliveryException(
						String.format("An error occured during the email delivery. Cause: %s", request.getStatusText()));
			}
		} catch (UnirestException e) {
			throw new MailDeliveryException(
					String.format(Messages.MAIL_DELIVERY_ERROR_MESSAGE));
		}
	}

}
