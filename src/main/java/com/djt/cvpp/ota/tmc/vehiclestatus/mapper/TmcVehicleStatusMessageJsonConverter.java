/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.vehiclestatus.mapper;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.mapper.JsonConverter;
import com.djt.cvpp.ota.common.mapper.impl.AbstractJsonConverter;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TmcVehicleStatusMessageJsonConverter extends AbstractJsonConverter implements JsonConverter<com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage> {
	
	private TmcVehicleStatusMessageDtoMapper dtoMapper = new TmcVehicleStatusMessageDtoMapper();
	
	public com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage unmarshallFromJsonToEntity(String json) {
		try {
			com.djt.cvpp.ota.tmc.vehiclestatus.mapper.dto.VehicleStatusMessage dto = this.getObjectMapper().readValue(json, new TypeReference<com.djt.cvpp.ota.tmc.vehiclestatus.mapper.dto.VehicleStatusMessage>(){});
			return this.dtoMapper.mapDtoToEntity(dto);
		} catch (IOException e) {
			throw new FenixRuntimeException("Unable to unmarshall/map to TMC TmcVehicle Status Message from json: [" + json + "], error: [" + e.getMessage() + "].", e);
		}
	}
	
	public String marshallFromEntityToJson(com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage e) {
		try {
			return this.getObjectMapper().writeValueAsString(this.dtoMapper.mapEntityToDto(e));
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to marshall/map from TMC TmcVehicle Status Message to json: [" + e + "], error: [" + ioe.getMessage() + "].", ioe);
		}
	}
}
