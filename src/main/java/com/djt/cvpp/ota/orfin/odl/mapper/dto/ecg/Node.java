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
public class Node {
	
	private String address;
	private String OVTP;
	private String ovtpDestinationAddress;
    private String networkName;
    private String networkProtocol;
    private Integer networkDataRate;
    private List<Ecuacronym> ecuacronym = new ArrayList<>();
    
    public void addEcuacronym(Ecuacronym ecuacronym) {
    	this.ecuacronym.add(ecuacronym);
    }
}
