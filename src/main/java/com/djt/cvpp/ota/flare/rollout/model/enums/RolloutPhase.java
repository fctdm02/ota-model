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
public enum RolloutPhase {

    /**
     * Only vehicles designated as "validation phase" will be contacted/updated
     */
	VALIDATION,

    /**
     * All vehicles (excluding the validation phase vehicles) will be eligible for contacting for updates
     */
    NORMAL
}
