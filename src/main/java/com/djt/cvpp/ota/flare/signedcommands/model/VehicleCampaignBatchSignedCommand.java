/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.signedcommands.model;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VehicleCampaignBatchSignedCommand  extends AbstractEntity {

	private static final long serialVersionUID = 1L;


	private VehicleCampaignBatch parentVehicleCampaignBatch;
	private String nodeAcronym;
	private Integer sequenceNumber;
	private String key;
	private byte[] value;
	private Set<SuCounter> suCounters = new TreeSet<>();
	private Set<CcCounter> ccCounters = new TreeSet<>();

	@SuppressWarnings("unused")
	private VehicleCampaignBatchSignedCommand() {
	}
	
	public VehicleCampaignBatchSignedCommand(
		VehicleCampaignBatch parentVehicleCampaignBatch,
		String nodeAcronym,
		Integer sequenceNumber,
		String key,
		byte[] value) {

		this.parentVehicleCampaignBatch = parentVehicleCampaignBatch;
		this.nodeAcronym = nodeAcronym;
		this.sequenceNumber = sequenceNumber;
		this.key = key;
		this.value = Arrays.copyOf(value, value.length);
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentVehicleCampaignBatch,
			nodeAcronym,
			sequenceNumber
		);
	}

	public void validate(List<String> validationMessages) {

		validateNotNull(validationMessages, "parentVehicleCampaignBatch", parentVehicleCampaignBatch);
		validateNotNull(validationMessages, "nodeAcronym", nodeAcronym); 
		validateNumber(validationMessages, "sequenceNumber", sequenceNumber, Integer.valueOf(0));
		validateNotNull(validationMessages, "key", key); 
		validateNotNull(validationMessages, "value", value);

		
		//  TODO: TDM: Talk to Sushanta about this (as all the signed commands logic needs to be lifted from the MVP code)
		/*
		if (suCounters == null || suCounters.isEmpty()) {
			validationMessages.add("At least one SuCounter must be specified.");
		} else {
			Iterator<SuCounter> iterator = suCounters.iterator();
			while (iterator.hasNext()) {

				SuCounter suCounter = iterator.next();
				suCounter.validate(validationMessages);
			}
		}
		
		if (ccCounters == null || ccCounters.isEmpty()) {
			validationMessages.add("At least one CcCounter must be specified.");
		} else {
			Iterator<CcCounter> iterator = ccCounters.iterator();
			while (iterator.hasNext()) {

				CcCounter ccCounter = iterator.next();
				ccCounter.validate(validationMessages);
			}
		}
		*/
	}
	
	public VehicleCampaignBatch getParentVehicleCampaignBatch() {
		return parentVehicleCampaignBatch;
	}

	public String getNodeAcronym() {
		return nodeAcronym;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public String getKey() {
		return key;
	}

	public byte[] getValue() {
		return Arrays.copyOf(value, value.length);
	}

	public Set<SuCounter> getSuCounters() {
		return suCounters;
	}

	public Set<CcCounter> getCcCounters() {
		return ccCounters;
	}


	// BUSINESS BEHAVIORS
	public void generateSignedCommands() {
		throw new RuntimeException("Not implemented yet.");
	}	
}
