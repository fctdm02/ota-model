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
import com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue;
import com.djt.cvpp.ota.orfin.policy.model.vehicle.Vehicle;


/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class PolicyVehicleOverride extends AbstractPolicyOverride {

	private static final long serialVersionUID = 1L;

	
	private Vehicle vehicle;
	
	private PolicyVehicleOverride(PolicyVehicleOverrideBuilder builder) throws ValidationException {
		
		super(builder.parentPolicy, builder.policyOverrideValue);
		this.vehicle = builder.vehicle;
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			this.getParentPolicy(),
			vehicle
		);
	}

	public void validate(List<String> validationMessages) {

		super.validate(validationMessages);
		if (vehicle == null) {
			validationMessages.add("vehicle must be specified.");
		}
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public static final class PolicyVehicleOverrideBuilder {

		private AbstractPolicy parentPolicy;
		private Vehicle vehicle;
		private AbstractPolicyValue policyOverrideValue;

		public PolicyVehicleOverrideBuilder() {
		}
		
		public PolicyVehicleOverrideBuilder withParentPolicy(AbstractPolicy parentPolicy) {
			this.parentPolicy = parentPolicy;
			return this;
		}
				
		public PolicyVehicleOverrideBuilder withVehicle(Vehicle vehicle) {
			this.vehicle = vehicle;
			return this;
		}
		
		public PolicyVehicleOverrideBuilder withPolicyOverrideValue(AbstractPolicyValue policyOverrideValue) {
			this.policyOverrideValue = policyOverrideValue;
			return this;
		}
		
		public PolicyVehicleOverride build() throws ValidationException {
			return new PolicyVehicleOverride(this);
		}
	}	
}
