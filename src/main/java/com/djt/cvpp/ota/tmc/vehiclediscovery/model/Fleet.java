/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.model;

import java.util.List;
import java.util.UUID;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Fleet extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;


	private UUID fleetId;
	
	public Fleet(UUID fleetId) {
		this.fleetId = fleetId;
	}

	public String getNaturalIdentity() {
		return this.fleetId.toString();
	}

	public void validate(List<String> validationMessages) {

		if (fleetId == null) {
			validationMessages.add("fleetId must be specified.");
		}
	}
	
	public UUID getFleetId() {
		return fleetId;
	}	
}
