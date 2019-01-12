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
 * ReleaseArtifactRequest:
 * <pre>
	{
		"tenant_id": "ed67d6c9-3f14-4e7d-8d08-94265a53f326",
		"name": "1FA6P8TH4J5107939_3-1",
		"description": "manifest",
		"author": {
			"id": "e100a463-54d1-4b56-95bc-81a77b725018",
			"tenant_id": "ed67d6c9-3f14-4e7d-8d08-94265a53f326",
			"name": "TMC Communicator",
			"description": "microservice",
			"type": "microservice"
		},
		"uri": "aui:bytestream::/M_3-1",
		"type": "manifest",
		"checksum": [],
		"byte_length": 1,
		"tags": {}
	} 
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@Data
public class TmcReleaseArtifactRequest {
	
	private UUID tenantUuid;
	private String name;
	private String description;
	private Author author;
	private URI uri;
	private String type;
	private String checksum;
	
	@JsonProperty("byte_length")
	private Integer byteLength;
	private List<Tag> tags = new ArrayList<>();
}
