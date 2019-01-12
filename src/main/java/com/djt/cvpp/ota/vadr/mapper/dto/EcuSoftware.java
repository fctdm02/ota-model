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
    "ecuLogicalAddress",
    "ecuSoftwarePartNumber",
    "softwareName",
    "softwareVersionNumber",
    "category",
    "description",
    "binaryMetadata"
})
/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 * Generated using http://www.jsonschema2pojo.org/
 */
public class EcuSoftware {

    @JsonProperty("ecuAcronymName")
    private String ecuAcronymName;
    @JsonProperty("ecuLogicalAddress")
    private String ecuLogicalAddress;
    @JsonProperty("ecuSoftwarePartNumber")
    private String ecuSoftwarePartNumber;
    @JsonProperty("softwareName")
    private String softwareName;
    @JsonProperty("softwareVersionNumber")
    private String softwareVersionNumber;
    @JsonProperty("category")
    private String category;
    @JsonProperty("description")
    private String description;
    @JsonProperty("binaryMetadata")
    private BinaryMetadata binaryMetadata;

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

    @JsonProperty("ecuSoftwarePartNumber")
    public String getEcuSoftwarePartNumber() {
        return ecuSoftwarePartNumber;
    }

    @JsonProperty("ecuSoftwarePartNumber")
    public void setEcuSoftwarePartNumber(String ecuSoftwarePartNumber) {
        this.ecuSoftwarePartNumber = ecuSoftwarePartNumber;
    }

    @JsonProperty("softwareName")
    public String getSoftwareName() {
        return softwareName;
    }

    @JsonProperty("softwareName")
    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    @JsonProperty("softwareVersionNumber")
    public String getSoftwareVersionNumber() {
        return softwareVersionNumber;
    }

    @JsonProperty("softwareVersionNumber")
    public void setSoftwareVersionNumber(String softwareVersionNumber) {
        this.softwareVersionNumber = softwareVersionNumber;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("binaryMetadata")
    public BinaryMetadata getBinaryMetadata() {
        return binaryMetadata;
    }

    @JsonProperty("binaryMetadata")
    public void setBinaryMetadata(BinaryMetadata binaryMetadata) {
        this.binaryMetadata = binaryMetadata;
    }

}
