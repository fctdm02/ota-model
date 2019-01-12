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
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.odl.model.enums.SpecificationCategoryType;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Node extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	public static final Integer DEFAULT_VEHICLE_INHIBIT_ACTIVATION_TIME = Integer.valueOf(30);
	

	private Network parentNetwork;
	private String acronym;
	private String address;
	private String gatewayNodeId;
	private String gatewayType;
	private Boolean hasConditionBasedOnDtc;
	private Boolean isOvtp;
	private String ovtpDestinationAddress;
	private Integer diagnosticSpecificationResponse;
	private SpecificationCategoryType specificationCategoryType;
	
	// These attributes are used for update aggregation validation and/or manifest attributes and are *not* delivered to the vehicle (see the "renderOdlForProgram()" operation on the service interface 
	private Integer activationTime; // time it takes to program (a.k.a. update time)
	private Integer vehicleInhibitActivationTime = Integer.valueOf(DEFAULT_VEHICLE_INHIBIT_ACTIVATION_TIME);  // time that the vehicle is inoperable.
	
	private Set<Did> dids = new TreeSet<>();
	private Set<String> ignoredDids = new TreeSet<>();
	
	private Node(NodeBuilder builder) {
		
		this.parentNetwork = builder.parentNetwork;
		this.specificationCategoryType = builder.specificationCategoryType;
		this.acronym = builder.acronym;
		this.address = builder.address;
		this.gatewayNodeId = builder.gatewayNodeId;
		this.gatewayType = builder.gatewayType;
		this.activationTime = builder.activationTime;
		this.hasConditionBasedOnDtc = builder.hasConditionBasedOnDtc;
		this.isOvtp = builder.isOvtp;
		this.ovtpDestinationAddress = builder.ovtpDestinationAddress;
		this.diagnosticSpecificationResponse = builder.diagnosticSpecificationResponse;
		this.specificationCategoryType = builder.specificationCategoryType;
		this.ignoredDids = builder.ignoredDids;

		this.dids = builder.dids;
		Iterator<Did> didsIterator = dids.iterator();
		while (didsIterator.hasNext()) {

			Did did = didsIterator.next();
			did.setParentNode(this);
		}		
	}
	
	public String getNaturalIdentity() {

		return AbstractEntity.buildNaturalIdentity(
			acronym,
			address);
	}

	public void validate(List<String> validationMessages) {

		if (parentNetwork == null) {
			validationMessages.add("parentNetwork must be specified.");
		}

		if (specificationCategoryType == null) {
			validationMessages.add("specificationCategoryType must be specified.");
		}
		
		if (acronym == null || acronym.trim().isEmpty()) {
			validationMessages.add("acronym must be specified.");
		}

		if (address == null || address.trim().isEmpty()) {
			validationMessages.add("address must be specified.");
		}

		if (gatewayNodeId == null || gatewayNodeId.trim().isEmpty()) {
			validationMessages.add("gatewayNodeId must be specified.");
		}
		
		if (activationTime == null || activationTime.intValue() <= 0) {
			validationMessages.add("activationTime must be a non-zero integer");
		}

		if (vehicleInhibitActivationTime == null || vehicleInhibitActivationTime.intValue() <= 0 || vehicleInhibitActivationTime.intValue() <= activationTime.intValue()) {
			validationMessages.add("vehicleInhibitActivationTime must be a non-zero integer that is greater then the activation time");
		}
		
		if (hasConditionBasedOnDtc == null) {
			validationMessages.add("hasConditionBasedOnDtc must be specified.");
		}
		
		if (isOvtp == null) {
			validationMessages.add("isOvtp must be specified.");
		}
		
		if (dids == null) {
			validationMessages.add("At least one Did must be specified.");
		} else {
			Iterator<Did> didsIterator = dids.iterator();
			while (didsIterator.hasNext()) {

				Did did = didsIterator.next();
				did.validate(validationMessages);
			}
		}
	}

	protected void setParentNetwork(Network parentNetwork) {
		this.parentNetwork = parentNetwork;
	}
	
	public Network getParentNetwork() {
		return parentNetwork;
	}
	
	public Integer getDiagnosticSpecificationResponse() {
		return diagnosticSpecificationResponse;
	}
	
	public SpecificationCategoryType getSpecificationCategoryType() {
		return specificationCategoryType;
	}

	public String getAcronym() {
		return acronym;
	}

	public String getAddress() {
		return address;
	}

	public String getGatewayNodeId() {
		return gatewayNodeId;
	}
	
	public String getGatewayType() {
		return gatewayType;
	}

	public Integer getActivationTime() {
		return activationTime;
	}

	public Integer getVehicleInhibitActivationTime() {
		return vehicleInhibitActivationTime;
	}

	public Boolean getHasConditionBasedOnDtc() {
		return hasConditionBasedOnDtc;
	}

	public Boolean getIsOvtp() {
		return isOvtp;
	}

	public String getOvtpDestinationAddress() {
		return ovtpDestinationAddress;
	}

	public Set<Did> getDids() {
		return dids;
	}
	
	public void addDid(Did did) throws EntityAlreadyExistsException {
		
		if (this.dids.contains(did)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has did: " + did);
		}
		this.dids.add(did);
		did.setParentNode(this);
	}
	
	public List<String> getIgnoredDids() {
		List<String> list = new ArrayList<>();
		list.addAll(ignoredDids);
		return list;
	}
	
	public void setIgnoredDids(List<String> ignoredDids) {
		this.ignoredDids.clear();
		this.ignoredDids.addAll(ignoredDids);
	}

	public static final class NodeBuilder {

		private Network parentNetwork;
		private Integer diagnosticSpecificationResponse;
		private SpecificationCategoryType specificationCategoryType;	
		private String acronym;
		private String address;
		private String gatewayNodeId;
		private String gatewayType;		
		private Integer activationTime; // time it takes to program (a.k.a. update time)
		private Boolean hasConditionBasedOnDtc;
		private Boolean isOvtp;
		private String ovtpDestinationAddress;
		private Set<Did> dids = new HashSet<>();
		private Set<String> ignoredDids = new HashSet<>();
		
		public NodeBuilder() {
		}

		public NodeBuilder withParentNetwork(Network parentNetwork) {
			this.parentNetwork = parentNetwork;
			return this;
		}
		
		public NodeBuilder withSpecificationCategoryType(SpecificationCategoryType specificationCategoryType) {
			this.specificationCategoryType = specificationCategoryType;
			return this;
		}
		
		public NodeBuilder withAcronym(String acronym) {
			this.acronym = acronym;
			return this;
		}

		public NodeBuilder withAddress(String address) {
			this.address = address;
			return this;
		}

		public NodeBuilder withGatewayNodeId(String gatewayNodeId) {
			this.gatewayNodeId = gatewayNodeId;
			return this;
		}

		public NodeBuilder withGatewayType(String gatewayType) {
			this.gatewayType = gatewayType;
			return this;
		}
		
		public NodeBuilder withActivationTime(Integer activationTime) {
			this.activationTime = activationTime;
			return this;
		}

		public NodeBuilder withHasConditionBasedOnDtc(Boolean hasConditionBasedOnDtc) {
			this.hasConditionBasedOnDtc = hasConditionBasedOnDtc;
			return this;
		}
		
		public NodeBuilder withIsOvtp(Boolean isOvtp) {
			this.isOvtp = isOvtp;
			return this;
		}

		public NodeBuilder withOvtpDestinationAddress(String ovtpDestinationAddress) {
			this.ovtpDestinationAddress = ovtpDestinationAddress;
			return this;
		}

		public NodeBuilder withDids(Set<Did> dids) {
			this.dids = dids;
			return this;
		}

		public NodeBuilder withIgnoredDids(Set<String> ignoredDids) {
			this.ignoredDids = ignoredDids;
			return this;
		}
		
		public NodeBuilder withDiagnosticSpecificationResponse(Integer diagnosticSpecificationResponse) {
			this.diagnosticSpecificationResponse = diagnosticSpecificationResponse;
			return this;
		}

		public Node build() {
			return new Node(this);
		}
	}
}
