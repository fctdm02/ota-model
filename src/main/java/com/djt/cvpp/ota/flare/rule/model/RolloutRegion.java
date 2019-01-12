/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rule.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class RolloutRegion extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private RolloutRegion parentRolloutRegion;
	private String regionName;
	
	public RolloutRegion(String regionName) {
		this(regionName, null);
	}
	
	public RolloutRegion(String regionName, RolloutRegion parentRolloutRegion) {
		this.regionName = regionName;
		this.parentRolloutRegion = parentRolloutRegion;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentRolloutRegion,
			regionName
		);
	}

	public void validate(List<String> validationMessages) {
		
		validateNotNull(validationMessages, "regionName", regionName);
		validateNotNull(validationMessages, "parentRolloutRegion", parentRolloutRegion);
	}

	public RolloutRegion getParentRolloutRegion() {
		return parentRolloutRegion;
	}

	public String getRegionName() {
		return regionName;
	}
}
