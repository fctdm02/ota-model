/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Region extends AbstractEntity {

	private static final long serialVersionUID = 1L;
		
	private String regionName;

	private Region() {
	}
	
	private Region(RegionBuilder builder) {

		this.persistentIdentity = builder.persistentIdentity;
		this.regionName = builder.regionName;
	}
	
	public String getNaturalIdentity() {

		return regionName;
	}

	public void validate(List<String> validationMessages) {

		if (regionName == null || regionName.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'regionName' must be specified.");
		}
	}

	public String getRegionName() {
		return regionName;
	}
	
	public static final class RegionBuilder {

        private Long persistentIdentity;
		private String regionName;

		public RegionBuilder() {
		}

		public RegionBuilder withPersistentIdentity(Long persistentIdentity) {
			this.persistentIdentity = persistentIdentity;
			return this;
		}

		public RegionBuilder withRegionName(String regionName) {
			this.regionName = regionName;
			return this;
		}
		
		public Region build() {
			return new Region(this);
		}
	}
}
