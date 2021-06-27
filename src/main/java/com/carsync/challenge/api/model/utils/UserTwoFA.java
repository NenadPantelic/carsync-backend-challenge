package com.carsync.challenge.api.model.utils;

import com.carsync.challenge.api.model.TwoFAToken;
import com.carsync.challenge.api.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTwoFA {

	private User _user;
	private TwoFAToken _token;
}
