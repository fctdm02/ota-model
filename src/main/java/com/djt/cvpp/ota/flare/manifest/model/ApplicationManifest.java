/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.manifest.model;

import java.sql.Timestamp;
import java.util.List;

import com.djt.cvpp.ota.flare.rollout.model.Campaign;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class ApplicationManifest extends AbstractSoftwareManifest {

	private static final long serialVersionUID = 1L;

	public ApplicationManifest(
		Campaign parentCampaign,
		String manifestName,
		String triggerType,
		String audience,
		String releaseNotes,
		Timestamp timestamp,
		String manifestPayload) {
		super(
			parentCampaign,
			manifestName,
			triggerType,
			audience,
			releaseNotes,
			timestamp,
			manifestPayload);
	}
	
	public void validateManifestPayload(List<String> validationMessages) {
		
		// TODO: TDM: Deserialize JSON payload (i.e. manifestPayload) to DTO object graph and validate its contents.
		// NOTE: The manifest payload is the result of generating the manifest for the associated parent campaign and is what will be sent to TMC as a release artifact.
	}
	
	
	// BUSINESS BEHAVIORS
	public void generateManifest() {
		
		// TODO: TDM: Walk all necessary data and create the manifest here (as a DTO object graph) and assign to the "manifestPayload" attribute. 
	}
}



