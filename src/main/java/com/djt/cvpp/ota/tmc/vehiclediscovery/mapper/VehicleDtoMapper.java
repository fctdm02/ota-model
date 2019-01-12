/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.mapper;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.djt.cvpp.ota.common.mapper.DtoMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VehicleDtoMapper implements DtoMapper<com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle, com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Vehicle> {

	public com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Vehicle mapEntityToDto(com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle vehicleEntity) {
		
		com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Vehicle vehicleDto = new com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Vehicle();
		vehicleDto.setCreated(vehicleEntity.getCreatedByTimestamp().toString());
		vehicleDto.setFleetId(vehicleEntity.getParentFleet().getFleetId().toString());
		vehicleDto.setId(vehicleEntity.getUuid().toString());
		vehicleDto.setMake(vehicleEntity.getMake());
		vehicleDto.setModel(vehicleEntity.getModel());
		vehicleDto.setName(vehicleEntity.getName());
		vehicleDto.setTenantId(vehicleEntity.getTenantUuid().toString());
		vehicleDto.setType(vehicleEntity.getType());
		vehicleDto.setVin(vehicleEntity.getVin());
		vehicleDto.setYear(vehicleEntity.getYear());
		vehicleDto.setTags(mapTagsEntityToDto(vehicleEntity.getTags()));
		
		List<com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Module> modules = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.tmc.vehiclediscovery.model.Node> nodesIterator = vehicleEntity.getNodes().iterator();
		while (nodesIterator.hasNext()) {
		
			com.djt.cvpp.ota.tmc.vehiclediscovery.model.Node nodeEntity = nodesIterator.next();
			
			com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Module moduleDto = new com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Module();
			moduleDto.setAddress(nodeEntity.getAddress());
			moduleDto.setName(nodeEntity.getName());
			modules.add(moduleDto);
			
			List<com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Did> dids = new ArrayList<>();
			Iterator<com.djt.cvpp.ota.tmc.vehiclediscovery.model.Did> didsIterator = nodeEntity.getDids().iterator();
			while (didsIterator.hasNext()) {
				
				com.djt.cvpp.ota.tmc.vehiclediscovery.model.Did didEntity = didsIterator.next();
				
				com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Did didDto = new com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Did();
				didDto.setName(didEntity.getDidAddress());
				didDto.setValue(didEntity.getValue());
				dids.add(didDto);
			}
			moduleDto.setDids(dids);
		}
		vehicleDto.setModules(modules);		
		
		return vehicleDto;
	}
	
	private com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Tags mapTagsEntityToDto(Set<com.djt.cvpp.ota.tmc.common.model.Tag> tagEntities) {
		
		com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Tags dto = new com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Tags(); 
		
		Iterator<com.djt.cvpp.ota.tmc.common.model.Tag> iterator = tagEntities.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.tmc.common.model.Tag tagEntity = iterator.next();
			
			String tagName = tagEntity.getName();
			String tagValue = tagEntity.getValue();
			
			if (tagName.equals(com.djt.cvpp.ota.tmc.common.model.Tag.CAMPAIGN_ID)) {
				dto.setCampaignID(tagValue);
			} 
			
			if (tagName.equals(com.djt.cvpp.ota.tmc.common.model.Tag.DEPLOYMENT_PROTOCOL)) {
				dto.setDeploymentProtocol(tagValue);
			}
			
			if (tagName.equals(com.djt.cvpp.ota.tmc.common.model.Tag.DEPLOYMENT_URL)) {
				dto.setDeploymentURL(tagValue);
			}
			
			if (tagName.equals(com.djt.cvpp.ota.tmc.common.model.Tag.DOMAIN)) {
				dto.setDomain(tagValue);
			}
			
			if (tagName.equals(com.djt.cvpp.ota.tmc.common.model.Tag.DOMAIN_INSTANCE)) {
				dto.setDomainInstance(tagValue);
			}
			
			if (tagName.equals(com.djt.cvpp.ota.tmc.common.model.Tag.DOMAIN_INSTANCE_VERSION)) {
				dto.setDomainInstanceVersion(tagValue);
			}
			
			if (tagName.equals(com.djt.cvpp.ota.tmc.common.model.Tag.PROGRAM_CODE)) {
				dto.setProgramCode(tagValue);
			}
			
			if (tagName.equals(com.djt.cvpp.ota.tmc.common.model.Tag.MODEL_YEAR)) {
				dto.setModelYear(tagValue);
			}
		}		
		
		return dto;
	}
	
	public com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle mapDtoToEntity(com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Vehicle vehicleDto) {
		
		UUID uuid = UUID.fromString(vehicleDto.getId());
		String name = vehicleDto.getName();
		String description = "";
		URI uri = null;
		Instant createdTimestamp = null;
		com.djt.cvpp.ota.tmc.common.model.Author author = null;
		
		UUID tenantUUid = UUID.fromString(vehicleDto.getTenantId());
		
		Set<com.djt.cvpp.ota.tmc.common.model.Tag> tags = mapTagsDtoToEntity(vehicleDto.getTags());
		
		UUID fleetId = UUID.fromString(vehicleDto.getFleetId());
		com.djt.cvpp.ota.tmc.vehiclediscovery.model.Fleet parentFleet = new com.djt.cvpp.ota.tmc.vehiclediscovery.model.Fleet(fleetId);
		
		String vin = vehicleDto.getVin();
		String make = vehicleDto.getMake();
		String model = vehicleDto.getModel();
		String year = vehicleDto.getYear();
		String type = vehicleDto.getType();
		Instant modifiedTimestamp = null;
		
		com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle vehicleEntity = new com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle(
			tenantUUid,	
			uuid,
			name,
			description,
			uri,
			createdTimestamp,
			author,
			tags,
			parentFleet,
			vin,
			make,
			model,
			year,
			type,
			modifiedTimestamp);
		
		Set<com.djt.cvpp.ota.tmc.vehiclediscovery.model.Node> nodes = new TreeSet<>();
		Iterator<com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Module> iterator = vehicleDto.getModules().iterator();
		while (iterator.hasNext()) {
			
			nodes.add(mapNodeDtoToNodeEntity(iterator.next()));
		}
		vehicleEntity.setNodes(nodes);
						
		return vehicleEntity;		
	}
	
	private Set<com.djt.cvpp.ota.tmc.common.model.Tag> mapTagsDtoToEntity(com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Tags dto) {
		
		Set<com.djt.cvpp.ota.tmc.common.model.Tag> tagEntities = new HashSet<>();
		
		if (isNonEmpty(dto.getCampaignID())) {
			tagEntities.add(new com.djt.cvpp.ota.tmc.common.model.Tag(com.djt.cvpp.ota.tmc.common.model.Tag.CAMPAIGN_ID, dto.getCampaignID()));
		}
		
		if (isNonEmpty(dto.getDeploymentProtocol())) {
			tagEntities.add(new com.djt.cvpp.ota.tmc.common.model.Tag(com.djt.cvpp.ota.tmc.common.model.Tag.DEPLOYMENT_PROTOCOL, dto.getDeploymentProtocol()));
		} 
		
		if (isNonEmpty(dto.getDeploymentURL())) {
			tagEntities.add(new com.djt.cvpp.ota.tmc.common.model.Tag(com.djt.cvpp.ota.tmc.common.model.Tag.DEPLOYMENT_URL, dto.getDeploymentURL()));
		} 
		
		if (isNonEmpty(dto.getDomain())) {
			tagEntities.add(new com.djt.cvpp.ota.tmc.common.model.Tag(com.djt.cvpp.ota.tmc.common.model.Tag.DOMAIN, dto.getDomain()));
		}
		
		if (isNonEmpty(dto.getDomainInstance())) {
			tagEntities.add(new com.djt.cvpp.ota.tmc.common.model.Tag(com.djt.cvpp.ota.tmc.common.model.Tag.DOMAIN_INSTANCE, dto.getDomainInstance()));
		}
		
		if (isNonEmpty(dto.getDomainInstanceVersion())) {
			tagEntities.add(new com.djt.cvpp.ota.tmc.common.model.Tag(com.djt.cvpp.ota.tmc.common.model.Tag.DOMAIN_INSTANCE_VERSION, dto.getDomainInstanceVersion()));
		}
		
		if (isNonEmpty(dto.getProgramCode())) {
			tagEntities.add(new com.djt.cvpp.ota.tmc.common.model.Tag(com.djt.cvpp.ota.tmc.common.model.Tag.PROGRAM_CODE, dto.getProgramCode()));
		}
		
		if (isNonEmpty(dto.getModelYear())) {
			tagEntities.add(new com.djt.cvpp.ota.tmc.common.model.Tag(com.djt.cvpp.ota.tmc.common.model.Tag.MODEL_YEAR, dto.getModelYear()));
		}
				
		return tagEntities;
	}
	
	private boolean isNonEmpty(String s) {
		if (s != null && !s.isEmpty()) {
			return true;
		}
		return false;
	}
	
	private com.djt.cvpp.ota.tmc.vehiclediscovery.model.Node mapNodeDtoToNodeEntity(com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Module nodeDto) {

		com.djt.cvpp.ota.tmc.vehiclediscovery.model.Node nodeEntity = new com.djt.cvpp.ota.tmc.vehiclediscovery.model.Node(nodeDto.getName(), nodeDto.getAddress());
		
		Set<com.djt.cvpp.ota.tmc.vehiclediscovery.model.Did> dids = new TreeSet<>();		
		Iterator<com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Did> iterator = nodeDto.getDids().iterator();
		while (iterator.hasNext()) {
			
			dids.add(mapDidDtoToDidEntity(iterator.next()));
		}
		nodeEntity.setDids(dids);		
		
		return nodeEntity;
	}
	
	private com.djt.cvpp.ota.tmc.vehiclediscovery.model.Did mapDidDtoToDidEntity(com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.dto.Did didDto) {
		
		return new com.djt.cvpp.ota.tmc.vehiclediscovery.model.Did(didDto.getName(), didDto.getValue());
	}	
}
