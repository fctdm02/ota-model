/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.vadr.model.enums.ApplicationNecessity;
import com.djt.cvpp.ota.vadr.model.enums.ReleaseState;

/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Application extends AbstractEntity implements BinaryMetadataContainer {

	private static final long serialVersionUID = 1L;	

	
	private String appId;
	private String appName;
	private String appVersion;
	private Region region;
	private String description;
	private String ownerGroup;
	private String ownerKeyContact;
	private Boolean requiresSubscription;
	private ApplicationNecessity applicationNecessity;
	private ReleaseState releaseState;
	private Set<DomainInstance> domainInstances = new TreeSet<>();
	private Set<Application> applicationDependencies = new TreeSet<>();
	private Set<BinaryMetadata> binaryMetadatas = new TreeSet<>();

	private Application() {
	}

	private Application(ApplicationBuilder builder) {

		this.persistentIdentity = builder.persistentIdentity;
		this.appId = builder.appId;


		// TODO: TDM: Once VADR adds appId, remove the below
		this.appId = builder.appName + "_" + builder.appVersion;


		this.appName = builder.appName;
		this.appVersion = builder.appVersion;
		this.region = builder.region;
		this.description = builder.description;
		this.ownerGroup = builder.ownerGroup;
		this.ownerKeyContact = builder.ownerKeyContact;
		this.requiresSubscription = builder.requiresSubscription;
		this.applicationNecessity = builder.applicationNecessity;
		this.releaseState = builder.releaseState;
		this.domainInstances = builder.domainInstances;
		this.applicationDependencies = builder.applicationDependencies;
		this.binaryMetadatas = builder.binaryMetadatas;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			this.appId,
			this.domainInstances);
	}
	
	public void validate(List<String> validationMessages) {

		if (appId == null || appId.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'appId' must be specified.");
		}

		if (appName == null || appName.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'appName' must be specified.");
		}

		if (appVersion == null || appVersion.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'appVersion' must be specified.");
		}
		
		if (region == null) {
			validationMessages.add(getClassAndIdentity() + "'region' must be specified.");
		}
		
		if (binaryMetadatas == null || binaryMetadatas.isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "At least one BinaryMetadata must be specified.");
		} else {
			Iterator<BinaryMetadata> binaryMetadatasIterator = binaryMetadatas.iterator();
			while (binaryMetadatasIterator.hasNext()) {

				BinaryMetadata binaryMetadata = binaryMetadatasIterator.next();
				if (binaryMetadata.hasSkeletonBinaryMetadataOnly()) {
					// TODO: TDM: When VADR team uses swPartNumber, which is the natural identity of the binary metadata,
					// instead of fileId (which is the artificial, primary key, and can change by environment),
					// then change to specify the natural identity (which won't change by environment), here.
					validationMessages.add(getClassAndIdentity() + "BinaryMetadata with persistentIdentity: [" +  binaryMetadata.getPersistentIdentity() + "] is missing.");
				} else {
					binaryMetadata.validate(validationMessages);
				}
			}
		}		
		
	}
		
	public String getAppId() {
		return appId;
	}

	public String getAppName() {
		return appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public Region getRegion() {
		return region;
	}
	
	public String getDescription() {
		return description;
	}

	public String getOwnerGroup() {
		return ownerGroup;
	}

	public String getOwnerKeyContact() {
		return ownerKeyContact;
	}

	public Boolean getRequiresSubscription() {
		return requiresSubscription;
	}

	public ApplicationNecessity getApplicationNecessity() {
		return applicationNecessity;
	}

	public ReleaseState getReleaseState() {
		return releaseState;
	}

	public List<DomainInstance> getDomainInstances() {
		List<DomainInstance> list = new ArrayList<>();
		list.addAll(domainInstances);
		Collections.sort(list);
		return list;
	}

	public List<Application> getApplicationDependencies() {
		List<Application> list = new ArrayList<>();
		list.addAll(applicationDependencies);
		Collections.sort(list);
		return list;
	}

	protected void addDomainInstance(DomainInstance domainInstance) throws EntityAlreadyExistsException {
		if (this.domainInstances.contains(domainInstance)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already contains " + domainInstance.getClassAndIdentity());
		}
		this.domainInstances.add(domainInstance);
	}
		
	public List<BinaryMetadata> getBinaryMetadatas() {
		List<BinaryMetadata> list = new ArrayList<>();
		list.addAll(this.binaryMetadatas);
		Collections.sort(list);
		return list;
	}

	public void addBinaryMetadata(BinaryMetadata binaryMetadata) {
		
		if (binaryMetadata == null ) {
			throw new FenixRuntimeException("binaryMetadata cannot be null when adding to Application: " + this.getClassAndIdentity());
		}
		this.binaryMetadatas.add(binaryMetadata);
	}
	
	public void addBinaryMetadatas(Set<BinaryMetadata> binaryMetadatas) {
		
		this.binaryMetadatas.addAll(binaryMetadatas);
	}
	
	public static final class ApplicationBuilder {

		private Long persistentIdentity;
		private String appId;
		private String appName;
		private String appVersion;
		private Region region;
		private String description;
		private String ownerGroup;
		private String ownerKeyContact;
		private Boolean requiresSubscription;
		private ApplicationNecessity applicationNecessity;
		private ReleaseState releaseState;
		private Set<DomainInstance> domainInstances = new TreeSet<>();
		private Set<Application> applicationDependencies = new TreeSet<>();
		private Set<BinaryMetadata> binaryMetadatas = new TreeSet<>();

		public ApplicationBuilder() {
		}

		public ApplicationBuilder withPersistentIdentity(Long persistentIdentity) {
			this.persistentIdentity = persistentIdentity;
			return this;
		}

		public ApplicationBuilder withAppId(String appId) {
			this.appId = appId;
			return this;
		}
		
		public ApplicationBuilder withAppName(String appName) {
			this.appName = appName;
			return this;
		}

		public ApplicationBuilder withAppVersion(String appVersion) {
			this.appVersion = appVersion;
			return this;
		}

		public ApplicationBuilder withRegion(Region region) {
			this.region = region;
			return this;
		}
		
		public ApplicationBuilder withDescription(String description) {
			this.description = description;
			return this;
		}

		public ApplicationBuilder withOwnerGroup(String ownerGroup) {
			this.ownerGroup = ownerGroup;
			return this;
		}

		public ApplicationBuilder withOwnerKeyContact(String ownerKeyContact) {
			this.ownerKeyContact = ownerKeyContact;
			return this;
		}
		
		public ApplicationBuilder withRequiresSubscription(Boolean requiresSubscription) {
			this.requiresSubscription = requiresSubscription;
			return this;
		}
			
		public ApplicationBuilder withApplicationNecessity(ApplicationNecessity applicationNecessity) {
			this.applicationNecessity = applicationNecessity;
			return this;
		}
		
		public ApplicationBuilder withReleaseState(ReleaseState releaseState) {
			this.releaseState = releaseState;
			return this;
		}
		
		public ApplicationBuilder withDomainInstances(Set<DomainInstance> domainInstances) {
			this.domainInstances = domainInstances;
			return this;
		}
		
		public ApplicationBuilder withApplicationDependencies(Set<Application> applicationDependencies) {
			this.applicationDependencies = applicationDependencies;
			return this;
		}
		
		public ApplicationBuilder withBinaryMetadatas(Set<BinaryMetadata> binaryMetadatas) {
			this.binaryMetadatas = binaryMetadatas;
			return this;
		}

		public Application build() {
			return new Application(this);
		}
	}
}
