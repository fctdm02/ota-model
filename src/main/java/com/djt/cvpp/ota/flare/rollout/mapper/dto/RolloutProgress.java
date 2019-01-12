/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.mapper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.djt.cvpp.ota.flare.rollout.model.enums.RolloutState;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class RolloutProgress {

	private int totalNumberOfCampaigns;
	private int totalNumberofBatches;
	private int totalNumberOfVehicles;
	private int totalNumberOfVehicleMessages;
	
	private int numberIncomplete;
	private int numberComplete;
	private int numberSuccessful;
	private int numberFailure;
	
	private double percentageComplete;
	private double percentageSuccessful;
	private double percentageFailure;
	
	private RolloutState rolloutState;
}
