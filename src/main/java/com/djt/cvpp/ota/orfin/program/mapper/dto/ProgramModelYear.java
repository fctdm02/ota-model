/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.program.mapper.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.odl.mapper.dto.Odl;
import com.djt.cvpp.ota.orfin.policy.mapper.dto.PolicySet;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramModelYear {
	
	private Program parentProgram;
	private ModelYear parentModelYear;
	
	private Odl odl;
	private PolicySet policySet;
	
	private List<DeliveryRuleSet> deliveryRuleSets = new ArrayList<>();
}
