/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class EcuNode extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	
	private Domain parentDomain;
	
	private String ecuAcronymName;
	private String ecuLogicalAddress;

	private EcuNode() {
	}
	
	private EcuNode(EcuNodeBuilder builder) {

		this.persistentIdentity = builder.persistentIdentity;
		this.parentDomain = builder.parentDomain;
		this.ecuAcronymName = builder.ecuAcronymName;
		this.ecuLogicalAddress = builder.ecuLogicalAddress;
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			ecuAcronymName,
			ecuLogicalAddress
		);
	}

	public void validate(List<String> validationMessages) {

		if (ecuAcronymName == null || ecuAcronymName.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'ecuAcronymName' must be specified.");
		}

		if (ecuLogicalAddress == null || ecuLogicalAddress.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'ecuLogicalAddress' must be specified.");
		}
	}

	public void setParentDomain(Domain parentDomain) {
		if (this.parentDomain != null) {
			throw new IllegalStateException("Node: [" + this + "] is already associated with Domain: [" + this.parentDomain + "].");
		}
		this.parentDomain = parentDomain;
	}
	
	public String getEcuAcronymName() {
		return ecuAcronymName;
	}

	public String getEcuLogicalAddress() {
		return ecuLogicalAddress;
	}

	public Domain getParentDomain() {
		return parentDomain;
	}
	
	public static final class EcuNodeBuilder {

		private Long persistentIdentity;
		private Domain parentDomain;
		private String ecuAcronymName;
		private String ecuLogicalAddress;

		public EcuNodeBuilder() {
		}

		public EcuNodeBuilder withPersistentIdentity(Long persistentIdentity) {
			this.persistentIdentity = persistentIdentity;
			return this;
		}
		
		public EcuNodeBuilder withParentDomain(Domain parentDomain) {
			this.parentDomain = parentDomain;
			return this;
		}
		
		public EcuNodeBuilder withEcuAcronymName(String ecuAcronymName) {
			this.ecuAcronymName = ecuAcronymName;
			return this;
		}

		public EcuNodeBuilder withEcuLogicalAddress(String ecuLogicalAddress) {
			this.ecuLogicalAddress = ecuLogicalAddress;
			return this;
		}
		
		public EcuNode build() {
			return new EcuNode(this);
		}
	}
}
