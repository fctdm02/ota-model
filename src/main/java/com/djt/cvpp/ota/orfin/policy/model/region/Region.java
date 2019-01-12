/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.model.region;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Region extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private String regionCode;
	private String countryName;
	
	private Region(RegionBuilder builder) {
		
		this.regionCode = builder.regionCode;
		this.countryName = builder.countryName;
	}
	
	public String getNaturalIdentity() {

		return this.regionCode;
	}

	public void validate(List<String> validationMessages) {

		if (regionCode == null || regionCode.trim().isEmpty()) {
			validationMessages.add("regionCode must be specified.");
		}
		
		if (countryName == null || countryName.trim().isEmpty()) {
			validationMessages.add("countryName must be specified.");
		}
	}
	
	public String getRegionCode() {
		return regionCode;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public static final class RegionBuilder {

		private String regionCode;
		private String countryName;

		public RegionBuilder() {
		}
		
		public RegionBuilder withRegionCode(String regionCode) {
			this.regionCode = regionCode;
			return this;
		}
		
		public RegionBuilder withCountryName(String countryName) {
			this.countryName = countryName;
			return this;
		}
		
		public Region build() {
			return new Region(this);
		}
	}	
}
