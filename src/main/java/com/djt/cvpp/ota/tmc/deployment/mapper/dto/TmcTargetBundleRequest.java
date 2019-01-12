/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.deployment.mapper.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 * 
 * TmcTargetBundleRequest:
 * <pre>
	{
		"tenant_id": "ed67d6c9-3f14-4e7d-8d08-94265a53f326",
		"name": "1FA6P8TH4J5107939_3-1",
		"description": "bundle",
		"author": {
			"id": "e100a463-54d1-4b56-95bc-81a77b725018",
			"tenant_id": "ed67d6c9-3f14-4e7d-8d08-94265a53f326",
			"name": "TMC Communicator",
			"description": "microservice",
			"type": "microservice"
		},
		"artifact_ids": ["3a6fda3e-fa7b-4f85-a53b-54a9761990e4", "b77733a3-335e-448d-a9af-d781ccf1330e"],
		"matchers": [{
			"name": "vin",
			"value": "1FA6P8TH4J5107939",
			"type": "Field"
		}],
		"tags": {}
	} 
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@Data
public class TmcTargetBundleRequest {

	private UUID tenantUuid;
	private String name;
	private String description;
	private Author author;
	private List<TargetMatcher> targetMatchers = new ArrayList<>();
	
	@JsonProperty("artifact_ids")
	private List<UUID> releaseArtifactUuids = new ArrayList<>();
	
	private List<Tag> tags = new ArrayList<>();	
}
