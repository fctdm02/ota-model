/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.model;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class SoftwareUpdateRollout extends AbstractRollout {
	
	private static final long serialVersionUID = 1L;
	
	
	private String deliveryRuleSetName;

	public SoftwareUpdateRollout(
		String rolloutName,
		String deliveryRuleSetName) {
		super(rolloutName);
		this.deliveryRuleSetName = deliveryRuleSetName;
	}

	public String getDeliveryRuleSetName() {
		return deliveryRuleSetName;
	}	
}
