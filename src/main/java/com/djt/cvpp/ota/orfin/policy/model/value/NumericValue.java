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
public class NumericValue extends AbstractPolicyValue {
	
	private static final long serialVersionUID = 1L;
	

	private Number value;
	
	public NumericValue(Number value) {
		this(null, value);
	}
	
	public NumericValue(
		AbstractPolicy parentPolicy,
		Number value) {
		super(parentPolicy);
		this.value = value;
	}
	
	
	// BUSINESS BEHAVIORS
	public Number getPolicyValue() {
		return this.value;		
	}
}
