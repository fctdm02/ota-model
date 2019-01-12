/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.vehiclestatus.mapper.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VehicleStatusMessage {

    @JsonProperty("request_id")
    private String requestId;
    
    @JsonProperty("vehicle_id")
    private String vehicleId;
    
    private Instant timestamp;
    
    private Fields fields;
}
