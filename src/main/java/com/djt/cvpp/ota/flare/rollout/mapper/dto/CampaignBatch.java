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
public class CampaignBatch {

	private String campaignBatchName;
	private String tmcDeploymentId;
	private long beginDate;
	private long endDate;
	private String campaignBatchState;
	private List<VehicleCampaignBatch> vehicleCampaignBatches = new ArrayList<>();
}
