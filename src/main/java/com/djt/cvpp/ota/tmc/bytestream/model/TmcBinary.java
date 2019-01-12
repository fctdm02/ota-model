/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.bytestream.model;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TmcBinary extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private UUID binaryId;
	private String description;
	private byte[] byteArray;

	public TmcBinary(
		UUID binaryId,
		String description,
		byte[] byteArray) {
		
		this.binaryId = binaryId;
		this.description = description;
		this.byteArray = Arrays.copyOf(byteArray, byteArray.length);
	}

	public String getNaturalIdentity() {
		return binaryId.toString();
	}

	public void validate(List<String> validationMessages) {
		
		validateNotNull(validationMessages, "binaryId", binaryId);
		validateNotNull(validationMessages, "description", description);
		validateNotNull(validationMessages, "byteArray", byteArray);
	}

	public UUID getBinaryId() {
		return binaryId;
	}

	public String getDescription() {
		return description;
	}

	public byte[] getByteArray() {
		return Arrays.copyOf(byteArray, byteArray.length);
	}	
}
