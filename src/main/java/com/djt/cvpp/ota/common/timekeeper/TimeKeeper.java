/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.common.timekeeper;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface TimeKeeper {

	/** Used to limit usage of the test timekeeper implementation. */
	String ENV = "spring.profiles.active";

	/** Used to limit usage of the test timekeeper implementation. */
	String DEV = "dev";

	/** Used to limit usage of the test timekeeper implementation. */
	String TEST = "test";

	/** Used to limit usage of the test timekeeper implementation. */
	String LOCAL = "local";
	

	/** */
	long NUM_MILLIS_IN_HOUR = 3600000;
	
	/** */
	long NUM_MILLIS_IN_DAY = NUM_MILLIS_IN_HOUR * 24;

	/** */
	long NUM_MILLIS_IN_WEEK = NUM_MILLIS_IN_DAY * 7;

	/** */
	long NUM_MILLIS_IN_YEAR = 31536000000l;
	
	/**
	 * In epoch millis (UTC)
	 * 
	 * @return
	 */
	long getCurrentTimeInMillis();
	
	/**
	 * 
	 * In epoch millis (UTC)
	 * 
	 * @return
	 */
	Instant getCurrentInstant();

	/**
	 *
	 * In epoch millis (UTC)
	 *
	 * @return
	 */
	Timestamp getCurrentTimestamp();
	
	/**
	 * 
	 * In epoch millis (UTC)
	 * 
	 * @param offSetDays
	 * 
	 * @return
	 */
	Timestamp getTimestampForDaysFromCurrent(int offSetDays);


	// The methods below are strictly for testing and are not available in higher environments.
	/**
	 * Sets the new static time for test based TimeKeeper implementation only.
	 * That is, if this method is called for the "production" time keeper
	 * implementation, then an <code>UnsupportedOperationException</code>
	 * will be thrown.
	 * 
	 * @param newCurrentTimeInMillis
	 */
	void setCurrentTimeInMillis(long newCurrentTimeInMillis);

	/**
	 * Increments the static time forward by one day for test based TimeKeeper implementation only.
	 * That is, if this method is called for the "production" time keeper
	 * implementation, then an <code>UnsupportedOperationException</code>
	 * will be thrown.
	 */
	void setCurrentTimeInMillisForwardByOneHour();
	
	/**
	 * Increments the static time forward by one day for test based TimeKeeper implementation only.
	 * That is, if this method is called for the "production" time keeper
	 * implementation, then an <code>UnsupportedOperationException</code>
	 * will be thrown.
	 */
	void setCurrentTimeInMillisForwardByOneDay();

	/**
	 * Increments the static time forward by one week for test based TimeKeeper implementation only.
	 * That is, if this method is called for the "production" time keeper
	 * implementation, then an <code>UnsupportedOperationException</code>
	 * will be thrown.
	 */
	void setCurrentTimeInMillisForwardByOneWeek();

	/**
	 * Increments the static time forward by one year for test based TimeKeeper implementation only.
	 * That is, if this method is called for the "production" time keeper
	 * implementation, then an <code>UnsupportedOperationException</code>
	 * will be thrown.
	 */
	void setCurrentTimeInMillisForwardByOneYear();	
}
