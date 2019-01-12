/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Odl extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private String odlName;
	private Set<Network> networks = new TreeSet<>(); 
	private Set<ProgramModelYear> programModelYears = new TreeSet<>();
	private Set<EcgSignal> ecgSignals = new TreeSet<>();
	private Set<CustomOdl> customOdls = new TreeSet<>();
	
	private Odl(OdlBuilder builder) {
		
		this.odlName = builder.odlName;
		
		this.networks = builder.networks;
		Iterator<Network> networksIterator = this.networks.iterator();
		while (networksIterator.hasNext()) {
			Network network = networksIterator.next();
			network.setParentOdl(this);
		}
		
		this.programModelYears = builder.programModelYears;
		
		this.ecgSignals = builder.ecgSignals;
		Iterator<EcgSignal> ecgSignalsIterator = this.ecgSignals.iterator();
		while (ecgSignalsIterator.hasNext()) {
			EcgSignal ecgSignal = ecgSignalsIterator.next();
			ecgSignal.setParentOdl(this);
		}

		this.customOdls = builder.customOdls;
		Iterator<CustomOdl> customOdlIterator = this.customOdls.iterator();
		while (customOdlIterator.hasNext()) {
			CustomOdl customOdl = customOdlIterator.next();
			customOdl.setParentOdl(this);
		}
	}
	
	public String getNaturalIdentity() {

		return this.odlName;
	}

	public void validate(List<String> validationMessages) {

		if (odlName == null) {
			validationMessages.add("odlName must be specified.");
		}
		
		if (networks == null || networks.isEmpty()) {
			validationMessages.add("At least one Network must be specified.");
		} else {
			Iterator<Network> networksIterator = this.networks.iterator();
			while (networksIterator.hasNext()) {
				Network network = networksIterator.next();
				network.validate(validationMessages);
			}
		}
		
		if (programModelYears == null || programModelYears.isEmpty()) {
			validationMessages.add("At least one ProgramModelYear must be specified.");
		}		
	}
	
	public String getOdlName() {
		return this.odlName;
	}
	
	public void setOdlName(String odlName) {
		this.odlName = odlName;
	}

	public List<CustomOdl> getCustomOdls() {
		List<CustomOdl> list = new ArrayList<>();
		list.addAll(this.customOdls);
		return list;
	}

	public void addCustomOdl(CustomOdl customOdl) throws EntityAlreadyExistsException {
		
		if (this.customOdls.contains(customOdl)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has an association with customOdl: " + customOdl);
		}
		customOdl.setParentOdl(this);
		this.customOdls.add(customOdl);
	}
	
	public CustomOdl getCustomOdlByName(String customOdlName) throws EntityDoesNotExistException {
		
		Iterator<CustomOdl> iterator = this.customOdls.iterator();
		while (iterator.hasNext()) {
			
			CustomOdl customOdl = iterator.next();
			if (customOdl.getCustomOdlName().equals(customOdlName)) {
				return customOdl;
			}
		}
		throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have customOdl with name: " + customOdlName);
	}
	
	public List<EcgSignal> getEcgSignals() {
		List<EcgSignal> list = new ArrayList<>();
		list.addAll(this.ecgSignals);
		return list;
	}

	public void addEcgSignal(EcgSignal ecgSignal) throws EntityAlreadyExistsException {
		
		if (this.ecgSignals.contains(ecgSignal)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has an association with EcgSignal: " + ecgSignal);
		}
		ecgSignal.setParentOdl(this);
		this.ecgSignals.add(ecgSignal);
	}

	public boolean removeEcgSignal(EcgSignal ecgSignal) {
		
		return this.ecgSignals.remove(ecgSignal);
	}
	
	public List<ProgramModelYear> getProgramModelYears() {
		List<ProgramModelYear> list = new ArrayList<>();
		list.addAll(this.programModelYears);
		return list;
	}

	public void addProgramModelYear(ProgramModelYear programModelYear) throws EntityAlreadyExistsException {
		
		if (this.programModelYears.contains(programModelYear)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has an association with programModelYear: " + programModelYear);
		}
		
		this.programModelYears.add(programModelYear);
	}
	
	public void removeProgramModelYear(ProgramModelYear programModelYear) {
		
		programModelYear.setPolicySet(null);
		this.programModelYears.remove(programModelYear);
	}
	
	public List<Network> getNetworks() {
		List<Network> list = new ArrayList<>();
		list.addAll(this.networks);
		return list;
	}
	
	public void addNetwork(Network network) throws EntityAlreadyExistsException {
		
		if (this.networks.contains(network)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has network: " + network);
		}
		this.networks.add(network);
		network.setParentOdl(this);
	}
	
	public Network getNetworkByName(String networkName) throws EntityDoesNotExistException {
		
		Iterator<Network> iterator = this.networks.iterator();
		while (iterator.hasNext()) {
			
			Network network = iterator.next();
			if (network.getNetworkName().equals(networkName)) {
				return network;
			}
		}
		throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have network with name: " + networkName);
	}
	
	public void addNode(String networkName, Node node) throws EntityDoesNotExistException, EntityAlreadyExistsException {
		
		Network network = getNetworkByName(networkName);
		network.addNode(node);
	}
	
	public void addIgnoredDidsToNode(String networkName, String nodeAcronym, String nodeAddress, List<String> ignoredDids) throws EntityDoesNotExistException, EntityAlreadyExistsException {
		
		Network network = getNetworkByName(networkName);
		Node node = network.getNodeByAcronymAndAddress(nodeAcronym, nodeAddress);
		node.setIgnoredDids(ignoredDids);
	}
	
	public Set<Node> getAllNodes() {
		
		Set<Node> set = new HashSet<>();
		Iterator<Network> iterator = this.networks.iterator();
		while (iterator.hasNext()) {

			Network network = iterator.next();
			set.addAll(network.getAllNodes());
		}
		return set;
	}
	
	public Set<Node> getNodeSubset(List<String> customOdlNodeList) throws EntityDoesNotExistException {
		
		Set<Node> subsetNodes = new HashSet<>();
		
		Map<String, Node> map = new HashMap<>();
		Iterator<Node> nodeIterator = this.getAllNodes().iterator();
		while (nodeIterator.hasNext()) {
			
			Node node = nodeIterator.next();
			map.put(node.getNaturalIdentity(), node);
		}
		
		// customOdlNodeList A list of nodes where each node is of the form: <code>nodeAcronym_nodeAddress</code> and node *must* already be associated to the parent, or master, ODL 
		// e.g. AA_00,BB_01,CC_02
		Iterator<String> iterator = customOdlNodeList.iterator();
		while (iterator.hasNext()) {
			
			String naturalIdentity = iterator.next();
			Node node = map.get(naturalIdentity);
			if (node == null) {
				throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have a node with identity: " + naturalIdentity);	
			}
			subsetNodes.add(node);
		}
		
		return subsetNodes;
	}
	
	public static final class OdlBuilder {

		private String odlName;
		private Set<Network> networks = new TreeSet<>(); 
		private Set<ProgramModelYear> programModelYears = new TreeSet<>();	
		private Set<EcgSignal> ecgSignals = new TreeSet<>();
		private Set<CustomOdl> customOdls = new TreeSet<>();

		public OdlBuilder() {
		}
		
		public OdlBuilder withOdlName(String odlName) {
			this.odlName = odlName;
			return this;
		}

		public OdlBuilder withNetworks(Set<Network> networks) {
			this.networks = networks;
			return this;
		}
		
		public OdlBuilder withProgramModelYears(Set<ProgramModelYear> programModelYears) {
			this.programModelYears = programModelYears;
			return this;
		}

		public OdlBuilder withEcgSignals(Set<EcgSignal> ecgSignals) {
			this.ecgSignals = ecgSignals;
			return this;
		}

		public OdlBuilder withCustomOdls(Set<CustomOdl> customOdls) {
			this.customOdls = customOdls;
			return this;
		}
		
		public Odl build() {
			return new Odl(this);
		}
	}	
}
