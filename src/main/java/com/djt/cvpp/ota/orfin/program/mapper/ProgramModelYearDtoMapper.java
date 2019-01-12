/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.program.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.mapper.DtoMapper;
import com.djt.cvpp.ota.orfin.delivery.mapper.DeliveryRuleSetDtoMapper;
import com.djt.cvpp.ota.orfin.odl.mapper.OdlDtoMapper;
import com.djt.cvpp.ota.orfin.policy.mapper.PolicySetDtoMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class ProgramModelYearDtoMapper implements DtoMapper<com.djt.cvpp.ota.orfin.program.model.ProgramModelYear, com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear> {
	
	private OdlDtoMapper odlDtoMapper = new OdlDtoMapper();
	private PolicySetDtoMapper policySetDtoMapper = new PolicySetDtoMapper();
	private DeliveryRuleSetDtoMapper deliveryRuleSetDtoMapper = new DeliveryRuleSetDtoMapper();

	public com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear mapEntityToDto(com.djt.cvpp.ota.orfin.program.model.ProgramModelYear entity) {

		com.djt.cvpp.ota.orfin.program.mapper.dto.Program programDto = new com.djt.cvpp.ota.orfin.program.mapper.dto.Program();
		programDto.setProgramCode(entity.getParentProgram().getProgramCode());
		
		com.djt.cvpp.ota.orfin.program.mapper.dto.ModelYear modelYearDto = new com.djt.cvpp.ota.orfin.program.mapper.dto.ModelYear();
		modelYearDto.setModelYearValue(entity.getParentModelYear().getModelYear());
		
		com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear programModelYearDto = new com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear();
		programModelYearDto.setParentProgram(programDto);
		programModelYearDto.setParentModelYear(modelYearDto);
		
		if (entity.getOdl() != null) {
			programModelYearDto.setOdl(odlDtoMapper.mapEntityToDto(entity.getOdl()));	
		}
		
		if (entity.getPolicySet() != null) {
			programModelYearDto.setPolicySet(policySetDtoMapper.mapEntityToDto(entity.getPolicySet()));	
		}
		
		programModelYearDto.setDeliveryRuleSets(mapDeliveryRuleSetEntitiesToDtos(entity.getDeliveryRuleSets()));
		
		return programModelYearDto;
	}
	
	private List<com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet> mapDeliveryRuleSetEntitiesToDtos(List<com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet> entities) {
		
		List<com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet> deliveryRuleSets = new ArrayList<>();
		
		Iterator<com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet> iterator = entities.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet entity = iterator.next();
			com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet dto = deliveryRuleSetDtoMapper.mapEntityToDto(entity);
			deliveryRuleSets.add(dto);
		}
		
		return deliveryRuleSets;
	}
	
	public com.djt.cvpp.ota.orfin.program.model.ProgramModelYear mapDtoToEntity(com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear dto) {
		
		com.djt.cvpp.ota.orfin.program.model.Program parentProgramEntity = new com.djt.cvpp.ota.orfin.program.model.Program.ProgramBuilder().withProgramCode(dto.getParentProgram().getProgramCode()).build();
		com.djt.cvpp.ota.orfin.program.model.ModelYear parentModelYearEntity = new com.djt.cvpp.ota.orfin.program.model.ModelYear.ModelYearBuilder().withModelYearValue(dto.getParentModelYear().getModelYearValue()).build();

		com.djt.cvpp.ota.orfin.program.model.ProgramModelYear entity = new com.djt.cvpp.ota.orfin.program.model.ProgramModelYear
			.ProgramModelYearBuilder()
			.withParentProgram(parentProgramEntity)
			.withParentModelYear(parentModelYearEntity)
			.build();
		
		if (dto.getOdl() != null) {
		
			entity.setOdl(odlDtoMapper.mapDtoToEntity(dto.getOdl()));
		}
		
		if (dto.getPolicySet() != null) {
			
			try {
				entity.setPolicySet(policySetDtoMapper.mapDtoToEntity(dto.getPolicySet()));
			} catch (EntityAlreadyExistsException | EntityDoesNotExistException | ValidationException e) {
				// TODO TDM: Figure out why I added these exceptions to this mapper and not the other.
				throw new FenixRuntimeException("Could not map policy set DTO to entity, error: " + e.getMessage());
			}
		}
		
		entity.addDeliveryRuleSets(mapDeliveryRuleSetDtosToEntities(dto.getDeliveryRuleSets()));
		
		return entity;
	}
	
	private Set<com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet> mapDeliveryRuleSetDtosToEntities(List<com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet> dtos) {
		
		Set<com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet> deliveryRuleSets = new TreeSet<>();
		
		Iterator<com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet> iterator = dtos.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet dto = iterator.next();
			com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet entity = deliveryRuleSetDtoMapper.mapDtoToEntity(dto);
			deliveryRuleSets.add(entity);
		}
		
		return deliveryRuleSets;
	}
}
