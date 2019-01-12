/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper.dto.ecg;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "VIN",
    "ESN",
    "PolicyTable"
})
/**
 *
 * Generated from JSON from AbstractPolicy spec using http://www.jsonschema2pojo.org/
 * <pre>
{
	"VIN": "3453A",
	"ESN": "578373",
	"PolicyTable": [{
			"Name": "Level1Authorization",
			"Description": "Safety and security updates. No additional consent provided by customer",
			"Regional": "Y",
			"DefaultValue": "RegionCountryFile",
			"UserValue":"",
			"HMI": "Read/Write",
			"Phone": "None",
			"UserChangeable": "Y",
			"ServiceChangeable": "Y",
			"CustomerFeedback": "N",
			"VehicleHMIFile": "",
			"OTAFunction": "OTA MANAGER"
		},
		.
		.
		.
 }
 * </pre>
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class PolicyTableContainer {

    @JsonProperty("VIN")
    private String vIN;
    @JsonProperty("ESN")
    private String eSN;
    @JsonProperty("PolicyTable")
    private List<PolicyTable> policyTable = new ArrayList<>();

    @JsonProperty("VIN")
    public String getVIN() {
        return vIN;
    }

    @JsonProperty("VIN")
    public void setVIN(String vIN) {
        this.vIN = vIN;
    }

    @JsonProperty("ESN")
    public String getESN() {
        return eSN;
    }

    @JsonProperty("ESN")
    public void setESN(String eSN) {
        this.eSN = eSN;
    }

    @JsonProperty("PolicyTable")
    public List<PolicyTable> getPolicyTable() {
        return policyTable;
    }

    @JsonProperty("PolicyTable")
    public void setPolicyTable(List<PolicyTable> policyTable) {
        this.policyTable = policyTable;
    }
}
