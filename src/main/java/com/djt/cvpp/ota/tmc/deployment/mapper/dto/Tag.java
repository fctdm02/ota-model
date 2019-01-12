/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.deployment.mapper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Tag {
	
	private String name;
	private String value;
}
