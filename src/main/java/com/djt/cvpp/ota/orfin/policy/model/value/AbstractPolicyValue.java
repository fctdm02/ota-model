/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.model.value;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractPolicyValue extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	
	private AbstractPolicy parentPolicy;
	
	protected AbstractPolicyValue(AbstractPolicy parentPolicy) {
		this.parentPolicy = parentPolicy;
	}
		
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentPolicy,
			getPolicyValue()
		);
	}
	
	public void setParentPolicy(AbstractPolicy parentPolicy) {
		this.parentPolicy = parentPolicy;
	}

	public void validate(List<String> validationMessages) {

		validateNotNull(validationMessages, "parentPolicy", parentPolicy);
		validateNotNull(validationMessages, "policyValue", getPolicyValue());
	}

	
	// BUSINESS BEHAVIORS
	public abstract Object getPolicyValue();	
}
