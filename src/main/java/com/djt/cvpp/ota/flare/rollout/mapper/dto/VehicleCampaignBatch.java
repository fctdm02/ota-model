/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.mapper.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.VehicleStatusMessage;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class VehicleCampaignBatch {

	private String vin;
	private String tmcVehicleId;
	private String tmcVehicleObjectPayload;	
	private long beginDate;
	private long endDate;
	private Integer numRetries;
	private long lastRetryDate;
	private Boolean isValidationVehicle;
	private String vehicleCampaignBatchState;
	private List<VehicleStatusMessage> vehicleStatusMessages = new ArrayList<>();
}
