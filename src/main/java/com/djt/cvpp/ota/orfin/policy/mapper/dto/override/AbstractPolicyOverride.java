/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper.dto.override;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class AbstractPolicyOverride {
	
	private AbstractPolicyValue policyOverrideValue;
}
