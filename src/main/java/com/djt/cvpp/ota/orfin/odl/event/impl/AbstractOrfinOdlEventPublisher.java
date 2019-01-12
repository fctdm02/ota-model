/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.odl.event.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEvent;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEventPublisher;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEventSubscriber;

/**
  *
  * @author tmyers1@yahoo.com (Tom Myers)
  *
  */
public abstract class AbstractOrfinOdlEventPublisher implements OrfinOdlEventPublisher {
	
	protected List<OrfinOdlEventSubscriber> subscribers = new ArrayList<>();
	
	public void subscribe(OrfinOdlEventSubscriber orfinOdlEventSubscriber) {
		
		this.subscribers.add(orfinOdlEventSubscriber);
	}
	
	public void unsubscribe(OrfinOdlEventSubscriber orfinOdlEventSubscriber) {
		
		this.subscribers.remove(orfinOdlEventSubscriber);
	}
	
	public OrfinOdlEvent publishOrfinOdlEvent(
		String owner,
		String programCode,
		Integer modelYear,
		String odlName)
	throws 
		ValidationException {

		OrfinOdlEvent orfinOdlEvent = new OrfinOdlEvent(
			owner,
			programCode,
			modelYear,
			odlName);
		
		this.publishOrfinOdlEvent(orfinOdlEvent);
		
		return orfinOdlEvent;
	}
	
	public void publishOrfinOdlEvent(OrfinOdlEvent orfinOdlEvent) throws ValidationException {
		
		Iterator<OrfinOdlEventSubscriber> iterator = this.subscribers.iterator();
		while (iterator.hasNext()) {
			
			OrfinOdlEventSubscriber orfinOdlEventSubscriber = iterator.next();
			orfinOdlEventSubscriber.handleOrfinOdlEvent(orfinOdlEvent);
		}
	}
}
