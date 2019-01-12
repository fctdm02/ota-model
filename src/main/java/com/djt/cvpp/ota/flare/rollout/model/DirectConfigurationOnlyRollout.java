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
public class DirectConfigurationOnlyRollout extends AbstractRollout implements DirectConfigurationBasedRollout {
	
	private static final long serialVersionUID = 1L;

	
	private String nodeAcronym;
	private String nodeAddress;

	public DirectConfigurationOnlyRollout(
		String rolloutName,
		String nodeAcronym,
		String nodeAddress) {
		super(rolloutName);
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
