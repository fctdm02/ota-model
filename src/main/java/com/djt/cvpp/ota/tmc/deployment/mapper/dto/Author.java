/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.deployment.mapper.dto;

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
public class Author {
	
	private UUID authorId;
	private UUID tenantId;
	private String name;
	private String description;
	private String type;
}
