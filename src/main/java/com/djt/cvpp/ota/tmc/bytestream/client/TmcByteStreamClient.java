/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.bytestream.client;

import com.djt.cvpp.ota.flare.rollout.model.Campaign;
import com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.tmc.bytestream.mapper.TmcBinaryDtoMapper;
import com.djt.cvpp.ota.tmc.bytestream.mapper.TmcBinaryJsonConverter;
import com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface TmcByteStreamClient {

	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "TMC";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "BYTESTREAM";
	
	/**
	 * 
	 * @param campaign
	 */
	TmcBinary uploadGenericManifestToTmcByteStream(Campaign campaign);	

	/**
	 * 
	 * @param vehicleCampaignBatch
	 */
	TmcBinary uploadVehicleCampaignBatchSignedCommandToTmcByteStream(VehicleCampaignBatch vehicleCampaignBatch);
	
	/**
	 * 
	 * @param policySet
	 * @param policyTableJson
	 */
	TmcBinary uploadPolicyTableJsonToTmcByteStream(PolicySet policySet, String policyTableJson);	

	/**
	 * 
	 * @param odl
	 * @param odlJson
	 */
	TmcBinary uploadOdlJsonToTmcByteStream(Odl odl, String odlJson);
	
	/**
	 * 
	 * @return
	 */
	TmcBinaryJsonConverter getTmcBinaryJsonConverter();

	/**
	 * 
	 * @return
	 */
	TmcBinaryDtoMapper getTmcBinaryDtoMapper();
}
