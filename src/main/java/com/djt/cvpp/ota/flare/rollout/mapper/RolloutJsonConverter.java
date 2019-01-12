/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.rollout.mapper;

import java.io.IOException;

import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.mapper.JsonConverter;
import com.djt.cvpp.ota.common.mapper.impl.AbstractJsonConverter;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class RolloutJsonConverter extends AbstractJsonConverter implements JsonConverter<com.djt.cvpp.ota.flare.rollout.model.AbstractRollout> {
	
	private RolloutDtoMapper rolloutDtoMapper = new RolloutDtoMapper();
	
	public com.djt.cvpp.ota.flare.rollout.model.AbstractRollout unmarshallFromJsonToEntity(String json) {
		throw new UnsupportedOperationException("Converting from JSON to AbstractRollout are not supported.");
	}
	
	public String marshallFromEntityToJson(com.djt.cvpp.ota.flare.rollout.model.AbstractRollout e) {
		try {
			return this.getObjectMapper().writeValueAsString(this.rolloutDtoMapper.mapEntityToDto(e));
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to marshall/map from AbstractRollout to json: [" + e + "], error: [" + ioe.getMessage() + "].", ioe);
		}
	}
}
