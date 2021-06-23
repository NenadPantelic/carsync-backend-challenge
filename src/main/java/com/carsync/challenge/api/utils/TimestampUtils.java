package com.carsync.challenge.api.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimestampUtils {

	public static Instant getExpirationTime(long timeOffset) {
		return getCurrentTime().plus(timeOffset, ChronoUnit.MINUTES);
	}

	public static Instant getCurrentTime() {
		return Instant.now();
	}

}
