package com.carsync.challenge.api.model.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@Setter
@NoArgsConstructor
public class SMS extends Message {
	// NOTE: validation of phone number missing
	public SMS(String from, String to, String content) {
		super(from, to, content);

	}

}
