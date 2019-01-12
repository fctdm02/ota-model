/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Tags {

	private String campaignID;
    private String deploymentProtocol;
    private String deploymentURL;
    private String domain;
    private String domainInstance;
    private String domainInstanceVersion;
    private String programCode;
    private String modelYear;
}
