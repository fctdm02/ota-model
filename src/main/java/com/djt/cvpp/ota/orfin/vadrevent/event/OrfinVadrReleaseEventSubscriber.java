/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.vadrevent.event;

import com.djt.cvpp.ota.common.exception.ValidationException;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface OrfinVadrReleaseEventSubscriber {

	/**
	 * 
	 * @param orfinVadrReleaseEvent
	 * @throws ValidationException
	 */
	void handleOrfinVadrReleaseEvent(OrfinVadrReleaseEvent orfinVadrReleaseEvent) throws ValidationException;
}
