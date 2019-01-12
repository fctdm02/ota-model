/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper.dto.ecg;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Name",
    "Description",
    "Regional",
    "DefaultValue",
    "UserValue",
    "HMI",
    "Phone",
    "UserChangeable",
    "ServiceChangeable",
    "CustomerFeedback",
    "VehicleHMIFile",
    "OTAFunction",
    "Type"
})
/**
 * 
 * Generated from JSON from AbstractPolicy spec using http://www.jsonschema2pojo.org/
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class PolicyTable {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Regional")
    private String regional;
    @JsonProperty("DefaultValue")
    private String defaultValue;
    @JsonProperty("UserValue")
    private String userValue;
    @JsonProperty("HMI")
    private String hMI;
    @JsonProperty("Phone")
    private String phone;
    @JsonProperty("UserChangeable")
    private String userChangeable;
    @JsonProperty("ServiceChangeable")
    private String serviceChangeable;
    @JsonProperty("CustomerFeedback")
    private String customerFeedback;
    @JsonProperty("VehicleHMIFile")
    private String vehicleHMIFile;
    @JsonProperty("OTAFunction")
    private String oTAFunction;
    @JsonProperty("Type")
    private String type;

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("Regional")
    public String getRegional() {
        return regional;
    }

    @JsonProperty("Regional")
    public void setRegional(String regional) {
        this.regional = regional;
    }

    @JsonProperty("DefaultValue")
    public String getDefaultValue() {
        return defaultValue;
    }

    @JsonProperty("DefaultValue")
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @JsonProperty("UserValue")
    public String getUserValue() {
        return userValue;
    }

    @JsonProperty("UserValue")
    public void setUserValue(String userValue) {
        this.userValue = userValue;
    }

    @JsonProperty("HMI")
    public String getHMI() {
        return hMI;
    }

    @JsonProperty("HMI")
    public void setHMI(String hMI) {
        this.hMI = hMI;
    }

    @JsonProperty("Phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("Phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("UserChangeable")
    public String getUserChangeable() {
        return userChangeable;
    }

    @JsonProperty("UserChangeable")
    public void setUserChangeable(String userChangeable) {
        this.userChangeable = userChangeable;
    }

    @JsonProperty("ServiceChangeable")
    public String getServiceChangeable() {
        return serviceChangeable;
    }

    @JsonProperty("ServiceChangeable")
    public void setServiceChangeable(String serviceChangeable) {
        this.serviceChangeable = serviceChangeable;
    }

    @JsonProperty("CustomerFeedback")
    public String getCustomerFeedback() {
        return customerFeedback;
    }

    @JsonProperty("CustomerFeedback")
    public void setCustomerFeedback(String customerFeedback) {
        this.customerFeedback = customerFeedback;
    }

    @JsonProperty("VehicleHMIFile")
    public String getVehicleHMIFile() {
        return vehicleHMIFile;
    }

    @JsonProperty("VehicleHMIFile")
    public void setVehicleHMIFile(String vehicleHMIFile) {
        this.vehicleHMIFile = vehicleHMIFile;
    }

    @JsonProperty("OTAFunction")
    public String getOTAFunction() {
        return oTAFunction;
    }

    @JsonProperty("OTAFunction")
    public void setOTAFunction(String oTAFunction) {
        this.oTAFunction = oTAFunction;
    }

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

}
