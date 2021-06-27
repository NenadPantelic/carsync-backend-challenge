package com.carsync.challenge.api.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.dao.TwoFARepository;
import com.carsync.challenge.api.dao.UserRepository;
import com.carsync.challenge.api.dto.request.AuthRequestDTO;
import com.carsync.challenge.api.dto.request.TwoFADTO;
import com.carsync.challenge.api.dto.request.VerificationRequestDTO;
import com.carsync.challenge.api.exception.InvalidActionException;
import com.carsync.challenge.api.exception.InvalidVerificationTokenException;
import com.carsync.challenge.api.model.TwoFAToken;
import com.carsync.challenge.api.model.User;
import com.carsync.challenge.api.model.utils.SMS;
import com.carsync.challenge.api.model.utils.TokenType;
import com.carsync.challenge.api.service.AuthRequestService;
import com.carsync.challenge.api.service.MessageService;
import com.carsync.challenge.api.utils.AuthUtils;
import com.carsync.challenge.api.utils.FactoryUtils;
import com.carsync.challenge.api.utils.TimestampUtils;

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

	@Autowired
	private UserRepository _userRepository;

	@Transactional
	@Override
	public void createAuthRequest(AuthRequestDTO authReqData) {
		String phoneNo = ((TwoFADTO) authReqData).getPhoneNo();
		TwoFAToken twoFAToken = (TwoFAToken) FactoryUtils.createToken(TokenType.TWO_FA, phoneNo,
				getExpirationOffsetInMinutes());
		twoFAToken.setUserId(AuthUtils.fetchUserIdFromToken());
		log.info("2FA token created!");
		getTwoFARepository().save(twoFAToken);
		getMessageService().sendMessage(new SMS(getSenderPhoneNo(), phoneNo, twoFAToken.getToken()));
	}

	@Transactional
	@Override
	public void verifyAuthRequest(VerificationRequestDTO verificationData) {
		String token = verificationData.getVerificationToken();
		TwoFAToken twoFAToken = getTwoFARepository().findBy_token(token)
				.orElseThrow(() -> new InvalidVerificationTokenException("The verification token is not valid!"));
		Long userId = AuthUtils.fetchUserIdFromToken();

		checkTokenValidity(twoFAToken, userId);
		User user = getUserRepository().findById(userId).orElseThrow(() -> AuthUtils.unauthorized());
		updateUser(user, twoFAToken.getPhoneNo());
		log.info("2FA setup verified!");
		getTwoFARepository().deleteBy_userId(userId);
	}

	public void disable() {
		Long userId = AuthUtils.fetchUserIdFromToken();
		User user = getUserRepository().findById(userId).orElseThrow(() -> AuthUtils.unauthorized());
		if (!user.getTwoFAEnabled()) {
			throw new InvalidActionException("Invalid action!");
		}
		user.setTwoFAEnabled(false);
		getUserRepository().save(user);
		log.info("2FA successfully disable!");
		getTwoFARepository().deleteBy_userId(userId);
	}

	private void checkTokenValidity(TwoFAToken twoFAToken, Long userId) {
		if (twoFAToken.getUserId() != userId) {
			throw new InvalidVerificationTokenException(
					"The token you provided doesn't match your secret verification code! Please, check that again or reclaim new token!");
		}
		if (!TimestampUtils.isExpirationTimeValid(twoFAToken.getExpirationTime())) {
			throw new InvalidVerificationTokenException(
					"The token you provided is expired. Use another one or reclaim new token!");
		}
	}

	private void updateUser(User user, String phoneNo) {
		user.setPhoneNo(phoneNo);
		user.setTwoFAEnabled(true);
		getUserRepository().save(user);

	}

}
