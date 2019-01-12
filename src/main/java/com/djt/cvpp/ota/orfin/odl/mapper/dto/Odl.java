/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.mapper.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Odl {
	
	private String odlName;
	private List<Network> networks = new ArrayList<>(); 
	private List<ProgramModelYear> programModelYears = new ArrayList<>();
	private List<EcgSignal> ecgSignals = new ArrayList<>();
	private List<CustomOdl> customOdls = new ArrayList<>();
}	
