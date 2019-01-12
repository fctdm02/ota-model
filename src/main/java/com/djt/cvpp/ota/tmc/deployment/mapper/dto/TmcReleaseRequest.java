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
 * 
 * TmcReleaseRequest:
 * <pre>
	{
		"tenant_id": "ed67d6c9-3f14-4e7d-8d08-94265a53f326",
		"name": "3-1 deployment",
		"description": "release",
		"author": {
			"id": "e100a463-54d1-4b56-95bc-81a77b725018",
			"tenant_id": "ed67d6c9-3f14-4e7d-8d08-94265a53f326",
			"name": "TMC Communicator",
			"description": "microservice",
			"type": "microservice"
		},
		"bundle_ids": ["8a93f235-e0c9-4f87-bf12-0e23c19c7fba"],
		"tags": {}
	} 
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@Data
public class TmcReleaseRequest {
	
	private UUID tenantUuid;
	private String name;
	private String description;
	private Author author;
	
	@JsonProperty("bundle_ids")
	private List<UUID> targetBundleUuids = new ArrayList<>();
	
	private List<Tag> tags = new ArrayList<>();
}
