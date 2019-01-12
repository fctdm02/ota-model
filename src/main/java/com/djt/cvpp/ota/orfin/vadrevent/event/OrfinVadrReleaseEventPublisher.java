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
public interface OrfinVadrReleaseEventPublisher {

	/**
	 * 
	 * @param orfinVadrReleaseEventSubscriber
	 */
	void subscribe(OrfinVadrReleaseEventSubscriber orfinVadrReleaseEventSubscriber);
	
	/**
	 * 
	 * @param orfinVadrReleaseEventSubscriber
	 */
	void unsubscribe(OrfinVadrReleaseEventSubscriber orfinVadrReleaseEventSubscriber);

	/**
	 * 
	 * @param owner
	 * @param domainName
	 * @param domainInstanceName
	 * @param domainInstanceDescription
	 * @param domainInstanceVersion
	 * @param appId
	 * @param appVersion
	 * @param productionState
	 * @param releaseDate
	 * @param softwarePriorityLevel
	 * @return
	 * @throws ValidationException
	 */
	OrfinVadrReleaseEvent publishOrfinVadrReleaseEvent(
		String owner,
		String domainName,
		String domainInstanceName,
		String domainInstanceDescription,
		String domainInstanceVersion,
		String appId,
		String appVersion,
		String productionState,
		String releaseDate,
		String softwarePriorityLevel)
	throws 
		ValidationException;
}
