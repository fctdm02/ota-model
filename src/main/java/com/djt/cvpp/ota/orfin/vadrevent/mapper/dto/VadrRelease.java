/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.vadrevent.mapper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VadrRelease {
	
	private String domainName;
	private String domainInstanceName;
	private String domainInstanceDescription;
	private String domainInstanceVersion;
	private String appId;
	private String appVersion;
	private String productionState;
	private String softwarePriorityLevel;
	private String releaseDate;
}
