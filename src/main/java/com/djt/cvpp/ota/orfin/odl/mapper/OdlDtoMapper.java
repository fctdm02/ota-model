/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.mapper.DtoMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class OdlDtoMapper implements DtoMapper<com.djt.cvpp.ota.orfin.odl.model.Odl, com.djt.cvpp.ota.orfin.odl.mapper.dto.Odl> {

	public com.djt.cvpp.ota.orfin.odl.mapper.dto.Odl mapEntityToDto(com.djt.cvpp.ota.orfin.odl.model.Odl odlEntity) {

		com.djt.cvpp.ota.orfin.odl.mapper.dto.Odl odlDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.Odl();
		odlDto.setOdlName(odlEntity.getOdlName());
				
		List<com.djt.cvpp.ota.orfin.odl.mapper.dto.Network> networks = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.odl.model.Network> networksIterator = odlEntity.getNetworks().iterator();
		while (networksIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.odl.model.Network networkEntity = networksIterator.next();
			
			List<com.djt.cvpp.ota.orfin.odl.mapper.dto.Node> nodes = new ArrayList<>();
			Iterator<com.djt.cvpp.ota.orfin.odl.model.Node> nodesIterator = networkEntity.getNodes().iterator();
			while (nodesIterator.hasNext()) {
				
				com.djt.cvpp.ota.orfin.odl.model.Node nodeEntity = nodesIterator.next();
				nodes.add(mapNodeEntityToDto(nodeEntity));
			}
			
			com.djt.cvpp.ota.orfin.odl.mapper.dto.Network networkDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.Network();
			networkDto.setDataRate(networkEntity.getDataRate());
			networkDto.setDclName(networkEntity.getDclName());
			networkDto.setNetworkName(networkEntity.getNetworkName());
			networkDto.setNetworkPins(networkEntity.getNetworkPins());
			networkDto.setProtocol(networkEntity.getProtocol());
			networkDto.setNodes(nodes);
			networks.add(networkDto);
		}
		odlDto.setNetworks(networks);
		
		List<com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear> programModelYears = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.program.model.ProgramModelYear> programModelYearsIterator = odlEntity.getProgramModelYears().iterator();
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
		odlDto.setProgramModelYears(programModelYears);
		
		List<com.djt.cvpp.ota.orfin.odl.mapper.dto.EcgSignal> ecgSignals = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.odl.model.EcgSignal> ecgSignalsIterator = odlEntity.getEcgSignals().iterator();
		while (ecgSignalsIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.odl.model.EcgSignal ecgSignalEntity = ecgSignalsIterator.next();
		
			com.djt.cvpp.ota.orfin.odl.mapper.dto.EcgSignal ecgSignalDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.EcgSignal();
			ecgSignalDto.setEcgSignalName(ecgSignalEntity.getEcgSignalName());
			ecgSignals.add(ecgSignalDto);
		}
		odlDto.setEcgSignals(ecgSignals);

		List<com.djt.cvpp.ota.orfin.odl.mapper.dto.CustomOdl> customOdls = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.odl.model.CustomOdl> customOdlsIterator = odlEntity.getCustomOdls().iterator();
		while (customOdlsIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.odl.model.CustomOdl customOdlEntity = customOdlsIterator.next();
		
			com.djt.cvpp.ota.orfin.odl.mapper.dto.CustomOdl customOdlDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.CustomOdl();
			customOdlDto.setCustomOdlName(customOdlEntity.getCustomOdlName());
			
			List<com.djt.cvpp.ota.orfin.odl.mapper.dto.Node> nodes = new ArrayList<>();
			Iterator<com.djt.cvpp.ota.orfin.odl.model.Node> iterator = customOdlEntity.getNodes().iterator();
			while (iterator.hasNext()) {
				
				com.djt.cvpp.ota.orfin.odl.model.Node nodeEntity = iterator.next();
				nodes.add(mapNodeEntityToDto(nodeEntity));
			}
			customOdlDto.setNodes(nodes);
			
			customOdls.add(customOdlDto);
		}
		odlDto.setCustomOdls(customOdls);
		
		return odlDto;
	}
	
	private com.djt.cvpp.ota.orfin.odl.mapper.dto.Node mapNodeEntityToDto(com.djt.cvpp.ota.orfin.odl.model.Node nodeEntity) {
		
		List<com.djt.cvpp.ota.orfin.odl.mapper.dto.Did> dids = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.odl.model.Did> didsIterator = nodeEntity.getDids().iterator();
		while (didsIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.odl.model.Did didEntity = didsIterator.next();
			
			com.djt.cvpp.ota.orfin.odl.mapper.dto.Did didDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.Did();
			didDto.setDidName(didEntity.getDidName());
			didDto.setDescription(didEntity.getDescription());
			didDto.setDirectConfigurationDidFlag(didEntity.getDirectConfigurationDidFlag());
			didDto.setPrivateNetworkDidFlag(didEntity.getPrivateNetworkDidFlag());
			didDto.setVinSpecificDidFlag(didEntity.getVinSpecificDidFlag());
			dids.add(didDto);
		}
		
		List<String> ignoredDids = new ArrayList<>();
		ignoredDids.addAll(nodeEntity.getIgnoredDids());					
		
		com.djt.cvpp.ota.orfin.odl.mapper.dto.Node nodeDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.Node();
		nodeDto.setAcronym(nodeEntity.getAcronym());
		nodeDto.setAddress(nodeEntity.getAddress());
		nodeDto.setGatewayNodeId(nodeEntity.getGatewayNodeId());
		nodeDto.setGatewayType(nodeEntity.getGatewayType());
		nodeDto.setHasConditionBasedOnDtc(nodeEntity.getHasConditionBasedOnDtc());
		nodeDto.setIsOvtp(nodeEntity.getIsOvtp());
		nodeDto.setOvtpDestinationAddress(nodeEntity.getOvtpDestinationAddress());
		nodeDto.setSpecificationCategoryType(nodeEntity.getSpecificationCategoryType().toString());
		nodeDto.setActivationTime(nodeEntity.getActivationTime());
		nodeDto.setVehicleInhibitActivationTime(nodeEntity.getVehicleInhibitActivationTime());
		nodeDto.setIgnoredDids(ignoredDids);
		nodeDto.setDids(dids);
		
		return nodeDto;
	}
	
	public com.djt.cvpp.ota.orfin.odl.model.Odl mapDtoToEntity(com.djt.cvpp.ota.orfin.odl.mapper.dto.Odl odlDto) {

		Set<com.djt.cvpp.ota.orfin.odl.model.Network> networks = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.odl.mapper.dto.Network> networksIterator = odlDto.getNetworks().iterator();
		while (networksIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.odl.mapper.dto.Network networkDto = networksIterator.next();

			Set<com.djt.cvpp.ota.orfin.odl.model.Node> nodes = new TreeSet<>();
			Iterator<com.djt.cvpp.ota.orfin.odl.mapper.dto.Node> nodesIterator = networkDto.getNodes().iterator();
			while (nodesIterator.hasNext()) {
				
				com.djt.cvpp.ota.orfin.odl.mapper.dto.Node nodeDto = nodesIterator.next();
				nodes.add(mapNodeDtoToEntity(nodeDto));
			}
			
			com.djt.cvpp.ota.orfin.odl.model.Network networkEntity = new com.djt.cvpp.ota.orfin.odl.model.Network
				.NetworkBuilder()
				.withDataRate(networkDto.getDataRate())
				.withDclName(networkDto.getDclName())
				.withNetworkName(networkDto.getNetworkName())
				.withNetworkPins(networkDto.getNetworkPins())
				.withProtocol(networkDto.getProtocol())
				.withNodes(nodes)
				.build();
			networks.add(networkEntity);
		}
		
		Set<com.djt.cvpp.ota.orfin.program.model.ProgramModelYear> programModelYears = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear> programModelYearsIterator = odlDto.getProgramModelYears().iterator();
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
		
		Set<com.djt.cvpp.ota.orfin.odl.model.EcgSignal> ecgSignals = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.odl.mapper.dto.EcgSignal> ecgSignalsIterator = odlDto.getEcgSignals().iterator();
		while (ecgSignalsIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.odl.mapper.dto.EcgSignal ecgSignalDto = ecgSignalsIterator.next();

			try {
				com.djt.cvpp.ota.orfin.odl.model.EcgSignal ecgSignalEntity = new com.djt.cvpp.ota.orfin.odl.model.EcgSignal
					.EcgSignalBuilder()
					.withEcgSignalName(ecgSignalDto.getEcgSignalName())
					.build();
				ecgSignals.add(ecgSignalEntity);
			} catch (EntityAlreadyExistsException e) {
				throw new FenixRuntimeException(e.getMessage(), e);
			}
		}
		
		Set<com.djt.cvpp.ota.orfin.odl.model.CustomOdl> customOdls = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.odl.mapper.dto.CustomOdl> customOdlsIterator = odlDto.getCustomOdls().iterator();
		while (customOdlsIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.odl.mapper.dto.CustomOdl customOdlDto = customOdlsIterator.next();
			
			Set<com.djt.cvpp.ota.orfin.odl.model.Node> nodes = new TreeSet<>();
			Iterator<com.djt.cvpp.ota.orfin.odl.mapper.dto.Node> iterator = customOdlDto.getNodes().iterator();
			while (iterator.hasNext()) {
				
				com.djt.cvpp.ota.orfin.odl.mapper.dto.Node nodeDto = iterator.next();
				nodes.add(mapNodeDtoToEntity(nodeDto));
			}

			try {
				com.djt.cvpp.ota.orfin.odl.model.CustomOdl customOdlEntity = new com.djt.cvpp.ota.orfin.odl.model.CustomOdl
					.CustomOdlBuilder()
					.withCustomOdlName(customOdlDto.getCustomOdlName())
					.withNodes(nodes)
					.build();
				customOdls.add(customOdlEntity);
			} catch (ValidationException | EntityAlreadyExistsException e) {
				throw new FenixRuntimeException(e.getMessage(), e);
			}
		}
		
		return new com.djt.cvpp.ota.orfin.odl.model.Odl
			.OdlBuilder()
			.withOdlName(odlDto.getOdlName())
			.withNetworks(networks)
			.withEcgSignals(ecgSignals)
			.withCustomOdls(customOdls)
			.withProgramModelYears(programModelYears)
			.build();
	}
	
	private com.djt.cvpp.ota.orfin.odl.model.Node mapNodeDtoToEntity(com.djt.cvpp.ota.orfin.odl.mapper.dto.Node nodeDto) {

		Set<String> ignoredDids = new TreeSet<>();
		ignoredDids.addAll(nodeDto.getIgnoredDids());
		
		Set<com.djt.cvpp.ota.orfin.odl.model.Did> dids = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.orfin.odl.mapper.dto.Did> didsIterator = nodeDto.getDids().iterator();
		while (didsIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.odl.mapper.dto.Did didDto = didsIterator.next();

			com.djt.cvpp.ota.orfin.odl.model.Did didEntity = new com.djt.cvpp.ota.orfin.odl.model.Did
				.DidBuilder()
				.withDidName(didDto.getDidName())
				.withDescription(didDto.getDescription())
				.withDirectConfigurationDidFlag(didDto.getDirectConfigurationDidFlag())
				.withPrivateNetworkDidFlag(didDto.getPrivateNetworkDidFlag())
				.withVinSpecificDidFlag(didDto.getVinSpecificDidFlag())
				.build();
			dids.add(didEntity);
		}
		
		return new com.djt.cvpp.ota.orfin.odl.model.Node
			.NodeBuilder()
			.withAcronym(nodeDto.getAcronym())
			.withAddress(nodeDto.getAddress())
			.withActivationTime(nodeDto.getActivationTime())
			.withGatewayNodeId(nodeDto.getGatewayNodeId())
			.withGatewayType(nodeDto.getGatewayType())
			.withHasConditionBasedOnDtc(nodeDto.getHasConditionBasedOnDtc())
			.withIsOvtp(nodeDto.getIsOvtp())
			.withOvtpDestinationAddress(nodeDto.getOvtpDestinationAddress())
			.withSpecificationCategoryType(com.djt.cvpp.ota.orfin.odl.model.enums.SpecificationCategoryType.valueOf(nodeDto.getSpecificationCategoryType()))
			.withIgnoredDids(ignoredDids)
			.withDids(dids)
			.build();
	}
	
	public com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Odl mapEntityToVehicleDto(
		com.djt.cvpp.ota.orfin.odl.model.Odl odlEntity,
		Set<com.djt.cvpp.ota.orfin.odl.model.Node> nodesToInclude,
		Boolean includeEcgSignals) {

		com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Odl vehicleOdlDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Odl();
		
		List<com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Network> networks = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.orfin.odl.model.Network> networksIterator = odlEntity.getNetworks().iterator();
		while (networksIterator.hasNext()) {
			
			com.djt.cvpp.ota.orfin.odl.model.Network networkEntity = networksIterator.next();
			
			Map<String, com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.SpecificationCategory> specificationCategoryDtoMap = new HashMap<>();
			
			Iterator<com.djt.cvpp.ota.orfin.odl.model.Node> nodesIterator = networkEntity.getNodes().iterator();
			while (nodesIterator.hasNext()) {
				
				com.djt.cvpp.ota.orfin.odl.model.Node nodeEntity = nodesIterator.next();
				
				if (nodesToInclude.contains(nodeEntity)) {

					List<com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Did> dids = new ArrayList<>();
					Iterator<com.djt.cvpp.ota.orfin.odl.model.Did> didsIterator = nodeEntity.getDids().iterator();
					while (didsIterator.hasNext()) {
						
						com.djt.cvpp.ota.orfin.odl.model.Did didEntity = didsIterator.next();
						
						com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Did vehicleDidDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Did();
						vehicleDidDto.setDid(didEntity.getDidName());
						if (didEntity.getPrivateNetworkDidFlag()) {
							vehicleDidDto.setPrivateNetworkDIDFlag("Y");	
						} else {
							vehicleDidDto.setPrivateNetworkDIDFlag("N");	
						}
						if (didEntity.getDirectConfigurationDidFlag()) {
							vehicleDidDto.setDirectConfigurationDIDFlag("Y");	
						} else {
							vehicleDidDto.setDirectConfigurationDIDFlag("N");	
						}
						if (didEntity.getVinSpecificDidFlag()) {
							vehicleDidDto.setVinspecificDIDFlag("Y");	
						} else {
							vehicleDidDto.setVinspecificDIDFlag("N");	
						}
						dids.add(vehicleDidDto);
					}
					
					List<String> ignoredDids = new ArrayList<>();
					ignoredDids.addAll(nodeEntity.getIgnoredDids());					
					
					com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Node vehicleNodeDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Node();
					vehicleNodeDto.setAddress(nodeEntity.getAddress());
					if (nodeEntity.getIsOvtp() != null && nodeEntity.getIsOvtp()) {
						vehicleNodeDto.setOVTP("Y");	
					} else {
						vehicleNodeDto.setOVTP("N");
					}
					vehicleNodeDto.setOvtpDestinationAddress(nodeEntity.getOvtpDestinationAddress());
					vehicleNodeDto.setNetworkName(networkEntity.getNetworkName());
					vehicleNodeDto.setNetworkProtocol(networkEntity.getProtocol());
					if (networkEntity.getDataRate() != null && !networkEntity.getDataRate().trim().isEmpty()) {
						vehicleNodeDto.setNetworkDataRate(Integer.valueOf(networkEntity.getDataRate().trim()));
					}
										
					com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Ecuacronym vehicleEcuacronymDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Ecuacronym();
					com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Gateway vehicleGatewayDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Gateway();
					vehicleGatewayDto.setGatewayNodeID(nodeEntity.getGatewayNodeId());
					vehicleGatewayDto.setGatewayType(nodeEntity.getGatewayType());
					vehicleEcuacronymDto.setGateway(vehicleGatewayDto);
					com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.IgnoreDids vehicleIgnoreDidsDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.IgnoreDids();
					vehicleIgnoreDidsDto.setIgnoreDID(nodeEntity.getIgnoredDids());
					vehicleEcuacronymDto.setIgnoreDIDs(vehicleIgnoreDidsDto);
					vehicleEcuacronymDto.setAcronym(nodeEntity.getAcronym());
					vehicleEcuacronymDto.setOptimizedDID(dids);
					vehicleEcuacronymDto.setDiagnosticSpecificationResponse(nodeEntity.getDiagnosticSpecificationResponse());
					vehicleNodeDto.addEcuacronym(vehicleEcuacronymDto);
					if (nodeEntity.getHasConditionBasedOnDtc() != null && nodeEntity.getHasConditionBasedOnDtc()) {
						vehicleEcuacronymDto.setHasConditionBasedOnDTC("Y");	
					} else {
						vehicleEcuacronymDto.setHasConditionBasedOnDTC("N");
					}
					
					String specificationCategoryType = nodeEntity.getSpecificationCategoryType().toString();
					com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.SpecificationCategory specificationCategoryDto = specificationCategoryDtoMap.get(specificationCategoryType);
					if (specificationCategoryDto == null) {
						
						specificationCategoryDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.SpecificationCategory();
						specificationCategoryDto.setType(specificationCategoryType);
						specificationCategoryDtoMap.put(specificationCategoryType, specificationCategoryDto);
					}
					specificationCategoryDto.addNode(vehicleNodeDto);
				}
			}
			
			List<com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.SpecificationCategory> specificationCategories = new ArrayList<>();
			specificationCategories.addAll(specificationCategoryDtoMap.values());
			
			com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Network networkVehicleDto = new com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Network();
			networkVehicleDto.setNetworkDataRate(networkEntity.getDataRate());
			networkVehicleDto.setDlcname(networkEntity.getDclName());
			networkVehicleDto.setNetworkName(networkEntity.getNetworkName());
			networkVehicleDto.setPins(networkEntity.getNetworkPins());
			networkVehicleDto.setNetworkProtocol(networkEntity.getProtocol());
			networkVehicleDto.setSpecificationCategory(specificationCategories);
			networks.add(networkVehicleDto);
		}
		vehicleOdlDto.setNetwork(networks);
		
		List<String> ecgSignals = new ArrayList<>();
		if (includeEcgSignals) {
			
			Iterator<com.djt.cvpp.ota.orfin.odl.model.EcgSignal> iterator = odlEntity.getEcgSignals().iterator();
			while (iterator.hasNext()) {
				ecgSignals.add(iterator.next().getEcgSignalName());
			}
			vehicleOdlDto.setEcgSignals(ecgSignals);
		}
		
		return vehicleOdlDto;
	}
}
