/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Did {
	
	private String did;
	private String privateNetworkDIDFlag;
	private String directConfigurationDIDFlag;
	private String vinspecificDIDFlag;
	private String diddescription;
}
