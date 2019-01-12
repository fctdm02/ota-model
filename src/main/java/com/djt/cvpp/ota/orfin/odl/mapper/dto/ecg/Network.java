/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Network {
	
	private List<SpecificationCategory> specificationCategory = new ArrayList<>();
	private String networkName;
	private String networkProtocol;
	private String networkDataRate;
	private String pins;
	private String dlcname;
}
