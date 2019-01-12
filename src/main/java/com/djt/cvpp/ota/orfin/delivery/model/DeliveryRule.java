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
import com.djt.cvpp.ota.orfin.delivery.model.enums.ConnectionType;
import com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryAudience;
import com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryMethod;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class DeliveryRule extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private DeliveryRuleSet parentDeliveryRuleSet;
	private Boolean allowable;
	private Integer precedenceLevel;
	private DeliveryAudience deliveryAudience;
	private DeliveryMethod deliveryMethod;
	private ConnectionType connectionType;
	
	
	private DeliveryRule(DeliveryRuleBuilder builder) {
	
		this.parentDeliveryRuleSet = builder.parentDeliveryRuleSet;
		this.allowable = builder.allowable;
		this.precedenceLevel = builder.precedenceLevel;
		this.deliveryAudience = builder.deliveryAudience;
		this.deliveryMethod = builder.deliveryMethod;
		this.connectionType = builder.connectionType;
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentDeliveryRuleSet,
			deliveryAudience,
			deliveryMethod,
			connectionType
		);
	}

	public void validate(List<String> validationMessages) {

		if (parentDeliveryRuleSet == null) {
			validationMessages.add("parentDeliveryRuleSet must be specified.");
		}

		if (allowable == null) {
			validationMessages.add("allowable must be specified.");
		}

		if (precedenceLevel == null || precedenceLevel.intValue() < 0) {
			validationMessages.add("precedenceLevel must be a positive integer");
		}
		
		if (deliveryAudience == null) {
			validationMessages.add("deliveryAudience must be specified.");
		}
		
		if (deliveryMethod == null) {
			validationMessages.add("deliveryMethod must be specified.");
		}
		
		if (connectionType == null) {
			validationMessages.add("connectionType must be specified.");
		}
	}
	
	public void setParentDeliveryRuleSet(DeliveryRuleSet parentDeliveryRuleSet) {
		this.parentDeliveryRuleSet = parentDeliveryRuleSet;
	}
	
	public DeliveryRuleSet getParentDeliveryRuleSet() {
		return parentDeliveryRuleSet;
	}

	public DeliveryAudience getDeliveryAudience() {
		return deliveryAudience;
	}

	public DeliveryMethod getDeliveryMethod() {
		return deliveryMethod;
	}

	public ConnectionType getConnectionType() {
		return connectionType;
	}
	
	public Integer getPrecedenceLevel() {
		return precedenceLevel;
	}

	public Boolean getAllowable() {
		return allowable;
	}

	public static final class DeliveryRuleBuilder {

		private DeliveryRuleSet parentDeliveryRuleSet;
		private Boolean allowable;
		private Integer precedenceLevel;
		private DeliveryAudience deliveryAudience;
		private DeliveryMethod deliveryMethod;
		private ConnectionType connectionType;

		public DeliveryRuleBuilder() {
		}
		
		public DeliveryRuleBuilder withParentDeliveryRuleSet(DeliveryRuleSet parentDeliveryRuleSet) {
			this.parentDeliveryRuleSet = parentDeliveryRuleSet;
			return this;
		}

		public DeliveryRuleBuilder withAllowable(Boolean allowable) {
			this.allowable = allowable;
			return this;
		}

		public DeliveryRuleBuilder withPrecedenceLevel(Integer precedenceLevel) {
			this.precedenceLevel = precedenceLevel;
			return this;
		}
		
		public DeliveryRuleBuilder withDeliveryAudience(DeliveryAudience deliveryAudience) {
			this.deliveryAudience = deliveryAudience;
			return this;
		}

		public DeliveryRuleBuilder withDeliveryMethod(DeliveryMethod deliveryMethod) {
			this.deliveryMethod = deliveryMethod;
			return this;
		}
		
		public DeliveryRuleBuilder withConnectionType(ConnectionType connectionType) {
			this.connectionType = connectionType;
			return this;
		}
		
		public DeliveryRule build() {
			return new DeliveryRule(this);
		}
	}
}
