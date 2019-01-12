/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.mapper.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "appId",
    "appName",
    "appVersion",
    "region",
    "description",
    "ownerGroup",
    "ownerKeyContact",
    "requiresSubscription",
    "applicationNecessity",
    "releaseState",
    "applicationDependencies",
    "binaryMetadatas"
})
/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 * Generated using http://www.jsonschema2pojo.org/
 */
public class Application {

    @JsonProperty("appId")
    private String appId;
    @JsonProperty("appName")
    private String appName;
    @JsonProperty("appVersion")
    private String appVersion;
    @JsonProperty("region")
    private String region;
    @JsonProperty("description")
    private String description;
    @JsonProperty("ownerGroup")
    private String ownerGroup;
    @JsonProperty("ownerKeyContact")
    private String ownerKeyContact;
    @JsonProperty("requiresSubscription")
    private Boolean requiresSubscription;
    @JsonProperty("applicationNecessity")
    private String applicationNecessity;
    @JsonProperty("releaseState")
    private String releaseState;
    @JsonProperty("applicationDependencies")
    private List<Application> applicationDependencies = new ArrayList<>();
    @JsonProperty("binaryMetadatas")
    private List<BinaryMetadata> binaryMetadatas = new ArrayList<>();

    @JsonProperty("appId")
    public String getAppId() {
        return appId;
    }

    @JsonProperty("appId")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @JsonProperty("appName")
    public String getAppName() {
        return appName;
    }

    @JsonProperty("appName")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @JsonProperty("appVersion")
    public String getAppVersion() {
        return appVersion;
    }

    @JsonProperty("appVersion")
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("ownerGroup")
    public String getOwnerGroup() {
        return ownerGroup;
    }

    @JsonProperty("ownerGroup")
    public void setOwnerGroup(String ownerGroup) {
        this.ownerGroup = ownerGroup;
    }

    @JsonProperty("ownerKeyContact")
    public String getOwnerKeyContact() {
        return ownerKeyContact;
    }

    @JsonProperty("ownerKeyContact")
    public void setOwnerKeyContact(String ownerKeyContact) {
        this.ownerKeyContact = ownerKeyContact;
    }

    @JsonProperty("requiresSubscription")
    public Boolean getRequiresSubscription() {
        return requiresSubscription;
    }

    @JsonProperty("requiresSubscription")
    public void setRequiresSubscription(Boolean requiresSubscription) {
        this.requiresSubscription = requiresSubscription;
    }

    @JsonProperty("applicationNecessity")
    public String getApplicationNecessity() {
        return applicationNecessity;
    }

    @JsonProperty("applicationNecessity")
    public void setApplicationNecessity(String applicationNecessity) {
        this.applicationNecessity = applicationNecessity;
    }

    @JsonProperty("releaseState")
    public String getReleaseState() {
        return releaseState;
    }

    @JsonProperty("releaseState")
    public void setReleaseState(String releaseState) {
        this.releaseState = releaseState;
    }

    @JsonProperty("applicationDependencies")
    public List<Application> getApplicationDependencies() {
        return applicationDependencies;
    }

    @JsonProperty("applicationDependencies")
    public void setApplicationDependencies(List<Application> applicationDependencies) {
        this.applicationDependencies = applicationDependencies;
    }

    @JsonProperty("binaryMetadatas")
    public List<BinaryMetadata> getBinaryMetadatas() {
        return binaryMetadatas;
    }

    @JsonProperty("binaryMetadatas")
    public void setBinaryMetadatas(List<BinaryMetadata> binaryMetadatas) {
        this.binaryMetadatas = binaryMetadatas;
    }

}
