/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.model.value;

import com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class BooleanValue extends AbstractPolicyValue {
	
	private static final long serialVersionUID = 1L;
	

	private Boolean value;
	
	public BooleanValue(Boolean value) {
		this(null, value);
	}
	
	public BooleanValue(
		AbstractPolicy parentPolicy,	
		Boolean value) {
		super(parentPolicy);
		this.value = value;
	}
	
	
	// BUSINESS BEHAVIORS
	public Boolean getPolicyValue() {
		return this.value;		
	}
}
