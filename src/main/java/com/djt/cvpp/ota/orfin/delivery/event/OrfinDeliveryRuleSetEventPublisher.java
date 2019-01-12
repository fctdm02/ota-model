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
public interface OrfinDeliveryRuleSetEventPublisher {

	/**
	 * 
	 * @param orfinDeliveryRuleSetEventSubscriber
	 */
	void subscribe(OrfinDeliveryRuleSetEventSubscriber orfinDeliveryRuleSetEventSubscriber);
	
	/**
	 * 
	 * @param orfinDeliveryRuleSetEventSubscriber
	 */
	void unsubscribe(OrfinDeliveryRuleSetEventSubscriber orfinDeliveryRuleSetEventSubscriber);

	/**
	 * 
	 * @param owner
	 * @param updateAction
	 * @param deliveryRuleSetName
	 * @param nodeAcronym
	 * @param nodeAddress
	 * @return
	 * @throws ValidationException
	 */
	OrfinDeliveryRuleSetEvent publishOrfinDeliveryRuleSetEvent(
		String owner, 
		String updateAction,
		String deliveryRuleSetName,
		String nodeAcronym,
		String nodeAddress)
	throws 
		ValidationException;
}
