package com.carsync.challenge.api.model.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@Setter
@NoArgsConstructor
public class Email extends Message {
	private String _subject;

	public Email(String from, String to, String subject, String content) {
		super(from, to, content);
		_subject = subject;
	}
}
