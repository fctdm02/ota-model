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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.djt.cvpp.ota.tmc.common.model.Author;
import com.djt.cvpp.ota.tmc.common.model.Tag;
import com.djt.cvpp.ota.tmc.common.model.TmcEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TargetBundle extends TmcEntity {

	private static final long serialVersionUID = 1L;

	
	private Set<ReleaseArtifact> releaseArtifacts = new TreeSet<>();	
	private Set<TargetMatcher> targetMatchers = new TreeSet<>();
	
	public TargetBundle(
		UUID tenantUuid,	
		UUID uuid,
		String name,
		String description,
		URI uri,
		Instant createdTimestamp,
		Author author,
		Set<Tag> tags,
		Set<ReleaseArtifact> releaseArtifacts,
		Set<TargetMatcher> targetMatchers) {
		super( 
			tenantUuid,	
			uuid,
			name,
			description,
			uri,
			createdTimestamp,
			author,
			tags);

		this.releaseArtifacts = releaseArtifacts;	
		this.targetMatchers = targetMatchers;
	}

	public void validate(List<String> validationMessages) {
		
		super.validate(validationMessages);

		if (releaseArtifacts == null || releaseArtifacts.isEmpty()) {
			validationMessages.add("At least one ReleaseArtifact must be specified.");
		} else {
			Iterator<ReleaseArtifact> iterator = releaseArtifacts.iterator();
			while (iterator.hasNext()) {

				ReleaseArtifact releaseArtifact = iterator.next();
				releaseArtifact.validate(validationMessages);
			}
		}
		
		if (targetMatchers == null || targetMatchers.isEmpty()) {
			validationMessages.add("At least one TargetMatcher must be specified.");
		} else {
			Iterator<TargetMatcher> iterator = targetMatchers.iterator();
			while (iterator.hasNext()) {

				TargetMatcher targetMatcher = iterator.next();
				targetMatcher.validate(validationMessages);
			}
		}
	}
	
	public Set<ReleaseArtifact> getReleaseArtifacts() {
		return releaseArtifacts;
	}

	public Set<TargetMatcher> getTargetMatchers() {
		return targetMatchers;
	}
}
