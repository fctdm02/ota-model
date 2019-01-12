/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.deployment.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.djt.cvpp.ota.common.mapper.DtoMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class DeploymentDtoMapper implements DtoMapper<com.djt.cvpp.ota.tmc.deployment.model.Deployment, com.djt.cvpp.ota.tmc.deployment.mapper.dto.Deployment> {

	public com.djt.cvpp.ota.tmc.deployment.mapper.dto.Deployment mapEntityToDto(com.djt.cvpp.ota.tmc.deployment.model.Deployment entity) {
		
		com.djt.cvpp.ota.tmc.deployment.mapper.dto.Deployment dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.Deployment();
		dto.setTenantUuid(entity.getTenantUuid());
		dto.setUuid(entity.getUuid());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setUri(entity.getUri());
		dto.setCreatedTimestamp(entity.getCreatedTimestamp());
		dto.setAuthor(mapAuthorEntityToAuthorDto(entity.getAuthor()));
		dto.setTags(mapTagEntitiesToTagDtos(entity.getTags()));
		
		dto.setRelease(mapReleaseEntityToReleaseDto(entity.getRelease()));
		
		return dto;
	}
		
	private com.djt.cvpp.ota.tmc.deployment.mapper.dto.Release mapReleaseEntityToReleaseDto(com.djt.cvpp.ota.tmc.deployment.model.Release entity) {
		
		com.djt.cvpp.ota.tmc.deployment.mapper.dto.Release dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.Release();
		
		dto.setTenantUuid(entity.getTenantUuid());
		dto.setUuid(entity.getUuid());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setUri(entity.getUri());
		dto.setCreatedTimestamp(entity.getCreatedTimestamp());
		dto.setAuthor(mapAuthorEntityToAuthorDto(entity.getAuthor()));
		dto.setTags(mapTagEntitiesToTagDtos(entity.getTags()));
		
		dto.setTargetBundles(mapTargetBundleEntitiesToTargetBundleDtos(entity.getTargetBundles()));
		
		return dto;
	}
	
	private List<com.djt.cvpp.ota.tmc.deployment.mapper.dto.TargetBundle> mapTargetBundleEntitiesToTargetBundleDtos(Set<com.djt.cvpp.ota.tmc.deployment.model.TargetBundle> entities) {
		
		List<com.djt.cvpp.ota.tmc.deployment.mapper.dto.TargetBundle> list = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.tmc.deployment.model.TargetBundle> iterator = entities.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.tmc.deployment.model.TargetBundle entity = iterator.next();
			com.djt.cvpp.ota.tmc.deployment.mapper.dto.TargetBundle dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.TargetBundle();
			dto.setTenantUuid(entity.getTenantUuid());
			dto.setUuid(entity.getUuid());
			dto.setName(entity.getName());
			dto.setDescription(entity.getDescription());
			dto.setUri(entity.getUri());
			dto.setCreatedTimestamp(entity.getCreatedTimestamp());
			dto.setAuthor(mapAuthorEntityToAuthorDto(entity.getAuthor()));
			dto.setTags(mapTagEntitiesToTagDtos(entity.getTags()));
			
			dto.setReleaseArtifacts(mapReleaseArtifactEntitiesToReleaseArtifactDtos(entity.getReleaseArtifacts()));
			dto.setTargetMatchers(mapTargetMatcherEntitiesToTargetMatcherDtos(entity.getTargetMatchers()));
			
			list.add(dto);
		}
		return list;
	}

	private List<com.djt.cvpp.ota.tmc.deployment.mapper.dto.ReleaseArtifact> mapReleaseArtifactEntitiesToReleaseArtifactDtos(Set<com.djt.cvpp.ota.tmc.deployment.model.ReleaseArtifact> entities) {
		
		List<com.djt.cvpp.ota.tmc.deployment.mapper.dto.ReleaseArtifact> list = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.tmc.deployment.model.ReleaseArtifact> iterator = entities.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.tmc.deployment.model.ReleaseArtifact entity = iterator.next();
			com.djt.cvpp.ota.tmc.deployment.mapper.dto.ReleaseArtifact dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.ReleaseArtifact();
			dto.setTenantUuid(entity.getTenantUuid());
			dto.setUuid(entity.getUuid());
			dto.setName(entity.getName());
			dto.setDescription(entity.getDescription());
			dto.setUri(entity.getUri());
			dto.setCreatedTimestamp(entity.getCreatedTimestamp());
			dto.setAuthor(mapAuthorEntityToAuthorDto(entity.getAuthor()));
			dto.setTags(mapTagEntitiesToTagDtos(entity.getTags()));
			
			dto.setByteLength(entity.getByteLength());
			dto.setChecksum(entity.getChecksum());
			dto.setType(entity.getType());
			list.add(dto);
		}
		return list;
	}

	private List<com.djt.cvpp.ota.tmc.deployment.mapper.dto.TargetMatcher> mapTargetMatcherEntitiesToTargetMatcherDtos(Set<com.djt.cvpp.ota.tmc.deployment.model.TargetMatcher> entities) {
		
		List<com.djt.cvpp.ota.tmc.deployment.mapper.dto.TargetMatcher> list = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.tmc.deployment.model.TargetMatcher> iterator = entities.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.tmc.deployment.model.TargetMatcher entity = iterator.next();
			com.djt.cvpp.ota.tmc.deployment.mapper.dto.TargetMatcher dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.TargetMatcher();
			dto.setName(entity.getName());
			dto.setValue(entity.getValue());
			dto.setType(entity.getType());
			list.add(dto);
		}
		return list;
	}
	
	private com.djt.cvpp.ota.tmc.deployment.mapper.dto.Author mapAuthorEntityToAuthorDto(com.djt.cvpp.ota.tmc.common.model.Author entity) {
		
		com.djt.cvpp.ota.tmc.deployment.mapper.dto.Author dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.Author();
		dto.setTenantId(entity.getTenantId());
		dto.setAuthorId(entity.getAuthorId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setType(entity.getType());
		
		return dto;
	}
	
	private List<com.djt.cvpp.ota.tmc.deployment.mapper.dto.Tag> mapTagEntitiesToTagDtos(Set<com.djt.cvpp.ota.tmc.common.model.Tag> entities) {
		
		List<com.djt.cvpp.ota.tmc.deployment.mapper.dto.Tag> list = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.tmc.common.model.Tag> iterator = entities.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.tmc.common.model.Tag entity = iterator.next();
			com.djt.cvpp.ota.tmc.deployment.mapper.dto.Tag dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.Tag();
			dto.setName(entity.getName());
			dto.setValue(entity.getValue());
			list.add(dto);
		}
		return list;
	}
	
	public com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcReleaseArtifactRequest mapReleaseArtifactEntityToTmcReleaseArtifactRequestDto(com.djt.cvpp.ota.tmc.deployment.model.ReleaseArtifact entity) {
		
		com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcReleaseArtifactRequest dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcReleaseArtifactRequest();
		dto.setTenantUuid(entity.getTenantUuid());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setAuthor(mapAuthorEntityToAuthorDto(entity.getAuthor()));
		dto.setUri(entity.getUri());
		dto.setType(entity.getType());
		dto.setChecksum(entity.getChecksum());
		dto.setByteLength(entity.getByteLength());
		dto.setTags(mapTagEntitiesToTagDtos(entity.getTags()));
		
		return dto;
	}

	public com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcTargetBundleRequest mapTargetBundleEntityToTmcTargetBundleRequestDto(com.djt.cvpp.ota.tmc.deployment.model.TargetBundle entity) {
		
		com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcTargetBundleRequest dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcTargetBundleRequest();
		dto.setTenantUuid(entity.getTenantUuid());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setAuthor(mapAuthorEntityToAuthorDto(entity.getAuthor()));
		dto.setTargetMatchers(mapTargetMatcherEntitiesToTargetMatcherDtos(entity.getTargetMatchers()));

		List<UUID> releaseArtifactUuids = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.tmc.deployment.model.ReleaseArtifact> iterator = entity.getReleaseArtifacts().iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.tmc.deployment.model.ReleaseArtifact releaseArtifactEntity = iterator.next();
			releaseArtifactUuids.add(releaseArtifactEntity.getUuid());
		}
		dto.setReleaseArtifactUuids(releaseArtifactUuids);
		
		dto.setTags(mapTagEntitiesToTagDtos(entity.getTags()));
		
		return dto;
	}

	public com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcReleaseRequest mapReleaseEntityToTmcReleaseRequestDto(com.djt.cvpp.ota.tmc.deployment.model.Release entity) {

		com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcReleaseRequest dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcReleaseRequest();
		dto.setTenantUuid(entity.getTenantUuid());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setAuthor(mapAuthorEntityToAuthorDto(entity.getAuthor()));
		
		List<UUID> targetBundlesUuids = new ArrayList<>();
		Iterator<com.djt.cvpp.ota.tmc.deployment.model.TargetBundle> iterator = entity.getTargetBundles().iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.tmc.deployment.model.TargetBundle targetBundleEntity = iterator.next();
			targetBundlesUuids.add(targetBundleEntity.getUuid());
		}
		dto.setTargetBundleUuids(targetBundlesUuids);
		
		dto.setTags(mapTagEntitiesToTagDtos(entity.getTags()));
		
		return dto;
	}
	
	public com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcDeploymentRequest mapDeploymentEntityToTmcDeploymentRequestDto(com.djt.cvpp.ota.tmc.deployment.model.Deployment entity) {

		com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcDeploymentRequest dto = new com.djt.cvpp.ota.tmc.deployment.mapper.dto.TmcDeploymentRequest();
		dto.setTenantUuid(entity.getTenantUuid());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setAuthor(mapAuthorEntityToAuthorDto(entity.getAuthor()));
		dto.setReleaseUuid(entity.getRelease().getUuid());
		dto.setUri(entity.getUri());
		dto.setTags(mapTagEntitiesToTagDtos(entity.getTags()));
		
		return dto;
	}
	
	public com.djt.cvpp.ota.tmc.deployment.model.Deployment mapDtoToEntity(com.djt.cvpp.ota.tmc.deployment.mapper.dto.Deployment tmcBinaryDto) {

		throw new UnsupportedOperationException("Mapping TMC TmcVehicle Status Message DTO to entity is not supported.");
	}
}
