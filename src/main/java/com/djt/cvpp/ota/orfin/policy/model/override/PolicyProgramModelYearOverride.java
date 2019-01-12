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
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;


/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class PolicyProgramModelYearOverride extends AbstractPolicyOverride {

	private static final long serialVersionUID = 1L;

	private ProgramModelYear programModelYear;
	
	private PolicyProgramModelYearOverride(PolicyProgramModelYearOverrideBuilder builder) throws ValidationException {
		
		super(builder.parentPolicy, builder.policyOverrideValue);
		this.programModelYear = builder.programModelYear;
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			this.getParentPolicy(),
			programModelYear
		);
	}

	public void validate(List<String> validationMessages) {

		super.validate(validationMessages);
		if (programModelYear == null) {
			validationMessages.add("programModelYear must be specified.");
		}
	}
	
	public ProgramModelYear getProgramModelYear() {
		return programModelYear;
	}

	public void setProgramModelYear(ProgramModelYear programModelYear) {
		this.programModelYear = programModelYear;
	}
	
	public static final class PolicyProgramModelYearOverrideBuilder {

		private AbstractPolicy parentPolicy;
		private ProgramModelYear programModelYear;
		private AbstractPolicyValue policyOverrideValue;

		public PolicyProgramModelYearOverrideBuilder() {
		}
		
		public PolicyProgramModelYearOverrideBuilder withParentPolicy(AbstractPolicy parentPolicy) {
			this.parentPolicy = parentPolicy;
			return this;
		}
				
		public PolicyProgramModelYearOverrideBuilder withProgramModelYear(ProgramModelYear programModelYear) {
			this.programModelYear = programModelYear;
			return this;
		}
		
		public PolicyProgramModelYearOverrideBuilder withPolicyOverrideValue(AbstractPolicyValue policyOverrideValue) {
			this.policyOverrideValue = policyOverrideValue;
			return this;
		}
		
		public PolicyProgramModelYearOverride build() throws ValidationException {
			return new PolicyProgramModelYearOverride(this);
		}
	}	
}
