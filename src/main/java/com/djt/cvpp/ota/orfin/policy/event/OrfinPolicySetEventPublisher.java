/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.policy.event;

import com.djt.cvpp.ota.common.exception.ValidationException;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface OrfinPolicySetEventPublisher {

	/**
	 * 
	 * @param orfinPolicySetEventSubscriber
	 */
	void subscribe(OrfinPolicySetEventSubscriber orfinPolicySetEventSubscriber);
	
	/**
	 * 
	 * @param orfinPolicySetEventSubscriber
	 */
	void unsubscribe(OrfinPolicySetEventSubscriber orfinPolicySetEventSubscriber);
	
	/**
	 * 
	 * @param owner
	 * @param programCode
	 * @param modelYear
	 * @param  regionCode The region code is optional, if null, then all vehicles belonging to the given program code and model year will be targeted for the policy table update.  Otherwise, if specified,
	 * then only those vehicles that are "in" the given region will be targeted for the policy table update.
	 * @param policySetName
	 * @return
	 * @throws ValidationException
	 */
	OrfinPolicySetEvent publishOrfinPolicySetEvent(
		String owner,
		String programCode,
		Integer modelYear,
		String regionCode,
		String policySetName)
	throws 
		ValidationException;
}
