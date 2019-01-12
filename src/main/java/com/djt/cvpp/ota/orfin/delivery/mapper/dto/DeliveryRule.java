/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.delivery.mapper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryRule {
	
	private Boolean allowable;
	private Integer precedenceLevel;
	private String deliveryAudience;
	private String deliveryMethod;
	private String connectionType;
}
