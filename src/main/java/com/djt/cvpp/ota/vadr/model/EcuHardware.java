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
public class EcuHardware extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
		
	private EcuNode ecuNode;
	private String ecuHardwarePartNumber;
	private Set<DomainInstance> domainInstances = new TreeSet<>();

	private EcuHardware() {
	}

	private EcuHardware(EcuHardwareBuilder builder) {

		this.persistentIdentity = builder.persistentIdentity;
		this.ecuNode = builder.ecuNode;
		this.ecuHardwarePartNumber = builder.ecuHardwarePartNumber;
		this.domainInstances = builder.domainInstances;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			this.ecuNode,
			this.ecuHardwarePartNumber, 
			this.domainInstances);
	}

	public void validate(List<String> validationMessages) {

		if (domainInstances == null || domainInstances.isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "At least one DomainInstance must be specified.");
		}

		if (ecuNode == null) {
			validationMessages.add(getClassAndIdentity() + "'ecuNode' must be specified.");
		}
		
		if (ecuHardwarePartNumber == null || ecuHardwarePartNumber.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'ecuHardwarePartNumber' must be specified.");
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

	public String getEcuHardwarePartNumber() {
		return ecuHardwarePartNumber;
	}
	
	protected void addDomainInstance(DomainInstance domainInstanceEcuHardware) throws EntityAlreadyExistsException {
		if (this.domainInstances.contains(domainInstanceEcuHardware)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already contains " + domainInstanceEcuHardware.getClassAndIdentity());
		}
		this.domainInstances.add(domainInstanceEcuHardware);
	}
	
	public static final class EcuHardwareBuilder {

		private Long persistentIdentity;
		private EcuNode ecuNode;
		private String ecuHardwarePartNumber;
		private Set<DomainInstance> domainInstances = new TreeSet<>();

		public EcuHardwareBuilder() {
		}

		public EcuHardwareBuilder withPersistentIdentity(Long persistentIdentity) {
			this.persistentIdentity = persistentIdentity;
			return this;
		}

		public EcuHardwareBuilder withEcuNode(EcuNode ecuNode) {
			this.ecuNode = ecuNode;
			return this;
		}
		
		public EcuHardwareBuilder withEcuHardwarePartNumber(String ecuHardwarePartNumber) {
			this.ecuHardwarePartNumber = ecuHardwarePartNumber;
			return this;
		}

		public EcuHardwareBuilder withDomainInstances(Set<DomainInstance> domainInstances) {
			this.domainInstances = domainInstances;
			return this;
		}
		
		public EcuHardware build() {
			return new EcuHardware(this);
		}
	}	
}
