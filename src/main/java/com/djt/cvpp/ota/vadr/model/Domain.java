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
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Domain extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	
	private String domainName;
	private String domainDescription;

	private Set<EcuNode> ecuNodes = new TreeSet<>();
	private Set<DomainInstance> domainInstances = new TreeSet<>();
	private Set<Domain> domainDependencies = new TreeSet<>();

	private Domain() {
	}
	
	private Domain(DomainBuilder builder) {

		this.persistentIdentity = builder.persistentIdentity;
		this.domainName = builder.domainName;
		this.domainDescription = builder.domainDescription;
		this.ecuNodes = builder.ecuNodes;
		Iterator<EcuNode> ecuNodesIterator = this.ecuNodes.iterator();
		while (ecuNodesIterator.hasNext()) {
			EcuNode ecuNode = ecuNodesIterator.next();
			ecuNode.setParentDomain(this);
		}
		this.domainInstances = builder.domainInstances;
		this.domainDependencies = builder.domainDependencies;
	}

	public String getNaturalIdentity() {
		return domainName;
	}

	public void validate(List<String> validationMessages) {

		if (domainName == null || domainName.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'domainName' must be specified.");
		}

		if (domainDescription == null || domainDescription.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'domainDescription' must be specified.");
		}		
		
		if (ecuNodes == null) {
			validationMessages.add(getClassAndIdentity() + "At least one EcuNode must be specified.");
		} else {
			Iterator<EcuNode> ecuNodesIterator = ecuNodes.iterator();
			while (ecuNodesIterator.hasNext()) {

				EcuNode ecuNode = ecuNodesIterator.next();
				ecuNode.validate(validationMessages);
			}
		}
				
		if (domainInstances != null) {
			Iterator<DomainInstance> domainInstancesIterator = domainInstances.iterator();
			while (domainInstancesIterator.hasNext()) {

				DomainInstance domainInstance = domainInstancesIterator.next();
				domainInstance.validate(validationMessages);
			}
		}		
	}

	public String getDomainName() {
		return domainName;
	}

	public String getDomainDescription() {
		return domainDescription;
	}

	public List<EcuNode> getEcuNodes() {
		List<EcuNode> list = new ArrayList<>();
		list.addAll(this.ecuNodes);
		return list;
	}
	
	public void addDomainInstance(DomainInstance domainInstance) throws EntityAlreadyExistsException {
		if (this.domainInstances.contains(domainInstance)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already contains " + domainInstance.getClassAndIdentity());
		}
		this.domainInstances.add(domainInstance);
	}
	
	public List<DomainInstance> getDomainInstances() {
		List<DomainInstance> list = new ArrayList<>();
		list.addAll(this.domainInstances);
		Collections.sort(list);
		return list;
	}
	
	public void addDomainDependency(Domain dependentDomain) {
		this.domainDependencies.add(dependentDomain);
	}

	public List<Domain> getDomainDependencies() {
		List<Domain> list = new ArrayList<>();
		list.addAll(this.domainDependencies);
		Collections.sort(list);
		return list;
	}
	
	public EcuNode getChildEcuNode(String ecuAcronymName, String ecuLogicalAddress) throws EntityDoesNotExistException {
		Iterator<EcuNode> iterator = this.ecuNodes.iterator();
		while (iterator.hasNext()) {
			EcuNode ecuNode = iterator.next();
			if (ecuNode.getEcuAcronymName().equals(ecuAcronymName) 
				&& ecuNode.getEcuLogicalAddress().equals(ecuLogicalAddress)) {
				return ecuNode;
			}
		}
		throw new EntityDoesNotExistException("Domain: [" + this + "] does not contain an EcuNode with ecuAcronymName: [" + ecuAcronymName + "] and ecuLogicalAddress: [" + ecuLogicalAddress + "].");
	}
	
	public DomainInstance getDomainInstance(String domainInstanceName, String domainInstanceVersion) throws EntityDoesNotExistException {

		Iterator<DomainInstance> iterator = this.domainInstances.iterator();
		while (iterator.hasNext()) {
			DomainInstance domainInstance = iterator.next();
			if (domainInstance.getDomainInstanceName().equals(domainInstanceName) 
				&& domainInstance.getDomainInstanceVersion().equals(domainInstanceVersion)) {
				return domainInstance;
			}
		}
		throw new EntityDoesNotExistException("Domain: [" + this + "] does not contain an DomainInstance with domainInstanceName: [" + domainInstanceName + "] and domainInstanceVersion: [" + domainInstanceVersion + "].");
	}
	
	public DomainInstance getSourceDomainInstanceForVehicle(TmcVehicle tmcVehicle) throws EntityDoesNotExistException {

		Iterator<DomainInstance> iterator = this.domainInstances.iterator();
		while (iterator.hasNext()) {
			
			DomainInstance domainInstance = iterator.next();

			boolean isVehicleQualified = true;
			
			Iterator<EcuNode> ecuNodeIterator = domainInstance.getEcuNodes().iterator();
			while (ecuNodeIterator.hasNext()) {
				
				EcuNode ecuNode = ecuNodeIterator.next();
				
				String nodeName = ecuNode.getEcuAcronymName();
				String nodeAddress = ecuNode.getEcuLogicalAddress();
				
				boolean hardwareMatchesForEcuNode = false;
				boolean softwareMatchesForEcuNode = true;
							
				Iterator<String> ecuHardwarePartNumberIterator = domainInstance.getEcuHardwarePartNumbersForEcuNode(ecuNode).iterator();
				while (ecuHardwarePartNumberIterator.hasNext()) {
					
					String ecuHardwarePartNumber = ecuHardwarePartNumberIterator.next();
					if (tmcVehicle.hasDidValue(nodeName, nodeAddress, DomainInstance.HW_DID_ADDRESS, ecuHardwarePartNumber)) {
						
						hardwareMatchesForEcuNode = true;
						break;
					}
				}
				
				Map<String, String> map = domainInstance.getEcuSoftwarePartNumbersForEcuNode(ecuNode);
				for (Map.Entry<String, String> entry: map.entrySet()) {

					String didAddress = entry.getKey(); 
					String ecuSoftwarePartNumber = entry.getValue();
					if (!tmcVehicle.hasDidValue(nodeName, nodeAddress, didAddress, ecuSoftwarePartNumber)) {
						
						softwareMatchesForEcuNode = false;
						break;
					}
				}
				
				if (!hardwareMatchesForEcuNode || !softwareMatchesForEcuNode) {
					isVehicleQualified = false;
					break;
				}
			}
			
			if (isVehicleQualified) {
				return domainInstance;
			}
		}
		
		// The vehicle must have had a module replaced, or otherwise, is not at an atomic version of the software that corresponds to a domain instance version.
		// In this case, we cannot update the vehicle, so TODO: TDM: There needs to be a way to "report" this vehicle as being un-updateable
		return null;
	}


	// TODO: Temporary until a better solution is found (this is for the VADR Adapter service to output issues with data)
	private Set<String> validationWarnings = new TreeSet<>();
	public void setValidationWarnings() {
		this.validationWarnings.addAll(this.validate());
	}
	public Set<String> getValidationWarnings() {
		return this.validationWarnings;
	}


	public static final class DomainBuilder {

		private Long persistentIdentity;
		private String domainName;
		private String domainDescription;
		private Set<EcuNode> ecuNodes = new TreeSet<>();
		private Set<DomainInstance> domainInstances = new TreeSet<>();
		private Set<Domain> domainDependencies = new TreeSet<>();

		public DomainBuilder() {
		}

		public DomainBuilder withPersistentIdentity(Long persistentIdentity) {
			this.persistentIdentity = persistentIdentity;
			return this;
		}

		public DomainBuilder withDomainName(String domainName) {
			this.domainName = domainName;
			return this;
		}
		
		public DomainBuilder withDomainDescription(String domainDescription) {
			this.domainDescription = domainDescription;
			return this;
		}

		public DomainBuilder withEcuNodes(Set<EcuNode> ecuNodes) {
			this.ecuNodes = ecuNodes;
			return this;
		}

		public DomainBuilder withDomainInstances(Set<DomainInstance> domainInstances) {
			this.domainInstances = domainInstances;
			return this;
		}

		public DomainBuilder withDomainDependencies(Set<Domain> domainDependencies) {
			this.domainDependencies = domainDependencies;
			return this;
		}

		public Domain build() {
			return new Domain(this);
		}
	}
}
