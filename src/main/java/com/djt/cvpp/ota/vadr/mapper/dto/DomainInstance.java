/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.mapper.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "domainInstanceName",
    "domainInstanceVersion",
    "domainInstanceDescription",
    "releasedDate",
    "releaseState",
    "productionState",
    "ecuHardwares",
    "ecuSoftwares",
    "applications"
})
/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 * Generated using http://www.jsonschema2pojo.org/
 */
public class DomainInstance {

    @JsonProperty("domainInstanceName")
    private String domainInstanceName;
    @JsonProperty("domainInstanceVersion")
    private String domainInstanceVersion;
    @JsonProperty("domainInstanceDescription")
    private String domainInstanceDescription;
    @JsonProperty("releasedDate")
    private Timestamp releasedDate;
    @JsonProperty("releaseState")
    private String releaseState;
    @JsonProperty("productionState")
    private String productionState;
    @JsonProperty("ecuHardwares")
    private List<EcuHardware> ecuHardwares = new ArrayList<>();
    @JsonProperty("ecuSoftwares")
    private List<EcuSoftware> ecuSoftwares = new ArrayList<>();
    @JsonProperty("applications")
    private List<Application> applications = new ArrayList<>();

    @JsonProperty("domainInstanceName")
    public String getDomainInstanceName() {
        return domainInstanceName;
    }

    @JsonProperty("domainInstanceName")
    public void setDomainInstanceName(String domainInstanceName) {
        this.domainInstanceName = domainInstanceName;
    }

    @JsonProperty("domainInstanceVersion")
    public String getDomainInstanceVersion() {
        return domainInstanceVersion;
    }

    @JsonProperty("domainInstanceVersion")
    public void setDomainInstanceVersion(String domainInstanceVersion) {
        this.domainInstanceVersion = domainInstanceVersion;
    }

    @JsonProperty("domainInstanceDescription")
    public String getDomainInstanceDescription() {
        return domainInstanceDescription;
    }

    @JsonProperty("domainInstanceDescription")
    public void setDomainInstanceDescription(String domainInstanceDescription) {
        this.domainInstanceDescription = domainInstanceDescription;
    }

    @JsonProperty("releasedDate")
    public Timestamp getReleasedDate() {
        return new Timestamp(releasedDate.getTime());
    }

    @JsonProperty("releasedDate")
    public void setReleasedDate(Timestamp releasedDate) {
        this.releasedDate = new Timestamp(releasedDate.getTime());
    }

    @JsonProperty("releaseState")
    public String getReleaseState() {
        return releaseState;
    }

    @JsonProperty("releaseState")
    public void setReleaseState(String releaseState) {
        this.releaseState = releaseState;
    }

    @JsonProperty("productionState")
    public String getProductionState() {
        return productionState;
    }

    @JsonProperty("productionState")
    public void setProductionState(String productionState) {
        this.productionState = productionState;
    }

    @JsonProperty("ecuHardwares")
    public List<EcuHardware> getEcuHardwares() {
        return ecuHardwares;
    }

    @JsonProperty("ecuHardwares")
    public void setEcuHardwares(List<EcuHardware> ecuHardwares) {
        this.ecuHardwares = ecuHardwares;
    }

    @JsonProperty("ecuSoftwares")
    public List<EcuSoftware> getEcuSoftwares() {
        return ecuSoftwares;
    }

    @JsonProperty("ecuSoftwares")
    public void setEcuSoftwares(List<EcuSoftware> ecuSoftwares) {
        this.ecuSoftwares = ecuSoftwares;
    }

    @JsonProperty("applications")
    public List<Application> getApplications() {
        return applications;
    }

    @JsonProperty("applications")
    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

}
