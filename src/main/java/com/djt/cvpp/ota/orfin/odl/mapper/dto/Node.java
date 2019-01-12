/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.mapper.dto;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {
	
	private String acronym;
	private String address;
	private String gatewayNodeId;
	private String gatewayType;
	private Boolean hasConditionBasedOnDtc;
	private Boolean isOvtp;
	private String ovtpDestinationAddress;
	private Integer activationTime;
	private Integer vehicleInhibitActivationTime;
	private Integer diagnosticSpecificationResponse;
	private String specificationCategoryType;
	private List<Did> dids = new ArrayList<>();
	private List<String> ignoredDids = new ArrayList<>();
}
