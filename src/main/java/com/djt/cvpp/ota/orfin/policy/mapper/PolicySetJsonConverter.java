/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.mapper.JsonConverter;
import com.djt.cvpp.ota.common.mapper.impl.AbstractJsonConverter;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class PolicySetJsonConverter extends AbstractJsonConverter implements JsonConverter<com.djt.cvpp.ota.orfin.policy.model.PolicySet> {
	
	private PolicySetDtoMapper dtoMapper = new PolicySetDtoMapper();
	
	public com.djt.cvpp.ota.orfin.policy.model.PolicySet unmarshallFromJsonToEntity(String json) {
		try {
			com.djt.cvpp.ota.orfin.policy.mapper.dto.PolicySet dto = this.getObjectMapper().readValue(json, new TypeReference<com.djt.cvpp.ota.orfin.policy.mapper.dto.PolicySet>(){});
			return this.dtoMapper.mapDtoToEntity(dto);
		} catch (IOException | EntityAlreadyExistsException | EntityDoesNotExistException | ValidationException e) {
			throw new FenixRuntimeException("Unable to unmarshall/map to ORFIN PolicySet from json: [" + json + "], error: [" + e.getMessage() + "].", e);
		}
	}
	
	public String marshallFromEntityToJson(com.djt.cvpp.ota.orfin.policy.model.PolicySet e) {
		try {
			return this.getObjectMapper().writeValueAsString(this.dtoMapper.mapEntityToDto(e));
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to marshall/map from ORFIN PolicySet to json: [" + e + "], error: [" + ioe.getMessage() + "].", ioe);
		}
	}
}
