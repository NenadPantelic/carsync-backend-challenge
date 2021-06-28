package com.carsync.challenge.api.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Data
public class UserContext {
	private Long _userId;

}
