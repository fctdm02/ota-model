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
public class Release extends TmcEntity {
	
	private static final long serialVersionUID = 1L;

	
	private Set<TargetBundle> targetBundles = new TreeSet<>();	
		
	public Release(
		UUID tenantUuid,	
		UUID uuid,
		String name,
		String description,
		URI uri,
		Instant createdTimestamp,
		Author author,
		Set<Tag> tags,
		Set<TargetBundle> targetBundles) {
		super(
			tenantUuid,	
			uuid,
			name,
			description,
			uri,
			createdTimestamp,
			author,
			tags);
		this.targetBundles = targetBundles;
	}
	
	public void validate(List<String> validationMessages) {
		
		super.validate(validationMessages);

		if (targetBundles == null || targetBundles.isEmpty()) {
			validationMessages.add("At least one TargetBundle must be specified.");
		} else {
			Iterator<TargetBundle> targetBundleIterator = targetBundles.iterator();
			while (targetBundleIterator.hasNext()) {

				TargetBundle targetBundle = targetBundleIterator.next();
				targetBundle.validate(validationMessages);
			}
		}
	}

	public Set<TargetBundle> getTargetBundles() {
		return targetBundles;
	}
}
