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

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public abstract class AbstractRollout {
	
	private String rolloutName;
	private String initiatedBy;
	private long creationDate;
	private long startDate;
	private Integer expirationDays;
	private long endDate;
	private String rolloutState;
	private String rolloutPhase;
	private Integer maxNumberActiveCampaignBatches;
	private Integer maxNumberVehiclesPerCampaignBatch;
	private Integer priorityLevel; 
	private List<Campaign> campaigns = new ArrayList<>();
}
