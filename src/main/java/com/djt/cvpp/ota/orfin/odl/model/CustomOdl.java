/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class CustomOdl extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private Odl parentOdl;
	private String customOdlName;
	private Set<Node> nodes = new TreeSet<>();	
	
	private CustomOdl(CustomOdlBuilder builder) throws EntityAlreadyExistsException, ValidationException {
		
		this.parentOdl = builder.parentOdl;
		this.parentOdl.addCustomOdl(this);
		this.customOdlName = builder.customOdlName;
		
		this.nodes.addAll(builder.nodes);
	}
	
	public String getNaturalIdentity() {

		return AbstractEntity.buildNaturalIdentity(this.parentOdl, this.customOdlName);
	}

	public void validate(List<String> validationMessages) {

		if (parentOdl == null) {
			validationMessages.add("parentOdl must be specified.");
		}
		
		if (customOdlName == null) {
			validationMessages.add("customOdlName must be specified.");
		}
		
		if (nodes == null || nodes.isEmpty()) {
			validationMessages.add("At least one Node must be specified.");
		} else if (parentOdl != null) {
			
			Set<Node> parentOdlNodes = parentOdl.getAllNodes();
			Iterator<Node> iterator = nodes.iterator();
			while (iterator.hasNext()) { 
			
				Node node = iterator.next();
				if (!parentOdlNodes.contains(node)) {
					throw new IllegalStateException("Cannot associate node: [" + node + "] to custom ODL: [" + this + "] because the Parent ODL: [" + parentOdl + "] does not contain it.  Parent ODL Nodes are: [" + parentOdlNodes + "].");
				}
			}
		}
	}
	
	public void addNode(Node node) throws EntityAlreadyExistsException{
		
		if (this.nodes.contains(node)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has an association with node: " + node);
		}
		
		this.nodes.add(node);
	}

	public Odl getParentOdl() {
		return parentOdl;
	}

	public void setParentOdl(Odl parentOdl) {
		this.parentOdl = parentOdl;
	}

	public String getCustomOdlName() {
		return customOdlName;
	}

	public void setCustomOdlName(String customOdlName) {
		this.customOdlName = customOdlName;
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public static final class CustomOdlBuilder {

		private Odl parentOdl;
		private String customOdlName;
		private Set<Node> nodes = new TreeSet<>();	

		public CustomOdlBuilder() {
		}

		public CustomOdlBuilder withParentOdl(Odl parentOdl) {
			this.parentOdl = parentOdl;
			return this;
		}
		
		public CustomOdlBuilder withCustomOdlName(String customOdlName) {
			this.customOdlName = customOdlName;
			return this;
		}

		public CustomOdlBuilder withNodes(Set<Node> nodes) {
			this.nodes = nodes;
			return this;
		}
		
		public CustomOdl build() throws EntityAlreadyExistsException, ValidationException {
			return new CustomOdl(this);
		}
	}	
}
