/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.model;

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
	private String didAddress;
	private String value;
	
	public Did(
		String didAddress,
		String value) {
		this.didAddress = didAddress;
		this.value = value;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentNode,
			didAddress
		);
	}

	public void validate(List<String> validationMessages) {

		if (parentNode == null) {
			validationMessages.add(getClassAndIdentity() + "'parentNode' must be specified.");
		}

		if (didAddress == null || didAddress.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'didAddress' must be specified.");
		}
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}
	
	public Node getParentNode() {
		return parentNode;
	}
	
	public String getDidAddress() {
		return didAddress;
	}

	public String getValue() {
		return value;
	}
}
