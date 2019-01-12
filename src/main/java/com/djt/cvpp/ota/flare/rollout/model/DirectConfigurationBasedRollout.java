/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.model;

/**
 * 
 * Direct Configuration updates occur on a single module (a.k.a. Node or ECU) at a time, thus,
 * all that needs to be specified is the module identity.  There can be software updates that 
 * accompany a direct configuration update, or there can be direct configuration updates only. 
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface DirectConfigurationBasedRollout {
	
	/**
	 * 
	 * @return
	 */
	String getNodeAcronym();

	/**
	 * 
	 * @return
	 */
	String getNodeAddress();
}
