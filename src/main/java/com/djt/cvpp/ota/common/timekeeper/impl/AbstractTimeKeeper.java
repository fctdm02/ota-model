/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.common.timekeeper.impl;

import java.sql.Timestamp;
import java.time.Instant;

import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;

/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractTimeKeeper implements TimeKeeper {

	public AbstractTimeKeeper() {
	}

	public Instant getCurrentInstant() {
		return Instant.ofEpochMilli(this.getCurrentTimeInMillis());
	}

	public Timestamp getCurrentTimestamp() {
		return new Timestamp(Instant.ofEpochMilli(this.getCurrentTimeInMillis()).toEpochMilli());
	}
	
	public Timestamp getTimestampForDaysFromCurrent(int offSetDays) {
		return new Timestamp(Instant.ofEpochMilli(this.getCurrentTimeInMillis() + (NUM_MILLIS_IN_DAY * offSetDays)).toEpochMilli());
	}
}
