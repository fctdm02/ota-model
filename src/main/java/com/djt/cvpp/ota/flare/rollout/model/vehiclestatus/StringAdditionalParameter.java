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
public class StringAdditionalParameter extends VehicleStatusMessageAdditionalParameter {

	private static final long serialVersionUID = 1L;


	private String stringValue;

	public StringAdditionalParameter(
		VehicleStatusMessage parentVehicleStatusMessage,
		Integer sequenceNumber,
		String labelName,
		String stringValue) {
		
		super(parentVehicleStatusMessage, sequenceNumber, labelName);
		this.stringValue = stringValue;
	}

	
	// BUSINESS BEHAVIORS
	public String getAdditionalParameterValue() {
		return this.stringValue;
	}
}
