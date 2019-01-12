/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class PolicySet {
	
	private String policySetName;
	private List<AbstractPolicy> policies = new ArrayList<>();
	private List<ProgramModelYear> programModelYears = new ArrayList<>();
	
	public AbstractPolicy getPolicyByName(String policyName) throws EntityDoesNotExistException {
		
		Iterator<AbstractPolicy> iterator = this.policies.iterator();
		while (iterator.hasNext()) {
			
			AbstractPolicy policy = iterator.next();
			if (policy instanceof VehiclePolicy) {
				VehiclePolicy vehiclePolicy = (VehiclePolicy)policy;
				if (vehiclePolicy.getPolicyName().equals(policyName)) {
					return vehiclePolicy;
				} 
			} else if (policy instanceof CloudPolicy) {
				CloudPolicy cloudPolicy = (CloudPolicy)policy;
				if (cloudPolicy.getPolicyName().equals(policyName)) {
					return cloudPolicy;
				} 
			}
		}
		throw new EntityDoesNotExistException("PolicySet: [" + this.policySetName + "] does not have a policy with name: [" + policySetName + "].");
	}
}
