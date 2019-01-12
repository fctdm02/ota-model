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
public class CcCounter extends AbstractCounter {

	private static final long serialVersionUID = 1L;


	public CcCounter(
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
