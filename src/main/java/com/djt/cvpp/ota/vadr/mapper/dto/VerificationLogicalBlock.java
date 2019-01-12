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
    "startAddress",
    "length",
    "rootHash"
})
/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 * Generated using http://www.jsonschema2pojo.org/
 */
public class VerificationLogicalBlock {

    @JsonProperty("startAddress")
    private String startAddress;
    @JsonProperty("length")
    private String length;
    @JsonProperty("rootHash")
    private String rootHash;

    @JsonProperty("startAddress")
    public String getStartAddress() {
        return startAddress;
    }

    @JsonProperty("startAddress")
    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    @JsonProperty("length")
    public String getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(String length) {
        this.length = length;
    }

    @JsonProperty("rootHash")
    public String getRootHash() {
        return rootHash;
    }

    @JsonProperty("rootHash")
    public void setRootHash(String rootHash) {
        this.rootHash = rootHash;
    }

}
