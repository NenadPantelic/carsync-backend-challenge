package com.carsync.challenge.api.model.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	protected String _from;
	protected String _to;
	protected String _content;

}
