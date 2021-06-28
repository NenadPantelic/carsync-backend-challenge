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

	public static boolean isExpirationTimeValid(Instant timestamp) {
		return isTimestampValid(timestamp, getCurrentTime());
	}

	private static boolean isTimestampValid(Instant ts1, Instant ts2) {
		/**
		 * ts1 - first timestamp (referential one) ts2 - second timestamp (the one that
		 * is checked)
		 * 
		 * returns boolean - if ts1 is valid (before expiration)
		 */
		return ts1.compareTo(ts2) >= 0;
	}

}
