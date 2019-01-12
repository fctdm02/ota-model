/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.vadr.mapper;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.mapper.JsonConverter;
import com.djt.cvpp.ota.common.mapper.impl.AbstractJsonConverter;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class DomainJsonConverter extends AbstractJsonConverter implements JsonConverter<com.djt.cvpp.ota.vadr.model.Domain> {
	
	private DomainDtoMapper dtoMapper = new DomainDtoMapper();
	
	public com.djt.cvpp.ota.vadr.model.Domain unmarshallFromJsonToEntity(String json) {
		try {
			com.djt.cvpp.ota.vadr.mapper.dto.Domain domainDto = this.getObjectMapper().readValue(json, new TypeReference<com.djt.cvpp.ota.vadr.mapper.dto.Domain>(){});
			return this.dtoMapper.mapDtoToEntity(domainDto);
		} catch (IOException | EntityAlreadyExistsException | EntityDoesNotExistException e) {
			throw new FenixRuntimeException("Unable to unmarshall/map to VADR Domain from json: [" + json + "], error: [" + e.getMessage() + "].", e);
		}
	}
	
	public String marshallFromEntityToJson(com.djt.cvpp.ota.vadr.model.Domain e) {
		try {
			return this.getObjectMapper().writeValueAsString(this.dtoMapper.mapEntityToDto(e));
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to marshall/map from Domain to json: [" + e + "], error: [" + ioe.getMessage() + "].", ioe);
		}
	}
}
