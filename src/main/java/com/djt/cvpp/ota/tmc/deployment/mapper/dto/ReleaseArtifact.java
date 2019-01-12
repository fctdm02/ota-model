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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ReleaseArtifact {
	
	private UUID tenantUuid;
	private UUID uuid;
	private String name;
	private String description;
	private URI uri;
	private Instant createdTimestamp;	
	private Author author;
	private List<Tag> tags = new ArrayList<>();
	
	private Integer byteLength;
	private String checksum;
	private String type;
}
