/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.vehiclestatus.event.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.flare.rollout.model.Campaign;
import com.djt.cvpp.ota.tmc.vehiclestatus.event.TmcVehicleStatusMessageEvent;
import com.djt.cvpp.ota.tmc.vehiclestatus.event.TmcVehicleStatusMessageEventPublisher;
import com.djt.cvpp.ota.tmc.vehiclestatus.event.TmcVehicleStatusMessageEventSubscriber;

/**
  *
  * @author tmyers1@yahoo.com (Tom Myers)
  *
  */
public class MockTmcVehicleStatusMessageEventPublisher implements TmcVehicleStatusMessageEventPublisher {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Campaign.class);
	

	private static MockTmcVehicleStatusMessageEventPublisher INSTANCE = new MockTmcVehicleStatusMessageEventPublisher();
	public static MockTmcVehicleStatusMessageEventPublisher getInstance() {
		return INSTANCE;
	}
	private MockTmcVehicleStatusMessageEventPublisher() {
	}
	
	private List<TmcVehicleStatusMessageEventSubscriber> subscribers = new ArrayList<>();
	
	public void subscribe(TmcVehicleStatusMessageEventSubscriber tmcVehicleStatusMessageEventSubscriber) {
		
		this.subscribers.add(tmcVehicleStatusMessageEventSubscriber);
	}
	
	public void unsubscribe(TmcVehicleStatusMessageEventSubscriber tmcVehicleStatusMessageEventSubscriber) {
		
		this.subscribers.remove(tmcVehicleStatusMessageEventSubscriber);
	}
	
	public void publishTmcVehicleStatusMessageEvent(TmcVehicleStatusMessageEvent tmcVehicleStatusMessageEvent) throws EntityDoesNotExistException, ValidationException {
		
		Iterator<TmcVehicleStatusMessageEventSubscriber> iterator = this.subscribers.iterator();
		while (iterator.hasNext()) {
			
			TmcVehicleStatusMessageEventSubscriber tmcVehicleStatusMessageEventSubscriber = iterator.next();
			tmcVehicleStatusMessageEventSubscriber.handleTmcVehicleStatusMessageEvent(tmcVehicleStatusMessageEvent);
			LOGGER.debug("Publishing TMC VehicleStatusMessage: [{}] for TmcVehicle with TMC TmcVehicle ID: [{}].", tmcVehicleStatusMessageEvent.getPayload().getVehicleStatusMessageExpression(), tmcVehicleStatusMessageEvent.getPayload().getVehicleId());
		}
	}
}
