package com.carsync.challenge.api.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.dao.TwoFARepository;
import com.carsync.challenge.api.dto.request.AuthRequestDTO;
import com.carsync.challenge.api.dto.request.TwoFADTO;
import com.carsync.challenge.api.dto.request.VerificationRequestDTO;
import com.carsync.challenge.api.model.TwoFAToken;
import com.carsync.challenge.api.model.utils.SMS;
import com.carsync.challenge.api.model.utils.TokenType;
import com.carsync.challenge.api.service.AuthRequestService;
import com.carsync.challenge.api.service.MessageService;
import com.carsync.challenge.api.utils.FactoryUtils;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Accessors(prefix = "_")
@Data
@Slf4j
@Service("twoFAService")
public class TwoFAServiceImpl implements AuthRequestService {

	@Value("${twilio.sender-phone-no}")
	private String _senderPhoneNo;

	@Value("${verification-token.time-offset-in-minutes}")
	private long _expirationOffsetInMinutes;

	@Autowired
	@Qualifier("smsService")
	private MessageService _messageService;

	@Autowired
	private TwoFARepository _twoFARepository;

	@Transactional
	@Override
	public void createAuthRequest(AuthRequestDTO authReqData) {
		String phoneNo = ((TwoFADTO) authReqData).getPhoneNo();
		TwoFAToken twoFAToken = (TwoFAToken) FactoryUtils.createToken(TokenType.TWO_FA, phoneNo,
				getExpirationOffsetInMinutes());
		log.info("2FA token created!");
		getTwoFARepository().save(twoFAToken);
		getMessageService().sendMessage(new SMS(getSenderPhoneNo(), phoneNo, twoFAToken.getToken()));
	}

	@Override
	public void verifyAuthRequest(VerificationRequestDTO verificationData) {
		// TODO Auto-generated method stub

	}

}
