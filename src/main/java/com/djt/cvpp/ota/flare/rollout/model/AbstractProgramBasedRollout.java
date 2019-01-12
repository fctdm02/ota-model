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
public abstract class AbstractProgramBasedRollout extends AbstractRollout {
	
	private static final long serialVersionUID = 1L;
	
	
	private String programCode;
	private Integer modelYear;
	private String regionCode;

	public AbstractProgramBasedRollout(
		String rolloutName,
		String programCode,
		Integer modelYear,
		String regionCode) {
		super(rolloutName);
		this.programCode = programCode;
		this.modelYear = modelYear;
		this.regionCode = regionCode;
	}

	public String getProgramCode() {
		return programCode;
	}

	public Integer getModelYear() {
		return modelYear;
	}
	
	public String getRegionCode() {
		return regionCode;
	}
}
