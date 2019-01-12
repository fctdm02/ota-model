/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.model;

import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType;
import com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride;
import com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class CloudPolicy extends AbstractPolicy {
	
	private static final long serialVersionUID = 1L;

	
	private CloudPolicy(CloudPolicyBuilder builder) throws ValidationException {
		super(
			builder.parentPolicySet,
			builder.policyName,
			builder.policyDescription,
			builder.policyValue,
			builder.policyValueType,
			builder.policyValueConstraints,
			builder.policyOverrides);
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
		
		// TODO: TDM: Add in whatever special business logic is needed for cloud policy overrides here.

		return super.addPolicyOverride(policyOverride);
	}
	
	public static final class CloudPolicyBuilder {

		private PolicySet parentPolicySet;
		private String policyName;
		private String policyDescription;
		private AbstractPolicyValue policyValue;
		private PolicyValueType policyValueType = PolicyValueType.STRING; // If STRING, then policyValue and any overrides need to be strings.
		private String policyValueConstraints = ""; // If PolicyValueType is ENUM, then constraints contain a comma separated list of enum choices.  If PolicyValueType is NUMERIC, then the constraints are the lower and upperbound. 
		private Set<AbstractPolicyOverride> policyOverrides = new TreeSet<>();

		public CloudPolicyBuilder() {
		}
		
		public CloudPolicyBuilder withParentPolicySet(PolicySet parentPolicySet) {
			this.parentPolicySet = parentPolicySet;
			return this;
		}
		
		public CloudPolicyBuilder withPolicyName(String policyName) {
			this.policyName = policyName;
			return this;
		}
		
		public CloudPolicyBuilder withPolicyDescription(String policyDescription) {
			this.policyDescription = policyDescription;
			return this;
		}
		
		public CloudPolicyBuilder withPolicyValue(AbstractPolicyValue policyValue) {
			this.policyValue = policyValue;
			return this;
		}
				
		public CloudPolicyBuilder withPolicyValueType(PolicyValueType policyValueType) {
			this.policyValueType = policyValueType;
			return this;
		}
		
		public CloudPolicyBuilder withPolicyValueConstraints(String policyValueConstraints) {
			this.policyValueConstraints = policyValueConstraints;
			return this;
		}
		
		public CloudPolicyBuilder withPolicyOverrides(Set<AbstractPolicyOverride> policyOverrides) {
			this.policyOverrides = policyOverrides;
			return this;
		}
		
		public CloudPolicy build() throws ValidationException {
			return new CloudPolicy(this);
		}
	}	
}
