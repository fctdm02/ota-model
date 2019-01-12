/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rule.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class RolloutRule extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private String rolloutRuleName;	
	private RolloutRegion rolloutRegion;
	private Set<RolloutRuleValue> rolloutRuleValues;
	
	public RolloutRule(
		String rolloutRuleName,
		RolloutRegion rolloutRegion,
		Set<RolloutRuleValue> rolloutRuleValues) {
		this.rolloutRuleName = rolloutRuleName;
		this.rolloutRegion = rolloutRegion;
		this.rolloutRuleValues = rolloutRuleValues;
	}

	public String getNaturalIdentity() {
		return rolloutRuleName;
	}

	public void validate(List<String> validationMessages) {
		
		validateNotNull(validationMessages, "rolloutRuleName", rolloutRuleName);
		validateNotNull(validationMessages, "rolloutRegion", rolloutRegion);

		if (rolloutRuleValues == null || rolloutRuleValues.isEmpty()) {
			validationMessages.add(this.getClassAndIdentity() + " at least one RolloutRuleValue must be specified.");
		} else {
			Iterator<RolloutRuleValue> iterator = rolloutRuleValues.iterator();
			while (iterator.hasNext()) {

				RolloutRuleValue rolloutRuleValue = iterator.next();
				rolloutRuleValue.validate(validationMessages);
			}
		}
	}
	
	public String getRolloutRuleName() {
		return rolloutRuleName;
	}

	public RolloutRegion getRolloutRegion() {
		return rolloutRegion;
	}

	public Set<RolloutRuleValue> getRolloutRuleValues() {
		return rolloutRuleValues;
	}
}
