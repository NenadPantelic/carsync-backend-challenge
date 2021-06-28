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
import com.carsync.challenge.api.model.utils.UserTwoFA;
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
		Long userId = AuthUtils.fetchUserId();
		TwoFAToken twoFAToken = setTwoFA(userId, phoneNo);
		checkTokenValidity(twoFAToken, userId);
	}

	@Transactional
	@Override
	public void verifyAuthRequest(VerificationRequestDTO verificationData) {
		String token = verificationData.getVerificationToken();
		UserTwoFA user2FA = performTwoFA(token);
		log.info("2FA setup verified!");
		updateUser(user2FA.getUser(), user2FA.getToken().getPhoneNo());

	}

	@Override
	public void disable() {
		Long userId = AuthUtils.fetchUserId();
		User user = getUserRepository().findById(userId).orElseThrow(() -> AuthUtils.unauthorized());
		if (!user.getTwoFAEnabled()) {
			throw new InvalidActionException("Invalid action! 2FA is not enabled!");
		}
		user.setTwoFAEnabled(false);
		getUserRepository().save(user);
		log.info("2FA successfully disable!");
		getTwoFARepository().deleteBy_userId(userId);
	}

	TwoFAToken setTwoFA(Long userId, String phoneNo) {
		TwoFAToken twoFAToken = createToken(phoneNo);
		twoFAToken.setUserId(userId);
		log.info("2FA token created!");
		getTwoFARepository().save(twoFAToken);
		sendToken(phoneNo, twoFAToken.getToken());
		return twoFAToken;
	}

	UserTwoFA performTwoFA(String token) {
		log.info("Performing 2FA check...");
		TwoFAToken twoFAToken = resolveTwoFAToken(token);
		Long userId = twoFAToken.getUserId();
		User user = getUserRepository().findById(userId).orElseThrow(() -> AuthUtils.unauthorized());
		checkTokenValidity(twoFAToken, userId);
		log.info("Success - 2FA token matched!");
		getTwoFARepository().deleteBy_userId(userId);
		return new UserTwoFA(user, twoFAToken);
	}

	TwoFAToken resolveTwoFAToken(String token) {
		TwoFAToken twoFAToken = getTwoFARepository().findBy_token(token)
				.orElseThrow(() -> new InvalidVerificationTokenException("The verification token is not valid!"));
		return twoFAToken;
	}

	private void sendToken(String phoneNo, String token) {
		getMessageService().sendMessage(new SMS(getSenderPhoneNo(), phoneNo, token));
	}

	private TwoFAToken createToken(String phoneNo) {
		TwoFAToken twoFAToken = (TwoFAToken) FactoryUtils.createToken(TokenType.TWO_FA, phoneNo,
				getExpirationOffsetInMinutes());
		return twoFAToken;
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
