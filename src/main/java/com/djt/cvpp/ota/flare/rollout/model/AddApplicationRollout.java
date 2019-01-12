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
public class AddApplicationRollout extends ApplicationUpdateRollout {
	
	private static final long serialVersionUID = 1L;

	
	public AddApplicationRollout(
		String rolloutName,
		String deliveryRuleSetName) {
		super(rolloutName, deliveryRuleSetName);
	}	
}
