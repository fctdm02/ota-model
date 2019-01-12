/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.deployment.client.impl;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.repository.impl.AbstractMockRepository;
import com.djt.cvpp.ota.flare.manifest.model.GenericManifest;
import com.djt.cvpp.ota.flare.rollout.model.Campaign;
import com.djt.cvpp.ota.flare.rollout.model.CampaignBatch;
import com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch;
import com.djt.cvpp.ota.flare.rollout.model.enums.CampaignBatchState;
import com.djt.cvpp.ota.flare.signedcommands.model.VehicleCampaignBatchSignedCommand;
import com.djt.cvpp.ota.tmc.common.model.Author;
import com.djt.cvpp.ota.tmc.common.model.Tag;
import com.djt.cvpp.ota.tmc.deployment.client.TmcDeploymentClient;
import com.djt.cvpp.ota.tmc.deployment.exception.TmcDeploymentException;
import com.djt.cvpp.ota.tmc.deployment.mapper.DeploymentDtoMapper;
import com.djt.cvpp.ota.tmc.deployment.mapper.DeploymentJsonConverter;
import com.djt.cvpp.ota.tmc.deployment.model.Deployment;
import com.djt.cvpp.ota.tmc.deployment.model.Release;
import com.djt.cvpp.ota.tmc.deployment.model.ReleaseArtifact;
import com.djt.cvpp.ota.tmc.deployment.model.TargetBundle;
import com.djt.cvpp.ota.tmc.deployment.model.TargetMatcher;
import com.djt.cvpp.ota.tmc.deployment.simulator.TmcVehicleSimulator;
import com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.VehicleJsonConverter;
import com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockTmcDeploymentClientImpl extends AbstractMockRepository implements TmcDeploymentClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockTmcDeploymentClientImpl.class);

	private DeploymentJsonConverter jsonConverter = new DeploymentJsonConverter();
	private DeploymentDtoMapper dtoMapper = new DeploymentDtoMapper();
	
	private VehicleJsonConverter vehicleJsonConverter = new VehicleJsonConverter();
	
	public void reset() {
		throw new UnsupportedOperationException("Not supported");
	}
	
	public static final String TENANT_ID = "ed67d6c9-3f14-4e7d-8d08-94265a53f326"; 
	public static final UUID TENANT_UUID = UUID.fromString(TENANT_ID);

	
	private Map<String, ReleaseArtifact> tmcReleaseArtifactMap = new TreeMap<>();
	private Map<String, TargetBundle> tmcTargetBundleMap = new TreeMap<>();
	private Map<String, Release> tmcReleaseMap = new TreeMap<>();
	private Map<String, Deployment> tmcDeploymentMap = new TreeMap<>();

	public MockTmcDeploymentClientImpl() {
	}
	
	public Author createTmcAuthor() {
		
		// TODO: TDM: Is there literally only 1 tenantId for all of Digital Jukebox Technologies. LLC.? Regardless of system and/or environment?
		// TODO: TDM: I assume that an Author has to be created and then its resource id is passed in the response.
		UUID tenantUuid = TENANT_UUID;
		UUID authorId = UUID.randomUUID();
		String name = "FENIX-FLARE-TMCAdapter"; 
		String description = "microservice";
		String type = "microservice";
		
		 Author author = new Author(
			tenantUuid,
			authorId,
			name,
			description,
			type);
		 
		 return author;
	}
	
	public UUID getTenantId() {
		
		return TENANT_UUID;
	}
	
	public Deployment pushDeploymentToTmc(CampaignBatch campaignBatch) throws EntityDoesNotExistException, ValidationException, TmcDeploymentException {
		
		Deployment deployment = createTmcDeployment(campaignBatch);
		
		// Do the actual creation of the Deployment in the abstract base class.  The real implementation will interact with TMC for all the steps, 
		// but this mock implementation will use a stub for TMC (and a driver/vehicle simulator for emitting vehicle status messsage events)
		LOGGER.debug("TmcDeploymentAdapter::pushDeploymentToTmc(): campaignBatch [{}].", campaignBatch);
		
		
		// See how many successes, failures and offline vehicles we are supposed to produce.
		int numberOfVehicles = campaignBatch.getTotalNumberOfVehicles();
		int expectedNumberSuccess = TmcVehicleSimulator.getExpectedNumberSuccess(numberOfVehicles);
		int expectedNumberFailure = TmcVehicleSimulator.getExpectedNumberFailure(numberOfVehicles);
		int expectedNumberOffline = TmcVehicleSimulator.getExpectedNumberOffline(numberOfVehicles);
		
		// Make sure that the numbers all add up before going any further.
		if ((expectedNumberSuccess + expectedNumberFailure + expectedNumberOffline) != numberOfVehicles) {
			throw new RuntimeException("Number mismatch between vehicle campaign batch count: " 
				+ numberOfVehicles 
				+ " and expectedNumberSuccess: " 
				+ expectedNumberSuccess 
				+ ", expectedNumberFailure: " 
				+ expectedNumberFailure 
				+ ", expectedNumberOffline: " 
				+ expectedNumberOffline 
				+ "... COUNT=" 
				+ (expectedNumberSuccess + expectedNumberFailure + expectedNumberOffline));
			
		}
		
		// Create a vehicle simulator (that will emit status messages to the mock event publisher) for each vehicle in the batch.
		List<VehicleCampaignBatch> list = campaignBatch.getVehicleCampaignBatches();
		int listSize = list.size();
		int vehicleSuccessCount = 0;
		int vehicleFailureCount = 0;
		int vehicleOfflineCount = 0;
		for (int i=0; i < listSize; i++) {

			VehicleCampaignBatch vehicleCampaignBatch = list.get(i);
			
			if (i < expectedNumberSuccess) {
				
				TmcVehicleSimulator.setSendFailureMessages(Boolean.FALSE); 
				TmcVehicleSimulator.setIsOffline(Boolean.FALSE);
				this.sendVehicleDeploymentTriggerToTmc(vehicleCampaignBatch);
				vehicleSuccessCount = vehicleSuccessCount + 1;
				
			} else if (i < (expectedNumberFailure+expectedNumberSuccess)) {

				TmcVehicleSimulator.setSendFailureMessages(Boolean.TRUE); 
				TmcVehicleSimulator.setIsOffline(Boolean.FALSE);
				this.sendVehicleDeploymentTriggerToTmc(vehicleCampaignBatch);
				vehicleFailureCount = vehicleFailureCount + 1;
				
			} else {

				TmcVehicleSimulator.setSendFailureMessages(Boolean.FALSE); // This is ignored in the simulator code. 
				TmcVehicleSimulator.setIsOffline(Boolean.TRUE);
				this.sendVehicleDeploymentTriggerToTmc(vehicleCampaignBatch);
				vehicleOfflineCount = vehicleOfflineCount + 1;
				
			}
		}
		
		if (vehicleSuccessCount != expectedNumberSuccess) {
			throw new RuntimeException("vehicleSuccessCount: " + vehicleSuccessCount + " does not match expectedNumberSuccess: " + expectedNumberSuccess);
		}
		
		if (vehicleFailureCount != expectedNumberFailure) {
			throw new RuntimeException("vehicleFailureCount: " + vehicleFailureCount + " does not match expectedNumberFailure: " + expectedNumberFailure);
		}

		if (vehicleOfflineCount != expectedNumberOffline) {
			throw new RuntimeException("vehicleOfflineCount: " + vehicleOfflineCount + " does not match expectedNumberOffline: " + expectedNumberOffline);
		}
		
		return deployment;
	}
	
	public void sendVehicleDeploymentTriggerToTmc(VehicleCampaignBatch vehicleCampaignBatch) throws EntityDoesNotExistException, ValidationException, TmcDeploymentException {

		// In the real TMC deployment adapter, we need to send this request:
		/*
			URL: https://api.autonomic.ai/v1/deployment-group/ed67d6c9-3f14-4e7d-8d08-94265a53f326/vehicle/1FA6P8TH4J5107939/actuation
			REQUEST BODY: {"oem_correlation_id":"407d9470-3bcc-45c3-ab9f-7c016c18fc8d","actuation_type":"INVENTORY"}
			NOTE: oem_correlation_id corresponds to the TMC Deployment ID
		 */
		String tmcVehicleObjectPayload = vehicleCampaignBatch.getTmcVehicleObjectPayload();
		TmcVehicle tmcVehicle = this.vehicleJsonConverter.unmarshallFromJsonToEntity(tmcVehicleObjectPayload);

		TmcVehicleSimulator.processUpdate(
			vehicleCampaignBatch.getParentCampaignBatch(), 
			tmcVehicle, 
			TmcVehicleSimulator.getSendFailureMessages(), 
			TmcVehicleSimulator.getIsOffline());
	}
	
	public String sendCancelDeploymentRequestToTmc(Deployment deployment) throws TmcDeploymentException {
		throw new RuntimeException("Not implemented yet.");
	}
	
	private Deployment createTmcDeployment(CampaignBatch campaignBatch) {
		
		// TODO: TDM: Whatever common logic can be moved to the abstract class (so that the pure business logic can be re-used) should be moved there.  Some of this (including the state check) ought to be there.
		
		// First, make sure that the campaign batch is in a active state, which means that we are a go to push to TMC
		CampaignBatchState campaignBatchState = campaignBatch.getCampaignBatchState();
		if (campaignBatchState.equals(CampaignBatchState.ACTIVE)) {
			
			// Generic TMC entity info
			UUID tenantUuid = UUID.fromString("ed67d6c9-3f14-4e7d-8d08-94265a53f326");
			UUID uuid = UUID.randomUUID();
			String name = "deployment-" + campaignBatch.getNaturalIdentity(); 
			String description = "FENIX-FLARE-deployment";
			URI uri = null;
			Instant createdTimestamp = AbstractEntity.getTimeKeeper().getCurrentInstant();
			Author author = createTmcAuthor();
			Set<Tag> tags = new TreeSet<>();
			
			// Deployment specific info
			Release release = this.createRelease(campaignBatch);
			Deployment deployment = new Deployment(
				tenantUuid,	
				uuid,
				name,
				description,
				uri,
				createdTimestamp,
				author,
				tags,
				release);
			
			campaignBatch.setTmcDeploymentId(deployment.getUuid().toString());
			
			this.tmcDeploymentMap.put(deployment.getUuid().toString(), deployment);
			
			return deployment;
		}
		throw new FenixRuntimeException("Unable to create TMC Deployment for campaignBatch: [" + campaignBatch + "] because it is not in a ACTIVE state.  Instead, it was: [" + campaignBatchState + "].");
	}

	private Release createRelease(CampaignBatch campaignBatch) {
	
		// Generic TMC entity info
		UUID tenantUuid = this.getTenantId();	
		UUID uuid = UUID.randomUUID();
		String name = "release-" + campaignBatch.getNaturalIdentity();
		String description = "FENIX-FLARE-release";
		URI uri = null;
		Instant createdTimestamp = AbstractEntity.getTimeKeeper().getCurrentInstant();
		Author author = this.createTmcAuthor();
		Set<Tag> tags = new TreeSet<>();
		
		// Release specific info
		Set<TargetBundle> targetBundles = new TreeSet<>();
		
		Iterator<VehicleCampaignBatch> iterator = campaignBatch.getVehicleCampaignBatches().iterator();
		while (iterator.hasNext()) {
			
			VehicleCampaignBatch vehicleCampaignBatch = iterator.next();
			
			TargetBundle targetBundle = this.createTargetBundle(vehicleCampaignBatch);
			targetBundles.add(targetBundle);			
		}
		
		Release release = new Release(
			tenantUuid,
			uuid,
			name,
			description,
			uri,
			createdTimestamp,
			author,
			tags,
			targetBundles);
		
		this.tmcReleaseMap.put(release.getUuid().toString(), release);
		
		return release;
	}

	private TargetBundle createTargetBundle(VehicleCampaignBatch vehicleCampaignBatch) {

		// Generic TMC entity info
		UUID tenantUuid = this.getTenantId();	
		UUID uuid = UUID.randomUUID();
		String name = "target-bundle-" + vehicleCampaignBatch.getVin();
		String description = "FENIX-FLARE-target-bundle";
		URI uri = null;
		Instant createdTimestamp = AbstractEntity.getTimeKeeper().getCurrentInstant();
		Author author = this.createTmcAuthor();
		Set<Tag> tags = new TreeSet<>();
		
		// Target Bundle specific info
		Set<ReleaseArtifact> releaseArtifacts = new TreeSet<>();
		releaseArtifacts.add(createSignedCommandsArtifact(vehicleCampaignBatch));
		releaseArtifacts.add(createGenericManifestArtifact(vehicleCampaignBatch.getParentCampaignBatch().getParentCampaign()));
		
		Set<TargetMatcher> targetMatchers = new TreeSet<>();
		String targetMatcherName = "vin";
		String targetMatcherValue = vehicleCampaignBatch.getVin();
		String targetMatcherType = "Field";
		TargetMatcher targetMatcher = new TargetMatcher(targetMatcherName, targetMatcherValue, targetMatcherType);
		targetMatchers.add(targetMatcher);
		
		TargetBundle targetBundle = new TargetBundle(
			tenantUuid,	
			uuid,
			name,
			description,
			uri,
			createdTimestamp,
			author,
			tags,
			releaseArtifacts,
			targetMatchers);
		
		this.tmcTargetBundleMap.put(targetBundle.getUuid().toString(), targetBundle);
		
		return targetBundle;
	}
	
	private ReleaseArtifact createGenericManifestArtifact(Campaign campaign) {

		GenericManifest genericManifest = campaign.getGenericManifest();
		
		String releaseArtifactName = "manifest-" + campaign.getNaturalIdentity();
		String releaseArtifactDescription = "FENIX-FLARE-manifest";
		String type = "manifest";
		
		List<byte[]> byteArrayList = new ArrayList<>();
		byte[] byteArray = genericManifest.getManifestPayload().getBytes(Charset.forName("UTF-8"));
		byteArrayList.add(byteArray);
				
		ReleaseArtifact releaseArtifact = this.createArtifact(
			campaign.getTmcGenericManifestBinaryId().toString(),
			releaseArtifactName,
			releaseArtifactDescription,
			type,
			byteArrayList);
		
		campaign.setTmcGenericManifestBinaryId(releaseArtifact.getUuid());
		
		this.tmcReleaseArtifactMap.put(releaseArtifact.getUuid().toString(), releaseArtifact);
		
		return releaseArtifact;
	}
	
	private ReleaseArtifact createSignedCommandsArtifact(VehicleCampaignBatch vehicleCampaignBatch) {

		String releaseArtifactName = "signed-commands-" + vehicleCampaignBatch.getNaturalIdentity();
		String releaseArtifactDescription = "FENIX-FLARE-signed-commands";
		String type = "signed_command";
		
		List<byte[]> byteArrayList = new ArrayList<>();
		Iterator<VehicleCampaignBatchSignedCommand> iterator = vehicleCampaignBatch.getVehicleCampaignBatchSignedCommands().iterator();
		while (iterator.hasNext()) {
			
			VehicleCampaignBatchSignedCommand vehicleCampaignBatchSignedCommand = iterator.next();
			
			byte[] byteArray = vehicleCampaignBatchSignedCommand.getValue();
			byteArrayList.add(byteArray);
		}

		ReleaseArtifact releaseArtifact = this.createArtifact(
			vehicleCampaignBatch.getTmcSignedCommandsBinaryId().toString(),	
			releaseArtifactName,
			releaseArtifactDescription,
			type,
			byteArrayList);
		
		vehicleCampaignBatch.setTmcSignedCommandsBinaryId(releaseArtifact.getUuid());
		
		this.tmcReleaseArtifactMap.put(releaseArtifact.getUuid().toString(), releaseArtifact);
		
		return releaseArtifact;
	}
	
	private ReleaseArtifact createArtifact(
		String binaryId, 	
		String releaseArtifactName,
		String releaseArtifactDescription,
		String type,
		List<byte[]> byteArrayList) {
				
		// Generic TMC entity info
		UUID tenantUuid = UUID.fromString("ed67d6c9-3f14-4e7d-8d08-94265a53f326");
		UUID uuid = UUID.randomUUID();
		URI uri = null;
		Instant createdTimestamp = AbstractEntity.getTimeKeeper().getCurrentInstant();
		Author author = createTmcAuthor();
		Set<Tag> tags = new TreeSet<>();
		
		// Release Artifact specific info
		String bytestreamUrl = "aui:bytestream::/" + binaryId;
		Integer byteLength = Integer.valueOf(1);
		String checksum = "";

		ReleaseArtifact releaseArtifact = new ReleaseArtifact(
			tenantUuid,	
			uuid,
			releaseArtifactName,
			releaseArtifactDescription,
			uri,
			createdTimestamp,
			author,
			tags,
			bytestreamUrl,
			byteLength,
			checksum,
			type);
	
		return releaseArtifact;
	}
	
	public ReleaseArtifact createTmcReleaseArtifact(String requestJson) {

		// TODO: TDM: Implement
		throw new RuntimeException("Not implemented yet.");
	}
	
	public TargetBundle createTmcTargetBundle(String requestJson) {

		// TODO: TDM: Implement
		throw new RuntimeException("Not implemented yet.");
	}

	public Release createTmcRelease(String requestJson) {

		// TODO: TDM: Implement
		throw new RuntimeException("Not implemented yet.");
	}
	
	public Deployment createTmcDeployment(String requestJson) {
		
		// TODO: TDM: Implement
		throw new RuntimeException("Not implemented yet.");
	}
	
	public AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity) {
		throw new UnsupportedOperationException("This method is not supported by TmcDeploymentClient");
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		throw new UnsupportedOperationException("This method is not supported by TmcDeploymentClient");
	}
	
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		throw new UnsupportedOperationException("This method is not supported by TmcDeploymentClient");
	}
	
	public DeploymentJsonConverter getDeploymentJsonConverter() {
		return this.jsonConverter;
	}

	public DeploymentDtoMapper getDeploymentDtoMapper() {
		return this.dtoMapper;
	}
}
