/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.delivery.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class ComplexCondition extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private DeliveryRuleSet parentDeliveryRuleSet;
	private String complexConditionName;
	private String complexConditionValue; 
	
	private ComplexCondition(ComplexConditionBuilder builder) {
		
		this.parentDeliveryRuleSet = builder.parentDeliveryRuleSet;
		this.complexConditionName = builder.complexConditionName;
		this.complexConditionValue = builder.complexConditionValue;
	}
	
	public String getNaturalIdentity() {

		StringBuilder sb = new StringBuilder();
		sb.append(this.parentDeliveryRuleSet);
		sb.append(NATURAL_IDENTITY_DELIMITER);
		sb.append(this.complexConditionName);
		return sb.toString();
	}

	public void validate(List<String> validationMessages) {

		if (parentDeliveryRuleSet == null) {
			validationMessages.add("parentDeliveryRuleSet must be specified.");
		}
		
		if (complexConditionName == null || complexConditionName.trim().isEmpty()) {
			validationMessages.add("complexConditionName must be specified.");
		}
		
		if (complexConditionValue == null || complexConditionValue.trim().isEmpty()) {
			validationMessages.add("complexConditionValue must be specified.");
		}
	}

	public DeliveryRuleSet getParentDeliveryRuleSet() {
		return parentDeliveryRuleSet;
	}

	public String getComplexConditionName() {
		return complexConditionName;
	}

	public String getComplexConditionValue() {
		return complexConditionValue;
	}
	
	protected void setParentDeliveryRuleSet(DeliveryRuleSet parentDeliveryRuleSet) {
		this.parentDeliveryRuleSet = parentDeliveryRuleSet;
	}

	public static final class ComplexConditionBuilder {

		private DeliveryRuleSet parentDeliveryRuleSet;
		private String complexConditionName;
		private String complexConditionValue; 

		public ComplexConditionBuilder() {
		}
		
		public ComplexConditionBuilder withParentDeliveryRuleSet(DeliveryRuleSet parentDeliveryRuleSet) {
			this.parentDeliveryRuleSet = parentDeliveryRuleSet;
			return this;
		}
		
		public ComplexConditionBuilder withComplexConditionName(String complexConditionName) {
			this.complexConditionName = complexConditionName;
			return this;
		}

		public ComplexConditionBuilder withComplexConditionValue(String complexConditionValue) {
			this.complexConditionValue = complexConditionValue;
			return this;
		}
		
		public ComplexCondition build() {
			return new ComplexCondition(this);
		}
	}
}
