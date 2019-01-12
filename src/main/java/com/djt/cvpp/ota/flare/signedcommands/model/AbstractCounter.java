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

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractCounter  extends AbstractEntity {

	private static final long serialVersionUID = 1L;


	private VehicleCampaignBatchSignedCommand parentVehicleCampaignBatchSignedCommand;
	private String fesn;
	private Integer msb;
	private Integer isb;
	private Integer lsb;

	@SuppressWarnings("unused")
	private AbstractCounter() {
	}
	
	public AbstractCounter(
		VehicleCampaignBatchSignedCommand parentVehicleCampaignBatchSignedCommand,
		String fesn,
		Integer msb,
		Integer isb,
		Integer lsb) {
		this.parentVehicleCampaignBatchSignedCommand = parentVehicleCampaignBatchSignedCommand;
		this.fesn = fesn;
		this.msb = msb;
		this.isb = isb;
		this.lsb = lsb;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
				parentVehicleCampaignBatchSignedCommand, 
			fesn);
	}

	public void validate(List<String> validationMessages) {
		validateNotNull(validationMessages, "parentVehicleCampaignBatchSignedCommand", parentVehicleCampaignBatchSignedCommand);
		validateNotNull(validationMessages, "fesn", fesn); 
		validateNumber(validationMessages, "msb", msb, Integer.valueOf(0));
		validateNumber(validationMessages, "isb", isb, Integer.valueOf(0));
		validateNumber(validationMessages, "lsb", lsb, Integer.valueOf(0));
	}

	public VehicleCampaignBatchSignedCommand getParentVehicleCampaignBatchSignedCommand() {
		return parentVehicleCampaignBatchSignedCommand;
	}

	public String getFesn() {
		return fesn;
	}

	public Integer getMsb() {
		return msb;
	}

	public Integer getIsb() {
		return isb;
	}

	public Integer getLsb() {
		return lsb;
	}
}
