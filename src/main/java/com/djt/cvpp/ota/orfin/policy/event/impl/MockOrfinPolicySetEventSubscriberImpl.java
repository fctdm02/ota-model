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
import java.util.List;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEvent;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEventSubscriber;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockOrfinPolicySetEventSubscriberImpl implements OrfinPolicySetEventSubscriber {
	
	private static MockOrfinPolicySetEventSubscriberImpl INSTANCE = new MockOrfinPolicySetEventSubscriberImpl();
	public static MockOrfinPolicySetEventSubscriberImpl getInstance() {
		return INSTANCE;
	}
	private MockOrfinPolicySetEventSubscriberImpl() {
	}
	
	
	private List<OrfinPolicySetEvent> events = new ArrayList<>();

	public void handleOrfinPolicySetEvent(OrfinPolicySetEvent orfinPolicySetEvent) throws ValidationException {
		this.events.add(orfinPolicySetEvent);
	}
	
	public List<OrfinPolicySetEvent> getOrfinPolicySetEvents() {
		return this.events;
	}
	
	public void clearEvents() {
		this.events.clear();
	}
}
