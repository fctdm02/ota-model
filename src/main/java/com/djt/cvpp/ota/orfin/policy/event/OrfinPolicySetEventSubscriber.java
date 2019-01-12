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
public interface OrfinPolicySetEventSubscriber {
	
	/**
	 * 
	 * @param orfinPolicySetEvent
	 * @throws ValidationException
	 */
	void handleOrfinPolicySetEvent(OrfinPolicySetEvent orfinPolicySetEvent) throws ValidationException;
}
