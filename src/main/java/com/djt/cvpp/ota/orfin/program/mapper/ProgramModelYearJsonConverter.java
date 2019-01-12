/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.program.mapper;

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
public class ProgramModelYearJsonConverter extends AbstractJsonConverter implements JsonConverter<com.djt.cvpp.ota.orfin.program.model.ProgramModelYear> {
	
	private ProgramModelYearDtoMapper dtoMapper = new ProgramModelYearDtoMapper();
	
	public com.djt.cvpp.ota.orfin.program.model.ProgramModelYear unmarshallFromJsonToEntity(String json) {
		try {
			com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear dto = this.getObjectMapper().readValue(json, new TypeReference<com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear>(){});
			return this.dtoMapper.mapDtoToEntity(dto);
		} catch (IOException e) {
			throw new FenixRuntimeException("Unable to unmarshall/map to ORFIN ProgramModelYear from json: [" + json + "], error: [" + e.getMessage() + "].", e);
		}
	}
	
	public String marshallFromEntityToJson(com.djt.cvpp.ota.orfin.program.model.ProgramModelYear e) {
		try {
			return this.getObjectMapper().writeValueAsString(this.dtoMapper.mapEntityToDto(e));
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to marshall/map from ORFIN ProgramModelYear to json: [" + e + "], error: [" + ioe.getMessage() + "].", ioe);
		}
	}
}
