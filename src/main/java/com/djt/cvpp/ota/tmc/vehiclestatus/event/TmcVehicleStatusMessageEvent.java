/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.vehiclestatus.event;

import com.djt.cvpp.ota.common.event.AbstractEvent;
import com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TmcVehicleStatusMessageEvent extends AbstractEvent {
	
	private static final long serialVersionUID = -5077555245017589051L;

	
	private TmcVehicleStatusMessage payload;

	public TmcVehicleStatusMessageEvent(String owner, TmcVehicleStatusMessage payload) {
		super(owner);
		this.payload = payload;
	}
	
    public TmcVehicleStatusMessage getPayload() {
    	return this.payload;
    }	
}
