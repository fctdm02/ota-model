/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.deployment.model;

import java.sql.Timestamp;
import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class DeviceState extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private String state;
	private Timestamp lastUpdated;
	
	public DeviceState(
		String state,
		Timestamp lastUpdated) {
		this.state = state;
		this.lastUpdated = new Timestamp(lastUpdated.getTime());
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			state,
			lastUpdated);
	}

	public void validate(List<String> validationMessages) {

		if (state == null || state.trim().isEmpty()) {
			validationMessages.add("state must be specified.");
		}

		if (lastUpdated == null) {
			validationMessages.add("lastUpdated must be a positive integer");
		}
	}

	public String getState() {
		return state;
	}

	public Timestamp getLastUpdated() {
		return new Timestamp(lastUpdated.getTime());
	}	
}
