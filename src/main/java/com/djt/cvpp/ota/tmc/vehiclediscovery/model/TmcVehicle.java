/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.model;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
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
public class TmcVehicle extends TmcEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	public static final String POLICY_OVERRIDE_PREFIX = "POLICY_OVERRIDE_";

	
	public static final String PROGRAM_CODE = "programCode";
	public static final String MODEL_YEAR = "modelYear";
	public static final String UNDERSCORE = "_";

	
	private Fleet parentFleet;
	private String vin;
	private String make;
	private String model;
	private String year;
	private String type;
	private Instant modifiedTimestamp;
	private Set<Node> nodes = new TreeSet<>();
	
	public TmcVehicle(
		UUID tenantUuid,	
		UUID uuid,
		String name,
		String description,
		URI uri,
		Instant createdTimestamp,
		Author author,
		Set<Tag> tags,
		Fleet parentFleet,
		String vin,
		String make,
		String model,
		String year,
		String type,
		Instant modifiedTimestamp) {
		super(
			tenantUuid,
			uuid,
			name,
			description,
			uri,
			createdTimestamp,
			author,
			tags);
		this.parentFleet = parentFleet;
		this.vin = vin;
		this.make = make;
		this.model = model;
		this.year = year;
		this.type = type;
		this.modifiedTimestamp = modifiedTimestamp;
	}

	public void validate(List<String> validationMessages) {
		
		super.validate(validationMessages);

		if (parentFleet == null) {
			validationMessages.add("uuid must be specified.");
		}

		if (vin == null || vin.trim().isEmpty()) {
			validationMessages.add("vin must be specified.");
		}
		
		if (make == null || make.trim().isEmpty()) {
			validationMessages.add("make must be specified.");
		}

		if (model == null || model.trim().isEmpty()) {
			validationMessages.add("model must be specified.");
		}

		if (year == null || year.trim().isEmpty()) {
			validationMessages.add("year must be specified.");
		}

		if (type == null || type.trim().isEmpty()) {
			validationMessages.add("type must be specified.");
		}

		if (modifiedTimestamp == null) {
			validationMessages.add("modifiedTimestamp must be specified.");
		}

		if (nodes == null || nodes.isEmpty()) {
			validationMessages.add("At least one Node must be specified.");
		} else {
			Iterator<Node> nodeIterator = nodes.iterator();
			while (nodeIterator.hasNext()) {

				Node node = nodeIterator.next();
				node.validate(validationMessages);
			}
		}
	}
		
	public Fleet getParentFleet() {
		return parentFleet;
	}

	public String getVin() {
		return vin;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public String getYear() {
		return year;
	}

	public String getType() {
		return type;
	}

	public Instant getModifiedTimestamp() {
		return modifiedTimestamp;
	}

	public List<Node> getNodes() {
		List<Node> list = new ArrayList<>();
		list.addAll(nodes);
		return list;
	}
	
	public void setNodes(Set<Node> nodes) {
		this.nodes.clear();
		this.nodes.addAll(nodes);
		Iterator<Node> iterator = this.nodes.iterator();
		while (iterator.hasNext()) {
			
			Node node = iterator.next();
			node.setParentVehicle(this);
		}
	}
	
	public Node getNode(String nodeName, String nodeAddress) {

		Iterator<Node> iterator = this.nodes.iterator();
		while (iterator.hasNext()) {
			
			Node node = iterator.next();
			if (node.getName().equals(nodeName) && node.getAddress().equals(nodeAddress)) {
				return node;
			}
		}
		return null;
	}
	
	public boolean hasDidValue(
		String nodeName,
		String nodeAddress,
		String didAddress,
		String didValue) {
		
		boolean hasDidValue = false;
		Node node = getNode(nodeName, nodeAddress);
		if (node != null) {

			Did did = node.getDid(didAddress);
			if (did != null && did.getValue().equals(didValue)) {
				
				hasDidValue = true;
			}		
		}
		return hasDidValue;
	}
	
	/**
	 * NOTE: It is assumed that vehicle level policy overrides will be encoded as tags. (so, if the tag is a policy, we need to identify it by having a "POLICY_OVERRIDE_" prefix.
	 * It is hoped that the TMC vehicle object be modified to store these as first class entities that would have the following attributes:
	 * 1. AbstractPolicy Name (maps to tag name now)
	 * 2. AbstractPolicy Value (maps to tag value now)
	 * 3. AbstractPolicy Value Type (either STRING or NUMERIC) 
	 * 
	 * @return
	 */
	public List<Tag> getVehicleLevelPolicyOverrides() {
		
		List<Tag> vehicleLevelPolicyOverrides = new ArrayList<>();
		
		Iterator<Tag> iterator = this.getTags().iterator();
		while (iterator.hasNext()) {
			
			Tag tag = iterator.next();
			if (tag.getName().startsWith(POLICY_OVERRIDE_PREFIX)) {
				vehicleLevelPolicyOverrides.add(tag);
			}
		}
		
		return vehicleLevelPolicyOverrides;
	}

	/**
	 * 
	 * @return
	 */
	public String getProgramCode() {
		
		Iterator<Tag> iterator = this.getTags().iterator();
		while (iterator.hasNext()) {
			
			Tag tag = iterator.next();
			if (tag.getName().equals(PROGRAM_CODE)) {
				return tag.getValue();
			}
		}
		throw new RuntimeException(
			"TmcVehicle: [" 
			+ this 
			+ "] does not contain required tag for programCode. Current tags are: [" 
			+ this.getTags() 
			+ "].");
	}

	/**
	 * 
	 * @return
	 */
	public Integer getModelYear() {
		
		Iterator<Tag> iterator = this.getTags().iterator();
		while (iterator.hasNext()) {
			
			Tag tag = iterator.next();
			if (tag.getName().equals(MODEL_YEAR)) {
				return Integer.valueOf(tag.getValue().trim());
			}
		}
		throw new RuntimeException(
			"TmcVehicle: [" 
			+ this 
			+ "] does not contain required tag for modelYear. Current tags are: [" 
			+ this.getTags() 
			+ "].");
	}
	
	/**
	 * 
	 * @return A string of the form XXXX_YYYY where XXXX is the programCode and modelYear is the 4 digit model year
	 */
	public String getProgramCodeModelYear() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(getProgramCode());
		sb.append(UNDERSCORE);
		sb.append(getModelYear());
		return sb.toString();
	}
}
