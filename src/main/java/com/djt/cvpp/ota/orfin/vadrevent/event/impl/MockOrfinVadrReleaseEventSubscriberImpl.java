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
import java.util.List;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.vadrevent.event.OrfinVadrReleaseEvent;
import com.djt.cvpp.ota.orfin.vadrevent.event.OrfinVadrReleaseEventSubscriber;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockOrfinVadrReleaseEventSubscriberImpl implements OrfinVadrReleaseEventSubscriber {
	
	private static MockOrfinVadrReleaseEventSubscriberImpl INSTANCE = new MockOrfinVadrReleaseEventSubscriberImpl();
	public static MockOrfinVadrReleaseEventSubscriberImpl getInstance() {
		return INSTANCE;
	}
	private MockOrfinVadrReleaseEventSubscriberImpl() {
	}
	
	
	private List<OrfinVadrReleaseEvent> events = new ArrayList<>();

	public void handleOrfinVadrReleaseEvent(OrfinVadrReleaseEvent orfinVadrReleaseEvent) throws ValidationException {
		this.events.add(orfinVadrReleaseEvent);
	}
	
	public List<OrfinVadrReleaseEvent> getOrfinVadrReleaseEvent() {
		return this.events;
	}
	
	public void clearEvents() {
		this.events.clear();
	}
}
