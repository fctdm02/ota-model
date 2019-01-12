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
    "domainName",
    "domainDescription",
    "ecuNodes",
    "domainInstances",
    "domainDependencies",
    "validationWarnings"
})
/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 * Generated using http://www.jsonschema2pojo.org/
 */
public class Domain {

    @JsonProperty("domainName")
    private String domainName;
    @JsonProperty("domainDescription")
    private String domainDescription;
    @JsonProperty("ecuNodes")
    private List<EcuNode> ecuNodes = new ArrayList<>();
    @JsonProperty("domainInstances")
    private List<DomainInstance> domainInstances = new ArrayList<>();
    @JsonProperty("domainDependencies")
    private List<Domain> domainDependencies = new ArrayList<>();
    @JsonProperty("validationWarnings")
    private List<String> validationWarnings = new ArrayList<>();

    @JsonProperty("domainName")
    public String getDomainName() {
        return domainName;
    }

    @JsonProperty("domainName")
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @JsonProperty("domainDescription")
    public String getDomainDescription() {
        return domainDescription;
    }

    @JsonProperty("domainDescription")
    public void setDomainDescription(String domainDescription) {
        this.domainDescription = domainDescription;
    }

    @JsonProperty("ecuNodes")
    public List<EcuNode> getEcuNodes() {
        return ecuNodes;
    }

    @JsonProperty("ecuNodes")
    public void setEcuNodes(List<EcuNode> ecuNodes) {
        this.ecuNodes = ecuNodes;
    }

    @JsonProperty("domainInstances")
    public List<DomainInstance> getDomainInstances() {
        return domainInstances;
    }

    @JsonProperty("domainInstances")
    public void setDomainInstances(List<DomainInstance> domainInstances) {
        this.domainInstances = domainInstances;
    }

    @JsonProperty("domainDependencies")
    public List<Domain> getDomainDependencies() {
        return domainDependencies;
    }

    @JsonProperty("domainDependencies")
    public void setDomainDependencies(List<Domain> domainDependencies) {
        this.domainDependencies = domainDependencies;
    }

    @JsonProperty("validationWarnings")
    public List<String> getValidationWarnings() {
        return validationWarnings;
    }

    @JsonProperty("validationWarnings")
    public void setValidationWarnings(List<String> validationWarnings) {
        this.validationWarnings = validationWarnings;
    }

}
