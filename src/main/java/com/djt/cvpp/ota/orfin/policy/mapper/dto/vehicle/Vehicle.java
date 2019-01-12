/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.djt.cvpp.ota.orfin.policy.mapper.dto.region.Region;

import lombok.Data;

/**
 * NOTE: Instances of this class are not persisted in ORFIN, rather, they are 
 * retrieved from TMC/AU (as a "TmcVehicle Object" from the Resources service)
 * and are unmarshalled here as a TmcVehicle entity using the TMC adapter.
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Vehicle {
	
	private String vin;
	private Region region;
}
