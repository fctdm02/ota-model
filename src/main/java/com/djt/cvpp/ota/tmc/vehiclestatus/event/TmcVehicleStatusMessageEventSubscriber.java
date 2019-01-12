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
public interface TmcVehicleStatusMessageEventSubscriber {
	
	/**
	 * 
	 * @param tmcVehicleStatusMessageEvent
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	void handleTmcVehicleStatusMessageEvent(TmcVehicleStatusMessageEvent tmcVehicleStatusMessageEvent) throws EntityDoesNotExistException, ValidationException;
}
