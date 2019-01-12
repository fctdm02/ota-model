/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.deployment.model;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.djt.cvpp.ota.tmc.common.model.Author;
import com.djt.cvpp.ota.tmc.common.model.Tag;
import com.djt.cvpp.ota.tmc.common.model.TmcEntity;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public class Deployment extends TmcEntity {
	
	private static final long serialVersionUID = 1L;

	
	private Release release;
	private Set<DeviceState> deviceStates = new TreeSet<>();
	private Set<DeploymentState> deploymentStates = new TreeSet<>();
	
	public Deployment(
		UUID tenantUuid,	
		UUID uuid,
		String name,
		String description,
		URI uri,
		Instant createdTimestamp,
		Author author,
		Set<Tag> tags,
		Release release) {
		super(
			tenantUuid,	
			uuid,
			name,
			description,
			uri,
			createdTimestamp,
			author,
			tags);
		this.release = release;
	}

	public void validate(List<String> validationMessages) {
		
		super.validate(validationMessages);

		if (release == null) {
			validationMessages.add("release must be specified.");
		}
	}
		
	public Release getRelease() {
		return release;
	}

	public Set<DeviceState> getDeviceStates() {
		return deviceStates;
	}

	public void addDeviceStates(DeviceState deviceState) {
		this.deviceStates.add(deviceState);
	}

	public Set<DeploymentState> getDeploymentStates() {
		return deploymentStates;
	}

	public void addDeploymentStates(DeploymentState deploymentState) {
		this.deploymentStates.add(deploymentState);
	}
}
