/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.delivery.mapper;

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
public class DeliveryRuleSetJsonConverter extends AbstractJsonConverter implements JsonConverter<com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet> {
	
	private DeliveryRuleSetDtoMapper dtoMapper = new DeliveryRuleSetDtoMapper();
	
	public com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet unmarshallFromJsonToEntity(String json) {
		try {
			com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet dto = this.getObjectMapper().readValue(json, new TypeReference<com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet>(){});
			return this.dtoMapper.mapDtoToEntity(dto);
		} catch (IOException e) {
			throw new FenixRuntimeException("Unable to unmarshall/map to ORFIN DeliveryRuleSet from json: [" + json + "], error: [" + e.getMessage() + "].", e);
		}
	}
	
	public String marshallFromEntityToJson(com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet e) {
		try {
			return this.getObjectMapper().writeValueAsString(this.dtoMapper.mapEntityToDto(e));
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to marshall/map from ORFIN DeliveryRuleSet to json: [" + e + "], error: [" + ioe.getMessage() + "].", ioe);
		}
	}
}
