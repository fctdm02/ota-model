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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.flare.rollout.model.enums.RolloutPhase;
import com.djt.cvpp.ota.flare.rollout.model.enums.RolloutState;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractRollout extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRollout.class);


	public static final Integer DEFAULT_MAX_NUMBER_OF_ACTIVE_CAMPAIGN_BATCHES = Integer.valueOf(100);
	public static final Integer DEFAULT_MAX_NUMBER_OF_VEHICLES_PER_CAMPAIGN_BATCH = Integer.valueOf(1000);
	public static final Integer DEFAULT_EXPIRATION_DAYS = Integer.valueOf(120);
	public static final Integer DEFAULT_PRIORITY_LEVEL = Integer.valueOf(1); // Higher values are more important
	public static final Integer DEFAULT_NUMBER_OF_PRIOR_DOMAIN_INSTANCES_TO_UPDATE = Integer.valueOf(1000);
	

	private String rolloutName;
	private String initiatedBy;
	private Timestamp creationDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
	private Timestamp startDate;
	private Integer expirationDays = DEFAULT_EXPIRATION_DAYS; // This is the number of days once the rollout starts (i.e. effectiveDate)
	private Timestamp endDate;

	private RolloutState rolloutState = RolloutState.PRE_ROLLOUT_INVALID;
	private RolloutPhase rolloutPhase = RolloutPhase.VALIDATION;
	
	private Integer maxNumberActiveCampaignBatches = DEFAULT_MAX_NUMBER_OF_ACTIVE_CAMPAIGN_BATCHES;
	private Integer maxNumberVehiclesPerCampaignBatch = DEFAULT_MAX_NUMBER_OF_VEHICLES_PER_CAMPAIGN_BATCH;
	private Integer priorityLevel = DEFAULT_PRIORITY_LEVEL;
	private Integer numberOfPriorDomainInstancesToUpdate = DEFAULT_NUMBER_OF_PRIOR_DOMAIN_INSTANCES_TO_UPDATE;
	
	private String tmcVehicleDiscoveryQuery;
		
	private Set<Campaign> campaigns = new TreeSet<>();
	
	@SuppressWarnings("unused")
	private AbstractRollout() {
	}
	
	public AbstractRollout(String rolloutName) {
		
		if (rolloutName == null || rolloutName.trim().isEmpty()) {
			throw new FenixRuntimeException("rolloutName must be specified.");
		}
		this.rolloutName = rolloutName;
		
		this.creationDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
	}

	public String getNaturalIdentity() {
		return rolloutName;
	}
	
	public void validate(List<String> validationMessages) {

		// The following validations are done regardless of state.
		validateNotNull(validationMessages, "rolloutName", rolloutName);
		validateDate(validationMessages, "creationDate", creationDate);
		validateNotNull(validationMessages, "expirationDays", expirationDays);
		validateNumericRange(validationMessages, "expirationDays", expirationDays, ONE, ONE_HUNDRED_EIGHTY);
		validateNotNull(validationMessages, "tmcVehicleDiscoveryQuery", tmcVehicleDiscoveryQuery);

		// TODO: Make the lower and upper bounds for each property to be configurable
		validateNumericRange(validationMessages, "maxNumberActiveCampaignBatches", maxNumberActiveCampaignBatches, ONE, ONE_THOUSAND);
		validateNumericRange(validationMessages, "maxNumberVehiclesPerCampaignBatch", maxNumberVehiclesPerCampaignBatch, ONE, TEN_THOUSAND);


		if (campaigns == null || campaigns.isEmpty()) {
			validationMessages.add("At least one Campaign must be specified.");
		} else {
			Iterator<Campaign> iterator = campaigns.iterator();
			while (iterator.hasNext()) {

				Campaign campaign = iterator.next();
				campaign.validate(validationMessages);
			}
		}
		
		boolean allReleaseArtifactsUploadedToTmcByteStream = this.getAllReleaseArtifactsUploadedToTmcByteStream();
		if (validationMessages.isEmpty()) {
			
			if (this.rolloutState.equals(RolloutState.PRE_ROLLOUT_INVALID)) {
				this.rolloutState = RolloutState.PRE_ROLLOUT_VALID;	
			} else if (this.rolloutState.equals(RolloutState.RELEASE_ARTIFACT_UPLOAD_PENDING) && allReleaseArtifactsUploadedToTmcByteStream) {
				this.setStateToPending();
			}			
		}
	}
	
	public String getRolloutName() {
		return rolloutName;
	}

	public String getInitiatedBy() {
		return initiatedBy;
	}

	public Timestamp getCreationDate() {
		return new Timestamp(creationDate.getTime());
	}

	public Timestamp getStartDate() {
		return new Timestamp(startDate.getTime());
	}

	public Integer getExpirationDays() {
		return expirationDays;
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

	public Integer getMaxNumberActiveCampaignBatches() {
		return maxNumberActiveCampaignBatches;
	}

	public Integer getMaxNumberVehiclesPerCampaignBatch() {
		return maxNumberVehiclesPerCampaignBatch;
	}

	public Integer getPriorityLevel() {
		return priorityLevel;
	}
	
	public Integer getNumberOfPriorDomainInstancesToUpdate() {
		return numberOfPriorDomainInstancesToUpdate;
	}

	public void setRolloutName(String rolloutName) {
		this.rolloutName = rolloutName;
	}

	public void setMaxNumberActiveCampaignBatches(Integer maxNumberActiveCampaignBatches) {
		this.maxNumberActiveCampaignBatches = maxNumberActiveCampaignBatches;
	}

	public void setMaxNumberVehiclesPerCampaignBatch(Integer maxNumberVehiclesPerCampaignBatch) {
		this.maxNumberVehiclesPerCampaignBatch = maxNumberVehiclesPerCampaignBatch;
	}

	public void setPriorityLevel(Integer priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	public void setNumberOfPriorDomainInstancesToUpdate(Integer numberOfPriorDomainInstancesToUpdate) {
		this.numberOfPriorDomainInstancesToUpdate = numberOfPriorDomainInstancesToUpdate;
	}

	public String getTmcVehicleDiscoveryQuery() {
		return tmcVehicleDiscoveryQuery;
	}

	public void setTmcVehicleDiscoveryQuery(String tmcVehicleDiscoveryQuery) {
		this.tmcVehicleDiscoveryQuery = tmcVehicleDiscoveryQuery;
	}

	public RolloutState getRolloutState() {
		return rolloutState;
	}

	public RolloutPhase getRolloutPhase() {
		return rolloutPhase;
	}

	public List<Campaign> getCampaigns() {
		List<Campaign> list = new ArrayList<>();
 		list.addAll(campaigns);
 		return list;
	}
	
	public void addCampaign(Campaign campaign) throws ValidationException {
		
		this.campaigns.add(campaign);
	}


	// BUSINESS BEHAVIORS
	private boolean getAllReleaseArtifactsUploadedToTmcByteStream() {
		
		boolean allReleaseArtifactsUploadedToTmcByteStream = true;
		
		Iterator<Campaign> iterator = this.campaigns.iterator();
		while (iterator.hasNext()) {
			
			Campaign campaign = iterator.next();
			if (!campaign.getAllReleaseArtifactsUploadedToTmcByteStream()) {
				
				allReleaseArtifactsUploadedToTmcByteStream = false;
				break;
			}
		}
	
		return allReleaseArtifactsUploadedToTmcByteStream;
	}
	
	public void setStateToReleaseArtifactUploadPending() {
		
		if (this.rolloutState == RolloutState.RELEASE_ARTIFACT_UPLOAD_PENDING) {
			return;
		}
		
		if (!this.rolloutState.equals(RolloutState.PRE_ROLLOUT_VALID)) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to RELEASE_ARTIFACT_UPLOAD_PENDING state from PRE_ROLLOUT_VALID state, yet was in state: " + this.rolloutState);
		}
		this.rolloutState = RolloutState.RELEASE_ARTIFACT_UPLOAD_PENDING;
		LOGGER.debug("Setting state to RELEASE_ARTIFACT_UPLOAD_PENDING for: [{}].", this);
	}
	
	protected void setStateToPending() {

		if (this.rolloutState == RolloutState.PENDING) {
			return;
		}

		if (!this.rolloutState.equals(RolloutState.RELEASE_ARTIFACT_UPLOAD_PENDING)) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to PENDING state from RELEASE_ARTIFACT_UPLOAD_PENDING state, yet was in state: " + this.rolloutState);
		}

		Iterator<Campaign> iterator = this.campaigns.iterator();
		while (iterator.hasNext()) {

			Campaign campaign = iterator.next();
			campaign.setStateToPending();
		}
		
		this.rolloutState = RolloutState.PENDING;
		LOGGER.debug("Setting state to PENDING for: [{}].", this);
	}

	public void setStateToDeploying() {
		
		if (this.rolloutState != RolloutState.PENDING) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to DEPLOYING state from PENDING state, yet was in state: " + this.rolloutState);
		}
		
		// TODO: This needs to be associated at the service layer, so that we can have the TMC Deployment Adapter send the actual update trigger to the vehicles in the batch.
		Iterator<Campaign> iterator = this.campaigns.iterator();
		while (iterator.hasNext()) {

			Campaign campaign = iterator.next();
			campaign.setStateToDeploying();
		}
		
		this.startDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		this.rolloutState = RolloutState.DEPLOYING;
		LOGGER.debug("Setting state to DEPLOYING for: [{}].", this);
	}
		
	public void pause() throws ValidationException {
		
		if (this.rolloutState == RolloutState.FINISHED 
			|| this.rolloutState == RolloutState.PAUSED) {
			return;
		}
		
		if (this.rolloutState != RolloutState.DEPLOYING) {
			
			throw new ValidationException("rolloutState", this.getClassAndIdentity() + " can only be moved back to DEPLOYING state from PAUSED state, yet was in state: " + this.rolloutState);
		}
		
		Iterator<Campaign> iterator = this.campaigns.iterator();
	    while (iterator.hasNext()) {
	    	
	    	Campaign campaign = iterator.next();
	    	campaign.pause();
	    }
		
		
		// TODO: Is there a "pause campaign" trigger that can be sent to the vehicle?
	    	    
	    this.rolloutState = RolloutState.PAUSED;
	    LOGGER.debug("Setting state to PAUSED for : [{}].", this);
	}

	public void resume() throws ValidationException {
		
		if (this.rolloutState == RolloutState.FINISHED 
			|| this.rolloutState == RolloutState.DEPLOYING) {
			return;
		}
		
		if (this.rolloutState != RolloutState.PAUSED) {
			
			throw new ValidationException("rolloutState", this.getClassAndIdentity() + " Only a vehicle campaign batch that is in a PAUSED state can be resumed, current state: " + this.rolloutState);
		}
		
		Iterator<Campaign> iterator = this.campaigns.iterator();
	    while (iterator.hasNext()) {
	    	
	    	Campaign campaign = iterator.next();
	    	campaign.resume();
	    }
		
		// TODO: Is there a "resume campaign" trigger that can be sent to the vehicle?
	    	    
	    this.rolloutState = RolloutState.DEPLOYING;
	    LOGGER.debug("Setting state back to DEPLOYING for: [{}].", this);
	}
	
	public void cancel() {
		
		if (this.rolloutState == RolloutState.FINISHED 
			|| this.rolloutState == RolloutState.CANCELLED) {
			return;
		}
		
		Iterator<Campaign> iterator = this.campaigns.iterator();
	    while (iterator.hasNext()) {
	    	
	    	Campaign campaign = iterator.next();
	    	campaign.cancel();
	    	
	    	// TODO: We need to move this method to the service so that we can instruct the TMC deployment adapter to send cancellation request for this batch/deployment.
	    }
		
		this.endDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		this.rolloutState = RolloutState.CANCELLED;
		LOGGER.debug("Cancelling: [{}].", this);
	}
	
	public List<CampaignBatch> getCampaignBatches() {
		
		List<CampaignBatch> list = new ArrayList<>();
 		Iterator<Campaign> iterator = this.campaigns.iterator();
 		while (iterator.hasNext()) {
 			list.addAll(iterator.next().getCampaignBatches());
 		}
 		return list;
	}

	public List<VehicleCampaignBatch> getVehicleCampaignBatches() {
		
		List<VehicleCampaignBatch> list = new ArrayList<>();
 		Iterator<Campaign> iterator = this.campaigns.iterator();
 		while (iterator.hasNext()) {
 			list.addAll(iterator.next().getVehicleCampaignBatches());
 		}
 		return list;
	}
	
	public int getNumberActiveCampaignBatches() {
		
		int activeCampaignBatches = 0;
		Iterator<Campaign> iterator = this.campaigns.iterator();
		while (iterator.hasNext()) {
	
			Campaign campaign = iterator.next();
			activeCampaignBatches = activeCampaignBatches + campaign.getNumberActiveCampaignBatches();
		}		
		return activeCampaignBatches;
	}
		
	public void setRolloutStateToFinished() {
		
		if (this.rolloutState == RolloutState.FINISHED 
			|| this.rolloutState == RolloutState.CANCELLED) {
			return;
		}
		
		if (this.rolloutState != RolloutState.DEPLOYING) {
			
			throw new FenixRuntimeException(this.getClassAndIdentity() + " Only a rollout that is in a DEPLOYING state can be marked as finished, current state: " + this.rolloutState);
		}
		
		this.endDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
	    this.rolloutState = RolloutState.FINISHED;
	    LOGGER.debug("Setting state to FINISHED for rollout: [{}].", this);
	}

	public int getTotalNumberOfCampaigns() {

		return this.campaigns.size();
	}
	
	public int getTotalNumberOfBatches() {
		
		int totalNumberOfBatches = 0;
		Iterator<Campaign> iterator = this.campaigns.iterator();
		while (iterator.hasNext()) {
			
			totalNumberOfBatches = totalNumberOfBatches + iterator.next().getTotalNumberOfBatches();
		}
		return totalNumberOfBatches;
	}
	
	public int getTotalNumberOfVehicles() {
		
		int totalNumberOfVehicles = 0;
		Iterator<Campaign> iterator = this.campaigns.iterator();
		while (iterator.hasNext()) {
			
			totalNumberOfVehicles = totalNumberOfVehicles + iterator.next().getTotalNumberOfVehicles();
		}
		return totalNumberOfVehicles;
	}

	public int getTotalNumberOfVehicleMessages() {
		
		int totalNumberOfMessages = 0;
		Iterator<Campaign> iterator = this.campaigns.iterator();
		while (iterator.hasNext()) {
			
			totalNumberOfMessages = totalNumberOfMessages + iterator.next().getTotalNumberOfVehicleMessages();
		}
		return totalNumberOfMessages;
	}
	
	public int getNumberComplete() {
		
		int numberComplete = 0;
		Iterator<Campaign> iterator = this.campaigns.iterator();
		while (iterator.hasNext()) {
			
			numberComplete = numberComplete + iterator.next().getNumberOfCompletions();
		}
		return numberComplete;
	}

	public int getNumberSuccessful() {
		
		int numberSuccessful = 0;
		Iterator<Campaign> iterator = this.campaigns.iterator();
		while (iterator.hasNext()) {
			
			numberSuccessful = numberSuccessful + iterator.next().getNumberSuccessful();
		}
		return numberSuccessful;
	}

	public int getNumberFailure() {
		
		int numberFailure = 0;
		Iterator<Campaign> iterator = this.campaigns.iterator();
		while (iterator.hasNext()) {
			
			numberFailure = numberFailure + iterator.next().getNumberFailure();
		}
		return numberFailure;
	}
	
	public int getNumberIncomplete() {
		return this.getTotalNumberOfVehicles() - this.getNumberSuccessful() - this.getNumberFailure();
	}

	public double getPercentageComplete() {
		
		int totalNumberOfVehicles = this.getTotalNumberOfVehicles();
		int numberSuccessful = this.getNumberSuccessful();
		int numberFailure = this.getNumberFailure();
		
		double percentageComplete = 0.0;
		if (totalNumberOfVehicles > 0) {
			
			percentageComplete = ((double)(numberSuccessful+numberFailure) / (double)totalNumberOfVehicles) * 100.0;
		}
		return percentageComplete;
	}

	public double getPercentageSuccessful() {
		
		int numberComplete = this.getNumberComplete();
		int numberSuccessful = this.getNumberSuccessful();
		
		double percentageSuccessful = 0.0;
		if (numberComplete > 0) {
			
			percentageSuccessful = ((double)numberSuccessful / (double)numberComplete) * 100.0;
		}
		return percentageSuccessful;
	}

	public double getPercentageFailure() {
		
		int numberComplete = this.getNumberComplete();
		int numberFailure = this.getNumberFailure();
		
		double percentageFailure = 0.0;
		if (numberComplete > 0) {
			
			percentageFailure = ((double)numberFailure / (double)numberComplete) * 100.0;
		}
		return percentageFailure;
	}

	public void initiateRollout(String initiatedBy) throws ValidationException {
		
		if (this.rolloutState != RolloutState.PENDING) {
			throw new ValidationException("rolloutState", this.getClassAndIdentity() + " cannot be initiated because it is not in the expected PENDING state, rather it was: " + this.rolloutState);
		}
		
		this.startDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		this.endDate = AbstractEntity.getTimeKeeper().getTimestampForDaysFromCurrent(this.expirationDays);
		this.initiatedBy = initiatedBy;
		
		// Ensure that we are in a proper state before transitioning into the deploying state.
		assertValid();
		this.rolloutState = RolloutState.DEPLOYING;
		
		LOGGER.debug("initiateRollout() for AbstractRollout: [{}] invoked with initiatedBy: [{}].  End date has been calculated to be: [{}].", this, initiatedBy, this.endDate);
		
		// Set all children to PENDING.
		Iterator<Campaign> campaignsIterator = campaigns.iterator();
		while (campaignsIterator.hasNext()) {

			Campaign campaign = campaignsIterator.next();
			campaign.setStateToPending();
		}
	}
	
	public VehicleCampaignBatch getVehicleCampaignBatchByTmcVehicleId(String tmcDeploymentId, String tmcVehicleId) {
		
		Iterator<Campaign> campaignIterator = this.campaigns.iterator();
		while (campaignIterator.hasNext()) {
			
			Campaign campaign = campaignIterator.next();
			
			Iterator<CampaignBatch> campaignBatchIterator = campaign.getCampaignBatches().iterator();
			while (campaignBatchIterator.hasNext()) {
				
				CampaignBatch campaignBatch = campaignBatchIterator.next();
				
				if (campaignBatch.getTmcDeploymentId().equals(tmcDeploymentId)) {
					
					Iterator<VehicleCampaignBatch> vehicleCampaignBatchIterator = campaignBatch.getVehicleCampaignBatches().iterator();
					while (vehicleCampaignBatchIterator.hasNext()) {
						
						VehicleCampaignBatch vehicleCampaignBatch = vehicleCampaignBatchIterator.next();
						
						if (vehicleCampaignBatch.getTmcVehicleId().equals(tmcVehicleId)) {
							return vehicleCampaignBatch;
						}
					}
				}
			}		
		}
		return null;
	}
	
	public VehicleCampaignBatch getVehicleCampaignBatchVin(String vin) {
		
		Iterator<Campaign> campaignIterator = this.campaigns.iterator();
		while (campaignIterator.hasNext()) {
			
			Campaign campaign = campaignIterator.next();
			
			Iterator<CampaignBatch> campaignBatchIterator = campaign.getCampaignBatches().iterator();
			while (campaignBatchIterator.hasNext()) {
				
				CampaignBatch campaignBatch = campaignBatchIterator.next();
				
				Iterator<VehicleCampaignBatch> vehicleCampaignBatchIterator = campaignBatch.getVehicleCampaignBatches().iterator();
				while (vehicleCampaignBatchIterator.hasNext()) {
					
					VehicleCampaignBatch vehicleCampaignBatch = vehicleCampaignBatchIterator.next();
					
					if (vehicleCampaignBatch.getVin().equals(vin)) {
						return vehicleCampaignBatch;
					}
				}
			}		
		}
		return null;
	}
	
	public boolean isActive() {
		
		boolean isActive = false;
		switch(this.rolloutState) {
		case CANCELLED:
			break;
		case DEPLOYING:
			isActive = true;
			break;
		case FINISHED:
			break;
		case PAUSED:
			isActive = true;
			break;
		case PENDING:
			isActive = true;
			break;
		case PRE_ROLLOUT_INVALID:
			isActive = true;
			break;
		case PRE_ROLLOUT_VALID:
			isActive = true;
			break;
		case RELEASE_ARTIFACT_UPLOAD_PENDING:
			isActive = true;
			break;
		default:
			break;
		}
		return isActive;
	}
}
