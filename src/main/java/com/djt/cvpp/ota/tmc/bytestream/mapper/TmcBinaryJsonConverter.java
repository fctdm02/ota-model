/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.bytestream.mapper;

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
public class TmcBinaryJsonConverter extends AbstractJsonConverter implements JsonConverter<com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary> {
	
	private TmcBinaryDtoMapper dtoMapper = new TmcBinaryDtoMapper();
	
	public com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary unmarshallFromJsonToEntity(String json) {
		try {
			com.djt.cvpp.ota.tmc.bytestream.mapper.dto.TmcBinary dto = this.getObjectMapper().readValue(json, new TypeReference<com.djt.cvpp.ota.tmc.bytestream.mapper.dto.TmcBinary>(){});
			return this.dtoMapper.mapDtoToEntity(dto);
		} catch (IOException e) {
			throw new FenixRuntimeException("Unable to unmarshall/map to TMC TmcBinary from json: [" + json + "], error: [" + e.getMessage() + "].", e);
		}
	}
	
	public String marshallFromEntityToJson(com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary e) {
		try {
			return this.getObjectMapper().writeValueAsString(this.dtoMapper.mapEntityToDto(e));
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to marshall/map from TMC TmcBinary to json: [" + e + "], error: [" + ioe.getMessage() + "].", ioe);
		}
	}
}
