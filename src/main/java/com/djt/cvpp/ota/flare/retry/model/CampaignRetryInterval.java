/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.retry.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class CampaignRetryInterval extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private Integer intervalDays;
	
	public CampaignRetryInterval(Integer intervalDays) {

		this.intervalDays = intervalDays;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			intervalDays
		);
	}

	public void validate(List<String> validationMessages) {
		
		validateNotNull(validationMessages, "intervalDays", intervalDays);
	}

	public Integer getIntervalDays() {
		return intervalDays;
	}
}
