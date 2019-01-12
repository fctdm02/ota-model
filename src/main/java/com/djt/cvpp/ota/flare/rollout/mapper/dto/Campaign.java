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
public class Campaign {

	private long beginDate;
	private long endDate;
	private String domainName;
	private String domainInstanceName;
	private String domainInstanceVersion;
	private String applicationName;
	private String applicationVersion;
	private String programCode;
	private Integer modelYear;
	private String odlPayload;
	private String campaignState;
    private List<CampaignBatch> campaignBatches = new ArrayList<>();
}
