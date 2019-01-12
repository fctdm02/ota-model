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
import com.djt.cvpp.ota.flare.rollout.model.enums.VehicleCampaignBatchState;
import com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessage;
import com.djt.cvpp.ota.flare.signedcommands.model.VehicleCampaignBatchSignedCommand;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VehicleCampaignBatch extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VehicleCampaignBatch.class);
	

	// TODO: TDM: Verify that this is the proper ECU acronym name for vehicle inhibit signed commands.
	private static final String CAVC = "CAVC";
	
	private CampaignBatch parentCampaignBatch;
	private String vin;
	private String tmcVehicleId;
	private String tmcVehicleObjectPayload;	
	private Timestamp beginDate;
	private Timestamp endDate;
	private Integer numRetries;
	private Timestamp lastRetryDate;
	private Boolean isValidationVehicle;
	
	private VehicleCampaignBatchState vehicleCampaignBatchState = VehicleCampaignBatchState.PRE_ROLLOUT_INVALID;
	
	private Set<VehicleCampaignBatchSignedCommand> vehicleCampaignBatchSignedCommands = new TreeSet<>();
	private UUID tmcSignedCommandsBinaryId;
	
	private Set<VehicleStatusMessage> vehicleStatusMessages = new TreeSet<>();
	
	@SuppressWarnings("unused")
	private VehicleCampaignBatch() {
	}

	public VehicleCampaignBatch(
		CampaignBatch parentCampaignBatch, 
		String vin,
		String tmcVehicleId,
		String tmcVehicleObjectPayload) {
		
		this.parentCampaignBatch = parentCampaignBatch;
		this.vin = vin;
		this.tmcVehicleId = tmcVehicleId;
		this.tmcVehicleObjectPayload = tmcVehicleObjectPayload;
		
		LOGGER.debug("VehicleCampaignBatch: [{}] with TMC VehicleId: [{}] created.", AbstractEntity.buildNaturalIdentity(parentCampaignBatch,vin), tmcVehicleId);
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentCampaignBatch,
			vin
		);
	}

	public void validate(List<String> validationMessages) {

		validateNotNull(validationMessages, "parentCampaignBatch", parentCampaignBatch);
		validateNotNull(validationMessages, "vin", vin);
		validateNotNull(validationMessages, "tmcVehicleId", tmcVehicleId);
		validateNotNull(validationMessages, "tmcVehicleObjectPayload", tmcVehicleObjectPayload);
		
		if (vehicleCampaignBatchSignedCommands == null || vehicleCampaignBatchSignedCommands.isEmpty()) {
			validationMessages.add("At least one VehicleCampaignBatchSignedCommand must be specified for VehicleCampaignBatch: [" + this + "].");
		} else {
			Iterator<VehicleCampaignBatchSignedCommand> iterator = vehicleCampaignBatchSignedCommands.iterator();
			while (iterator.hasNext()) {

				VehicleCampaignBatchSignedCommand vehicleCampaignBatchSignedCommand = iterator.next();
				vehicleCampaignBatchSignedCommand.validate(validationMessages);
			}
		}
		
		boolean allReleaseArtifactsUploadedToTmcByteStream = this.getAllReleaseArtifactsUploadedToTmcByteStream();
		if (validationMessages.isEmpty()) {
			
			if (this.vehicleCampaignBatchState.equals(VehicleCampaignBatchState.PRE_ROLLOUT_INVALID)) {
				this.vehicleCampaignBatchState = VehicleCampaignBatchState.PRE_ROLLOUT_VALID;	
			} else if (this.vehicleCampaignBatchState.equals(VehicleCampaignBatchState.RELEASE_ARTIFACT_UPLOAD_PENDING) && allReleaseArtifactsUploadedToTmcByteStream) {
				this.setStateToPending();
			}			
		}
	}
	
	public CampaignBatch getParentCampaignBatch() {
		return parentCampaignBatch;
	}

	public String getVin() {
		return vin;
	}

	public String getTmcVehicleId() {
		return tmcVehicleId;
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

	public Integer getNumRetries() {
		return numRetries;
	}

	public Timestamp getLastRetryDate() {
		if (lastRetryDate != null) {
			return new Timestamp(lastRetryDate.getTime());	
		}
		return null;
	}

	public long getLastRetryDateAsMillis() {
		if (lastRetryDate != null) {
			return lastRetryDate.getTime();	
		}
		return -1L;
	}
	
	public Boolean getIsValidationVehicle() {
		return isValidationVehicle;
	}

	public UUID getTmcSignedCommandsBinaryId() {
		return tmcSignedCommandsBinaryId;
	}

	public void setTmcSignedCommandsBinaryId(UUID tmcSignedCommandsBinaryId) {
		this.tmcSignedCommandsBinaryId = tmcSignedCommandsBinaryId;
	}
	
	public String getTmcVehicleObjectPayload() {
		return tmcVehicleObjectPayload;
	}
		
	public VehicleCampaignBatchState getVehicleCampaignBatchState() {
		return vehicleCampaignBatchState;
	}
	
	public List<VehicleCampaignBatchSignedCommand> getVehicleCampaignBatchSignedCommands() {
		List<VehicleCampaignBatchSignedCommand> list = new ArrayList<>();
		list.addAll(this.vehicleCampaignBatchSignedCommands);
		return list;
	}
	
	public List<VehicleStatusMessage> getVehicleStatusMessages() {
		List<VehicleStatusMessage> list = new ArrayList<>();
		list.addAll(this.vehicleStatusMessages);
		return list;
	}
	
	
	// BUSINESS BEHAVIORS
	protected boolean getAllReleaseArtifactsUploadedToTmcByteStream() {
		
		boolean allReleaseArtifactsUploadedToTmcByteStream = true;
		
		if (this.tmcSignedCommandsBinaryId == null) {
			
			allReleaseArtifactsUploadedToTmcByteStream = false;
		}
	
		return allReleaseArtifactsUploadedToTmcByteStream;
	}
	
	public void evaluateVehicleCampaignBatchState() throws ValidationException {
		
		validate();
		
		if (this.vehicleCampaignBatchState.equals(VehicleCampaignBatchState.RELEASE_ARTIFACT_UPLOAD_PENDING) && this.tmcSignedCommandsBinaryId != null) {
			this.setStateToPending();
		}
	}
	
	/*
	 * <pre>
		1. Any status message with [Any string]_ 'S'[any integers](_[variableinformation])n would be mapped to 'Deploying' TMC state
		2. Status message which is specifically OTAM_S1010(_[variable information])* will be mapped to 'Deployment succeeded' TMC state
		3. Any status message with [Any string]_'E'[any integers](_[variableinformation])n will be mapped to an TMC error state
 
 		OTAM_1001 (Trigger received)
		OTAM_1006 (Download start)
		OTAM_1007 (Ready to activate)
		OTAM_1008 (Activation complete)
		OTAM_1010 (Campaign finished)
		OTAM_1011 (Campaign status change)
		OTAM_1015 (Master reset)
	 * </pre> 
	 * 
	 * @param vehicleStatusMessage
	 */
	public void addVehicleStatusMessage(VehicleStatusMessage vehicleStatusMessage ) throws ValidationException{
		
		synchronized(this.vehicleStatusMessages) {
			vehicleStatusMessage.assertValid();
			
			String fullLookupCodeValue = vehicleStatusMessage.getFullLookupCodeValue();
			if (fullLookupCodeValue.startsWith(VehicleStatusMessage.FINISHED_WITH_SUCCESS_STATUS_MESSAGE_VALUE)) {

				this.endDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
				this.vehicleCampaignBatchState = VehicleCampaignBatchState.FINISHED_WITH_SUCCESS;
				LOGGER.debug("TmcVehicle: [{}] finished update with SUCCESS: [{}].", this, fullLookupCodeValue);
				
			} else if (fullLookupCodeValue.contains(VehicleStatusMessage.FINISHED_WITH_FAILURE_PREFIX)) {
				
				this.endDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
				this.vehicleCampaignBatchState = VehicleCampaignBatchState.FINISHED_WITH_FAILURE;
				LOGGER.debug("TmcVehicle: [{}] finished update with FAILURE: [{}].", this, fullLookupCodeValue);
			}
		
			this.vehicleStatusMessages.add(vehicleStatusMessage);
		}
	}
	
	public int getTotalNumberOfVehicleMessages() {
		
		return this.vehicleStatusMessages.size();
	}
	
	public void setStateToReleaseArtifactUploadPending() {
		
		if (this.vehicleCampaignBatchState == VehicleCampaignBatchState.RELEASE_ARTIFACT_UPLOAD_PENDING) {
			return;
		}
		
		if (!this.vehicleCampaignBatchState.equals(VehicleCampaignBatchState.PRE_ROLLOUT_VALID)) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to RELEASE_ARTIFACT_UPLOAD_PENDING state from PRE_ROLLOUT_VALID state, yet was in state: " + this.vehicleCampaignBatchState + ", validation messages: " + this.validate());
		}
		this.vehicleCampaignBatchState = VehicleCampaignBatchState.RELEASE_ARTIFACT_UPLOAD_PENDING;
		LOGGER.debug("Setting state to RELEASE_ARTIFACT_UPLOAD_PENDING for: [{}].", this);
	}
	
	protected void setStateToPending() {

		if (this.vehicleCampaignBatchState == VehicleCampaignBatchState.PENDING) {
			return;
		}

		if (!this.vehicleCampaignBatchState.equals(VehicleCampaignBatchState.RELEASE_ARTIFACT_UPLOAD_PENDING)) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to PENDING state from RELEASE_ARTIFACT_UPLOAD_PENDING state, yet was in state: " + this.vehicleCampaignBatchState);
		}

		if (this.tmcSignedCommandsBinaryId == null) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to PENDING state if release artifacts were uploaded, meaning that tmcSignedCommandsBinaryId is non-null");
		}
		
		this.vehicleCampaignBatchState = VehicleCampaignBatchState.PENDING;
		LOGGER.debug("Setting state to PENDING for: [{}].", this);
	}

	public void setStateToDeploying() {
		
		if (this.vehicleCampaignBatchState != VehicleCampaignBatchState.PENDING) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to DEPLOYING state from PENDING state, yet was in state: " + this.vehicleCampaignBatchState);
		}
		
		this.beginDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		this.vehicleCampaignBatchState = VehicleCampaignBatchState.DEPLOYING;
		LOGGER.debug("Setting state to DEPLOYING for: [{}].", this);
	}
		
	public void pause() {
		
		if (this.vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_SUCCESS 
			|| this.vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_FAILURE 
			|| this.vehicleCampaignBatchState == VehicleCampaignBatchState.PAUSED) {
			return;
		}
		
		if (this.vehicleCampaignBatchState != VehicleCampaignBatchState.DEPLOYING) {
			
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved to PAUSED state from DEPLOYING state, yet was in state: " + this.vehicleCampaignBatchState);
		}
		
		// TODO: Is there a "pause campaign" trigger that can be sent to the vehicle?
	    	    
	    this.vehicleCampaignBatchState = VehicleCampaignBatchState.PAUSED;
	    LOGGER.debug("Setting state to PAUSED for : [{}].", this);
	}

	public void resume() {
		
		if (this.vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_SUCCESS 
			|| this.vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_FAILURE 
			|| this.vehicleCampaignBatchState == VehicleCampaignBatchState.DEPLOYING) {
			return;
		}
		
		if (this.vehicleCampaignBatchState != VehicleCampaignBatchState.PAUSED) {
			
			throw new FenixRuntimeException(this.getClassAndIdentity() + " can only be moved back to DEPLOYING state from PAUSED state, yet was in state: " + this.vehicleCampaignBatchState);
		}
		
		// TODO: Is there a "resume campaign" trigger that can be sent to the vehicle?
	    	    
	    this.vehicleCampaignBatchState = VehicleCampaignBatchState.DEPLOYING;
	    LOGGER.debug("Setting state back to DEPLOYING for: [{}].", this);
	}
	
	public void cancel() {
		
		if (this.vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_SUCCESS 
			|| this.vehicleCampaignBatchState == VehicleCampaignBatchState.FINISHED_WITH_FAILURE
			|| this.vehicleCampaignBatchState == VehicleCampaignBatchState.CANCELLED) {
			return;
		}
		
		this.endDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		this.vehicleCampaignBatchState = VehicleCampaignBatchState.CANCELLED;
		LOGGER.debug("Cancelling: [{}].", this);
	}
	
	public void generateSignedCommands() {

		LOGGER.debug("Generating signed commands for: [{}].", this);
		// TODO: TDM: Implement using refactored logic from existing MVP code.
		this.vehicleCampaignBatchSignedCommands.add(createVehicleInhibitSignedCommand());
	}	
	
	/*
	 *  TmcVehicle Inhibit signed commands are needed for every update
	 */
	public VehicleCampaignBatchSignedCommand createVehicleInhibitSignedCommand() {
		
		// TODO: TDM: Implement using refactored logic from existing MVP code.
		String nodeAcronym = CAVC;
		Integer sequenceNumber = Integer.valueOf(1);
		String key = "KEY";
		byte[] value = new byte[1];
		value[0] = '0';
		
		VehicleCampaignBatchSignedCommand vehicleCampaignBatchSignedCommand = new VehicleCampaignBatchSignedCommand(
			this,
			nodeAcronym,
			sequenceNumber,
			key,
			value);
		
		return vehicleCampaignBatchSignedCommand;
	}
}
