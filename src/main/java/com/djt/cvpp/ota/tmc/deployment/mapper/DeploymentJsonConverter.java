/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.deployment.mapper;

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
public class DeploymentJsonConverter extends AbstractJsonConverter implements JsonConverter<com.djt.cvpp.ota.tmc.deployment.model.Deployment> {
	
	private DeploymentDtoMapper dtoMapper = new DeploymentDtoMapper();
	
	public com.djt.cvpp.ota.tmc.deployment.model.Deployment unmarshallFromJsonToEntity(String json) {
		try {
			com.djt.cvpp.ota.tmc.deployment.mapper.dto.Deployment dto = this.getObjectMapper().readValue(json, new TypeReference<com.djt.cvpp.ota.tmc.deployment.mapper.dto.Deployment>(){});
			return this.dtoMapper.mapDtoToEntity(dto);
		} catch (IOException e) {
			throw new FenixRuntimeException("Unable to unmarshall/map to TMC Deployment from json: [" + json + "], error: [" + e.getMessage() + "].", e);
		}
	}
	
	public String marshallFromEntityToJson(com.djt.cvpp.ota.tmc.deployment.model.Deployment e) {
		try {
			return this.getObjectMapper().writeValueAsString(this.dtoMapper.mapEntityToDto(e));
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to marshall/map from TMC Deployment to json: [" + e + "], error: [" + ioe.getMessage() + "].", ioe);
		}
	}
}
