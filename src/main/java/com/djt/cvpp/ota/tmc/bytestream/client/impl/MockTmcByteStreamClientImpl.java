/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.bytestream.client.impl;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.repository.impl.AbstractMockRepository;
import com.djt.cvpp.ota.flare.manifest.model.GenericManifest;
import com.djt.cvpp.ota.flare.rollout.model.Campaign;
import com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch;
import com.djt.cvpp.ota.flare.signedcommands.model.VehicleCampaignBatchSignedCommand;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.tmc.bytestream.client.TmcByteStreamClient;
import com.djt.cvpp.ota.tmc.bytestream.mapper.TmcBinaryDtoMapper;
import com.djt.cvpp.ota.tmc.bytestream.mapper.TmcBinaryJsonConverter;
import com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockTmcByteStreamClientImpl extends AbstractMockRepository implements TmcByteStreamClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockTmcByteStreamClientImpl.class);

	private TmcBinaryJsonConverter tmcBinaryJsonConverter = new TmcBinaryJsonConverter();
	private TmcBinaryDtoMapper tmcBinaryDtoMapper = new TmcBinaryDtoMapper();
		
	private Map<String, TmcBinary> tmcBinaryMap = new TreeMap<>();
	
	public void reset() {
		this.tmcBinaryMap.clear();
	}
	
	public TmcBinary uploadGenericManifestToTmcByteStream(Campaign campaign) {

		GenericManifest genericManifest = campaign.getGenericManifest();

		UUID binaryId = UUID.randomUUID();
		String description = "manifest-" + campaign.getNaturalIdentity();
		byte[] byteArray = genericManifest.getManifestPayload().getBytes(Charset.forName("UTF-8"));
				
		TmcBinary tmcBinary = this.createTmcBinary(binaryId, description, byteArray);
		LOGGER.debug("Created TmcBinary for: {} with uuid: {}", campaign, binaryId);
		
		campaign.setTmcGenericManifestBinaryId(tmcBinary.getBinaryId());
		
		return tmcBinary;
	}

	public TmcBinary uploadVehicleCampaignBatchSignedCommandToTmcByteStream(VehicleCampaignBatch vehicleCampaignBatch) {

		UUID binaryId = UUID.randomUUID();
		String description = "signedcommands-" + vehicleCampaignBatch.getNaturalIdentity();

		byte[] byteArray = null;
		Iterator<VehicleCampaignBatchSignedCommand> iterator = vehicleCampaignBatch.getVehicleCampaignBatchSignedCommands().iterator();
		while (iterator.hasNext()) {
			
			VehicleCampaignBatchSignedCommand vehicleCampaignBatchSignedCommand = iterator.next();
			byteArray = ArrayUtils.addAll(vehicleCampaignBatchSignedCommand.getValue());
		}

		TmcBinary tmcBinary = this.createTmcBinary(binaryId, description, byteArray);
		LOGGER.debug("Created TmcBinary for: {} with uuid: {}", vehicleCampaignBatch, binaryId);
		
		vehicleCampaignBatch.setTmcSignedCommandsBinaryId(tmcBinary.getBinaryId());
		
		return tmcBinary;
	}	
	
	public TmcBinary uploadPolicyTableJsonToTmcByteStream(PolicySet policySet, String policyTableJson) {
	
		UUID binaryId = UUID.randomUUID();
		String description = "policy-" + policySet.getNaturalIdentity();
		byte[] byteArray = policyTableJson.getBytes(Charset.forName("UTF-8"));
				
		TmcBinary tmcBinary = this.createTmcBinary(binaryId, description, byteArray);
		LOGGER.debug("Created TmcBinary for: {} with uuid: {}", policySet, binaryId);
		
		return tmcBinary;
	}

	public TmcBinary uploadOdlJsonToTmcByteStream(Odl odl, String odlJson) {

		UUID binaryId = UUID.randomUUID();
		String description = "odl-" + odl.getNaturalIdentity();
		byte[] byteArray = odlJson.getBytes(Charset.forName("UTF-8"));
				
		TmcBinary tmcBinary = this.createTmcBinary(binaryId, description, byteArray);
		LOGGER.debug("Created TmcBinary for: {} with uuid: {}", odl, binaryId);
		
		return tmcBinary;
	}
	
	private TmcBinary createTmcBinary(
		UUID binaryId,
		String description,
		byte[] byteArray) {
		
		TmcBinary tmcBinary = new TmcBinary(
			binaryId,
			description,	
			byteArray);
		
		tmcBinaryMap.put(binaryId.toString(), tmcBinary);
	
		return tmcBinary;
	}
	
	public TmcBinaryJsonConverter getTmcBinaryJsonConverter() {
		return this.tmcBinaryJsonConverter;
	}

	public TmcBinaryDtoMapper getTmcBinaryDtoMapper() {
		return this.tmcBinaryDtoMapper;
	}
	
	public AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity) {
		throw new UnsupportedOperationException("This method is not supported by TmcByteStreamClient");
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		throw new UnsupportedOperationException("This method is not supported by TmcByteStreamClient");
	}
	
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		throw new UnsupportedOperationException("This method is not supported by TmcByteStreamClient");
	}
}
