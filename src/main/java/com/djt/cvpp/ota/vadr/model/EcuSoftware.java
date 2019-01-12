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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class EcuSoftware extends AbstractEntity implements BinaryMetadataContainer {

	private static final long serialVersionUID = 1L;

	
	private EcuNode ecuNode;
	private String ecuSoftwarePartNumber;
	private String softwareName;
	private String softwareVersionNumber;	
	private String category;
	private String description;
	private Set<DomainInstance> domainInstances = new TreeSet<>();
	private BinaryMetadata binaryMetadata;

	private EcuSoftware() {
	}

	private EcuSoftware(EcuSoftwareBuilder builder) {

		this.persistentIdentity = builder.persistentIdentity;
		this.ecuNode = builder.ecuNode;
		this.ecuSoftwarePartNumber = builder.ecuSoftwarePartNumber;
		this.softwareName = builder.softwareName;
		this.softwareVersionNumber = builder.softwareVersionNumber;
		this.category = builder.category;
		this.description = builder.description;
		this.domainInstances = builder.domainInstances;
		this.binaryMetadata = builder.binaryMetadata;
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			this.ecuNode,
			this.ecuSoftwarePartNumber, 
			this.domainInstances);
	}

	public void validate(List<String> validationMessages) {

		if (domainInstances == null || domainInstances.isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "At least one DomainInstance must be specified.");
		}

		if (ecuNode == null) {
			validationMessages.add(getClassAndIdentity() + "'ecuNode' must be specified.");
		}

		if (ecuSoftwarePartNumber == null || ecuSoftwarePartNumber.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'ecuSoftwarePartNumber' must be specified.");
		}

		if (softwareName == null || softwareName.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'softwareName' must be specified.");
		}
		
		if (softwareVersionNumber == null || softwareVersionNumber.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'softwareVersionNumber' must be specified.");
		}
		
		if (binaryMetadata == null) {
			validationMessages.add(getClassAndIdentity() + "Exactly one BinaryMetadata must be specified.");
		} else if (binaryMetadata.hasSkeletonBinaryMetadataOnly()) {
			// TODO: TDM: When VADR team uses swPartNumber, which is the natural identity of the binary metadata,
			// instead of fileId (which is the artificial, primary key, and can change by environment),
			// then change to specify the natural identity (which won't change by environment), here.
			validationMessages.add(getClassAndIdentity() + "BinaryMetadata with persistentIdentity: [" +  binaryMetadata.getPersistentIdentity() + "] is missing.");
		} else {
			binaryMetadata.validate(validationMessages);
		}
	}

	public List<DomainInstance> getDomainInstances() {
		List<DomainInstance> list = new ArrayList<>();
		list.addAll(domainInstances);
		Collections.sort(list);
		return list;
	}

	public EcuNode getEcuNode() {
		return ecuNode;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public String getSoftwareVersionNumber() {
		return softwareVersionNumber;
	}

	public String getEcuSoftwarePartNumber() {
		return ecuSoftwarePartNumber;
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public BinaryMetadata getBinaryMetadata() {
		return this.binaryMetadata;
	}

	public void setBinaryMetadata(BinaryMetadata binaryMetadata) {
		this.binaryMetadata = binaryMetadata;
	}

	protected void addDomainInstance(DomainInstance domainInstanceEcuSoftware) throws EntityAlreadyExistsException {
		if (this.domainInstances.contains(domainInstanceEcuSoftware)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already contains " + domainInstanceEcuSoftware.getClassAndIdentity());
		}
		this.domainInstances.add(domainInstanceEcuSoftware);
	}

	public List<BinaryMetadata> getBinaryMetadatas() {
		List<BinaryMetadata> list = new ArrayList<>();
		list.add(this.binaryMetadata);
		return list;
	}

	public void addBinaryMetadata(BinaryMetadata binaryMetadata) {
		this.binaryMetadata = binaryMetadata;
	}
	
	public void addBinaryMetadatas(Set<BinaryMetadata> binaryMetadatas) {
		throw new UnsupportedOperationException("addBinaryMetadata() should be called instead for EcuSoftware instances.");
	}
	
	public static final class EcuSoftwareBuilder {

		private Long persistentIdentity;
		private EcuNode ecuNode;
		private String ecuSoftwarePartNumber;
		private String softwareName;
		private String softwareVersionNumber;	
		private String category;
		private String description;
		private Set<DomainInstance> domainInstances = new TreeSet<>();
		private BinaryMetadata binaryMetadata;

		public EcuSoftwareBuilder() {
		}

		public EcuSoftwareBuilder withPersistentIdentity(Long persistentIdentity) {
			this.persistentIdentity = persistentIdentity;
			return this;
		}

		public EcuSoftwareBuilder withEcuNode(EcuNode ecuNode) {
			this.ecuNode = ecuNode;
			return this;
		}

		public EcuSoftwareBuilder withEcuSoftwarePartNumber(String ecuSoftwarePartNumber) {
			this.ecuSoftwarePartNumber = ecuSoftwarePartNumber;
			return this;
		}
		
		public EcuSoftwareBuilder withSoftwareName(String softwareName) {
			this.softwareName = softwareName;
			return this;
		}

		public EcuSoftwareBuilder withSoftwareVersionNumber(String softwareVersionNumber) {
			this.softwareVersionNumber = softwareVersionNumber;
			return this;
		}

		public EcuSoftwareBuilder withCategory(String category) {
			this.category = category;
			return this;
		}

		public EcuSoftwareBuilder withDescription(String description) {
			this.description = description;
			return this;
		}
		
		public EcuSoftwareBuilder withDomainInstances(Set<DomainInstance> domainInstances) {
			this.domainInstances = domainInstances;
			return this;
		}

		public EcuSoftwareBuilder withBinaryMetadata(BinaryMetadata binaryMetadata) {
			this.binaryMetadata = binaryMetadata;
			return this;
		}

		public EcuSoftware build() {
			return new EcuSoftware(this);
		}
	}
}
