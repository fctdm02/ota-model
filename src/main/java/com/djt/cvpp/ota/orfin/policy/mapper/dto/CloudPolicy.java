/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.djt.cvpp.ota.orfin.policy.mapper.dto.value.AbstractPolicyValue;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CloudPolicy extends AbstractPolicy {
	
	private String policyName;
	private String policyDescription;
	private AbstractPolicyValue policyValue;
	private String policyValueType;
	private String policyValueConstraints; 	
}
