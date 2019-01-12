/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.policy.event;

import com.djt.cvpp.ota.common.event.AbstractEvent;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class OrfinPolicySetEvent extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;

	private String programCode;
	private Integer modelYear;
	private String regionCode;
	private String policySetName;
	

	public OrfinPolicySetEvent(
		String owner,
		String programCode,
		Integer modelYear,
		String regionCode,
		String policySetName) {
		super(owner);
		this.programCode = programCode;
		this.modelYear = modelYear;
		this.regionCode = regionCode;
		this.policySetName = policySetName;
	}

	public String getProgramCode() {
		return programCode;
	}

	public Integer getModelYear() {
		return modelYear;
	}
	
	public String getRegionCode() {
		return regionCode;
	}

	public String getPayload() {
		return policySetName;
	}
}
