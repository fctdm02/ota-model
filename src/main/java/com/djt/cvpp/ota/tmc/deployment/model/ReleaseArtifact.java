/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.deployment.model;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.djt.cvpp.ota.tmc.common.model.Author;
import com.djt.cvpp.ota.tmc.common.model.Tag;
import com.djt.cvpp.ota.tmc.common.model.TmcEntity;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public class ReleaseArtifact extends TmcEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String byteStreamUrl;
	private Integer byteLength;
	private String checksum;
	private String type;
	
	public ReleaseArtifact(
		UUID tenantUuid,	
		UUID uuid,
		String name,
		String description,
		URI uri,
		Instant createdTimestamp,
		Author author,
		Set<Tag> tags,
		String byteStreamUrl,
		Integer byteLength,
		String checksum,
		String type) {
		super(
			tenantUuid,
			uuid,
			name,
			description,
			uri,
			createdTimestamp,
			author,
			tags);
		this.byteStreamUrl = byteStreamUrl;
		this.byteLength = byteLength;
		this.checksum = checksum;
		this.type = type;
	}
	
	public void validate(List<String> validationMessages) {
		
		super.validate(validationMessages);
		
		if (byteStreamUrl == null) {
			validationMessages.add("byteStreamUrl must be specified.");
		}
			
		if (byteLength == null || byteLength.intValue() < 0) {
			validationMessages.add("byteLength must be specified.");
		}

		if (checksum == null || checksum.trim().isEmpty()) {
			validationMessages.add("checksum must be specified.");
		}

		if (type == null || type.trim().isEmpty()) {
			validationMessages.add("type must be specified.");
		}
	}

	public String getByteStreamUrl() {
		return byteStreamUrl;
	}

	public Integer getByteLength() {
		return byteLength;
	}

	public String getChecksum() {
		return checksum;
	}

	public String getType() {
		return type;
	}
}
