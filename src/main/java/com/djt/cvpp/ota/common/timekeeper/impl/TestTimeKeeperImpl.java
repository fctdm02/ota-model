/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.common.timekeeper.impl;

import java.util.Map;

import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;

/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TestTimeKeeperImpl extends AbstractTimeKeeper implements TimeKeeper {


	/** Default to 01/01/2020 00:00:00 */
	public static final long DEFAULT_TEST_EPOCH_MILLIS_01_01_2020 = 1577854800000L;


	private long epochMillis;
	
	public TestTimeKeeperImpl() {
		this(System.currentTimeMillis());
	}
	
	/**
	 * @param epochMillis The number of milliseconds since midnight, January 1, 1970 UTC 
	 * to set the "current" time to.  Time doesn't advance with this implementation and
	 * can only be used in the dev-wks and dev-int environments. 
	 */
	public TestTimeKeeperImpl(long epochMillis) {
		
		// See if the environment property has been set via Operating System environment variable.
        Map<String, String> osEnvVarsMap = System.getenv();
        String env = osEnvVarsMap.get(ENV);
        if (env == null) {
        	env = osEnvVarsMap.get(ENV.toLowerCase());
        }
        
        // See if the environment property has been set via JRE System Property.
		if (env == null) {
			env = System.getProperty(ENV);
			if (env == null) {
				env = System.getProperty(ENV.toLowerCase());
			}
		}
		
		// Ensure that we are running in a DEV or TEST environment
		if (env != null && !env.contains(DEV) && !env.equalsIgnoreCase(TEST) && !env.equalsIgnoreCase(LOCAL)) {
			throw new IllegalStateException("Cannot use this instance:[" + this.getClass().getSimpleName() + "] in non-dev/non-test environment: [" + env + "].");
		}

		this.epochMillis = epochMillis;
	}

	public long getCurrentTimeInMillis() {
		return this.epochMillis;
	}

	public void setCurrentTimeInMillis(long newCurrentTimeInMillis) {
		 this.epochMillis = newCurrentTimeInMillis;
	}

	public void setCurrentTimeInMillisForwardByOneHour() {
		this.epochMillis = this.epochMillis + NUM_MILLIS_IN_HOUR;
	}

	public void setCurrentTimeInMillisForwardByOneDay() {
		this.epochMillis = this.epochMillis + NUM_MILLIS_IN_DAY;
	}	

	public void setCurrentTimeInMillisForwardByOneWeek() {
		this.epochMillis = this.epochMillis + NUM_MILLIS_IN_WEEK;
	}

	public void setCurrentTimeInMillisForwardByOneYear() {
		this.epochMillis = this.epochMillis + NUM_MILLIS_IN_YEAR;
	}	
}
