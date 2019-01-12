/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.odl.event;

import com.djt.cvpp.ota.common.exception.ValidationException;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface OrfinOdlEventSubscriber {

	/**
	 * 
	 * @param orfinOdlEvent
	 * @throws ValidationException
	 */
	void handleOrfinOdlEvent(OrfinOdlEvent orfinOdlEvent) throws ValidationException;
}
