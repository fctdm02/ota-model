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
 */
public enum CampaignState {

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
     * The Campaign is in a valid state and is ready to be deployed (because all manifests and 
     * signed commands are available in TMC bytestream).
     */
    PENDING,
    
    /**
     * The Campaign is in an active state of deploying updates to vehicles via campaign batches
     */
    DEPLOYING,

    /**
     *  The Campaign is in an active state of deployment, yet any pending updates to non-processed
     *  vehicles are blocked/paused for ALL batches/vehicles
     */
    PAUSED,

    /**
     * The Campaign was cancelled before it could finish (i.e. all vehicles contacted)
     */
    CANCELLED,

    /**
     *  The Campaign completed contacting all vehicles (i.e. sending updates down to all vehicles)
     *  in its "family tree", or all VehicleCampaignBatch in all CampaignBatches belonging to this
     *  Campaign or max retries have been reached for all outstanding vehicles, or the expiration
     *  days for the top level Rollout has been reached (which would mean that all pending vehicle
     *  updates would be cancelled)
     */
    FINISHED
}
