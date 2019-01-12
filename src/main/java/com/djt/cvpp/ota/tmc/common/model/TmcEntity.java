/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.common.model;

import java.net.URI;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public abstract class TmcEntity extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	

	private UUID tenantUuid;
	private UUID uuid;
	private String name;
	private String description;
	private URI uri;
	private Instant createdTimestamp;	
	private Author author;
	private Set<Tag> tags = new TreeSet<>();
	
	public TmcEntity(
		UUID tenantUuid,
		UUID uuid,
		String name,
		String description,
		URI uri,
		Instant createdTimestamp,
		Author author,
		Set<Tag> tags) {

		this.tenantUuid = tenantUuid;
		this.uuid = uuid;
		this.name = name;
		this.description = description;
		this.uri = uri;
		this.createdTimestamp = createdTimestamp;	
		this.author = author;
		if (tags != null && !tags.isEmpty()) {
			this.tags.addAll(tags);	
		}
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(tenantUuid, uuid);
	}

	public void validate(List<String> validationMessages) {

		if (tenantUuid == null) {
			validationMessages.add("tenantUuid must be specified.");
		}
		
		if (uuid == null) {
			validationMessages.add("uuid must be specified.");
		}

		if (name == null || name.trim().isEmpty()) {
			validationMessages.add("name must be specified.");
		}
		
		if (uri == null) {
			validationMessages.add("uri must be specified.");
		}

		if (createdTimestamp == null) {
			validationMessages.add("createdTimestamp must be specified.");
		}

		if (author == null) {
			validationMessages.add("author must be specified.");
		}
		
		if (this.tags != null) {
			Iterator<Tag> tagsIterator = tags.iterator();
			while (tagsIterator.hasNext()) {

				Tag tag = tagsIterator.next();
				tag.validate(validationMessages);
			}
		}
	}

	public UUID getTenantUuid() {
		return tenantUuid;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public URI getUri() {
		return uri;
	}

	public Instant getCreatedTimestamp() {
		return createdTimestamp;
	}

	public Author getAuthor() {
		return author;
	}

	public Set<Tag> getTags() {
		return tags;
	}	
}
