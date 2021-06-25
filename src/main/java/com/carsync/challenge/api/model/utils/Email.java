package com.carsync.challenge.api.model.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
	private String _from;
	private String _to;
	private String _subject;
	private String _content;

}
