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
import java.sql.Timestamp;
import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.mapper.DtoMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TmcVehicleStatusMessageDtoMapper implements DtoMapper<com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage, com.djt.cvpp.ota.tmc.vehiclestatus.mapper.dto.VehicleStatusMessage> {

	public com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage mapDtoToEntity(com.djt.cvpp.ota.tmc.vehiclestatus.mapper.dto.VehicleStatusMessage vehicleStatusMessageDto) {
		
		String deploymentId = "";
		String vehicleId = "";
		Timestamp timestamp = null;
		String vehicleStatusMessageExpression = "";
		
		// This code was lifted from the MVP VehicleStatusListener repo.
		com.djt.cvpp.ota.tmc.vehiclestatus.mapper.dto.Deployment deployment = vehicleStatusMessageDto.getFields().getDeployment();
        if (deployment != null) {

    		com.djt.cvpp.ota.tmc.vehiclestatus.mapper.dto.AdditionalData additionalData = deployment.getAdditionalData();
            if (additionalData != null) {

                String additionalDataValueJson = additionalData.getValue();
                if (additionalDataValueJson != null) {
                    String[] additionalDataValues = additionalDataValueJson.split(",");
                    try {
                    deploymentId = getAdditionalDataValue(additionalDataValues, "campaignID");
                    vehicleStatusMessageExpression = getAdditionalDataValue(additionalDataValues, "lookupCode");
                    } catch (IOException ioe) {
                    	throw new FenixRuntimeException("TMC/AU response had invalid 'additional_data' JSON: [" + additionalDataValueJson + "], error: [" + ioe.getMessage() + "].", ioe);
                    }
                }

                if (deploymentId == null || deploymentId.trim().isEmpty()) {
                    throw new FenixRuntimeException("TMC/AU response was missing campaignID.");
                }

                if (vehicleStatusMessageExpression == null || vehicleStatusMessageExpression.trim().isEmpty()) {
                    throw new FenixRuntimeException("TMC/AU response was missing lookupCode.");
                }

                vehicleId = vehicleStatusMessageDto.getVehicleId();
                if (vehicleId == null || vehicleId.trim().isEmpty()) {
                    throw new FenixRuntimeException("TMC/AU response was missing vehicleId.");
                }

                Instant timestampAsInstant = vehicleStatusMessageDto.getTimestamp();
                if (timestampAsInstant == null) {
                    throw new FenixRuntimeException("TMC/AU response was missing timestamp.");
                }
                timestamp = new Timestamp(timestampAsInstant.toEpochMilli());
            }
        }
		
		com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage vehicleStatusMessageEntity = new com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage(
			vehicleStatusMessageDto.getRequestId(),
			vehicleId,
			timestamp,
			vehicleStatusMessageExpression);
						
		return vehicleStatusMessageEntity;		
	}
	
    private String getAdditionalDataValue(String[] additionalDataValues, String fieldName) throws IOException {

    	ObjectMapper mapper = new ObjectMapper(); 
        String fieldValue = null;
        if (additionalDataValues != null) {
            for (int i=0; i < additionalDataValues.length; i++) {
                JsonNode jsonNode = mapper.readTree(additionalDataValues[i].trim());
                if (jsonNode != null) {
                    JsonNode jsonField = jsonNode.get(fieldName);
                    if (jsonField != null) {
                        fieldValue = jsonField.asText();
                        if (fieldValue != null) {
                            break;
                        }
                    }
                }
            }
        }
        return fieldValue;
    }
    
	public com.djt.cvpp.ota.tmc.vehiclestatus.mapper.dto.VehicleStatusMessage mapEntityToDto(com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage entity) {
		
		throw new UnsupportedOperationException("Mapping TMC TmcVehicle Status Message entity to DTO is not supported.");
	}
}
