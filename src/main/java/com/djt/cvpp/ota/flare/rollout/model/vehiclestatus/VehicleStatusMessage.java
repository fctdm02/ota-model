/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.model.vehiclestatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VehicleStatusMessage extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	public static final String FINISHED_WITH_SUCCESS_STATUS_MESSAGE_VALUE = "OTAM_S1010";
	public static final String FINISHED_WITH_FAILURE_PREFIX = "_E";
	
	
	private VehicleCampaignBatch parentVehicleCampaignBatch;
	private Timestamp timestamp;
	private String fullLookupCodeValue;
	private Set<VehicleStatusMessageAdditionalParameter> vehicleStatusMessageAdditionalParameters = new TreeSet<>();

	public VehicleStatusMessage(
		VehicleCampaignBatch parentVehicleCampaignBatch,
		Timestamp timestamp,
		String fullLookupCodeValue) {

		this.parentVehicleCampaignBatch = parentVehicleCampaignBatch;
		this.timestamp = new Timestamp(timestamp.getTime());
		this.fullLookupCodeValue = fullLookupCodeValue;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentVehicleCampaignBatch,
			fullLookupCodeValue
		);
	}

	public void validate(List<String> validationMessages) {

		validateNotNull(validationMessages, "parentVehicleCampaignBatch", parentVehicleCampaignBatch);
		validateNotNull(validationMessages, "timestamp", timestamp);
		validateNotNull(validationMessages, "fullLookupCodeValue", fullLookupCodeValue);

		if (!vehicleStatusMessageAdditionalParameters.isEmpty()) {
			Iterator<VehicleStatusMessageAdditionalParameter> vehicleStatusMessageAdditionalParametersIterator = vehicleStatusMessageAdditionalParameters.iterator();
			while (vehicleStatusMessageAdditionalParametersIterator.hasNext()) {

				VehicleStatusMessageAdditionalParameter vehicleStatusMessageAdditionalParameter = vehicleStatusMessageAdditionalParametersIterator.next();
				vehicleStatusMessageAdditionalParameter.validate(validationMessages);
			}
		}
	}


	public VehicleCampaignBatch getParentVehicleCampaignBatch() {
		return parentVehicleCampaignBatch;
	}

	public Timestamp getTimestamp() {
		return new Timestamp(timestamp.getTime());
	}

	public String getFullLookupCodeValue() {
		return fullLookupCodeValue;
	}

	public List<VehicleStatusMessageAdditionalParameter> getVehicleStatusMessageAdditionalParameters() {
		List<VehicleStatusMessageAdditionalParameter> list = new ArrayList<>();
		list.addAll(this.vehicleStatusMessageAdditionalParameters);
		return list;
	}
	
	public void setVehicleStatusMessageAdditionalParameters(List<VehicleStatusMessageAdditionalParameter> vehicleStatusMessageAdditionalParameters) {
		this.vehicleStatusMessageAdditionalParameters.addAll(vehicleStatusMessageAdditionalParameters);
	}

	
	// BUSINESS BEHAVIORS
	public Map<String, Object> generateLabeledAdditionalParametersMap() {

		Map<String, Object> map = new TreeMap<>();
		Iterator<VehicleStatusMessageAdditionalParameter> vehicleStatusMessageAdditionalParametersIterator = vehicleStatusMessageAdditionalParameters.iterator();
		while (vehicleStatusMessageAdditionalParametersIterator.hasNext()) {

			VehicleStatusMessageAdditionalParameter vehicleStatusMessageAdditionalParameter = vehicleStatusMessageAdditionalParametersIterator.next();
			map.put(vehicleStatusMessageAdditionalParameter.getAdditionalParameterLabelName(), vehicleStatusMessageAdditionalParameter.getAdditionalParameterValue());
		}
		return map;
	}
	
	public String generateLabeledAdditionalParameters() {

		StringBuilder sb = new StringBuilder();
		Iterator<VehicleStatusMessageAdditionalParameter> vehicleStatusMessageAdditionalParametersIterator = vehicleStatusMessageAdditionalParameters.iterator();
		while (vehicleStatusMessageAdditionalParametersIterator.hasNext()) {

			VehicleStatusMessageAdditionalParameter vehicleStatusMessageAdditionalParameter = vehicleStatusMessageAdditionalParametersIterator.next();
			String name = vehicleStatusMessageAdditionalParameter.getAdditionalParameterLabelName();
			Object value = vehicleStatusMessageAdditionalParameter.getAdditionalParameterValue();
			
			if (!name.equals("BLANK-LABEL")) {
				
				sb.append(name);
				sb.append(": ");
			}			
			sb.append(value);
			if (vehicleStatusMessageAdditionalParametersIterator.hasNext()) {
				sb.append(" | ");
			}
		}
		return sb.toString();
	}
	
	public String generateRolloutDataAccessorLookupCode() {
		
		return fullLookupCodeValue;
	}
}
