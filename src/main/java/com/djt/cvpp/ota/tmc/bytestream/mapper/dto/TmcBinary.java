/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.bytestream.mapper.dto;

import java.util.Arrays;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class TmcBinary {
	
	private UUID binaryId;
	private String description;
	private byte[] byteArray;
	
	public byte[] getByteArray() {
		return Arrays.copyOf(byteArray, byteArray.length);
	}
	
	public void setByteArray(byte[] byteArray) {
		this.byteArray = Arrays.copyOf(byteArray, byteArray.length);
	}	
}
