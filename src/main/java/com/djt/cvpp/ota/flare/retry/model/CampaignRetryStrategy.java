/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.retry.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class CampaignRetryStrategy extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private String retryStrategyName;
	private Set<CampaignRetryInterval> campaignRetryIntervals;
		
	public CampaignRetryStrategy(
		String retryStrategyName,
		Set<CampaignRetryInterval> campaignRetryIntervals) {

		this.retryStrategyName = retryStrategyName;
		this.campaignRetryIntervals = campaignRetryIntervals;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			retryStrategyName,
			campaignRetryIntervals
		);
	}

	public void validate(List<String> validationMessages) {
		
		validateNotNull(validationMessages, "retryStrategyName", retryStrategyName);

		if (campaignRetryIntervals == null || campaignRetryIntervals.isEmpty()) {
			validationMessages.add(this.getClassAndIdentity() + " at least one CampaignRetryInterval must be specified.");
		} else {
			Iterator<CampaignRetryInterval> iterator = campaignRetryIntervals.iterator();
			while (iterator.hasNext()) {

				CampaignRetryInterval campaignRetryInterval = iterator.next();
				campaignRetryInterval.validate(validationMessages);
			}
		}
	}

	public String getRetryStrategyName() {
		return retryStrategyName;
	}

	public Set<CampaignRetryInterval> getCampaignRetryIntervals() {
		return campaignRetryIntervals;
	}
}
