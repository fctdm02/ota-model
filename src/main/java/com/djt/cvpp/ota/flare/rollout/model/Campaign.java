/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.flare.manifest.model.DomainManifest;
import com.djt.cvpp.ota.flare.manifest.model.GenericManifest;
import com.djt.cvpp.ota.flare.rollout.model.enums.CampaignBatchState;
import com.djt.cvpp.ota.flare.rollout.model.enums.CampaignState;
import com.djt.cvpp.ota.flare.signedcommands.model.SignedCommandSoftware;
import com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryAudience;
import com.djt.cvpp.ota.vadr.model.DomainInstance;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Campaign extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Campaign.class);


	private AbstractRollout parentRollout;
	private Timestamp beginDate;
	private Timestamp endDate;
	
	// The domain name, domain instance name and domain instance version came from the rollout event.
	// The domain instance itself will come from VADR, by virtue of extraction out of the vadrPayload of the rollout event.
	private String domainName;
	private String domainInstanceName;
	private String domainInstanceVersion;
	private String applicationName;
	private String applicationVersion;
	
	// The program code and model year came from the vehicle discovery process.  
	// The corresponding ODL came from ORFIN.
	private String programCode;
	private Integer modelYear;
	private String odlPayload;
	
	private CampaignState campaignState = CampaignState.PRE_ROLLOUT_INVALID;

    private Set<CampaignBatch> campaignBatches = new TreeSet<>();

    // The following are generated on the fly based on upstream data (from VADR and ORFIN)
    // NOTE: That the set of signed command softwares is the *same* for all vehicles, yet
    // each vehicle's set of signed commands byte arrays is unique and is kept at the
    // campaign batch vehicle level (the signed command softwares here can be thought of
    // as the form letter and the vehicle's ECG FESN being the data)
	private GenericManifest genericManifest;
	private UUID tmcGenericManifestBinaryId;
    private Set<SignedCommandSoftware> signedCommandSoftwares = new TreeSet<>();

	@SuppressWarnings("unused")
	private Campaign() {
	}

	public Campaign(
        AbstractRollout parentRollout,
    	String domainName,
    	String domainInstanceName,
    	String domainInstanceVersion,
        String programCode,
        Integer modelYear,
        String odlPayload) {
		this(
			parentRollout,
			domainName,
			domainInstanceName,
			domainInstanceVersion,
			null,
			null,
			programCode,
			modelYear,
			odlPayload);
    }
	
	public Campaign(
        AbstractRollout parentRollout,
    	String domainName,
    	String domainInstanceName,
    	String domainInstanceVersion,
    	String applicationName,
    	String applicationVersion,
        String programCode,
        Integer modelYear,
        String odlPayload) {
	    this.parentRollout = parentRollout;
		this.domainName = domainName;
		this.domainInstanceName = domainInstanceName;
		this.domainInstanceVersion = domainInstanceVersion;
		this.applicationName = applicationName;
		this.applicationVersion = applicationVersion;
	    this.programCode = programCode;
	    this.modelYear = modelYear;
	    this.odlPayload = odlPayload;
    }

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentRollout,
			domainName,
	    	domainInstanceName,
	    	domainInstanceVersion,
	    	applicationName,
	    	applicationVersion,
	        programCode,
	        modelYear
		);
	}

	public void validate(List<String> validationMessages) {

		// The following validations are done regardless of state.
		validateNotNull(validationMessages, "parentRollout", parentRollout);
		validateNotNull(validationMessages, "domainName", domainName);
		validateNotNull(validationMessages, "domainInstanceName", domainInstanceName);
		validateNotNull(validationMessages, "domainInstanceVersion", domainInstanceVersion);

		// Used to build the manifest for this campaign.  Since the top level rollout may be associated with *many*
		// rollouts, we specify each combination of program code/model year here (as the manifest will be different)
		validateNotNull(validationMessages, "programCode", programCode);
		validateNotNull(validationMessages, "modelYear", modelYear);
		
		
		// TODO: TDM: Since FENIX-FIRE will be generating the manifest, there is no need to store it in the campaign anymore.
		// validateNotNull(validationMessages, "odlPayload", odlPayload);
		
		if (this.genericManifest != null) {
			
			this.genericManifest.validate(validationMessages);
		}
		
		if (campaignBatches == null || campaignBatches.isEmpty()) {
			validationMessages.add("At least one CampaignBatch must be specified for Campaign: [" + this + "].");
		} else {
			Iterator<CampaignBatch> campaignBatchesIterator = campaignBatches.iterator();
			while (campaignBatchesIterator.hasNext()) {

				CampaignBatch campaignBatch = campaignBatchesIterator.next();
				campaignBatch.validate(validationMessages);
			}
		}

		boolean allReleaseArtifactsUploadedToTmcByteStream = this.getAllReleaseArtifactsUploadedToTmcByteStream();
		if (validationMessages.isEmpty()) {
			
			if (this.campaignState.equals(CampaignState.PRE_ROLLOUT_INVALID)) {
				this.campaignState = CampaignState.PRE_ROLLOUT_VALID;	
			} else if (this.campaignState.equals(CampaignState.RELEASE_ARTIFACT_UPLOAD_PENDING) && allReleaseArtifactsUploadedToTmcByteStream) {
				this.setStateToPending();
			}			
		}
	}

	public AbstractRollout getParentRollout() {
		return parentRollout;
	}

	public String getDomainName() {
		return domainName;
	}

	public String getDomainInstanceName() {
		return domainInstanceName;
	}

	public String getDomainInstanceVersion() {
		return domainInstanceVersion;
	}
	
	public String getApplicationName() {
		return applicationName;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public Timestamp getBeginDate() {
		return new Timestamp(beginDate.getTime());
	}

	public Timestamp getEndDate() {
		if (endDate != null) {
			return new Timestamp(endDate.getTime());	
		}
		return null;
	}
	
	public long getEndDateAsMillis() {
		if (endDate != null) {
			return endDate.getTime();	
		}
		return -1L;
	}
	
	public String getProgramCode() {
		return programCode;
	}

	public Integer getModelYear() {
		return modelYear;
	}
	
	public String getOdlPayload() {
		return odlPayload;
	}

	public CampaignState getCampaignState() {
		return campaignState;
	}
	
	public List<CampaignBatch> getCampaignBatches() {
		List<CampaignBatch> list = new ArrayList<>();
		list.addAll(this.campaignBatches);
		return list;
	}
	
	public void addCampaignBatch(CampaignBatch campaignBatch) throws ValidationException {
		
		this.campaignBatches.add(campaignBatch);
	}

	public GenericManifest getGenericManifest() {
		return genericManifest;
	}

	public UUID getTmcGenericManifestBinaryId() {
		return tmcGenericManifestBinaryId;
	}

	public void setTmcGenericManifestBinaryId(UUID tmcGenericManifestBinaryId) {
		this.tmcGenericManifestBinaryId = tmcGenericManifestBinaryId;
	}
	
	public Set<SignedCommandSoftware> getSignedCommandSoftwares() {
		return signedCommandSoftwares;
	}

	
	// BUSINESS BEHAVIORS
	protected boolean getAllReleaseArtifactsUploadedToTmcByteStream() {
		
		boolean allReleaseArtifactsUploadedToTmcByteStream = true;
		
		if (this.tmcGenericManifestBinaryId == null) {
			allReleaseArtifactsUploadedToTmcByteStream = false;
		} else {
			Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
			while (iterator.hasNext()) {
				
				CampaignBatch campaignBatch = iterator.next();
				if (!campaignBatch.getAllReleaseArtifactsUploadedToTmcByteStream()) {
					
					allReleaseArtifactsUploadedToTmcByteStream = false;
					break;
				}
			}
		}
	
		return allReleaseArtifactsUploadedToTmcByteStream;
	}
	
	public void setStateToReleaseArtifactUploadPending() {
		
		if (this.campaignState == CampaignState.RELEASE_ARTIFACT_UPLOAD_PENDING) {
			return;
		}
		
		if (!this.campaignState.equals(CampaignState.PRE_ROLLOUT_VALID)) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to RELEASE_ARTIFACT_UPLOAD_PENDING state from PRE_ROLLOUT_VALID state, yet was in state: " + this.campaignState + ", validationMessages: [" + this.validate() + "].");
		}
		this.campaignState = CampaignState.RELEASE_ARTIFACT_UPLOAD_PENDING;
		LOGGER.debug("Setting state to RELEASE_ARTIFACT_UPLOAD_PENDING for: [{}].", this);
	}
	
	protected void setStateToPending() {

		if (this.campaignState == CampaignState.PENDING) {
			return;
		}

		if (!this.campaignState.equals(CampaignState.RELEASE_ARTIFACT_UPLOAD_PENDING)) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to PENDING state from RELEASE_ARTIFACT_UPLOAD_PENDING state, yet was in state: " + this.campaignState);
		}

		Iterator<CampaignBatch> iterator = campaignBatches.iterator();
		while (iterator.hasNext()) {

			CampaignBatch campaignBatch = iterator.next();
			campaignBatch.setStateToPending();
		}
		
		this.campaignState = CampaignState.PENDING;
		LOGGER.debug("Setting state to PENDING for: [{}].", this);
	}

	public void setStateToDeploying() {

		if (this.campaignState.equals(CampaignState.DEPLOYING)) {
			return;
		}
		
		if (!this.campaignState.equals(CampaignState.PENDING)) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to DEPLOYING state from PENDING state, yet was in state: " + this.campaignState);
		}
		
		this.beginDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		this.campaignState = CampaignState.DEPLOYING;
		LOGGER.debug("Setting state to DEPLOYING for: [{}].", this);
	}
		
	public void pause() {
		
		if (this.campaignState == CampaignState.FINISHED 
			|| this.campaignState == CampaignState.PAUSED) {
			return;
		}
		
		if (this.campaignState != CampaignState.DEPLOYING) {
			
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved back to DEPLOYING state from PAUSED state, yet was in state: " + this.campaignState);
		}
		
		Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
	    while (iterator.hasNext()) {
	    	
	    	CampaignBatch campaignBatch = iterator.next();
	    	campaignBatch.pause();
	    }
		
		
		// TODO: Is there a "pause campaign" trigger that can be sent to the vehicle?
	    	    
	    this.campaignState = CampaignState.PAUSED;
	    LOGGER.debug("Setting state to PAUSED for : [{}].", this);
	}

	public void resume() {
		
		if (this.campaignState == CampaignState.FINISHED 
			|| this.campaignState == CampaignState.DEPLOYING) {
			return;
		}
		
		if (this.campaignState != CampaignState.PAUSED) {
			
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved back to DEPLOYING state from PAUSED state, yet was in state: " + this.campaignState);
		}
		
		Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
	    while (iterator.hasNext()) {
	    	
	    	CampaignBatch campaignBatch = iterator.next();
	    	campaignBatch.resume();
	    }
		
		// TODO: Is there a "resume campaign" trigger that can be sent to the vehicle?
	    	    
	    this.campaignState = CampaignState.DEPLOYING;
	    LOGGER.debug("Setting state back to DEPLOYING for: [{}].", this);
	}
	
	public void cancel() {
		
		if (this.campaignState == CampaignState.FINISHED 
			|| this.campaignState == CampaignState.CANCELLED) {
			return;
		}
		
		Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
	    while (iterator.hasNext()) {
	    	
	    	CampaignBatch campaignBatch = iterator.next();
	    	campaignBatch.cancel();
	    	
	    	// TODO: We need to move this method to the service so that we can instruct the TMC deployment adapter to send cancellation request for this batch/deployment.
	    }
		
		this.endDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		this.campaignState = CampaignState.CANCELLED;
		LOGGER.debug("Cancelling: [{}].", this);
	}
	
	public void setStateToFinished() {

		if (this.campaignState == CampaignState.FINISHED) {
			return;
		}
		
		if (this.campaignState != CampaignState.DEPLOYING) {
			
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved back to FINISHED state from DEPLOYING state, yet was in state: " + this.campaignState);
		}
		
		this.endDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		this.campaignState = CampaignState.FINISHED;
		LOGGER.debug("Finishing: [{}].", this);
	}
	
	public List<VehicleCampaignBatch> getVehicleCampaignBatches() {
		
		List<VehicleCampaignBatch> list = new ArrayList<>();
 		Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
 		while (iterator.hasNext()) {
 			list.addAll(iterator.next().getVehicleCampaignBatches());
 		}
 		return list;
	}
		
	public int getNumberActiveCampaignBatches() {
		
		int activeCampaignBatches = 0;
		Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
		while (iterator.hasNext()) {
			
			CampaignBatch campaignBatch = iterator.next();
			if (campaignBatch.getCampaignBatchState() == CampaignBatchState.ACTIVE) {
				activeCampaignBatches++;
			}
		}
		return activeCampaignBatches;
	}

	public int getTotalNumberOfBatches() {
		
		return this.campaignBatches.size();
	}
	
	public int getTotalNumberOfVehicles() {
		
		int totalNumberOfVehicles = 0;
		Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
		while (iterator.hasNext()) {
			
			totalNumberOfVehicles = totalNumberOfVehicles + iterator.next().getTotalNumberOfVehicles();
		}
		return totalNumberOfVehicles;
	}
	
	public int getTotalNumberOfVehicleMessages() {
		
		int totalNumberOfMessages = 0;
		Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
		while (iterator.hasNext()) {
			
			totalNumberOfMessages = totalNumberOfMessages + iterator.next().getTotalNumberOfVehicleMessages();
		}
		return totalNumberOfMessages;
	}
	
	public int getNumberOfCompletions() {
		
		int numberOfCompletions = 0;
		Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
		while (iterator.hasNext()) {
			
			numberOfCompletions = numberOfCompletions + iterator.next().getNumberOfCompletions();
		}
		return numberOfCompletions;
	}

	public int getNumberSuccessful() {
		
		int numberSuccessful = 0;
		Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
		while (iterator.hasNext()) {
			
			numberSuccessful = numberSuccessful + iterator.next().getNumberSuccessful();
		}
		return numberSuccessful;
	}

	public int getNumberFailure() {
		
		int numberFailure = 0;
		Iterator<CampaignBatch> iterator = this.campaignBatches.iterator();
		while (iterator.hasNext()) {
			
			numberFailure = numberFailure + iterator.next().getNumberFailure();
		}
		return numberFailure;
	}

	public CampaignBatch getFirstNonActiveCampaignBatch() {
		
		Iterator<CampaignBatch> campaignBatchIterator = this.campaignBatches.iterator();
		CampaignBatch campaignBatch = null;
	    while (campaignBatchIterator.hasNext()) {
	    	
	    	campaignBatch = campaignBatchIterator.next();
	    	if (campaignBatch.getCampaignBatchState() == CampaignBatchState.PENDING) {
	    		return campaignBatch;
	    	}
	    }
	    return campaignBatch;
	}
	
	public CampaignBatch getFirstNonFullCampaignBatch() {
		
		int maxNumberOfVehiclesPerCampaignBatch = this.getParentRollout().getMaxNumberVehiclesPerCampaignBatch();
		Iterator<CampaignBatch> campaignBatchIterator = this.campaignBatches.iterator();
		CampaignBatch campaignBatch = null;
	    while (campaignBatchIterator.hasNext()) {
	    	
	    	campaignBatch = campaignBatchIterator.next();
	    	if (campaignBatch.getVehicleCampaignBatches().size() < maxNumberOfVehiclesPerCampaignBatch) {
	    		break;
	    	}
	    }
	    return campaignBatch;
	}
		
	public void retryCampaign() {
		throw new RuntimeException("Not implemented yet.");
	}

	public void deleteVehicle() {
		throw new RuntimeException("Not implemented yet.");
	}

	public void generateManifest(DomainInstance domainInstance, String odlPayload) {

		// TODO: TDM: All this needs to be pulled over from the MVP code and reworked
		LOGGER.debug("Generating manifest for campaign: [{}].", this);
		Campaign parentCampaign = this;
		String manifestName = this.getNaturalIdentity() + "_MANIFEST";
		String triggerType = "VEHICLE_UPDATE";
		String audience = DeliveryAudience.ENGINEERING.toString();
		String releaseNotes = "release_notes_go_here";
		Timestamp timestamp = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		String manifestPayload = "manifest_payload_goes_here";
		String ovtpUrl = "ovtp_url_goes_here";
		
		this.genericManifest = new DomainManifest(
			parentCampaign,
			manifestName,
			triggerType,
			audience,
			releaseNotes,
			timestamp,
			manifestPayload,
			ovtpUrl);
	}
}
