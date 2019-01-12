/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.common.timekeeper.impl;

import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;


/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public final class TimeKeeperImpl extends AbstractTimeKeeper implements TimeKeeper {

	public long getCurrentTimeInMillis() {
		return System.currentTimeMillis();
	}
	
	public void setCurrentTimeInMillis(long millis) {
		throw new UnsupportedOperationException("This method cannot be invoked for the production implementation of TimeKeeper.");
	}

	public void setCurrentTimeInMillisForwardByOneHour() {
		throw new UnsupportedOperationException("This method cannot be invoked for the production implementation of TimeKeeper.");
	}

	public void setCurrentTimeInMillisForwardByOneDay() {
		throw new UnsupportedOperationException("This method cannot be invoked for the production implementation of TimeKeeper.");
	}

	public void setCurrentTimeInMillisForwardByOneWeek() {
		throw new UnsupportedOperationException("This method cannot be invoked for the production implementation of TimeKeeper.");
	}

	public void setCurrentTimeInMillisForwardByOneYear() {
		throw new UnsupportedOperationException("This method cannot be invoked for the production implementation of TimeKeeper.");
	}	
}
