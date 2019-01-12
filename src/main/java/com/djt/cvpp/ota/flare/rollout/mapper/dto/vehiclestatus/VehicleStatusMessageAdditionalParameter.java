/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public abstract class VehicleStatusMessageAdditionalParameter {

	private Integer sequenceNumber;
	private String additionalParameterLabelName;
}
