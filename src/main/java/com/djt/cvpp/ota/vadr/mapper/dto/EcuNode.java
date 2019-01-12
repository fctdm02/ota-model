/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.mapper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ecuAcronymName",
    "ecuLogicalAddress"
})
/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 * Generated using http://www.jsonschema2pojo.org/
 */
public class EcuNode {

    @JsonProperty("ecuAcronymName")
    private String ecuAcronymName;
    @JsonProperty("ecuLogicalAddress")
    private String ecuLogicalAddress;

    @JsonProperty("ecuAcronymName")
    public String getEcuAcronymName() {
        return ecuAcronymName;
    }

    @JsonProperty("ecuAcronymName")
    public void setEcuAcronymName(String ecuAcronymName) {
        this.ecuAcronymName = ecuAcronymName;
    }

    @JsonProperty("ecuLogicalAddress")
    public String getEcuLogicalAddress() {
        return ecuLogicalAddress;
    }

    @JsonProperty("ecuLogicalAddress")
    public void setEcuLogicalAddress(String ecuLogicalAddress) {
        this.ecuLogicalAddress = ecuLogicalAddress;
    }

}
