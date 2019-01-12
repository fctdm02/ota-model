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
import java.util.List;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEvent;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEventSubscriber;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockOrfinOdlEventSubscriberImpl implements OrfinOdlEventSubscriber {
	
	private static MockOrfinOdlEventSubscriberImpl INSTANCE = new MockOrfinOdlEventSubscriberImpl();
	public static MockOrfinOdlEventSubscriberImpl getInstance() {
		return INSTANCE;
	}
	private MockOrfinOdlEventSubscriberImpl() {
	}
	
	
	private List<OrfinOdlEvent> events = new ArrayList<>();

	public void handleOrfinOdlEvent(OrfinOdlEvent orfinOdlEvent) throws ValidationException {
		this.events.add(orfinOdlEvent);
	}
	
	public List<OrfinOdlEvent> getOrfinOdlEvents() {
		return this.events;
	}
	
	public void clearEvents() {
		this.events.clear();
	}
}
