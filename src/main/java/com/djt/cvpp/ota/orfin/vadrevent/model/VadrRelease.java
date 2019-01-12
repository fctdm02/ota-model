/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.vadrevent.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VadrRelease extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private DeliveryRuleSet parentDeliveryRuleSet;  
	private String domainName;
	private String domainInstanceName;
	private String domainInstanceDescription;
	private String domainInstanceVersion;
	private String appId;
	private String appVersion;
	private String productionState;
	private String releaseDate;
	private String softwarePriorityLevel;
	
	private VadrRelease(VadrReleaseBuilder builder) {
		
		this.parentDeliveryRuleSet = builder.parentDeliveryRuleSet;
		this.domainName = builder.domainName;
		this.domainInstanceName = builder.domainInstanceName;
		this.domainInstanceDescription = builder.domainInstanceDescription;
		this.domainInstanceVersion = builder.domainInstanceVersion;
		this.appId = builder.appId;
		this.appVersion = builder.appVersion;
		this.productionState = builder.productionState;
		this.releaseDate = builder.releaseDate;
		this.softwarePriorityLevel = builder.softwarePriorityLevel;
	}
	
	public String getNaturalIdentity() {

		return AbstractEntity.buildNaturalIdentity(
			domainName,
			domainInstanceName,
			domainInstanceVersion,
			appId,
			appVersion,
			productionState,
			releaseDate);
	}

	public void validate(List<String> validationMessages) {

		this.validateNotNull(validationMessages, "domainName", domainName);
		this.validateNotNull(validationMessages, "domainInstanceName", domainInstanceName);
		this.validateNotNull(validationMessages, "domainInstanceVersion", domainInstanceVersion);
		this.validateNotNull(validationMessages, "productionState", productionState);
		this.validateNotNull(validationMessages, "releaseDate", releaseDate);
	}
	
	public DeliveryRuleSet getParentDeliveryRuleSet() {
		return parentDeliveryRuleSet;
	}

	public void setParentDeliveryRuleSet(DeliveryRuleSet parentDeliveryRuleSet) {
		this.parentDeliveryRuleSet = parentDeliveryRuleSet;
	}
	
	public String getDomainName() {
		return domainName;
	}

	public String getDomainInstanceName() {
		return domainInstanceName;
	}

	public String getDomainInstanceDescription() {
		return domainInstanceDescription;
	}
	
	public String getDomainInstanceVersion() {
		return domainInstanceVersion;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public String getProductionState() {
		return productionState;
	}

	public String getReleaseDate() {
		return releaseDate;
	}
	
	public String getSoftwarePriorityLevel() {
		return softwarePriorityLevel;
	}

	public static final class VadrReleaseBuilder {

		private DeliveryRuleSet parentDeliveryRuleSet;  
		private String domainName;
		private String domainInstanceName;
		private String domainInstanceDescription;
		private String domainInstanceVersion;
		private String appId;
		private String appVersion;
		private String productionState;
		private String softwarePriorityLevel;
		private String releaseDate;
		
		public VadrReleaseBuilder() {
		}

		public VadrReleaseBuilder withParentDeliveryRuleSet(DeliveryRuleSet parentDeliveryRuleSet) {
			this.parentDeliveryRuleSet = parentDeliveryRuleSet;
			return this;
		}
		
		public VadrReleaseBuilder withDomainName(String domainName) {
			this.domainName = domainName;
			return this;
		}

		public VadrReleaseBuilder withDomainInstanceName(String domainInstanceName) {
			this.domainInstanceName = domainInstanceName;
			return this;
		}

		public VadrReleaseBuilder withDomainInstanceDescription(String domainInstanceDescription) {
			this.domainInstanceDescription = domainInstanceDescription;
			return this;
		}
		
		public VadrReleaseBuilder withDomainInstanceVersion(String domainInstanceVersion) {
			this.domainInstanceVersion = domainInstanceVersion;
			return this;
		}
		
		public VadrReleaseBuilder withAppId(String appId) {
			this.appId = appId;
			return this;
		}

		public VadrReleaseBuilder withAppVersion(String appVersion) {
			this.appVersion = appVersion;
			return this;
		}

		public VadrReleaseBuilder withProductionState(String productionState) {
			this.productionState = productionState;
			return this;
		}
				
		public VadrReleaseBuilder withSoftwarePriorityLevel(String softwarePriorityLevel) {
			this.softwarePriorityLevel = softwarePriorityLevel;
			return this;
		}

		public VadrReleaseBuilder withReleaseDate(String releaseDate) {
			this.releaseDate = releaseDate;
			return this;
		}
		
		public VadrRelease build() {
			return new VadrRelease(this);
		}
	}	
}
