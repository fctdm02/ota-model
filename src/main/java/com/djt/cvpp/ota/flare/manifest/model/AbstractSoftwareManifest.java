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

import com.djt.cvpp.ota.flare.rollout.model.Campaign;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractSoftwareManifest extends GenericManifest {

	private static final long serialVersionUID = 1L;


	public AbstractSoftwareManifest(
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
}
