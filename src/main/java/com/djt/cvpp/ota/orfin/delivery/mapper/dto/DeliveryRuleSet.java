/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.delivery.mapper.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear;
import com.djt.cvpp.ota.orfin.vadrevent.mapper.dto.VadrRelease;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryRuleSet {
	
	private String deliveryRuleSetName;
	private String authorizedBy;
	private String messageToConsumer;
	private String consentType;
	private Timestamp scheduledRolloutDate;
	private List<ComplexCondition> complexConditions = new ArrayList<>();
	private List<DeliveryRule> deliveryRules = new ArrayList<>();
	private List<ProgramModelYear> programModelYears = new ArrayList<>();
	private List<VadrRelease> vadrReleases = new ArrayList<>();
	
	/**
	 * Inclusions take the form of: XXXX_YYYY where XXXX is the programCode and modelYear is the 4 digit model year
	 * 
	 * @return
	 */
	public Set<String> getProgramCodeModelYearInclusions() {
		
		Set<String> programCodeModelYearInclusions = new HashSet<>(); 					
		Iterator<ProgramModelYear> iterator = this.programModelYears.iterator();
		while (iterator.hasNext()) {
			
			ProgramModelYear programModelYear = iterator.next();
			
			programCodeModelYearInclusions.add(
				AbstractEntity.buildNaturalIdentity(
					programModelYear.getParentProgram().getProgramCode(), 
					programModelYear.getParentModelYear().getModelYearValue()));
		}
		
		return programCodeModelYearInclusions;
	}
}
