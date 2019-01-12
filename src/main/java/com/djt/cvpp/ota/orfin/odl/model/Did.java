/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Did extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private Node parentNode;
	private String didName;
	private String description;
	private Boolean vinSpecificDidFlag;
	private Boolean directConfigurationDidFlag;
	private Boolean privateNetworkDidFlag;

	private Did(DidBuilder builder) {
		
		this.parentNode = builder.parentNode;
		this.didName = builder.didName;
		this.description = builder.description;
		this.vinSpecificDidFlag = builder.vinSpecificDidFlag;
		this.directConfigurationDidFlag = builder.directConfigurationDidFlag;
		this.privateNetworkDidFlag = builder.privateNetworkDidFlag;
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentNode,
			didName);
	}

	public void validate(List<String> validationMessages) {

		if (parentNode == null) {
			validationMessages.add("parentNode must be specified.");
		}

		if (didName == null || didName.trim().isEmpty()) {
			validationMessages.add("didName must be specified.");
		}

		if (description == null || description.trim().isEmpty()) {
			validationMessages.add("description must be specified.");
		}

		if (vinSpecificDidFlag == null) {
			validationMessages.add("vinSpecificDidFlag must be specified.");
		}

		if (directConfigurationDidFlag == null) {
			validationMessages.add("directConfigurationDidFlag must be specified.");
		}

		if (privateNetworkDidFlag == null) {
			validationMessages.add("privateNetworkDidFlag must be specified.");
		}
	}
	
	protected void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public String getDidName() {
		return didName;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getVinSpecificDidFlag() {
		return vinSpecificDidFlag;
	}

	public Boolean getDirectConfigurationDidFlag() {
		return directConfigurationDidFlag;
	}

	public Boolean getPrivateNetworkDidFlag() {
		return privateNetworkDidFlag;
	}

	public static final class DidBuilder {

		private Node parentNode;
		private String didName;
		private String description;
		private Boolean vinSpecificDidFlag;
		private Boolean directConfigurationDidFlag;
		private Boolean privateNetworkDidFlag;

		public DidBuilder() {
		}
		
		public DidBuilder withParentNode(Node parentNode) {
			this.parentNode = parentNode;
			return this;
		}
		
		public DidBuilder withDidName(String didName) {
			this.didName = didName;
			return this;
		}

		public DidBuilder withDescription(String description) {
			this.description = description;
			return this;
		}

		public DidBuilder withVinSpecificDidFlag(Boolean vinSpecificDidFlag) {
			this.vinSpecificDidFlag = vinSpecificDidFlag;
			return this;
		}

		public DidBuilder withDirectConfigurationDidFlag(Boolean directConfigurationDidFlag) {
			this.directConfigurationDidFlag = directConfigurationDidFlag;
			return this;
		}

		public DidBuilder withPrivateNetworkDidFlag(Boolean privateNetworkDidFlag) {
			this.privateNetworkDidFlag = privateNetworkDidFlag;
			return this;
		}

		public Did build() {
			return new Did(this);
		}
	}
}
