/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rule.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class RolloutRuleValue extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private Integer rolloutRuleValue;
	
	public RolloutRuleValue(Integer rolloutRuleValue) {
		this.rolloutRuleValue = rolloutRuleValue;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			rolloutRuleValue
		);
	}

	public void validate(List<String> validationMessages) {
		
		validateNotNull(validationMessages, "rolloutRuleValue", rolloutRuleValue);
	}

	public Integer getRolloutRuleValue() {
		return rolloutRuleValue;
	}
}
