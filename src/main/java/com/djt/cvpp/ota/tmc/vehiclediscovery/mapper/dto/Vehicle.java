/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Vehicle {

    private String modified;
    private String created;
    private Tags tags;
    
    @JsonProperty("tenant_id")
    private String tenantId;
    
    private String name;
    private String id;
    private String type;
    private String year;
    private Pid pid;
    private String model;
    private String vin;
    
    @JsonProperty("fleet_id")
    private String fleetId;
    
    private List<Module> modules = new ArrayList<>();
    private String make;
    private String trim;
}
