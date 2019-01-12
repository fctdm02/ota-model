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
public enum CampaignBatchState {

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
     * The CampaignBatch is in a valid state and is ready to be deployed (because all signed commands are available 
     * in TMC bytestream).
     */
    PENDING,

    /**
     * The CampaignBatch is actively being processed
     * (all the vehicles in the batch are being contacted for updates)
     */
    ACTIVE,

    /**
     * The CampaignBatch that previously was active, is now paused,
     * so any pending vehicles are blocked (until resumed/cancelled)
     */
    PAUSED,

    /**
     * The CampaignBatch was cancelled before it could finish
     * (i.e. all vehicles contacted)
     */
    CANCELLED,

    /**
     *  The CampaignBatch completed contacting all vehicles (i.e. sending updates
     *  down to all vehicles), or max retries have been reached for all outstanding
     *  vehicles, or the expiration date for the top level Rollout has been reached
     *  (which would mean that all pending vehicle updates would be cancelled)
     */
    FINISHED
}
