/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.model.vehiclestatus;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class BooleanAdditionalParameter extends VehicleStatusMessageAdditionalParameter {

	private static final long serialVersionUID = 1L;


	private Boolean booleanValue;

	public BooleanAdditionalParameter(
		VehicleStatusMessage parentVehicleStatusMessage,
		Integer sequenceNumber,
		String labelName,
		Boolean booleanValue) {
		
		super(parentVehicleStatusMessage, sequenceNumber, labelName);
		this.booleanValue = booleanValue;
	}

	
	// BUSINESS BEHAVIORS
	public Boolean getAdditionalParameterValue() {
		return this.booleanValue;
	}
}
