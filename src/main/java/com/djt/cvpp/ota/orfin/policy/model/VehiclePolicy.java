/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.model;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.policy.model.enums.OtaFunction;
import com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType;
import com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyProgramModelYearOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyRegionOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyVehicleOverride;
import com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue;

/**
 * Example JSON that is delivered to the vehicle:
 * <pre>
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
 * </pre>
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VehiclePolicy extends AbstractPolicy {
	
	private static final long serialVersionUID = 1L;
	
	public static final String UNDEFINED_POLICY_VALUE_ERROR = "A vehicle policy whose policy value is 'UNDEFINED' cannot be created with a 'allowRegionalChangeable' value of 'FALSE'";
	
	private Boolean allowRegionalChangeable;
	private Boolean allowUserChangeable;
	private Boolean allowServiceChangeable;
	private Boolean allowCustomerFeedback;
	private String hmi;
	private String vehicleHmiFile;
	private String phone;
	private OtaFunction otaFunction;

	private VehiclePolicy(VehiclePolicyBuilder builder) throws ValidationException {
		super(
			builder.parentPolicySet,
			builder.policyName,
			builder.policyDescription,
			builder.policyValue,
			builder.policyValueType,
			builder.policyValueConstraints,
			builder.policyOverrides);
		
		this.allowRegionalChangeable = builder.allowRegionalChangeable;
		this.allowUserChangeable = builder.allowUserChangeable;
		this.allowServiceChangeable = builder.allowServiceChangeable;
		this.allowCustomerFeedback = builder.allowCustomerFeedback;
		this.hmi = builder.hmi;
		this.vehicleHmiFile = builder.vehicleHmiFile;
		this.phone = builder.phone;	
		this.otaFunction = builder.otaFunction;
		
		try {
			if (builder.allowRegionalChangeable.equals(Boolean.FALSE) && builder.policyValue.getPolicyValue().toString().equals(PolicySet.UNDEFINED)) {
				throw new ValidationException((builder.parentPolicySet + "_" + builder.policyName), UNDEFINED_POLICY_VALUE_ERROR);
			}
		} catch (NullPointerException npe) {
			System.out.println("BREAKPOINT");
		}
	}
	
	public void validate(List<String> validationMessages) {
		
		super.validate(validationMessages);
		
		validateNotNull(validationMessages, "allowRegionalChangeable", allowRegionalChangeable);
		validateNotNull(validationMessages, "allowUserChangeable", allowUserChangeable);
		validateNotNull(validationMessages, "allowServiceChangeable", allowServiceChangeable);
		validateNotNull(validationMessages, "allowCustomerFeedback", allowCustomerFeedback);
		validateNotNull(validationMessages, "otaFunction", otaFunction);
	}

	public Boolean getAllowRegionalChangeable() {
		return allowRegionalChangeable;
	}

	public Boolean getAllowUserChangeable() {
		return allowUserChangeable;
	}

	public Boolean getAllowServiceChangeable() {
		return allowServiceChangeable;
	}

	public Boolean getAllowCustomerFeedback() {
		return allowCustomerFeedback;
	}

	public String getHmi() {
		return hmi;
	}

	public String getVehicleHmiFile() {
		return vehicleHmiFile;
	}

	public String getPhone() {
		return phone;
	}

	public OtaFunction getOtaFunction() {
		return otaFunction;
	}

	public void setAllowRegionalChangeable(Boolean allowRegionalChangeable) {
		this.allowRegionalChangeable = allowRegionalChangeable;
	}

	public void setAllowUserChangeable(Boolean allowUserChangeable) {
		this.allowUserChangeable = allowUserChangeable;
	}

	public void setAllowServiceChangeable(Boolean allowServiceChangeable) {
		this.allowServiceChangeable = allowServiceChangeable;
	}

	public void setAllowCustomerFeedback(Boolean allowCustomerFeedback) {
		this.allowCustomerFeedback = allowCustomerFeedback;
	}

	public void setHmi(String hmi) {
		this.hmi = hmi;
	}

	public void setVehicleHmiFile(String vehicleHmiFile) {
		this.vehicleHmiFile = vehicleHmiFile;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setOtaFunction(OtaFunction otaFunction) {
		this.otaFunction = otaFunction;
	}
	
	public boolean addPolicyOverride(AbstractPolicyOverride policyOverride) throws EntityAlreadyExistsException, ValidationException {
		
		if (this.getPolicyOverrides().contains(policyOverride)) {
			throw new EntityAlreadyExistsException("AbstractPolicy: [" 
				+ this 
				+ "] already has a policy override with type: [" 
				+ policyOverride.getClass().getSimpleName() 
				+ "] and identity: [" 
				+ policyOverride 
				+ "].");
		}
		
		if (policyOverride instanceof PolicyVehicleOverride && this.allowServiceChangeable.equals(Boolean.FALSE) && this.allowUserChangeable.equals(Boolean.FALSE)) {
			throw new ValidationException("policyOverride", "Vehicle level overrides are not allowed because allowServiceChangeable is: [" 
				+ this.allowServiceChangeable 
				+ "] and allowUserChangeable is: [" 
				+ this.allowUserChangeable 
				+ "], but both need to be true.");	
		}

		if (policyOverride instanceof PolicyRegionOverride && this.allowRegionalChangeable.equals(Boolean.FALSE)) {
			throw new ValidationException("policyOverride", "Region level overrides are not allowed because allowRegionalChangeable is: [" 
				+ this.allowRegionalChangeable 
				+ "], and it needs to be true.");	
		}
		
		if (policyOverride instanceof PolicyProgramModelYearOverride && !this.getParentPolicySet().getPolicySetName().equals(PolicySet.GLOBAL_POLICY_SET_NAME)) {
			throw new ValidationException("policyOverride", "Only the global PolicySet can be associated with ProgramModelYear overrides.");
		}

		return super.addPolicyOverride(policyOverride);
	}
	
	public static final class VehiclePolicyBuilder {

		private PolicySet parentPolicySet;
		private String policyName;
		private String policyDescription;
		private AbstractPolicyValue policyValue;
		private Boolean allowRegionalChangeable;
		private Boolean allowUserChangeable;
		private Boolean allowServiceChangeable;
		private Boolean allowCustomerFeedback;
		private String hmi;
		private String vehicleHmiFile;
		private String phone;
		private OtaFunction otaFunction;
		private PolicyValueType policyValueType = PolicyValueType.STRING; // If STRING, then policyValue and any overrides need to be strings.
		private String policyValueConstraints = ""; // If PolicyValueType is ENUM, then constraints contain a comma separated list of enum choices.  If PolicyValueType is NUMERIC, then the constraints are the lower and upperbound. 
		private Set<AbstractPolicyOverride> policyOverrides = new TreeSet<>();

		public VehiclePolicyBuilder() {
		}
		
		public VehiclePolicyBuilder withParentPolicySet(PolicySet parentPolicySet) {
			this.parentPolicySet = parentPolicySet;
			return this;
		}
		
		public VehiclePolicyBuilder withPolicyName(String policyName) {
			this.policyName = policyName;
			return this;
		}
		
		public VehiclePolicyBuilder withPolicyDescription(String policyDescription) {
			this.policyDescription = policyDescription;
			return this;
		}
		
		public VehiclePolicyBuilder withAllowRegionalChangeable(Boolean allowRegionalChangeable) {
			this.allowRegionalChangeable = allowRegionalChangeable;
			return this;
		}

		public VehiclePolicyBuilder withAllowUserChangeable(Boolean allowUserChangeable) {
			this.allowUserChangeable = allowUserChangeable;
			return this;
		}

		public VehiclePolicyBuilder withAllowServiceChangeable(Boolean allowServiceChangeable) {
			this.allowServiceChangeable = allowServiceChangeable;
			return this;
		}

		public VehiclePolicyBuilder withAllowCustomerFeedback(Boolean allowCustomerFeedback) {
			this.allowCustomerFeedback = allowCustomerFeedback;
			return this;
		}
		
		public VehiclePolicyBuilder withPolicyValue(AbstractPolicyValue policyValue) {
			this.policyValue = policyValue;
			return this;
		}
		
		public VehiclePolicyBuilder withHmi(String hmi) {
			this.hmi = hmi;
			return this;
		}
		
		public VehiclePolicyBuilder withPhone(String phone) {
			this.phone = phone;
			return this;
		}
		
		public VehiclePolicyBuilder withVehicleHmiFile(String vehicleHmiFile) {
			this.vehicleHmiFile = vehicleHmiFile;
			return this;
		}
		
		public VehiclePolicyBuilder withOtaFunction(OtaFunction otaFunction) {
			this.otaFunction = otaFunction;
			return this;
		}
		
		public VehiclePolicyBuilder withPolicyValueType(PolicyValueType policyValueType) {
			this.policyValueType = policyValueType;
			return this;
		}
		
		public VehiclePolicyBuilder withPolicyValueConstraints(String policyValueConstraints) {
			this.policyValueConstraints = policyValueConstraints;
			return this;
		}
		
		public VehiclePolicyBuilder withPolicyOverrides(Set<AbstractPolicyOverride> policyOverrides) {
			this.policyOverrides = policyOverrides;
			return this;
		}
		
		public VehiclePolicy build() throws ValidationException {
			return new VehiclePolicy(this);
		}
	}
}
