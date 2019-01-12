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
public class NumericAdditionalParameter extends VehicleStatusMessageAdditionalParameter {

	private static final long serialVersionUID = 1L;


	private Number numericValue;

	public NumericAdditionalParameter(
		VehicleStatusMessage parentVehicleStatusMessage,
		Integer sequenceNumber,
		String labelName,
		Number numericValue) {
		
		super(parentVehicleStatusMessage, sequenceNumber, labelName);
		this.numericValue = numericValue;
	}

	
	// BUSINESS BEHAVIORS
	public Number getAdditionalParameterValue() {
		return this.numericValue;
	}
}
