/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.vadr.model.enums.ProductionState;
import com.djt.cvpp.ota.vadr.model.enums.ReleaseState;
import com.djt.cvpp.ota.vadr.model.enums.SoftwarePriorityLevel;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class DomainInstance extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	
	public static final String LEFT_PAREN = "(";
	public static final String RIGHT_PAREN = ")";
	public static final String OR = " OR ";
	public static final String AND = " AND ";
	public static final String MODULES_PREFIX = "modules[\"";
	public static final String HW_DID_ADDRESS = "F111";
	public static final String DIDS_PREFIX = "\"].dids[\"";
	public static final String DIDS_SUFFIX = "\"] == \"";
	public static final String DOUBLE_QUOTE = "\"";

	
	private Domain parentDomain;
	private String domainInstanceName;
	private String domainInstanceVersion;

	private String domainInstanceDescription;
	private Timestamp releasedDate;
	private ReleaseState releaseState;
	private ProductionState productionState;
	private SoftwarePriorityLevel softwarePriorityLevel;

	private Set<EcuHardware> ecuHardwares = new TreeSet<>();
	private Set<EcuSoftware> ecuSoftwares = new TreeSet<>();
	private Set<Application> applications = new TreeSet<>();

	private DomainInstance() {
	}

	private DomainInstance(DomainInstanceBuilder builder) throws EntityAlreadyExistsException {

		this.persistentIdentity = builder.persistentIdentity;
		this.parentDomain = builder.parentDomain;
		this.parentDomain.addDomainInstance(this);
		this.domainInstanceName = builder.domainInstanceName;
		this.domainInstanceVersion = builder.domainInstanceVersion;
		this.domainInstanceDescription = builder.domainInstanceDescription;
		this.releasedDate = builder.releasedDate;
		this.releaseState = builder.releaseState;
		this.productionState = builder.productionState;
		this.softwarePriorityLevel = builder.softwarePriorityLevel;
		
		this.ecuHardwares = builder.ecuHardwares;
		Iterator<EcuHardware> ecuHardwaresIterator = this.ecuHardwares.iterator();
		while (ecuHardwaresIterator.hasNext()) {

			EcuHardware ecuHardware = ecuHardwaresIterator.next();
			ecuHardware.addDomainInstance(this);
		}
		 
		
		this.ecuSoftwares = builder.ecuSoftwares;
		Iterator<EcuSoftware> ecuSoftwaresIterator = ecuSoftwares.iterator();
		while (ecuSoftwaresIterator.hasNext()) {

			EcuSoftware ecuSoftware = ecuSoftwaresIterator.next();
			ecuSoftware.addDomainInstance(this);
		}

		this.applications = builder.applications;
		Iterator<Application> applicationsIterator = this.applications.iterator();
		while (applicationsIterator.hasNext()) {
			Application application = applicationsIterator.next();
			application.addDomainInstance(this);
		}
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentDomain,
			domainInstanceName,
			domainInstanceVersion
		);
	}

	public void validate(List<String> validationMessages) {

		if (parentDomain == null) {
			validationMessages.add(getClassAndIdentity() + "'parentDomain' must be specified.");
		}

		if (domainInstanceName == null || domainInstanceName.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'domainInstanceName' must be specified.");
		}

		if (domainInstanceVersion == null || domainInstanceVersion.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'domainInstanceVersion' must be specified.");
		}

		if (ecuHardwares == null || ecuHardwares.isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "At least one EcuHardware must be specified.");
		} else {
			Iterator<EcuHardware> ecuHardwaresIterator = ecuHardwares.iterator();
			while (ecuHardwaresIterator.hasNext()) {

				EcuHardware domainInstanceEcuHardware = ecuHardwaresIterator.next();
				domainInstanceEcuHardware.validate(validationMessages);
			}
		}

		if (ecuSoftwares == null || ecuSoftwares.isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "At least one EcuSoftware must be specified.");
		} else {
			Iterator<EcuSoftware> ecuSoftwaresIterator = ecuSoftwares.iterator();
			while (ecuSoftwaresIterator.hasNext()) {

				EcuSoftware ecuSoftware = ecuSoftwaresIterator.next();
				ecuSoftware.validate(validationMessages);
			}
		}

		Iterator<Application> applicationsIterator = applications.iterator();
		while (applicationsIterator.hasNext()) {

			Application application = applicationsIterator.next();
			application.validate(validationMessages);
		}
	}

	public Domain getParentDomain() {
		return parentDomain;
	}

	public String getDomainInstanceName() {
		return domainInstanceName;
	}

	public String getDomainInstanceVersion() {
		return domainInstanceVersion;
	}

	public String getDomainInstanceDescription() {
		return domainInstanceDescription;
	}

	public Timestamp getReleasedDate() {
		return new Timestamp(releasedDate.getTime());
	}

	public ReleaseState getReleaseState() {
		return releaseState;
	}

	public ProductionState getProductionState() {
		return productionState;
	}

	public SoftwarePriorityLevel getSoftwarePriorityLevel() {
		return softwarePriorityLevel;
	}
	
	public List<EcuHardware> getEcuHardwares() {
		List<EcuHardware> list = new ArrayList<>();
		list.addAll(this.ecuHardwares);
		Collections.sort(list);
		return list;
	}

	public List<EcuSoftware> getEcuSoftwares() {
		List<EcuSoftware> list = new ArrayList<>();
		list.addAll(this.ecuSoftwares);
		Collections.sort(list);
		return list;
	}

	public List<Application> getApplications() {
		List<Application> list = new ArrayList<>();
		list.addAll(this.applications);
		Collections.sort(list);
		return list;
	}

	public boolean addApplications(Set<Application> applications) {
		return this.applications.addAll(applications);
	}

	public Set<BinaryMetadataContainer> getAllBinaryMetadataContainers() {

		Set<BinaryMetadataContainer> binaryMetadataContainers = new HashSet<>();

		// Collect all the "binary metadata containers" for this domain instance.  That is, all EcuSoftwares, for which
		// there is a 1 to 1 with BinaryMetadata and Applications, for which there is a 1 to many with BinaryMetadata
		Iterator<EcuSoftware> ecuSoftwareIterator = this.ecuSoftwares.iterator();
		while (ecuSoftwareIterator.hasNext()) {

			binaryMetadataContainers.add(ecuSoftwareIterator.next());
		}

		Iterator<Application> applicationIterator = this.applications.iterator();
		while (applicationIterator.hasNext()) {

			binaryMetadataContainers.add(applicationIterator.next());
		}

		return binaryMetadataContainers;
	}
	
	public Set<EcuNode> getEcuNodes() {
		
		Set<EcuNode> set = new TreeSet<>();
		Iterator<EcuHardware> ecuHardwareIterator = this.ecuHardwares.iterator();
		while (ecuHardwareIterator.hasNext()) {
			
			EcuHardware ecuHardware = ecuHardwareIterator.next();
			EcuNode ecuNode = ecuHardware.getEcuNode();
			if (!set.contains(ecuNode)) {
				set.add(ecuNode);
			}
		}

		Iterator<EcuSoftware> ecuSoftwareIterator = this.ecuSoftwares.iterator();
		while (ecuSoftwareIterator.hasNext()) {
			
			EcuSoftware ecuSoftware = ecuSoftwareIterator.next();
			EcuNode ecuNode = ecuSoftware.getEcuNode();
			if (!set.contains(ecuNode)) {
				set.add(ecuNode);
			}
		}
		return set;
	}
	
	public Set<String> getEcuHardwarePartNumbersForEcuNode(EcuNode ecuNode) {
		
		Set<String> set = new TreeSet<>();
		Iterator<EcuHardware> ecuHardwareIterator = this.ecuHardwares.iterator();
		while (ecuHardwareIterator.hasNext()) {
			
			EcuHardware ecuHardware = ecuHardwareIterator.next();
			if (ecuHardware.getEcuNode().equals(ecuNode)) {
				set.add(ecuHardware.getEcuHardwarePartNumber());
			}
		}
		return set;
	}

	public Map<String,String> getEcuSoftwarePartNumbersForEcuNode(EcuNode ecuNode) {
		
		Map<String, String> map = new TreeMap<>();
		Iterator<EcuSoftware> ecuSoftwareIterator = this.ecuSoftwares.iterator();
		while (ecuSoftwareIterator.hasNext()) {
			
			EcuSoftware ecuSoftware = ecuSoftwareIterator.next();
			if (ecuSoftware.getEcuNode().equals(ecuNode)) {
				
				BinaryMetadata binaryMetadata = ecuSoftware.getBinaryMetadata();
				String didAddress = binaryMetadata.getDidAddress();
				String ecuSoftwarePartNumber = ecuSoftware.getEcuSoftwarePartNumber();
				map.put(didAddress, ecuSoftwarePartNumber);
			}
		}
		return map;
	}
	
	/**
	 * https://api.autonomic.ai/1/resources/<tenant_id>/vehicles?q=modules["APIM"].dids["D1D0"] == "v1" AND modules["BCM"].dids["D1D1"] == "v1" AND modules["ECM"].dids["D1D2"] == "v1"
	 * 
	 * @return
	 */
	public String buildTmcVehicleDiscoveryQuery() {
		
		StringBuilder sb = new StringBuilder();
		Iterator<EcuNode> ecuNodeIterator = this.getEcuNodes().iterator();
		while (ecuNodeIterator.hasNext()) {
			
			EcuNode ecuNode = ecuNodeIterator.next();
			sb.append(LEFT_PAREN);
			sb.append(LEFT_PAREN);
			Iterator<String> ecuHardwarePartNumberIterator = this.getEcuHardwarePartNumbersForEcuNode(ecuNode).iterator();
			while (ecuHardwarePartNumberIterator.hasNext()) {
				
				sb.append(MODULES_PREFIX);
				sb.append(ecuNode.getEcuAcronymName());
				sb.append(DIDS_PREFIX);
				sb.append(HW_DID_ADDRESS);
				sb.append(DIDS_SUFFIX);
				sb.append(ecuHardwarePartNumberIterator.next());
				sb.append(DOUBLE_QUOTE);
				if (ecuHardwarePartNumberIterator.hasNext()) {
					sb.append(OR);
				}
			}
			sb.append(RIGHT_PAREN);
			sb.append(AND);
			sb.append(LEFT_PAREN);
			Map<String, String> map = this.getEcuSoftwarePartNumbersForEcuNode(ecuNode);
			Iterator<Map.Entry<String, String>> ecuSoftwarePartNumberIterator = map.entrySet().iterator();
			while (ecuSoftwarePartNumberIterator.hasNext()) {

				Map.Entry<String, String> entry = ecuSoftwarePartNumberIterator.next();
				String didAddress = entry.getKey();
				String didValue = entry.getValue();
				sb.append(MODULES_PREFIX);
				sb.append(ecuNode.getEcuAcronymName());
				sb.append(DIDS_PREFIX);
				sb.append(didAddress);
				sb.append(DIDS_SUFFIX);
				sb.append(didValue);
				sb.append(DOUBLE_QUOTE);
				if (ecuSoftwarePartNumberIterator.hasNext()) {
					sb.append(AND);
				}
				
			}
			sb.append(RIGHT_PAREN);
			sb.append(RIGHT_PAREN);
		}
		return sb.toString();
	}
	
	public static final class DomainInstanceBuilder {

		private Long persistentIdentity;
		private Domain parentDomain;
		private String domainInstanceName;
		private String domainInstanceVersion;

		private String domainInstanceDescription;
		private Timestamp releasedDate;
		private ReleaseState releaseState;
		private ProductionState productionState;
		private SoftwarePriorityLevel softwarePriorityLevel;

		private Set<EcuHardware> ecuHardwares = new TreeSet<>();
		private Set<EcuSoftware> ecuSoftwares = new TreeSet<>();
		private Set<Application> applications = new TreeSet<>();

		public DomainInstanceBuilder() {
		}

		public DomainInstanceBuilder withPersistentIdentity(Long persistentIdentity) {
			this.persistentIdentity = persistentIdentity;
			return this;
		}

		public DomainInstanceBuilder withParentDomain(Domain parentDomain) {
			this.parentDomain = parentDomain;
			return this;
		}

		public DomainInstanceBuilder withDomainInstanceName(String domainInstanceName) {
			this.domainInstanceName = domainInstanceName;
			return this;
		}

		public DomainInstanceBuilder withDomainInstanceVersion(String domainInstanceVersion) {
			this.domainInstanceVersion = domainInstanceVersion;
			return this;
		}

		public DomainInstanceBuilder withDomainInstanceDescription(String domainInstanceDescription) {
			this.domainInstanceDescription = domainInstanceDescription;
			return this;
		}

		public DomainInstanceBuilder withReleasedDate(Timestamp releasedDate) {
			this.releasedDate = new Timestamp(releasedDate.getTime());
			return this;
		}

		public DomainInstanceBuilder withReleaseState(ReleaseState releaseState) {
			this.releaseState = releaseState;
			return this;
		}

		public DomainInstanceBuilder withProductionState(ProductionState productionState) {
			this.productionState = productionState;
			return this;
		}

		public DomainInstanceBuilder withSoftwarePriorityLevel(SoftwarePriorityLevel softwarePriorityLevel) {
			this.softwarePriorityLevel = softwarePriorityLevel;
			return this;
		}

		public DomainInstanceBuilder withEcuHardwares(Set<EcuHardware> ecuHardwares) {
			this.ecuHardwares = ecuHardwares;
			return this;
		}

		public DomainInstanceBuilder withEcuSoftwares(Set<EcuSoftware> ecuSoftwares) {
			this.ecuSoftwares = ecuSoftwares;
			return this;
		}

		public DomainInstanceBuilder withApplications(Set<Application> applications) {
			this.applications = applications;
			return this;
		}

		public DomainInstance build() throws EntityAlreadyExistsException {
			return new DomainInstance(this);
		}
	}
}
