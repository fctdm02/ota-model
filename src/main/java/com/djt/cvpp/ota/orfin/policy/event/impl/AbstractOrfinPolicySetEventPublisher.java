/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.policy.event.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEvent;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEventPublisher;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEventSubscriber;

/**
  *
  * @author tmyers1@yahoo.com (Tom Myers)
  *
  */
public abstract class AbstractOrfinPolicySetEventPublisher implements OrfinPolicySetEventPublisher {
	
	protected List<OrfinPolicySetEventSubscriber> subscribers = new ArrayList<>();
	
	public void subscribe(OrfinPolicySetEventSubscriber orfinPolicySetEventSubscriber) {
		
		this.subscribers.add(orfinPolicySetEventSubscriber);
	}
	
	public void unsubscribe(OrfinPolicySetEventSubscriber orfinPolicySetEventSubscriber) {
		
		this.subscribers.remove(orfinPolicySetEventSubscriber);
	}
	
	public OrfinPolicySetEvent publishOrfinPolicySetEvent(
		String owner,
		String programCode,
		Integer modelYear,
		String regionCode,
		String policySetName)
	throws 
		ValidationException {

		OrfinPolicySetEvent orfinPolicySetEvent = new OrfinPolicySetEvent(
			owner,
			programCode,
			modelYear,
			regionCode,
			policySetName);
		
		this.publishOrfinPolicySetEvent(orfinPolicySetEvent);
		
		return orfinPolicySetEvent;
	}
	
	protected void publishOrfinPolicySetEvent(OrfinPolicySetEvent orfinPolicySetEvent) throws ValidationException {
		
		Iterator<OrfinPolicySetEventSubscriber> iterator = this.subscribers.iterator();
		while (iterator.hasNext()) {
			
			OrfinPolicySetEventSubscriber orfinPolicySetEventSubscriber = iterator.next();
			orfinPolicySetEventSubscriber.handleOrfinPolicySetEvent(orfinPolicySetEvent);
		}
	}
}
