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
import com.djt.cvpp.ota.flare.rollout.model.enums.CampaignBatchState;
import com.djt.cvpp.ota.flare.rollout.model.enums.VehicleCampaignBatchState;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class CampaignBatch extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CampaignBatch.class);


	private Campaign parentCampaign;
	private String campaignBatchName;
	private String tmcDeploymentId; // To be set once the deployment corresponding to this campaign batch has been sent to TMC/AU.
	private Timestamp beginDate;
	private Timestamp endDate;
	
	private CampaignBatchState campaignBatchState = CampaignBatchState.PRE_ROLLOUT_INVALID;

	private Set<VehicleCampaignBatch> vehicleCampaignBatches = new TreeSet<>();
	

	@SuppressWarnings("unused")
	private CampaignBatch() {
	}

	public CampaignBatch(
		Campaign parentCampaign,
		String campaignBatchName) {

		this.parentCampaign = parentCampaign;
		this.campaignBatchName = campaignBatchName;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentCampaign,
			campaignBatchName
		);
	}

	public void validate(List<String> validationMessages) {
		
		validateNotNull(validationMessages, "parentCampaign", parentCampaign);
		validateNotNull(validationMessages, "campaignBatchName", campaignBatchName);

		if (vehicleCampaignBatches == null || vehicleCampaignBatches.isEmpty()) {
			validationMessages.add("At least one VehicleCampaignBatch must be specified for CampaignBatch: [" + this + "].");
		} else {
			Iterator<VehicleCampaignBatch> iterator = vehicleCampaignBatches.iterator();
			while (iterator.hasNext()) {

				VehicleCampaignBatch vehicleCampaignBatch = iterator.next();
				vehicleCampaignBatch.validate(validationMessages);
			}
		}
		
		boolean allReleaseArtifactsUploadedToTmcByteStream = this.getAllReleaseArtifactsUploadedToTmcByteStream();
		if (validationMessages.isEmpty()) {
			
			if (this.campaignBatchState.equals(CampaignBatchState.PRE_ROLLOUT_INVALID)) {
				this.campaignBatchState = CampaignBatchState.PRE_ROLLOUT_VALID;	
			} else if (this.campaignBatchState.equals(CampaignBatchState.RELEASE_ARTIFACT_UPLOAD_PENDING) && allReleaseArtifactsUploadedToTmcByteStream) {
				this.setStateToPending();
			}			
		}
	}

	public String getTmcDeploymentId() {
		return tmcDeploymentId;
	}
	
	public void setTmcDeploymentId(String tmcDeploymentId) {
		this.tmcDeploymentId = tmcDeploymentId;
		LOGGER.debug("CampaignBatch: [{}] assigned with TMC DeploymentId: [{}].", this, tmcDeploymentId);
	}

	public Campaign getParentCampaign() {
		return parentCampaign;
	}

	public String getCampaignBatchName() {
		return campaignBatchName;
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

	public List<VehicleCampaignBatch> getVehicleCampaignBatches() {
		List<VehicleCampaignBatch> list = new ArrayList<>();
		list.addAll(this.vehicleCampaignBatches);
		return list;
	}
	
	public void addVehicleCampaignBatch(VehicleCampaignBatch vehicleCampaignBatch) throws ValidationException {
		
		this.vehicleCampaignBatches.add(vehicleCampaignBatch);
	}

	public CampaignBatchState getCampaignBatchState() {
		return this.campaignBatchState;
	}

	
	// BUSINESS BEHAVIORS
	protected boolean getAllReleaseArtifactsUploadedToTmcByteStream() {
		
		boolean allReleaseArtifactsUploadedToTmcByteStream = true;
		
		Iterator<VehicleCampaignBatch> iterator = this.vehicleCampaignBatches.iterator();
		while (iterator.hasNext()) {
			
			VehicleCampaignBatch vehicleCampaignBatch = iterator.next();
			if (!vehicleCampaignBatch.getAllReleaseArtifactsUploadedToTmcByteStream()) {
				
				allReleaseArtifactsUploadedToTmcByteStream = false;
				break;
			}
		}
	
		return allReleaseArtifactsUploadedToTmcByteStream;
	}
	
	public void setStateToReleaseArtifactUploadPending() {
		
		if (this.campaignBatchState == CampaignBatchState.RELEASE_ARTIFACT_UPLOAD_PENDING) {
			return;
		}
		
		if (!this.campaignBatchState.equals(CampaignBatchState.PRE_ROLLOUT_VALID)) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to RELEASE_ARTIFACT_UPLOAD_PENDING state from PRE_ROLLOUT_VALID state, yet was in state: " + this.campaignBatchState);
		}
		this.campaignBatchState = CampaignBatchState.RELEASE_ARTIFACT_UPLOAD_PENDING;
		LOGGER.debug("Setting state to RELEASE_ARTIFACT_UPLOAD_PENDING for: [{}].", this);
	}
	
	protected void setStateToPending() {

		if (this.campaignBatchState == CampaignBatchState.PENDING) {
			return;
		}

		if (!this.campaignBatchState.equals(CampaignBatchState.RELEASE_ARTIFACT_UPLOAD_PENDING)) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to PENDING state from RELEASE_ARTIFACT_UPLOAD_PENDING state, yet was in state: " + this.campaignBatchState);
		}

		Iterator<VehicleCampaignBatch> iterator = vehicleCampaignBatches.iterator();
		while (iterator.hasNext()) {

			VehicleCampaignBatch vehicleCampaignBatch = iterator.next();
			vehicleCampaignBatch.setStateToPending();
		}
		
		this.campaignBatchState = CampaignBatchState.PENDING;
		LOGGER.debug("Setting state to PENDING for: [{}].", this);
	}

	public void setStateToActive() {
		
		if (this.campaignBatchState != CampaignBatchState.PENDING) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to ACTIVE state from PENDING state, yet was in state: " + this.campaignBatchState);
		}
		
		// TODO: This needs to be associated at the service layer, so that we can have the TMC Deployment Adapter send the actual update trigger to the vehicles in the batch.
		Iterator<VehicleCampaignBatch> iterator = vehicleCampaignBatches.iterator();
		while (iterator.hasNext()) {

			VehicleCampaignBatch vehicleCampaignBatch = iterator.next();
			vehicleCampaignBatch.setStateToDeploying();
		}
		
		this.beginDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		this.campaignBatchState = CampaignBatchState.ACTIVE;
		LOGGER.debug("Setting state to ACTIVE for: [{}].", this);
	}
		
	public void pause() {
		
		if (this.campaignBatchState == CampaignBatchState.FINISHED 
			|| this.campaignBatchState == CampaignBatchState.PAUSED) {
			return;
		}
		
		if (this.campaignBatchState != CampaignBatchState.ACTIVE) {
			
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved back to ACTIVE state from PAUSED state, yet was in state: " + this.campaignBatchState);
		}
		
		Iterator<VehicleCampaignBatch> iterator = this.vehicleCampaignBatches.iterator();
	    while (iterator.hasNext()) {
	    	
	    	VehicleCampaignBatch vehicleCampaignBatch = iterator.next();
	    	vehicleCampaignBatch.pause();
	    }
		
		
		// TODO: Is there a "pause campaign" trigger that can be sent to the vehicle?
	    	    
	    this.campaignBatchState = CampaignBatchState.PAUSED;
	    LOGGER.debug("Setting state to PAUSED for : [{}].", this);
	}

	public void resume() {
		
		if (this.campaignBatchState == CampaignBatchState.FINISHED 
			|| this.campaignBatchState == CampaignBatchState.ACTIVE) {
			return;
		}
		
		if (this.campaignBatchState != CampaignBatchState.PAUSED) {
			
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved back to DEPLOYING state from PAUSED state, yet was in state: " + this.campaignBatchState);
		}
		
		Iterator<VehicleCampaignBatch> iterator = this.vehicleCampaignBatches.iterator();
	    while (iterator.hasNext()) {
	    	
	    	VehicleCampaignBatch vehicleCampaignBatch = iterator.next();
	    	vehicleCampaignBatch.resume();
	    }
		
		// TODO: Is there a "resume campaign" trigger that can be sent to the vehicle?
	    	    
	    this.campaignBatchState = CampaignBatchState.ACTIVE;
	    LOGGER.debug("Setting state back to ACTIVE for: [{}].", this);
	}
	
	public void cancel() {
		
		if (this.campaignBatchState == CampaignBatchState.FINISHED 
			|| this.campaignBatchState == CampaignBatchState.CANCELLED) {
			return;
		}
		
		Iterator<VehicleCampaignBatch> iterator = this.vehicleCampaignBatches.iterator();
	    while (iterator.hasNext()) {
	    	
	    	VehicleCampaignBatch vehicleCampaignBatch = iterator.next();
	    	vehicleCampaignBatch.cancel();
	    	
	    	// TODO: We need to move this method to the service so that we can instruct the TMC deployment adapter to send cancellation request for this batch/deployment.
	    }
		
		this.endDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		this.campaignBatchState = CampaignBatchState.CANCELLED;
		LOGGER.debug("Cancelling: [{}].", this);
	}
	
	public int getTotalNumberOfVehicles() {
		
		return this.vehicleCampaignBatches.size();
	}
	
	public int getTotalNumberOfVehicleMessages() {
		
		int totalNumberOfMessages = 0;
		Iterator<VehicleCampaignBatch> iterator = this.vehicleCampaignBatches.iterator();
		while (iterator.hasNext()) {
			
			totalNumberOfMessages = totalNumberOfMessages + iterator.next().getTotalNumberOfVehicleMessages();
		}
		return totalNumberOfMessages;
	}
	
	public int getNumberOfCompletions() {
		
		int numberOfCompletions = 0;
		Iterator<VehicleCampaignBatch> iterator = vehicleCampaignBatches.iterator();
		while (iterator.hasNext()) {
			
			VehicleCampaignBatchState vehicleCampaignBatchState = iterator.next().getVehicleCampaignBatchState();
			if (vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_SUCCESS || vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_FAILURE) {
				numberOfCompletions++;
			}
		}
		return numberOfCompletions;
	}

	public int getNumberSuccessful() {
		
		int numberSuccessful = 0;
		Iterator<VehicleCampaignBatch> iterator = vehicleCampaignBatches.iterator();
		while (iterator.hasNext()) {
			
			VehicleCampaignBatchState vehicleCampaignBatchState = iterator.next().getVehicleCampaignBatchState();
			if (vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_SUCCESS) {
				numberSuccessful++;
			}
		}
		return numberSuccessful;
	}

	public int getNumberFailure() {
		
		int numberFailure = 0;
		Iterator<VehicleCampaignBatch> iterator = vehicleCampaignBatches.iterator();
		while (iterator.hasNext()) {
			
			VehicleCampaignBatchState vehicleCampaignBatchState = iterator.next().getVehicleCampaignBatchState();
			if (vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_FAILURE) {
				numberFailure++;
			}
		}
		return numberFailure;
	}
	
	public void evaluateCampaignBatchState() throws ValidationException {
		
		// See if we can trigger any new deployments, which are at the campaign batch level.  We simply evaluate each child entity.
		int numberOfVehicleCampaignBatches = this.getVehicleCampaignBatches().size();
		int numberOfCompleteVehicleCampaignBatches = 0;
		Iterator<VehicleCampaignBatch> iterator = this.getVehicleCampaignBatches().iterator();
		while (iterator.hasNext()) {
			
			VehicleCampaignBatch vehicleCampaignBatch = iterator.next();
			
			vehicleCampaignBatch.evaluateVehicleCampaignBatchState();
			
			VehicleCampaignBatchState vehicleCampaignBatchState = vehicleCampaignBatch.getVehicleCampaignBatchState();
			
			if (vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_SUCCESS 
				|| vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_FAILURE
				|| vehicleCampaignBatchState == VehicleCampaignBatchState.CANCELLED) {
				
				numberOfCompleteVehicleCampaignBatches++;
			}
		}
		
		// TODO: TDM: There may be a percentage threshold, by which a campaign batch is considered complete.
		if (numberOfCompleteVehicleCampaignBatches == numberOfVehicleCampaignBatches) {
			this.campaignBatchState = CampaignBatchState.FINISHED;
		}
	}
}
