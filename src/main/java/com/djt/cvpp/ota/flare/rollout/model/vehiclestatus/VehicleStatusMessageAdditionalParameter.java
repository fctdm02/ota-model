/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.model.vehiclestatus;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class VehicleStatusMessageAdditionalParameter extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	
	private VehicleStatusMessage parentVehicleStatusMessage;
	private Integer sequenceNumber;
	private String additionalParameterLabelName;

	public VehicleStatusMessageAdditionalParameter(
		VehicleStatusMessage parentVehicleStatusMessage,
		Integer sequenceNumber,
		String additionalParameterLabelName) {

		this.parentVehicleStatusMessage = parentVehicleStatusMessage;
		this.sequenceNumber = sequenceNumber;
		this.additionalParameterLabelName = additionalParameterLabelName;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentVehicleStatusMessage,
			sequenceNumber,
			additionalParameterLabelName
		);
	}

	public void validate(List<String> validationMessages) {

		validateNotNull(validationMessages, "parentVehicleStatusMessage", parentVehicleStatusMessage);
		validateNotNull(validationMessages, "sequenceNumber", sequenceNumber);
		validateNotNull(validationMessages, "additionalParameterLabelName", additionalParameterLabelName);
		validateNotNull(validationMessages, "additionalParameterValue", getAdditionalParameterValue());
	}

	public VehicleStatusMessage getParentVehicleStatusMessage() {
		return this.parentVehicleStatusMessage;
	}
	
	public Integer getSequenceNumber() {
		return this.sequenceNumber;
	}

	public String getAdditionalParameterLabelName() {
		return this.additionalParameterLabelName;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(AbstractEntity that) {
		
		return this.getSequenceNumber().compareTo(((VehicleStatusMessageAdditionalParameter)(that)).getSequenceNumber());	
	}
	
	
	// BUSINESS BEHAVIORS
	public abstract Object getAdditionalParameterValue();
}
