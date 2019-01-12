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
public class SoftwareWithDirectConfigurationRollout extends SoftwareUpdateRollout implements DirectConfigurationBasedRollout {
	
	private static final long serialVersionUID = 1L;

	
	private String nodeAcronym;
	private String nodeAddress;
	
	public SoftwareWithDirectConfigurationRollout(
		String rolloutName,
		String deliveryRuleSetName,
		String nodeAcronym,
		String nodeAddress) {
		super(rolloutName, deliveryRuleSetName);
		this.nodeAcronym = nodeAcronym;
		this.nodeAddress = nodeAddress;
	}

	public String getNodeAcronym() {
		return nodeAcronym;
	}

	public String getNodeAddress() {
		return nodeAddress;
	}
}
