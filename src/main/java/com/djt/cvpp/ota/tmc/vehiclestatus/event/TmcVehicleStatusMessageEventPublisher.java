/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.vehiclestatus.event;

import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface TmcVehicleStatusMessageEventPublisher {
	
	/**
	 * 
	 * @param tmcVehicleStatusMessageEventSubscriber
	 */
	void subscribe(TmcVehicleStatusMessageEventSubscriber tmcVehicleStatusMessageEventSubscriber);
	
	/**
	 * 
	 * @param tmcVehicleStatusMessageEventSubscriber
	 */
	void unsubscribe(TmcVehicleStatusMessageEventSubscriber tmcVehicleStatusMessageEventSubscriber);
	
	/**
	 * 
	 * @param tmcVehicleStatusMessageEvent
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	void publishTmcVehicleStatusMessageEvent(TmcVehicleStatusMessageEvent tmcVehicleStatusMessageEvent) throws EntityDoesNotExistException, ValidationException;
}
