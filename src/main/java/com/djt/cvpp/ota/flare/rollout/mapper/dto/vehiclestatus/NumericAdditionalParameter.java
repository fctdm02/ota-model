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
import lombok.EqualsAndHashCode;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class NumericAdditionalParameter extends VehicleStatusMessageAdditionalParameter {

	private Number numericValue;
}
