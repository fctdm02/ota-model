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

import com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public class EnumValue extends StringValue {
	
	private static final long serialVersionUID = 1L;
	

	private EnumChoice parentEnumChoice;
	
	public EnumValue(
		AbstractPolicy parentPolicy,
		String value,
		EnumChoice parentEnumChoice) {
		super(
			parentPolicy, 
			value);
		this.parentEnumChoice = parentEnumChoice;
	}
	
	public void validate(List<String> validationMessages) {

		super.validate(validationMessages);
		validateNotNull(validationMessages, "parentEnumChoice", parentEnumChoice);
	}
	
	public EnumChoice getParentEnumChoice() {
		return this.parentEnumChoice;
	}	
}
