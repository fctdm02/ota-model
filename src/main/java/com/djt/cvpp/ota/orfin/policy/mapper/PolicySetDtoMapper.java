/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.mapper.DtoMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class PolicySetDtoMapper implements DtoMapper<com.djt.cvpp.ota.orfin.policy.model.PolicySet, com.djt.cvpp.ota.orfin.policy.mapper.dto.PolicySet> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PolicySetDtoMapper.class);
	
	public com.djt.cvpp.ota.orfin.policy.mapper.dto.PolicySet mapEntityToDto(com.djt.cvpp.ota.orfin.policy.model.PolicySet policySetEntity) {

		com.djt.cvpp.ota.orfin.policy.mapper.dto.PolicySet policySetDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.PolicySet();
		policySetDto.setPolicySetName(policySetEntity.getPolicySetName());
		
		List<com.djt.cvpp.ota.orfin.policy.mapper.dto.AbstractPolicy> policies = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy> policiesIterator = policySetEntity.getPolicies().iterator();
		while (policiesIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy policyEntity = policiesIterator.next();
			
			if (policyEntity instanceof com.djt.cvpp.ota.orfin.policy.model.VehiclePolicy) {
				
				com.djt.cvpp.ota.orfin.policy.model.VehiclePolicy vehiclePolicyEntity = (com.djt.cvpp.ota.orfin.policy.model.VehiclePolicy)policyEntity;

				com.djt.cvpp.ota.orfin.policy.mapper.dto.VehiclePolicy policyDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.VehiclePolicy();
				policyDto.setPolicyName(policyEntity.getPolicyName());
				policyDto.setPolicyDescription(policyEntity.getPolicyDescription());
				policyDto.setAllowRegionalChangeable(vehiclePolicyEntity.getAllowRegionalChangeable());
				policyDto.setAllowUserChangeable(vehiclePolicyEntity.getAllowUserChangeable());
				policyDto.setAllowServiceChangeable(vehiclePolicyEntity.getAllowServiceChangeable());
				policyDto.setAllowCustomerFeedback(vehiclePolicyEntity.getAllowCustomerFeedback());
				policyDto.setPolicyValue(mapPolicyValueEntityToPolicyValueDto(policyEntity.getPolicyValue()));
				policyDto.setHmi(vehiclePolicyEntity.getHmi());	
				policyDto.setPhone(vehiclePolicyEntity.getPhone());	
				policyDto.setVehicleHmiFile(vehiclePolicyEntity.getVehicleHmiFile());
				com.djt.cvpp.ota.orfin.policy.model.enums.OtaFunction otaFunction = vehiclePolicyEntity.getOtaFunction();
				if (otaFunction != null) {
					policyDto.setOtaFunction(otaFunction.toString());	
				}
				com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType policyValueType = policyEntity.getPolicyValueType();
				if (policyValueType != null) {
					policyDto.setPolicyValueType(policyValueType.toString());	
				}
				policyDto.setPolicyValueConstraints(policyEntity.getPolicyValueConstraints());
				policyDto.setPolicyOverrides(mapPolicyOverrideEntitiesToDtos(policyEntity.getPolicyOverrides()));
				policies.add(policyDto);
				
			} else {

				com.djt.cvpp.ota.orfin.policy.mapper.dto.CloudPolicy policyDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.CloudPolicy();
				policyDto.setPolicyName(policyEntity.getPolicyName());
				policyDto.setPolicyDescription(policyEntity.getPolicyDescription());
				policyDto.setPolicyValue(mapPolicyValueEntityToPolicyValueDto(policyEntity.getPolicyValue()));
				com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType policyValueType = policyEntity.getPolicyValueType();
				if (policyValueType != null) {
					policyDto.setPolicyValueType(policyValueType.toString());	
				}
				policyDto.setPolicyValueConstraints(policyEntity.getPolicyValueConstraints());
				policies.add(policyDto);
				
			}
		}
		policySetDto.setPolicies(policies);
		
		List<com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear> programModelYears = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.program.model.ProgramModelYear> programModelYearsIterator = policySetEntity.getProgramModelYears().iterator();
		while (programModelYearsIterator.hasNext()) {
			
			programModelYears.add(mapProgramModelYearEntityToDto(programModelYearsIterator.next()));
		}
		policySetDto.setProgramModelYears(programModelYears);
		
		
		/*
		List<com.djt.cvpp.ota.orfin.policy.mapper.dto.PolicySetHistoryEvent> policySetHistoryEvents = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.policy.model.PolicySetHistoryEvent> policySetHistoryEventsIterator = policySetEntity.get.getProgramModelYears().iterator();
		while (programModelYearsIterator.hasNext()) {
			
			programModelYears.add(mapProgramModelYearEntityToDto(programModelYearsIterator.next()));
		}
		policySetDto.setProgramModelYears(programModelYears);
		*/
		
		
		return policySetDto;
	}

	private com.djt.cvpp.ota.orfin.policy.mapper.dto.value.AbstractPolicyValue mapPolicyValueEntityToPolicyValueDto(com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue policyValueEntity) {
		
		com.djt.cvpp.ota.orfin.policy.mapper.dto.value.AbstractPolicyValue policyValueDto = null;
		if (policyValueEntity instanceof com.djt.cvpp.ota.orfin.policy.model.value.BooleanValue) {
				
			policyValueDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.value.BooleanValue();
			((com.djt.cvpp.ota.orfin.policy.mapper.dto.value.BooleanValue)policyValueDto).setValue((Boolean)policyValueEntity.getPolicyValue());
			
		} else if (policyValueEntity instanceof com.djt.cvpp.ota.orfin.policy.model.value.NumericValue) {
			
			policyValueDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.value.NumericValue();
			((com.djt.cvpp.ota.orfin.policy.mapper.dto.value.NumericValue)policyValueDto).setValue((Number)policyValueEntity.getPolicyValue());
			
		} else {
			
			policyValueDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.value.StringValue();
			((com.djt.cvpp.ota.orfin.policy.mapper.dto.value.StringValue)policyValueDto).setValue((String)policyValueEntity.getPolicyValue());
			
		}
		
		return policyValueDto;
	}
	
	private com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear mapProgramModelYearEntityToDto(com.djt.cvpp.ota.orfin.program.model.ProgramModelYear programModelYearEntity) {

		com.djt.cvpp.ota.orfin.program.mapper.dto.Program programDto = new com.djt.cvpp.ota.orfin.program.mapper.dto.Program();
		programDto.setProgramCode(programModelYearEntity.getParentProgram().getProgramCode());
		
		com.djt.cvpp.ota.orfin.program.mapper.dto.ModelYear modelYearDto = new com.djt.cvpp.ota.orfin.program.mapper.dto.ModelYear();
		modelYearDto.setModelYearValue(programModelYearEntity.getParentModelYear().getModelYear());
	
		com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear programModelYearDto = new com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear();
		programModelYearDto.setParentProgram(programDto);
		programModelYearDto.setParentModelYear(modelYearDto);
		
		return programModelYearDto;
	}

	public com.djt.cvpp.ota.orfin.policy.mapper.dto.region.Region mapRegionEntityToDto(com.djt.cvpp.ota.orfin.policy.model.region.Region regionEntity) {

		com.djt.cvpp.ota.orfin.policy.mapper.dto.region.Region regionDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.region.Region();
		regionDto.setCountryName(regionEntity.getCountryName());
		regionDto.setRegionCode(regionEntity.getRegionCode());
		
		return regionDto;
	}

	private com.djt.cvpp.ota.orfin.policy.mapper.dto.vehicle.Vehicle mapVehicleEntityToDto(com.djt.cvpp.ota.orfin.policy.model.vehicle.Vehicle vehicleEntity) {

		com.djt.cvpp.ota.orfin.policy.mapper.dto.vehicle.Vehicle vehicleDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.vehicle.Vehicle();
		
		com.djt.cvpp.ota.orfin.policy.model.region.Region vehicleRegion = vehicleEntity.getRegion();
		if (vehicleRegion != null) {
			vehicleDto.setRegion(mapRegionEntityToDto(vehicleEntity.getRegion()));	
		} else {
			LOGGER.error("TMC Vehicle Object: [" + vehicleEntity + "] is not associated with any Region.");
		}
		vehicleDto.setVin(vehicleEntity.getVin());
		
		return vehicleDto;
	}
	
	private List<com.djt.cvpp.ota.orfin.policy.mapper.dto.override.AbstractPolicyOverride> mapPolicyOverrideEntitiesToDtos(List<com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride> policyOverrideEntities) {
		
		List<com.djt.cvpp.ota.orfin.policy.mapper.dto.override.AbstractPolicyOverride> list = new ArrayList<>();
		
		Iterator<com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride> iterator = policyOverrideEntities.iterator();
		com.djt.cvpp.ota.orfin.policy.mapper.dto.override.AbstractPolicyOverride policyOverrideDto = null;
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride policyOverrideEntity = iterator.next();
			
			if (policyOverrideEntity instanceof com.djt.cvpp.ota.orfin.policy.model.override.PolicyProgramModelYearOverride) {
				
				policyOverrideDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyProgramModelYearOverride();
				policyOverrideDto.setPolicyOverrideValue(mapPolicyValueEntityToPolicyValueDto(policyOverrideEntity.getPolicyOverrideValue()));
				((com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyProgramModelYearOverride)policyOverrideDto).setProgramModelYear(mapProgramModelYearEntityToDto(((com.djt.cvpp.ota.orfin.policy.model.override.PolicyProgramModelYearOverride) policyOverrideEntity).getProgramModelYear()));
				list.add(policyOverrideDto);
				
			} else if (policyOverrideEntity instanceof com.djt.cvpp.ota.orfin.policy.model.override.PolicyRegionOverride) {
				
				policyOverrideDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyRegionOverride();
				policyOverrideDto.setPolicyOverrideValue(mapPolicyValueEntityToPolicyValueDto(policyOverrideEntity.getPolicyOverrideValue()));
				((com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyRegionOverride)policyOverrideDto).setRegion(mapRegionEntityToDto(((com.djt.cvpp.ota.orfin.policy.model.override.PolicyRegionOverride) policyOverrideEntity).getRegion()));
				list.add(policyOverrideDto);
				
			} else {
				
				policyOverrideDto = new com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyVehicleOverride();
				policyOverrideDto.setPolicyOverrideValue(mapPolicyValueEntityToPolicyValueDto(policyOverrideEntity.getPolicyOverrideValue()));
				((com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyVehicleOverride)policyOverrideDto).setVehicle(mapVehicleEntityToDto(((com.djt.cvpp.ota.orfin.policy.model.override.PolicyVehicleOverride) policyOverrideEntity).getVehicle()));
				list.add(policyOverrideDto);
			}			
		}
		
		return list;
	}
	
	public com.djt.cvpp.ota.orfin.policy.model.PolicySet mapDtoToEntity(com.djt.cvpp.ota.orfin.policy.mapper.dto.PolicySet policySetDto) throws EntityAlreadyExistsException, EntityDoesNotExistException, ValidationException {

		com.djt.cvpp.ota.orfin.policy.model.PolicySet policySetEntity = new com.djt.cvpp.ota.orfin.policy.model.PolicySet
			.PolicySetBuilder()
			.withPolicySetName(policySetDto.getPolicySetName())
			.build();
		
		Set<com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy> abstractPolicies = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.policy.mapper.dto.AbstractPolicy> policiesIterator = policySetDto.getPolicies().iterator();
		while (policiesIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.policy.mapper.dto.AbstractPolicy policyDto = policiesIterator.next();
			
			if (policyDto instanceof com.djt.cvpp.ota.orfin.policy.mapper.dto.VehiclePolicy) {
				
				com.djt.cvpp.ota.orfin.policy.mapper.dto.VehiclePolicy vehiclePolicyDto = (com.djt.cvpp.ota.orfin.policy.mapper.dto.VehiclePolicy)policyDto;

				String strOtaFunction = vehiclePolicyDto.getOtaFunction();
				com.djt.cvpp.ota.orfin.policy.model.enums.OtaFunction otaFunction = null;
				if (strOtaFunction != null) {
					otaFunction = com.djt.cvpp.ota.orfin.policy.model.enums.OtaFunction.valueOf(strOtaFunction);
				}
				
				String strPolicyValueType = vehiclePolicyDto.getPolicyValueType();
				com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType policyValueType = null;
				if (strPolicyValueType != null) {
					policyValueType = com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType.valueOf(strPolicyValueType);
				}
				
				com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy policyEntity = new com.djt.cvpp.ota.orfin.policy.model.VehiclePolicy
					.VehiclePolicyBuilder()
					.withPolicyName(vehiclePolicyDto.getPolicyName())
					.withParentPolicySet(policySetEntity)
					.withPolicyDescription(vehiclePolicyDto.getPolicyDescription())
					.withAllowRegionalChangeable(vehiclePolicyDto.getAllowRegionalChangeable())
					.withAllowUserChangeable(vehiclePolicyDto.getAllowUserChangeable())
					.withAllowServiceChangeable(vehiclePolicyDto.getAllowServiceChangeable())
					.withAllowCustomerFeedback(vehiclePolicyDto.getAllowCustomerFeedback())
					.withPolicyValue(mapPolicyValueDtoToPolicyValueEntity(vehiclePolicyDto.getPolicyValue()))
					.withHmi(vehiclePolicyDto.getHmi())
					.withPhone(vehiclePolicyDto.getPhone())
					.withVehicleHmiFile(vehiclePolicyDto.getVehicleHmiFile())
					.withOtaFunction(otaFunction)
					.withPolicyValueType(policyValueType)
					.withPolicyValueConstraints(vehiclePolicyDto.getPolicyValueConstraints())
					.build();
				
				Iterator<com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride> iterator = new PolicySetDtoMapper().mapPolicyOverrideDtosToEntities(policyEntity, vehiclePolicyDto.getPolicyOverrides()).iterator();
				while (iterator.hasNext()) {
					policyEntity.addPolicyOverride(iterator.next());
				}
				
				abstractPolicies.add(policyEntity);
				
			} else {
				
				com.djt.cvpp.ota.orfin.policy.mapper.dto.CloudPolicy cloudPolicyDto = (com.djt.cvpp.ota.orfin.policy.mapper.dto.CloudPolicy)policyDto;

				String strPolicyValueType = cloudPolicyDto.getPolicyValueType();
				com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType policyValueType = null;
				if (strPolicyValueType != null) {
					policyValueType = com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType.valueOf(strPolicyValueType);
				}
				
				com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy policyEntity = new com.djt.cvpp.ota.orfin.policy.model.CloudPolicy
					.CloudPolicyBuilder()
					.withPolicyName(cloudPolicyDto.getPolicyName())
					.withParentPolicySet(policySetEntity)
					.withPolicyDescription(cloudPolicyDto.getPolicyDescription())
					.withPolicyValue(mapPolicyValueDtoToPolicyValueEntity(cloudPolicyDto.getPolicyValue()))
					.withPolicyValueType(policyValueType)
					.withPolicyValueConstraints(cloudPolicyDto.getPolicyValueConstraints())
					.build();
				
				abstractPolicies.add(policyEntity);
			}
		}
		policySetEntity.addPolicies(abstractPolicies);
				
		Set<com.djt.cvpp.ota.orfin.program.model.ProgramModelYear> programModelYears = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear> programModelYearsIterator = policySetDto.getProgramModelYears().iterator();
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
		policySetEntity.addProgramModelYears(programModelYears);
		
		return policySetEntity;		
	}

	private com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue mapPolicyValueDtoToPolicyValueEntity(com.djt.cvpp.ota.orfin.policy.mapper.dto.value.AbstractPolicyValue policyValueDto) {

		com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue policyValueEntity = null;
		if (policyValueDto instanceof com.djt.cvpp.ota.orfin.policy.mapper.dto.value.BooleanValue) {
			
			policyValueEntity = new com.djt.cvpp.ota.orfin.policy.model.value.BooleanValue(((com.djt.cvpp.ota.orfin.policy.mapper.dto.value.BooleanValue)policyValueDto).getValue());
			
		} else if (policyValueDto instanceof com.djt.cvpp.ota.orfin.policy.mapper.dto.value.NumericValue) {
			
			policyValueEntity = new com.djt.cvpp.ota.orfin.policy.model.value.NumericValue(((com.djt.cvpp.ota.orfin.policy.mapper.dto.value.NumericValue)policyValueDto).getValue());
			
		} else {
			
			policyValueEntity = new com.djt.cvpp.ota.orfin.policy.model.value.StringValue(((com.djt.cvpp.ota.orfin.policy.mapper.dto.value.StringValue)policyValueDto).getValue());
			
		}
		
		return policyValueEntity;
	}
	
	private com.djt.cvpp.ota.orfin.program.model.ProgramModelYear mapProgramModelYearDtoToEntity(com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear programModelYearDto) {

		com.djt.cvpp.ota.orfin.program.model.Program programEntity = new com.djt.cvpp.ota.orfin.program.model.Program.ProgramBuilder().withProgramCode(programModelYearDto.getParentProgram().getProgramCode()).build();
		com.djt.cvpp.ota.orfin.program.model.ModelYear modelYearEntity = new com.djt.cvpp.ota.orfin.program.model.ModelYear.ModelYearBuilder().withModelYearValue(programModelYearDto.getParentModelYear().getModelYearValue()).build();

		com.djt.cvpp.ota.orfin.program.model.ProgramModelYear programModelYearEntity = new com.djt.cvpp.ota.orfin.program.model.ProgramModelYear
			.ProgramModelYearBuilder()
			.withParentProgram(programEntity)
			.withParentModelYear(modelYearEntity)
			.build();
		
		return programModelYearEntity;
	}

	public com.djt.cvpp.ota.orfin.policy.model.region.Region mapRegionDtoToEntity(com.djt.cvpp.ota.orfin.policy.mapper.dto.region.Region regionDto) {

		com.djt.cvpp.ota.orfin.policy.model.region.Region regionEntity = new com.djt.cvpp.ota.orfin.policy.model.region.Region
			.RegionBuilder()
			.withCountryName(regionDto.getCountryName())
			.withRegionCode(regionDto.getRegionCode())
			.build();
		
		return regionEntity;
	}

	private com.djt.cvpp.ota.orfin.policy.model.vehicle.Vehicle mapVehicleDtoToEntity(com.djt.cvpp.ota.orfin.policy.mapper.dto.vehicle.Vehicle vehicleDto) {

		com.djt.cvpp.ota.orfin.policy.model.vehicle.Vehicle vehicleEntity = new com.djt.cvpp.ota.orfin.policy.model.vehicle.Vehicle
			.VehicleBuilder()
			.withVin(vehicleDto.getVin())
			.withRegion(mapRegionDtoToEntity(vehicleDto.getRegion()))
			.build();
		
		return vehicleEntity;
	}
	
	private List<com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride> mapPolicyOverrideDtosToEntities(
		com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy policyEntity,	
		List<com.djt.cvpp.ota.orfin.policy.mapper.dto.override.AbstractPolicyOverride> policyOverrideDtos) throws ValidationException {
		
		List<com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride> list = new ArrayList<>();
		
		Iterator<com.djt.cvpp.ota.orfin.policy.mapper.dto.override.AbstractPolicyOverride> iterator = policyOverrideDtos.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.policy.mapper.dto.override.AbstractPolicyOverride policyOverrideDto = iterator.next();
			
			if (policyOverrideDto instanceof com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyProgramModelYearOverride) {
				
				com.djt.cvpp.ota.orfin.policy.model.override.PolicyProgramModelYearOverride policyOverrideEntity = new com.djt.cvpp.ota.orfin.policy.model.override.PolicyProgramModelYearOverride
					.PolicyProgramModelYearOverrideBuilder()
					.withParentPolicy(policyEntity)
					.withPolicyOverrideValue(mapPolicyValueDtoToPolicyValueEntity(policyOverrideDto.getPolicyOverrideValue()))
					.withProgramModelYear(mapProgramModelYearDtoToEntity(((com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyProgramModelYearOverride) policyOverrideDto).getProgramModelYear()))
					.build();
				list.add(policyOverrideEntity);
				
			} else if (policyOverrideDto instanceof com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyRegionOverride) {
				
				com.djt.cvpp.ota.orfin.policy.model.override.PolicyRegionOverride policyOverrideEntity = new com.djt.cvpp.ota.orfin.policy.model.override.PolicyRegionOverride
					.PolicyRegionOverrideBuilder()
					.withParentPolicy(policyEntity)
					.withPolicyOverrideValue(mapPolicyValueDtoToPolicyValueEntity(policyOverrideDto.getPolicyOverrideValue()))
					.withRegion(mapRegionDtoToEntity(((com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyRegionOverride) policyOverrideDto).getRegion()))
					.build();
				list.add(policyOverrideEntity);
				
			} else {
				
				com.djt.cvpp.ota.orfin.policy.model.override.PolicyVehicleOverride policyOverrideEntity = new com.djt.cvpp.ota.orfin.policy.model.override.PolicyVehicleOverride
					.PolicyVehicleOverrideBuilder()
					.withParentPolicy(policyEntity)
					.withPolicyOverrideValue(mapPolicyValueDtoToPolicyValueEntity(policyOverrideDto.getPolicyOverrideValue()))
					.withVehicle(mapVehicleDtoToEntity(((com.djt.cvpp.ota.orfin.policy.mapper.dto.override.PolicyVehicleOverride) policyOverrideDto).getVehicle()))
					.build();
				list.add(policyOverrideEntity);
			}			
		}
		
		return list;
	}	
}
