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
public class RemoveApplicationRollout extends ApplicationUpdateRollout {
	
	private static final long serialVersionUID = 1L;

	
	public RemoveApplicationRollout(
		String rolloutName,
		String deliveryRuleSetName) {
		super(rolloutName, deliveryRuleSetName);
	}	
}
