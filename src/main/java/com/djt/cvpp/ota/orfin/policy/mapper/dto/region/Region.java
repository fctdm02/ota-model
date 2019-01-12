/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper.dto.region;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * NOTE: Instances of this class are not persisted in ORFIN, rather, they are 
 * retrieved from TMC/AU (as a "Region Object" from the Resources service)
 * and are unmarshalled here as a Region entity using the TMC adapter.
 * 
 * (The same goes for PolicyVehicleOverride instances as well)
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Region {
	
	private String regionCode;
	private String countryName;
}
