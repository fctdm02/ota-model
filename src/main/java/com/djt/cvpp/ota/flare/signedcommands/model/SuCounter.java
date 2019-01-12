/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.signedcommands.model;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class SuCounter extends AbstractCounter {

	private static final long serialVersionUID = 1L;


	public SuCounter(
		VehicleCampaignBatchSignedCommand parentVehicleCampaignBatchSignedCommand,
		String fesn,
		Integer msb,
		Integer isb,
		Integer lsb) {
		super(
				parentVehicleCampaignBatchSignedCommand,
			fesn,
			msb,
			isb,
			lsb);
	}
}
