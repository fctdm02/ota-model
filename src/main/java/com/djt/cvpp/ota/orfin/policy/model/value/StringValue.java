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
public class StringValue extends AbstractPolicyValue {
	
	private static final long serialVersionUID = 1L;
	

	private String value;
	
	public StringValue(String value) {
		this(null, value);
	}
	
	public StringValue(
		AbstractPolicy parentPolicy,
		String value) {
		super(parentPolicy);
		this.value = value;
	}
		
	// BUSINESS BEHAVIORS
	public String getPolicyValue() {
		return this.value;		
	}
}
