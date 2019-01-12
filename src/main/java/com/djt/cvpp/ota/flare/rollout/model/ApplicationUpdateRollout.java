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
public class ApplicationUpdateRollout extends SoftwareUpdateRollout {
	
	private static final long serialVersionUID = 1L;

	
	public ApplicationUpdateRollout(
		String rolloutName,
		String deliveryRuleSetName) {
		super(rolloutName, deliveryRuleSetName);
	}	
}
