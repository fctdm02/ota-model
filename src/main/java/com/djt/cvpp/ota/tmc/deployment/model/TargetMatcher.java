/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.deployment.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TargetMatcher extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private String name;
	private String value;
	private String type;
	
	public TargetMatcher(
		String name,
		String value,
		String type) {
		
		this.name = name;
		this.value = value;
		this.type = type;		
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			this.name, 
			this.value, 
			this.type);
	}

	public void validate(List<String> validationMessages) {

		if (name == null || name.trim().isEmpty()) {
			validationMessages.add("name must be specified.");
		}
		
		if (value == null || value.trim().isEmpty()) {
			validationMessages.add("value");
		}
		
		if (type == null || type.trim().isEmpty()) {
			validationMessages.add("type");
		}
	}
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getType() {
		return type;
	}
}
