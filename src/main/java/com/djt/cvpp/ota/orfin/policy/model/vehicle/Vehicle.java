/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.model.vehicle;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.policy.model.region.Region;

/**
 * NOTE: Instances of this class are not persisted in ORFIN, rather, they are 
 * retrieved from TMC/AU (as a "TmcVehicle Object" from the Resources service)
 * and are unmarshalled here as a TmcVehicle entity using the TMC adapter.
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Vehicle extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private String vin;
	private Region region;
	
	private Vehicle(VehicleBuilder builder) {
		
		this.vin = builder.vin;
		this.region = builder.region;
	}
	
	public String getNaturalIdentity() {

		return this.vin;
	}

	public void validate(List<String> validationMessages) {

		if (vin == null || vin.trim().isEmpty()) {
			validationMessages.add("vin must be specified.");
		}
		
		if (region == null) {
			validationMessages.add("region must be specified.");
		}
	}
	
	public String getVin() {
		return vin;
	}
	
	public Region getRegion() {
		return region;
	}
	
	public static final class VehicleBuilder {

		private String vin;
		private Region region;

		public VehicleBuilder() {
		}
		
		public VehicleBuilder withVin(String vin) {
			this.vin = vin;
			return this;
		}
		
		public VehicleBuilder withRegion(Region region) {
			this.region = region;
			return this;
		}
		
		public Vehicle build() {
			return new Vehicle(this);
		}
	}	
}
