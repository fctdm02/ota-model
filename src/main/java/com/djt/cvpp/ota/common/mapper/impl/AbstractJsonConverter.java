/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.common.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractJsonConverter {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public ObjectMapper getObjectMapper() {
		return this.mapper;
	}
}
