/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.deployment.mapper.dto;

import java.net.URI;
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
 * TmcDeploymentRequest:
 * <pre>
	{
		"tenant_id": "ed67d6c9-3f14-4e7d-8d08-94265a53f326",
		"name": "3-1 deployment",
		"description": "deploy",
		"author": {
			"id": "e100a463-54d1-4b56-95bc-81a77b725018",
			"tenant_id": "ed67d6c9-3f14-4e7d-8d08-94265a53f326",
			"name": "TMC Communicator",
			"description": "microservice",
			"type": "microservice"
		},
		"release_id": "263cda1d-6d06-42ce-8469-7b76b9b27203",
		"callback_uri": "",
		"tags": {}
	} 
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@Data
public class TmcDeploymentRequest {

	private UUID tenantUuid;
	private String name;
	private String description;
	private Author author;
	
	@JsonProperty("release_id")
	private UUID releaseUuid;
	
	@JsonProperty("callback_uri")
	private URI uri;

	private List<Tag> tags = new ArrayList<>();	
}
