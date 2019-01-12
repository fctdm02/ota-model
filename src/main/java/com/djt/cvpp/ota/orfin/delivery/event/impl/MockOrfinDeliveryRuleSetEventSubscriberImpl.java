/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.delivery.event.impl;

import java.util.ArrayList;
import java.util.List;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEvent;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEventSubscriber;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockOrfinDeliveryRuleSetEventSubscriberImpl implements OrfinDeliveryRuleSetEventSubscriber {
	
	private static MockOrfinDeliveryRuleSetEventSubscriberImpl INSTANCE = new MockOrfinDeliveryRuleSetEventSubscriberImpl();
	public static MockOrfinDeliveryRuleSetEventSubscriberImpl getInstance() {
		return INSTANCE;
	}
	private MockOrfinDeliveryRuleSetEventSubscriberImpl() {
	}
	
	
	private List<OrfinDeliveryRuleSetEvent> events = new ArrayList<>();

	public void handleDeliveryRuleSetEvent(OrfinDeliveryRuleSetEvent orfinDeliveryRuleSetEvent) throws ValidationException {
		this.events.add(orfinDeliveryRuleSetEvent);
	}
	
	public List<OrfinDeliveryRuleSetEvent> getOrfinDeliveryRuleSetEvents() {
		return this.events;
	}
	
	public void clearEvents() {
		this.events.clear();
	}
}
