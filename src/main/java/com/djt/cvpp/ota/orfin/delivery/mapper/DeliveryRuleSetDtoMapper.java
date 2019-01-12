/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.delivery.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.mapper.DtoMapper;
import com.djt.cvpp.ota.orfin.vadrevent.mapper.VadrReleaseEventDtoMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class DeliveryRuleSetDtoMapper implements DtoMapper<com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet, com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet> {

	public com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet mapEntityToDto(com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet deliveryRuleSetEntity) {

		com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet deliveryRuleSetDto = new com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet();
		deliveryRuleSetDto.setDeliveryRuleSetName(deliveryRuleSetEntity.getDeliveryRuleSetName());
		deliveryRuleSetDto.setAuthorizedBy(deliveryRuleSetEntity.getAuthorizedBy());
		deliveryRuleSetDto.setMessageToConsumer(deliveryRuleSetEntity.getMessageToConsumer());
		
		if (deliveryRuleSetEntity.getConsentType() != null) {
			deliveryRuleSetDto.setConsentType(deliveryRuleSetEntity.getConsentType().toString());	
		}
		
		deliveryRuleSetDto.setScheduledRolloutDate(deliveryRuleSetEntity.getScheduledRolloutDate());
		
		
		List<com.djt.cvpp.ota.orfin.delivery.mapper.dto.ComplexCondition> complexConditions = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.delivery.model.ComplexCondition> complexConditionsIterator = deliveryRuleSetEntity.getComplexConditions().iterator();
		while (complexConditionsIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.delivery.model.ComplexCondition complexConditionEntity = complexConditionsIterator.next();
		
			com.djt.cvpp.ota.orfin.delivery.mapper.dto.ComplexCondition complexConditionDto = new com.djt.cvpp.ota.orfin.delivery.mapper.dto.ComplexCondition();
			complexConditionDto.setComplexConditionName(complexConditionEntity.getComplexConditionName());
			complexConditionDto.setComplexConditionValue(complexConditionEntity.getComplexConditionValue());
			complexConditions.add(complexConditionDto);
		}
		deliveryRuleSetDto.setComplexConditions(complexConditions);

		
		List<com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRule> deliveryRules = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.delivery.model.DeliveryRule> deliveryRulesIterator = deliveryRuleSetEntity.getDeliveryRules().iterator();
		while (deliveryRulesIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.delivery.model.DeliveryRule deliveryRuleEntity = deliveryRulesIterator.next();
			
			com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRule deliveryRuleDto = new com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRule();
			deliveryRuleDto.setAllowable(deliveryRuleEntity.getAllowable());
			deliveryRuleDto.setPrecedenceLevel(deliveryRuleEntity.getPrecedenceLevel());
			deliveryRuleDto.setDeliveryAudience(deliveryRuleEntity.getDeliveryAudience().toString());
			deliveryRuleDto.setDeliveryMethod(deliveryRuleEntity.getDeliveryMethod().toString());
			deliveryRuleDto.setConnectionType(deliveryRuleEntity.getConnectionType().toString());
			deliveryRules.add(deliveryRuleDto);
		}
		deliveryRuleSetDto.setDeliveryRules(deliveryRules);

		
		List<com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear> programModelYears = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.program.model.ProgramModelYear> programModelYearsIterator = deliveryRuleSetEntity.getProgramModelYears().iterator();
		while (programModelYearsIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.program.model.ProgramModelYear programModelYearEntity = programModelYearsIterator.next();
			
			com.djt.cvpp.ota.orfin.program.mapper.dto.Program programDto = new com.djt.cvpp.ota.orfin.program.mapper.dto.Program();
			programDto.setProgramCode(programModelYearEntity.getParentProgram().getProgramCode());
			
			com.djt.cvpp.ota.orfin.program.mapper.dto.ModelYear modelYearDto = new com.djt.cvpp.ota.orfin.program.mapper.dto.ModelYear();
			modelYearDto.setModelYearValue(programModelYearEntity.getParentModelYear().getModelYear());
		
			com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear programModelYearDto = new com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear();
			programModelYearDto.setParentProgram(programDto);
			programModelYearDto.setParentModelYear(modelYearDto);
			programModelYears.add(programModelYearDto);
		}
		deliveryRuleSetDto.setProgramModelYears(programModelYears);
		
		
		VadrReleaseEventDtoMapper vadrReleaseEventDtoMapper = new VadrReleaseEventDtoMapper();
		List<com.djt.cvpp.ota.orfin.vadrevent.mapper.dto.VadrRelease> vadrReleases = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease> vadrReleasesIterator = deliveryRuleSetEntity.getVadrReleases().iterator();
		while (vadrReleasesIterator.hasNext()) {
			
			vadrReleases.add(vadrReleaseEventDtoMapper.mapEntityToDto(vadrReleasesIterator.next()));
		}
		deliveryRuleSetDto.setVadrReleases(vadrReleases);

		return deliveryRuleSetDto;
	}
	
	public com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet mapDtoToEntity(com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet deliveryRuleSetDto) {

		Set<com.djt.cvpp.ota.orfin.delivery.model.ComplexCondition> complexConditions = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.delivery.mapper.dto.ComplexCondition> complexConditionsIterator = deliveryRuleSetDto.getComplexConditions().iterator();
		while (complexConditionsIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.delivery.mapper.dto.ComplexCondition complexConditionDto = complexConditionsIterator.next();
			
			com.djt.cvpp.ota.orfin.delivery.model.ComplexCondition complexConditionEntity = new com.djt.cvpp.ota.orfin.delivery.model.ComplexCondition
				.ComplexConditionBuilder()
				.withComplexConditionName(complexConditionDto.getComplexConditionName())
				.withComplexConditionValue(complexConditionDto.getComplexConditionValue())
				.build();
			
			complexConditions.add(complexConditionEntity);
		}
		
		
		Set<com.djt.cvpp.ota.orfin.delivery.model.DeliveryRule> deliveryRules = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRule> deliveryRulesIterator = deliveryRuleSetDto.getDeliveryRules().iterator();
		while (deliveryRulesIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRule deliveryRuleDto = deliveryRulesIterator.next();
			
			com.djt.cvpp.ota.orfin.delivery.model.DeliveryRule deliveryRuleEntity = new com.djt.cvpp.ota.orfin.delivery.model.DeliveryRule
				.DeliveryRuleBuilder()
				.withAllowable(deliveryRuleDto.getAllowable())
				.withPrecedenceLevel(deliveryRuleDto.getPrecedenceLevel())
				.withDeliveryAudience(com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryAudience.valueOf(deliveryRuleDto.getDeliveryAudience()))
				.withDeliveryMethod(com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryMethod.valueOf(deliveryRuleDto.getDeliveryMethod()))
				.withConnectionType(com.djt.cvpp.ota.orfin.delivery.model.enums.ConnectionType.valueOf(deliveryRuleDto.getConnectionType()))
				.build();
			
			deliveryRules.add(deliveryRuleEntity);
		}
		
				
		Set<com.djt.cvpp.ota.orfin.program.model.ProgramModelYear> programModelYears = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear> programModelYearsIterator = deliveryRuleSetDto.getProgramModelYears().iterator();
		while (programModelYearsIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear programModelYearDto = programModelYearsIterator.next();
			
			com.djt.cvpp.ota.orfin.program.mapper.dto.Program programDto = programModelYearDto.getParentProgram();
			com.djt.cvpp.ota.orfin.program.model.Program programEntity = new com.djt.cvpp.ota.orfin.program.model.Program.ProgramBuilder().withProgramCode(programDto.getProgramCode()).build();  
			
			com.djt.cvpp.ota.orfin.program.mapper.dto.ModelYear modelYearDto = programModelYearDto.getParentModelYear();
			com.djt.cvpp.ota.orfin.program.model.ModelYear modelYearEntity = new com.djt.cvpp.ota.orfin.program.model.ModelYear.ModelYearBuilder().withModelYearValue(modelYearDto.getModelYearValue()).build();
			
			com.djt.cvpp.ota.orfin.program.model.ProgramModelYear programModelYearEntity = new com.djt.cvpp.ota.orfin.program.model.ProgramModelYear
				.ProgramModelYearBuilder()
				.withParentProgram(programEntity)
				.withParentModelYear(modelYearEntity)
				.build();
			
			programModelYears.add(programModelYearEntity);
		}

		
		VadrReleaseEventDtoMapper vadrReleaseEventDtoMapper = new VadrReleaseEventDtoMapper();
		Set<com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease> vadrReleases = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.vadrevent.mapper.dto.VadrRelease> vadrReleasesIterator = deliveryRuleSetDto.getVadrReleases().iterator();
		while (vadrReleasesIterator.hasNext()) {
			
			vadrReleases.add(vadrReleaseEventDtoMapper.mapDtoToEntity(vadrReleasesIterator.next()));
		}
		
		
		return new com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet
			.DeliveryRuleSetBuilder()
			.withDeliveryRuleSetName(deliveryRuleSetDto.getDeliveryRuleSetName())
			.withAuthorizedBy(deliveryRuleSetDto.getAuthorizedBy())
			.withMessageToConsumer(deliveryRuleSetDto.getMessageToConsumer())
			.withConsentType(com.djt.cvpp.ota.orfin.delivery.model.enums.ConsentType.valueOf(deliveryRuleSetDto.getConsentType()))
			.withScheduledRolloutDate(deliveryRuleSetDto.getScheduledRolloutDate())
			.withComplexConditions(complexConditions)
			.withDeliveryRules(deliveryRules)
			.withProgramModelYears(programModelYears)
			.withVadrReleases(vadrReleases)
			.build();
	}
}
