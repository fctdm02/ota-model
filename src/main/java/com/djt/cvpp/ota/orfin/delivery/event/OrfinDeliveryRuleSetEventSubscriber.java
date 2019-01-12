/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.delivery.event;

import com.djt.cvpp.ota.common.exception.ValidationException;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface OrfinDeliveryRuleSetEventSubscriber {

	/**
	 * 
	 * @param orfinDeliveryRuleSetEvent
	 * @throws ValidationException
	 */
	void handleDeliveryRuleSetEvent(OrfinDeliveryRuleSetEvent orfinDeliveryRuleSetEvent) throws ValidationException;
}
