/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.model;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class OdlUpdateRollout extends AbstractProgramBasedRollout {
	
	private static final long serialVersionUID = 1L;

	public OdlUpdateRollout(
		String rolloutName,
		String programCode,
		Integer modelYear,
		String regionCode) {
		super(
			rolloutName,
			programCode,
			modelYear,
			regionCode);
	}	
}
