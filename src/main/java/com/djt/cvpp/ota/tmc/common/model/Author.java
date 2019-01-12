/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.common.model;

import java.util.List;
import java.util.UUID;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Author extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private UUID authorId;
	private UUID tenantId;
	private String name;
	private String description;
	private String type;
	
	protected Author() {
	}
	
	public Author(
		UUID authorId,
		UUID tenantId,
		String name, 
		String description,
		String type) {
		this.authorId = authorId;
		this.tenantId = tenantId;
		this.name = name;
		this.description = description;
		this.type = type;
	}
	
	public String getNaturalIdentity() {
		return authorId.toString();
	}

	public void validate(List<String> validationMessages) {

		if (authorId == null) {
			validationMessages.add("authorId must be specified.");
		}

		if (tenantId == null) {
			validationMessages.add("tenantId must be specified.");
		}
		
		if (name == null || name.trim().isEmpty()) {
			validationMessages.add("name must be specified.");
		}
		
		if (description == null || description.trim().isEmpty()) {
			validationMessages.add("description must be specified.");
		}
		
		if (type == null || type.trim().isEmpty()) {
			validationMessages.add("type");
		}
	}

	public UUID getAuthorId() {
		return authorId;
	}

	public UUID getTenantId() {
		return tenantId;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}	
}
