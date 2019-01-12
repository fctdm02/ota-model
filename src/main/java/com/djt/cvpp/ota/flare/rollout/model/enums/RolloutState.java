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
public enum RolloutState {

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
     * The Rollout is in a valid state and is ready to be deployed (because all manifests and signed commands 
     * are created/available in TMC bytestream and all TMC deployments have been created in TMC as well)
     */
    PENDING,
		
    /**
     * The Rollout is in an active state of deploying updates to vehicles via chunks of work
     * called CampaignBatches (that correspond to TMC/AU "Deployments").  Each CampaignBatch
     * belongs to a parent Campaign (and each Campaign, in turn, belongs to the top level Rollout)
     * where each Campaign has its unique combination of "GenericManifest" (that is tied to both a
     * particular combination of program code/model year, as well as a "source state" for the software
     * on the vehicle (NOTE: This may change if the ECG/PD team can support wildcards for "source"
     * version for the manifest)
     */
    DEPLOYING,

    /**
     *  The Rollout is in an active state of deployment, yet any pending updates to non-processed vehicles 
     *  are blocked/paused (for ALL campaigns/batches/vehicles)
     */
    PAUSED,

    /**
     * The user performed a "Cancel" operation on the UI. Any pending activity will cease
     */
    CANCELLED,

    /**
     *  The Rollout completed contacting all vehicles (i.e. sending updates down to all vehicles),
     *  or max retries have been reached for all outstanding vehicles,
     *  or the expiration days for the Rollout has been reached
     *  (which would mean that all pending vehicle updates would be cancelled)
     */
    FINISHED
}
