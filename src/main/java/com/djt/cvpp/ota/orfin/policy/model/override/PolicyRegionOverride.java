/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.model.override;

import java.util.List;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy;
import com.djt.cvpp.ota.orfin.policy.model.region.Region;
import com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue;


/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class PolicyRegionOverride extends AbstractPolicyOverride {

	private static final long serialVersionUID = 1L;

	
	private Region region;
	
	private PolicyRegionOverride(PolicyRegionOverrideBuilder builder) throws ValidationException {
		
		super(builder.parentPolicy, builder.policyOverrideValue);
		this.region = builder.region;
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			this.getParentPolicy(),
			region
		);
	}

	public void validate(List<String> validationMessages) {

		super.validate(validationMessages);
		if (region == null) {
			validationMessages.add("region must be specified.");
		}
	}
	
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
	
	public static final class PolicyRegionOverrideBuilder {

		private AbstractPolicy parentPolicy;
		private Region region;
		private AbstractPolicyValue policyOverrideValue;

		public PolicyRegionOverrideBuilder() {
		}
		
		public PolicyRegionOverrideBuilder withParentPolicy(AbstractPolicy parentPolicy) {
			this.parentPolicy = parentPolicy;
			return this;
		}
				
		public PolicyRegionOverrideBuilder withRegion(Region region) {
			this.region = region;
			return this;
		}
		
		public PolicyRegionOverrideBuilder withPolicyOverrideValue(AbstractPolicyValue policyOverrideValue) {
			this.policyOverrideValue = policyOverrideValue;
			return this;
		}
		
		public PolicyRegionOverride build() throws ValidationException {
			return new PolicyRegionOverride(this);
		}
	}	
}
