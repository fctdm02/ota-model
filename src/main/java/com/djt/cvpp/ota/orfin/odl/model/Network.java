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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Network extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private Odl parentOdl;
	private String networkName;
	private String protocol;
	private String dataRate;
	private String dclName;
	private String networkPins;
	private Set<Node> nodes = new TreeSet<>();
	
	private Network(NetworkBuilder builder) {
		
		this.parentOdl = builder.parentOdl;
		this.networkName = builder.networkName;
		this.protocol = builder.protocol;
		this.dataRate = builder.dataRate;
		this.dclName = builder.dclName;
		this.networkPins = builder.networkPins;
		this.nodes = builder.nodes;
		Iterator<Node> nodesIterator = nodes.iterator();
		while (nodesIterator.hasNext()) {

			Node node = nodesIterator.next();
			node.setParentNetwork(this);
		}
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentOdl,
			networkName);
	}

	public void validate(List<String> validationMessages) {

		if (parentOdl == null) {
			validationMessages.add("parentOdl must be specified.");
		}
		
		if (networkName == null || networkName.trim().isEmpty()) {
			validationMessages.add("networkName must be specified.");
		}

		if (protocol == null || protocol.trim().isEmpty()) {
			validationMessages.add("protocol must be specified.");
		}

		if (dataRate == null || dataRate.trim().isEmpty()) {
			validationMessages.add("dataRate must be specified.");
		}

		if (dclName == null || dclName.trim().isEmpty()) {
			validationMessages.add("dclName must be specified.");
		}

		if (networkPins == null || networkPins.trim().isEmpty()) {
			validationMessages.add("networkPins must be specified.");
		}
		
		if (nodes == null) {
			validationMessages.add("At least node must be specified.");
		} else {
			Iterator<Node> nodesIterator = nodes.iterator();
			while (nodesIterator.hasNext()) {

				Node node = nodesIterator.next();
				node.validate(validationMessages);
			}
		}
	}
	
	protected void setParentOdl(Odl parentOdl) {
		this.parentOdl = parentOdl;
	}

	public Odl getParentOdl() {
		return parentOdl;
	}

	public String getNetworkName() {
		return networkName;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getDataRate() {
		return dataRate;
	}

	public String getDclName() {
		return dclName;
	}

	public String getNetworkPins() {
		return networkPins;
	}

	public List<Node> getNodes() {
		List<Node> list = new ArrayList<>();
		list.addAll(this.nodes);
		return list;
	}

	public Set<Node> getAllNodes() {
		
		Set<Node> set = new HashSet<>();
		set.addAll(this.nodes);
		return set;
	}
	
	public void addNode(Node node) throws EntityAlreadyExistsException {
		
		if (this.nodes.contains(node)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has node: " + node);
		}
		this.nodes.add(node);
		node.setParentNetwork(this);
	}
	
	public Node getNodeByAcronymAndAddress(String nodeAcronym, String nodeAddress) throws EntityDoesNotExistException {
		
		Iterator<Node> iterator = this.nodes.iterator();
		while (iterator.hasNext()) {
			
			Node node = iterator.next();
			if (node.getAcronym().equals(nodeAcronym) && node.getAddress().equals(nodeAddress)) {
				return node;
			}
		}
		throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have node: " + nodeAcronym + nodeAddress);
	}
	
	public static final class NetworkBuilder {

		private Odl parentOdl;
		private String networkName;
		private String protocol;
		private String dataRate;
		private String dclName;
		private String networkPins;
		private Set<Node> nodes = new TreeSet<>();
		
		public NetworkBuilder() {
		}
		
		public NetworkBuilder withParentOdl(Odl parentOdl) {
			this.parentOdl = parentOdl;
			return this;
		}
		
		public NetworkBuilder withNetworkName(String networkName) {
			this.networkName = networkName;
			return this;
		}

		public NetworkBuilder withProtocol(String protocol) {
			this.protocol = protocol;
			return this;
		}

		public NetworkBuilder withDataRate(String dataRate) {
			this.dataRate = dataRate;
			return this;
		}

		public NetworkBuilder withDclName(String dclName) {
			this.dclName = dclName;
			return this;
		}

		public NetworkBuilder withNetworkPins(String networkPins) {
			this.networkPins = networkPins;
			return this;
		}

		public NetworkBuilder withNodes(Set<Node> nodes) {
			this.nodes = nodes;
			return this;
		}
		
		public Network build() {
			return new Network(this);
		}
	}
}
