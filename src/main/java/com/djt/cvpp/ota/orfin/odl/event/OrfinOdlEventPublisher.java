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
public interface OrfinOdlEventPublisher {

	/**
	 * 
	 * @param orfinOdlEventSubscriber
	 */
	void subscribe(OrfinOdlEventSubscriber orfinOdlEventSubscriber);
	
	/**
	 * 
	 * @param orfinOdlEventSubscriber
	 */
	void unsubscribe(OrfinOdlEventSubscriber orfinOdlEventSubscriber);
	
	/**
	 * 
	 * @param owner
	 * @param programCode
	 * @param modelYear
	 * @param odlName
	 * @return
	 * @throws ValidationException
	 */
	OrfinOdlEvent publishOrfinOdlEvent(
		String owner,
		String programCode,
		Integer modelYear,
		String odlName)
	throws 
		ValidationException;
}
