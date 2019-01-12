/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.model.enums;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 * The state of the vehicle for a given Rollout is kept by an instance of VehicleCampaignBatch on the
 * cloud side.  All VehicleStatusMessages that are sent by the vehicle to the cloud are parsed and
 * stored/associated with this instance.  All time-critical vehicle status messages will map to one
 * of the in-process states listed here, with some of these messages, like OTAM_S1010 or any Error
 * message, as being a terminal state of FINISHED_WITH_SUCCESS or FINISHED_WITH_FAILURE, respectively. 
 *
 */
public enum VehicleCampaignBatchState {

    /**
     * Aggregate entity graph has been created, but not yet in a valid state (for the whole graph)
     */
	PRE_ROLLOUT_INVALID,

    /**
     * The aggregate entity graph is in a valid state, because all child entities have been created
     * (including all 'generated' content, which for example, could be campaign generic manifests 
     * and vehicle campaign batch signed commands).  In this state, the entity graph is ready to move to
     * the 'upload' state, whereby the generated content will be uploaded to TMC byte stream.
     */
	PRE_ROLLOUT_VALID,

    /**
     * Release artifacts for the entity at this level (or any child entities) have not been completed.
     * Once all artifacts have been uploaded to TMC byte stream, then this entity can be moved to the
     * 'pending' state.
     */
    RELEASE_ARTIFACT_UPLOAD_PENDING,
    
    /**
     * The VehicleCampaignBatch is in a valid state and is ready to be deployed (because the signed commands 
     * are available in TMC bytestream).
     */
    PENDING,
        
    /**
     * The VehicleCampaignBatch is in an active state of deploying updates to the vehicle identified by the VIN
     * (with respect to TMC, it will be identified by the TMC TmcVehicle ID)
     */
    DEPLOYING,

    /**
     *  The VehicleCampaignBatch is in an active state of deployment, yet any pending updates/downloads to
     *  the vehicle are blocked/paused (via a special pause trigger that is sent down to the vehicle)
     */
    PAUSED,

    /**
     * The deployment to the VehicleCampaignBatch was cancelled before it could finish (i.e. all updates/downloads
     * for all softwares/applications, ODLs or AbstractPolicy Table updates specified in the manifest sent to the vehicle)
     */
    CANCELLED,

    /**
     *  The deployment to the VehicleCampaignBatch completed and the final "success" message was received from the vehicle
     */
    FINISHED_WITH_SUCCESS,

    /**
     * The deployment to the VehicleCampaignBatch completed and an error message was received from the vehicle,
     * indicating that the deployment failed.
     */
    FINISHED_WITH_FAILURE
}
