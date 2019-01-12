/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.vehiclestatus.mapper.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Deployment {

    @JsonProperty("correlation_id")
    private CorrelationId correlationId;
    
    @JsonProperty("state")
    private State state;
    
    @JsonProperty("additional_data")
    private AdditionalData additionalData;
}
