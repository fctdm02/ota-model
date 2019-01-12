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

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.flare.rollout.model.Campaign;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class GenericManifest extends AbstractEntity {

	private static final long serialVersionUID = 1L;


	private Campaign parentCampaign;
	private String manifestName;
	private String triggerType;
	private String audience;
	private String releaseNotes;
	private Timestamp timestamp;
	private String manifestPayload;
	
	@SuppressWarnings("unused")
	private GenericManifest() {
	}

	public GenericManifest(
		Campaign parentCampaign,
		String manifestName,
		String triggerType,
		String audience,
		String releaseNotes,
		Timestamp timestamp,
		String manifestPayload) {
		
		this.parentCampaign = parentCampaign;
		this.manifestName = manifestName;
		this.triggerType = triggerType;
		this.audience = audience;
		this.releaseNotes = releaseNotes;
		this.timestamp = new Timestamp(timestamp.getTime());
		this.manifestPayload = manifestPayload;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentCampaign,
			manifestName);
	}
	
	public void validate(List<String> validationMessages) {

		validateNotNull(validationMessages, "parentCampaign", parentCampaign);
		validateNotNull(validationMessages, "manifestName", manifestName); 
		validateNotNull(validationMessages, "triggerType", triggerType); // TODO: TDM: Change this to an enum
		validateNotNull(validationMessages, "audience", audience); // TODO: TDM: Change this to an enum 
		validateNotNull(validationMessages, "releaseNotes", releaseNotes);
		validateDate(validationMessages, "timestamp", timestamp);
		validateNotNull(validationMessages, "manifestPayload", manifestPayload);
		
		validateManifestPayload(validationMessages);
	}
	
	public abstract void validateManifestPayload(List<String> validationMessages);

	public Campaign getParentCampaign() {
		return parentCampaign;
	}

	public String getManifestName() {
		return manifestName;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public String getAudience() {
		return audience;
	}

	public String getReleaseNotes() {
		return releaseNotes;
	}

	public Timestamp getTimestamp() {
		return new Timestamp(timestamp.getTime());
	}

	public String getManifestPayload() {
		return manifestPayload;
	}


	// BUSINESS BEHAVIORS
	public abstract void generateManifest();
}
