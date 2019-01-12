/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.vadrevent.event.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.vadrevent.event.OrfinVadrReleaseEvent;
import com.djt.cvpp.ota.orfin.vadrevent.event.OrfinVadrReleaseEventPublisher;
import com.djt.cvpp.ota.orfin.vadrevent.event.OrfinVadrReleaseEventSubscriber;

/**
  *
  * @author tmyers1@yahoo.com (Tom Myers)
  *
  */
public abstract class AbstractOrfinVadrReleaseEventPublisher implements OrfinVadrReleaseEventPublisher {
	
	protected List<OrfinVadrReleaseEventSubscriber> subscribers = new ArrayList<>();
	
	public void subscribe(OrfinVadrReleaseEventSubscriber orfinVadrReleaseEventSubscriber) {
		
		this.subscribers.add(orfinVadrReleaseEventSubscriber);
	}
	
	public void unsubscribe(OrfinVadrReleaseEventSubscriber orfinVadrReleaseEventSubscriber) {
		
		this.subscribers.remove(orfinVadrReleaseEventSubscriber);
	}
	
	public OrfinVadrReleaseEvent publishOrfinVadrReleaseEvent(
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
		ValidationException {

		OrfinVadrReleaseEvent orfinVadrReleaseEvent = new OrfinVadrReleaseEvent(
			owner,
			domainName,
			domainInstanceName,
			domainInstanceDescription,
			domainInstanceVersion,
			appId,
			appVersion,
			productionState,
			releaseDate,
			softwarePriorityLevel);
		
		this.publishOrfinVadrReleaseEvent(orfinVadrReleaseEvent);
		
		return orfinVadrReleaseEvent;
	}
		
	public void publishOrfinVadrReleaseEvent(OrfinVadrReleaseEvent orfinVadrReleaseEvent) throws ValidationException {
		
		Iterator<OrfinVadrReleaseEventSubscriber> iterator = this.subscribers.iterator();
		while (iterator.hasNext()) {
			OrfinVadrReleaseEventSubscriber orfinVadrReleaseEventSubscriber = iterator.next();
			orfinVadrReleaseEventSubscriber.handleOrfinVadrReleaseEvent(orfinVadrReleaseEvent);
		}
	}
}
