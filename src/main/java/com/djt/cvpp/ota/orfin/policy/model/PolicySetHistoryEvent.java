/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.model;

import java.util.Map;

import com.djt.cvpp.ota.common.event.AbstractEvent;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class PolicySetHistoryEvent extends AbstractEvent {
	
	private static final long serialVersionUID = 1L;

	
	private Map<String, String> payload;
	
	public PolicySetHistoryEvent(
		PolicySet parentPolicySet,
		Map<String, String> payload) {
		
		super(parentPolicySet.getNaturalIdentity());
		
		this.payload = payload;
	}
	
	public Object getPayload() {
	
		return this.payload.toString();
	}
}
