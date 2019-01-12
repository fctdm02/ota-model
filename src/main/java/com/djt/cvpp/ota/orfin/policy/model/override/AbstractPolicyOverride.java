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

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractPolicyOverride extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	public static final String PARENT_POLICY_ERROR = "parentPolicy must be specified.";
	public static final String POLICY_OVERRIDE_ERROR = "parentPolicy must be specified.";

	private AbstractPolicy parentPolicy;
	private AbstractPolicyValue policyOverrideValue;
	
	public AbstractPolicyOverride(
		AbstractPolicy parentPolicy,	
		AbstractPolicyValue policyOverrideValue) 
	throws 
		ValidationException {
		
		if (parentPolicy == null) {
			throw new ValidationException(this.getClassAndIdentity(), PARENT_POLICY_ERROR);
		}
		
		if (policyOverrideValue == null) {
			throw new ValidationException(this.getClassAndIdentity(), POLICY_OVERRIDE_ERROR);
		}
		
		this.parentPolicy = parentPolicy;
		this.policyOverrideValue = policyOverrideValue;
	}
	
	public void validate(List<String> validationMessages) {

		if (parentPolicy == null) {
			validationMessages.add(PARENT_POLICY_ERROR);
		}
		
		if (policyOverrideValue == null) {
			validationMessages.add(POLICY_OVERRIDE_ERROR);
		}
	}
	
	public AbstractPolicyValue getPolicyOverrideValue() {
		return this.policyOverrideValue;
	}
	
	public void setPolicyOverrideValue(AbstractPolicyValue policyOverrideValue) {
		this.policyOverrideValue = policyOverrideValue;
	}
	
	public AbstractPolicy getParentPolicy() {
		return parentPolicy;
	}
	
	public void setParentPolicy(AbstractPolicy parentPolicy) {
		this.parentPolicy = parentPolicy;
	}
}
