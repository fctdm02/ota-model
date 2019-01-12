/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class VehicleStatusMessage {

	private long timestamp;
	private String fullLookupCodeValue;
	private List<VehicleStatusMessageAdditionalParameter> vehicleStatusMessageAdditionalParameters = new ArrayList<>();
}
