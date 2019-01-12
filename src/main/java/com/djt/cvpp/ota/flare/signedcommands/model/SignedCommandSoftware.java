/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.signedcommands.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.flare.rollout.model.Campaign;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class SignedCommandSoftware extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	
	private Campaign parentCampaign;
	private String ecuName;
	private String ecuAddress;
	private String sourceSoftwarePartNumber;
	private String sourceFileName;
	private String destinationPartNumber;
	private String destinationFileName;

	@SuppressWarnings("unused")
	private SignedCommandSoftware() {
	}
	
	public SignedCommandSoftware(
		Campaign parentCampaign,
		String ecuName,
		String ecuAddress,
		String sourceSoftwarePartNumber,
		String sourceFileName,
		String destinationPartNumber,
		String destinationFileName) {
		
		this.parentCampaign = parentCampaign;
		this.ecuName = ecuName;
		this.ecuAddress = ecuAddress;
		this.sourceSoftwarePartNumber = sourceSoftwarePartNumber;
		this.sourceFileName = sourceFileName;
		this.destinationPartNumber = destinationPartNumber;
		this.destinationFileName = destinationFileName;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentCampaign,
			ecuName,
			ecuAddress);
	}

	public void validate(List<String> validationMessages) {
		validateNotNull(validationMessages, "parentCampaign", parentCampaign);
		validateNotNull(validationMessages, "ecuName", ecuName);
		validateNotNull(validationMessages, "ecuAddress", ecuAddress);
		validateNotNull(validationMessages, "sourceSoftwarePartNumber", sourceSoftwarePartNumber);
		validateNotNull(validationMessages, "sourceFileName", sourceFileName);
		validateNotNull(validationMessages, "destinationPartNumber", destinationPartNumber);
		validateNotNull(validationMessages, "destinationFileName", destinationFileName);
	}

	public Campaign getParentCampaign() {
		return parentCampaign;
	}

	public String getEcuName() {
		return ecuName;
	}

	public String getEcuAddress() {
		return ecuAddress;
	}

	public String getSourceSoftwarePartNumber() {
		return sourceSoftwarePartNumber;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public String getDestinationPartNumber() {
		return destinationPartNumber;
	}

	public String getDestinationFileName() {
		return destinationFileName;
	}
}
