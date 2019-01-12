/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.vadr.mapper;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.mapper.DtoMapper;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.vadr.model.enums.ApplicationNecessity;
import com.djt.cvpp.ota.vadr.model.enums.FileFormat;
import com.djt.cvpp.ota.vadr.model.enums.ReleaseState;
import com.djt.cvpp.ota.vadr.model.enums.SoftwareCategory;
import com.djt.cvpp.ota.vadr.model.enums.SoftwareClassification;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class DomainDtoMapper implements DtoMapper<com.djt.cvpp.ota.vadr.model.Domain, com.djt.cvpp.ota.vadr.mapper.dto.Domain> {

	public com.djt.cvpp.ota.vadr.mapper.dto.Domain mapEntityToDto(com.djt.cvpp.ota.vadr.model.Domain domainEntity) {
        
    	com.djt.cvpp.ota.vadr.mapper.dto.Domain domainDto = new com.djt.cvpp.ota.vadr.mapper.dto.Domain();
	    domainDto.setDomainName(domainEntity.getDomainName());
	    domainDto.setDomainDescription(domainEntity.getDomainDescription());
	
	    Iterator<com.djt.cvpp.ota.vadr.model.EcuNode> ecuNodeIterator = domainEntity.getEcuNodes().iterator();
	    while (ecuNodeIterator.hasNext()) {
	    
		    com.djt.cvpp.ota.vadr.model.EcuNode ecuNodeEntity = ecuNodeIterator.next();
		    
		    com.djt.cvpp.ota.vadr.mapper.dto.EcuNode ecuNodeDto = new com.djt.cvpp.ota.vadr.mapper.dto.EcuNode();
		    ecuNodeDto.setEcuAcronymName(ecuNodeEntity.getEcuAcronymName());
		    ecuNodeDto.setEcuLogicalAddress(ecuNodeEntity.getEcuLogicalAddress());
		    domainDto.getEcuNodes().add(ecuNodeDto);
	    }
	    
	    Iterator<com.djt.cvpp.ota.vadr.model.DomainInstance> domainInstanceIterator = domainEntity.getDomainInstances().iterator();
	    while (domainInstanceIterator.hasNext()) {
	    
		    com.djt.cvpp.ota.vadr.model.DomainInstance domainInstanceEntity = domainInstanceIterator.next();
		    
		    com.djt.cvpp.ota.vadr.mapper.dto.DomainInstance domainInstanceDto = new com.djt.cvpp.ota.vadr.mapper.dto.DomainInstance();
		    domainInstanceDto.setDomainInstanceName(domainInstanceEntity.getDomainInstanceName());
		    domainInstanceDto.setDomainInstanceVersion(domainInstanceEntity.getDomainInstanceVersion());
		    domainInstanceDto.setDomainInstanceDescription(domainInstanceEntity.getDomainInstanceDescription());
		    domainInstanceDto.setReleasedDate(domainInstanceEntity.getReleasedDate());
		    domainInstanceDto.setReleaseState(domainInstanceEntity.getReleaseState().toString());
		    domainInstanceDto.setProductionState(domainInstanceEntity.getProductionState().toString());
		    
		    Iterator<com.djt.cvpp.ota.vadr.model.EcuHardware> ecuHardwareIterator = domainInstanceEntity.getEcuHardwares().iterator();
		    while (ecuHardwareIterator.hasNext()) {
		    
			    com.djt.cvpp.ota.vadr.model.EcuHardware ecuHardwareEntity = ecuHardwareIterator.next();
			    
			    com.djt.cvpp.ota.vadr.mapper.dto.EcuHardware ecuHardwareDto = new com.djt.cvpp.ota.vadr.mapper.dto.EcuHardware();
			    ecuHardwareDto.setEcuAcronymName(ecuHardwareEntity.getEcuNode().getEcuAcronymName());
			    ecuHardwareDto.setEcuLogicalAddress(ecuHardwareEntity.getEcuNode().getEcuLogicalAddress());
			    ecuHardwareDto.setEcuHardwarePartNumber(ecuHardwareEntity.getEcuHardwarePartNumber());
			    domainInstanceDto.getEcuHardwares().add(ecuHardwareDto);
		    }
		    
		    Iterator<com.djt.cvpp.ota.vadr.model.EcuSoftware> ecuSoftwareIterator = domainInstanceEntity.getEcuSoftwares().iterator();
		    while (ecuSoftwareIterator.hasNext()) {
		    
			    com.djt.cvpp.ota.vadr.model.EcuSoftware ecuSoftwareEntity = ecuSoftwareIterator.next();
			    
			    com.djt.cvpp.ota.vadr.mapper.dto.EcuSoftware ecuSoftwareDto = new com.djt.cvpp.ota.vadr.mapper.dto.EcuSoftware();
			    ecuSoftwareDto.setEcuAcronymName(ecuSoftwareEntity.getEcuNode().getEcuAcronymName());
			    ecuSoftwareDto.setEcuLogicalAddress(ecuSoftwareEntity.getEcuNode().getEcuLogicalAddress());
			    ecuSoftwareDto.setEcuSoftwarePartNumber(ecuSoftwareEntity.getEcuSoftwarePartNumber());
			    ecuSoftwareDto.setSoftwareName(ecuSoftwareEntity.getSoftwareName());
			    ecuSoftwareDto.setSoftwareVersionNumber(ecuSoftwareEntity.getSoftwareVersionNumber());
			    ecuSoftwareDto.setCategory(ecuSoftwareEntity.getCategory());
			    ecuSoftwareDto.setDescription(ecuSoftwareEntity.getDescription());
			    
			    com.djt.cvpp.ota.vadr.model.BinaryMetadata binaryMetadataEntity = ecuSoftwareEntity.getBinaryMetadata();
			    com.djt.cvpp.ota.vadr.mapper.dto.BinaryMetadata binaryMetadataDto = mapBinaryMetadata(binaryMetadataEntity);
			    ecuSoftwareDto.setBinaryMetadata(binaryMetadataDto);
			    domainInstanceDto.getEcuSoftwares().add(ecuSoftwareDto);
		    }

		    Iterator<com.djt.cvpp.ota.vadr.model.Application> applicationIterator = domainInstanceEntity.getApplications().iterator();
		    while (applicationIterator.hasNext()) {
		    
			    com.djt.cvpp.ota.vadr.model.Application applicationEntity = applicationIterator.next();
			    com.djt.cvpp.ota.vadr.mapper.dto.Application applicationDto = mapApplication(applicationEntity);
			    domainInstanceDto.getApplications().add(applicationDto);
		    }		    
		    
		    domainDto.getDomainInstances().add(domainInstanceDto);
	    }
	    
	    Iterator<com.djt.cvpp.ota.vadr.model.Domain> dependentDomainIterator = domainEntity.getDomainDependencies().iterator();
	    while (dependentDomainIterator.hasNext()) {
	    
		    com.djt.cvpp.ota.vadr.model.Domain dependentDomainEntity = dependentDomainIterator.next();
		    com.djt.cvpp.ota.vadr.mapper.dto.Domain dependentDomainDto = this.mapEntityToDto(dependentDomainEntity);
		    domainDto.getDomainDependencies().add(dependentDomainDto);
	    }	    

	    // This is where the entity object graph comes into play
	    domainDto.getValidationWarnings().addAll(domainEntity.getValidationWarnings());
	    
	    return domainDto;
    }
    
    private com.djt.cvpp.ota.vadr.mapper.dto.Application mapApplication(com.djt.cvpp.ota.vadr.model.Application applicationEntity) {
    	
    	com.djt.cvpp.ota.vadr.mapper.dto.Application applicationDto = new com.djt.cvpp.ota.vadr.mapper.dto.Application();
	    applicationDto.setAppId(applicationEntity.getAppId());
	    applicationDto.setAppName(applicationEntity.getAppName());
	    applicationDto.setAppVersion(applicationEntity.getAppVersion());
	    com.djt.cvpp.ota.vadr.model.Region regionEntity = applicationEntity.getRegion();
	    if (regionEntity != null) {
	    	applicationDto.setRegion(regionEntity.toString());	
	    }
	    applicationDto.setDescription(applicationEntity.getDescription());
	    applicationDto.setOwnerGroup(applicationEntity.getOwnerGroup());
	    applicationDto.setOwnerKeyContact(applicationEntity.getOwnerKeyContact());
	    applicationDto.setRequiresSubscription(applicationEntity.getRequiresSubscription());
	    com.djt.cvpp.ota.vadr.model.enums.ApplicationNecessity applicationNecessityEntity = applicationEntity.getApplicationNecessity();
	    if (applicationNecessityEntity != null) {
	    	applicationDto.setApplicationNecessity(applicationNecessityEntity.toString());	
	    }
	    com.djt.cvpp.ota.vadr.model.enums.ReleaseState applicationReleaseState = applicationEntity.getReleaseState();
	    if (applicationReleaseState != null) {
	    	applicationDto.setReleaseState(applicationReleaseState.toString());	
	    }
	    Iterator<com.djt.cvpp.ota.vadr.model.BinaryMetadata> binaryMetadataEntityIterator = applicationEntity.getBinaryMetadatas().iterator();
	    while (binaryMetadataEntityIterator.hasNext()) {
	    	
	    	com.djt.cvpp.ota.vadr.model.BinaryMetadata binaryMetadataEntity = binaryMetadataEntityIterator.next();
	    	com.djt.cvpp.ota.vadr.mapper.dto.BinaryMetadata binaryMetadataDto = mapBinaryMetadata(binaryMetadataEntity);
		    applicationDto.getBinaryMetadatas().add(binaryMetadataDto);
	    }
	    			    
	    Iterator<com.djt.cvpp.ota.vadr.model.Application> applicationEntityIterator = applicationEntity.getApplicationDependencies().iterator();
	    while (applicationEntityIterator.hasNext()) {
	    	
	    	com.djt.cvpp.ota.vadr.model.Application dependentApplicationEntity = applicationEntityIterator.next();
	    	com.djt.cvpp.ota.vadr.mapper.dto.Application dependentApplicationDto = mapApplication(dependentApplicationEntity);
	    	applicationDto.getApplicationDependencies().add(dependentApplicationDto);
	    }
	    
	    return applicationDto;
    }
    
    private com.djt.cvpp.ota.vadr.mapper.dto.BinaryMetadata mapBinaryMetadata(com.djt.cvpp.ota.vadr.model.BinaryMetadata binaryMetadataEntity) {
    	
    	com.djt.cvpp.ota.vadr.mapper.dto.BinaryMetadata binaryMetadataDto = new com.djt.cvpp.ota.vadr.mapper.dto.BinaryMetadata();
	    binaryMetadataDto.setFileId(binaryMetadataEntity.getPersistentIdentity().intValue());
	    binaryMetadataDto.setEcuAcronymName(binaryMetadataEntity.getEcuAcronymName());
	    binaryMetadataDto.setEcuLogicalAddress(binaryMetadataEntity.getEcuLogicalAddress());
	    binaryMetadataDto.setPublicKeyHash(binaryMetadataEntity.getPublicKeyHash());
	    binaryMetadataDto.setSwPartNumber(binaryMetadataEntity.getSwPartNumber());
	    binaryMetadataDto.setLegacySwPartType(binaryMetadataEntity.getLegacySwPartType());
	    
	    String swCategory = null;
	    if (binaryMetadataEntity.getSwCategory() != null) {
	    	swCategory = binaryMetadataEntity.getSwCategory().toString();
	    }
	    binaryMetadataDto.setSwCategory(swCategory);

	    String swClassification = null;
	    if (binaryMetadataEntity.getSwClassification() != null) {
	    	swClassification = binaryMetadataEntity.getSwClassification().toString();
	    }
	    binaryMetadataDto.setSwClassification(swClassification);
	    
	    binaryMetadataDto.setDidAddress(binaryMetadataEntity.getDidAddress());
	    binaryMetadataDto.setIsOvtp(binaryMetadataEntity.getIsOvtp());
	    
	    Iterator<com.djt.cvpp.ota.vadr.model.ProgramLogicalBlock> programLogicalBlockEntityIterator = binaryMetadataEntity.getProgramLogicalBlocks().iterator();
	    while (programLogicalBlockEntityIterator.hasNext()) {
	    	
	    	com.djt.cvpp.ota.vadr.model.ProgramLogicalBlock programLogicalBlockEntity = programLogicalBlockEntityIterator.next();
	    	com.djt.cvpp.ota.vadr.mapper.dto.ProgramLogicalBlock programLogicalBlockDto = new com.djt.cvpp.ota.vadr.mapper.dto.ProgramLogicalBlock();
	    	programLogicalBlockDto.setStartAddress(programLogicalBlockEntity.getStartAddress());
	    	programLogicalBlockDto.setLength(programLogicalBlockEntity.getLength());
	    	binaryMetadataDto.getProgramLogicalBlocks().add(programLogicalBlockDto);
	    }

	    Iterator<com.djt.cvpp.ota.vadr.model.EraseLogicalBlock> eraseLogicalBlockEntityIterator = binaryMetadataEntity.getEraseLogicalBlocks().iterator();
	    while (eraseLogicalBlockEntityIterator.hasNext()) {
	    	
	    	com.djt.cvpp.ota.vadr.model.EraseLogicalBlock eraseLogicalBlockEntity = eraseLogicalBlockEntityIterator.next();
	    	com.djt.cvpp.ota.vadr.mapper.dto.EraseLogicalBlock eraseLogicalBlockDto = new com.djt.cvpp.ota.vadr.mapper.dto.EraseLogicalBlock();
	    	eraseLogicalBlockDto.setStartAddress(eraseLogicalBlockEntity.getStartAddress());
	    	eraseLogicalBlockDto.setLength(eraseLogicalBlockEntity.getLength());
	    	binaryMetadataDto.getEraseLogicalBlocks().add(eraseLogicalBlockDto);
	    }

	    Iterator<com.djt.cvpp.ota.vadr.model.VerificationLogicalBlock> verificationLogicalBlockEntityIterator = binaryMetadataEntity.getVerificationLogicalBlocks().iterator();
	    while (verificationLogicalBlockEntityIterator.hasNext()) {
	    	
	    	com.djt.cvpp.ota.vadr.model.VerificationLogicalBlock verificationLogicalBlockEntity = verificationLogicalBlockEntityIterator.next();
	    	com.djt.cvpp.ota.vadr.mapper.dto.VerificationLogicalBlock verificationLogicalBlockDto = new com.djt.cvpp.ota.vadr.mapper.dto.VerificationLogicalBlock();
	    	verificationLogicalBlockDto.setStartAddress(verificationLogicalBlockEntity.getStartAddress());
	    	verificationLogicalBlockDto.setLength(verificationLogicalBlockEntity.getLength());
	    	binaryMetadataDto.getVerificationLogicalBlocks().add(verificationLogicalBlockDto);
	    }
	    
	    binaryMetadataDto.setAutonomicUrl(binaryMetadataEntity.getAutonomicUrl());
	    binaryMetadataDto.setFileName(binaryMetadataEntity.getFileName());
	    
	    String binaryFileFormat = null;
	    if (binaryMetadataEntity.getFileFormat() != null) {
	    	binaryFileFormat = binaryMetadataEntity.getFileFormat().toString();
	    }
	    binaryMetadataDto.setFileFormat(binaryFileFormat);
	    
	    binaryMetadataDto.setDescription(binaryMetadataEntity.getDescription());
	    binaryMetadataDto.setUnzippedFileSize((int)binaryMetadataEntity.getUnzippedFileSize()); // TODO: Fix this cast
	    binaryMetadataDto.setZippedFileSize((int)binaryMetadataEntity.getZippedFileSize()); // TODO: Fix this cast
	    binaryMetadataDto.setVbfVersion(binaryMetadataEntity.getVbfVersion());
	    binaryMetadataDto.setDataFormatIdentifier(binaryMetadataEntity.getDataFormatIdentifier());
	    binaryMetadataDto.setFrameFormat(binaryMetadataEntity.getFrameFormat());
	    binaryMetadataDto.setCallStartAddress(binaryMetadataEntity.getCallStartAddress());
	    binaryMetadataDto.setFileChecksum(binaryMetadataEntity.getFileChecksum());
	    binaryMetadataDto.setFileFingerprint(binaryMetadataEntity.getFileFingerprint());
	    binaryMetadataDto.setProgramProtected(binaryMetadataEntity.isProgramProtected());
	    binaryMetadataDto.setFirmwareInstallType(binaryMetadataEntity.getFirmwareInstallType());
	    //binaryMetadataDto.setCreatedTimestamp(binaryMetadataEntity.getCreatedTimestamp()); // TODO: TDM: Fix this
	    //binaryMetadataDto.setLastModifiedTimestamp(binaryMetadataEntity.getLastModifiedByTimestamp()); // TODO: TDM: Fix this
	    			    
	    return binaryMetadataDto;
    }
    
    public com.djt.cvpp.ota.vadr.model.Domain mapDtoToEntity(com.djt.cvpp.ota.vadr.mapper.dto.Domain domainDto) throws EntityAlreadyExistsException, EntityDoesNotExistException {
    	
    	Set<com.djt.cvpp.ota.vadr.model.EcuNode> ecuNodes = new TreeSet<>();
    	Iterator<com.djt.cvpp.ota.vadr.mapper.dto.EcuNode> ecuNodeIterator = domainDto.getEcuNodes().iterator();
    	while (ecuNodeIterator.hasNext()) {
    		
    		com.djt.cvpp.ota.vadr.mapper.dto.EcuNode ecuNodeDto = ecuNodeIterator.next();
    		com.djt.cvpp.ota.vadr.model.EcuNode ecuNode = new com.djt.cvpp.ota.vadr.model.EcuNode
                    .EcuNodeBuilder()
                    .withEcuAcronymName(ecuNodeDto.getEcuAcronymName())
                    .withEcuLogicalAddress(ecuNodeDto.getEcuLogicalAddress())
                    .build();
            ecuNodes.add(ecuNode);
    	}
    	
    	com.djt.cvpp.ota.vadr.model.Domain domainEntity = new com.djt.cvpp.ota.vadr.model.Domain
                .DomainBuilder()
                .withDomainName(domainDto.getDomainName())
                .withDomainDescription(domainDto.getDomainDescription())
                .withEcuNodes(ecuNodes)
                .build();
    	
    	Iterator<com.djt.cvpp.ota.vadr.mapper.dto.DomainInstance> domainInstanceIterator = domainDto.getDomainInstances().iterator();
    	while (domainInstanceIterator.hasNext()) {
    		
    		com.djt.cvpp.ota.vadr.mapper.dto.DomainInstance domainInstanceDto = domainInstanceIterator.next();
    		
            Set<com.djt.cvpp.ota.vadr.model.EcuHardware> ecuHardwares = new TreeSet<>();
            Iterator<com.djt.cvpp.ota.vadr.mapper.dto.EcuHardware> ecuHardwareIterator = domainInstanceDto.getEcuHardwares().iterator();
            while (ecuHardwareIterator.hasNext()) {
            	
            	com.djt.cvpp.ota.vadr.mapper.dto.EcuHardware ecuHardwareDto = ecuHardwareIterator.next();
                com.djt.cvpp.ota.vadr.model.EcuHardware ecuHardware = new com.djt.cvpp.ota.vadr.model.EcuHardware
                        .EcuHardwareBuilder()
                        .withEcuNode(domainEntity.getChildEcuNode(ecuHardwareDto.getEcuAcronymName(), ecuHardwareDto.getEcuLogicalAddress()))
                        .withEcuHardwarePartNumber(ecuHardwareDto.getEcuHardwarePartNumber())
                        .build();
                ecuHardwares.add(ecuHardware);
            }
            
            Set<com.djt.cvpp.ota.vadr.model.EcuSoftware> ecuSoftwares = new TreeSet<>();
            Iterator<com.djt.cvpp.ota.vadr.mapper.dto.EcuSoftware> ecuSoftwareIterator = domainInstanceDto.getEcuSoftwares().iterator();
            while (ecuSoftwareIterator.hasNext()) {
            	
            	com.djt.cvpp.ota.vadr.mapper.dto.EcuSoftware ecuSoftwareDto = ecuSoftwareIterator.next();
            	
            	com.djt.cvpp.ota.vadr.model.BinaryMetadata binaryMetadataEntity = mapBinaryMetadataDtoToBinaryMetadataEntity(ecuSoftwareDto.getBinaryMetadata());
                
                com.djt.cvpp.ota.vadr.model.EcuSoftware ecuSoftwareEntity = new com.djt.cvpp.ota.vadr.model.EcuSoftware
                        .EcuSoftwareBuilder()
                        .withEcuNode(domainEntity.getChildEcuNode(ecuSoftwareDto.getEcuAcronymName(), ecuSoftwareDto.getEcuLogicalAddress()))
                        .withEcuSoftwarePartNumber(ecuSoftwareDto.getEcuSoftwarePartNumber())
                        .withSoftwareName(ecuSoftwareDto.getSoftwareName())
                        .withSoftwareVersionNumber(ecuSoftwareDto.getSoftwareVersionNumber())
                        .withCategory(ecuSoftwareDto.getCategory())
                        .withDescription(ecuSoftwareDto.getDescription())
                        .withBinaryMetadata(binaryMetadataEntity)
                        .build();
                ecuSoftwares.add(ecuSoftwareEntity);
            }

            Set<com.djt.cvpp.ota.vadr.model.Application> applications = new TreeSet<>();
            Iterator<com.djt.cvpp.ota.vadr.mapper.dto.Application> applicationIterator = domainInstanceDto.getApplications().iterator();
            while (applicationIterator.hasNext()) {
            	
            	com.djt.cvpp.ota.vadr.mapper.dto.Application applicationDto = applicationIterator.next();
            	com.djt.cvpp.ota.vadr.model.Application applicationEntity = mapApplicationDtoToApplicationEntity(applicationDto);
            	applications.add(applicationEntity);
            }
    		
            new com.djt.cvpp.ota.vadr.model.DomainInstance
	            .DomainInstanceBuilder()
	            .withParentDomain(domainEntity)
	            .withDomainInstanceName(domainInstanceDto.getDomainInstanceName())
	            .withDomainInstanceVersion(domainInstanceDto.getDomainInstanceVersion())
	            .withDomainInstanceDescription(domainInstanceDto.getDomainInstanceDescription())
	            .withReleasedDate(new Timestamp(AbstractEntity.getTimeKeeper().getCurrentTimeInMillis()))
	            .withReleaseState(com.djt.cvpp.ota.vadr.model.enums.ReleaseState.RELEASED)
	            .withProductionState(com.djt.cvpp.ota.vadr.model.enums.ProductionState.PRODUCTION)
	            .withEcuHardwares(ecuHardwares)
	            .withEcuSoftwares(ecuSoftwares)
	            .withApplications(applications)
	            .build();
        }
    	
    	Iterator<com.djt.cvpp.ota.vadr.mapper.dto.Domain> dependentDomainIterator = domainDto.getDomainDependencies().iterator();
    	while (dependentDomainIterator.hasNext()) {
    		
    		com.djt.cvpp.ota.vadr.mapper.dto.Domain depentDomainDto = dependentDomainIterator.next();
    		com.djt.cvpp.ota.vadr.model.Domain dependentDomainEntity = this.mapDtoToEntity(depentDomainDto);
    		domainEntity.addDomainDependency(dependentDomainEntity);
    	}    	
    	
    	return domainEntity;
    }

    private com.djt.cvpp.ota.vadr.model.Application mapApplicationDtoToApplicationEntity(com.djt.cvpp.ota.vadr.mapper.dto.Application applicationDto) {
    
    	Set<com.djt.cvpp.ota.vadr.model.BinaryMetadata> binaryMetadatas = new TreeSet<com.djt.cvpp.ota.vadr.model.BinaryMetadata>();
    	Iterator<com.djt.cvpp.ota.vadr.mapper.dto.BinaryMetadata> binaryMetadataIterator = applicationDto.getBinaryMetadatas().iterator();
    	while (binaryMetadataIterator.hasNext()) {
    		
    		com.djt.cvpp.ota.vadr.mapper.dto.BinaryMetadata binaryMetadataDto = binaryMetadataIterator.next();
    		com.djt.cvpp.ota.vadr.model.BinaryMetadata binaryMetadataEntity = mapBinaryMetadataDtoToBinaryMetadataEntity(binaryMetadataDto);
    		binaryMetadatas.add(binaryMetadataEntity);
    	}
    	
    	Set<com.djt.cvpp.ota.vadr.model.Application> dependentApplications = new TreeSet<com.djt.cvpp.ota.vadr.model.Application>();
    	Iterator<com.djt.cvpp.ota.vadr.mapper.dto.Application> dependentApplicationIterator = applicationDto.getApplicationDependencies().iterator();
    	while (dependentApplicationIterator.hasNext()) {
    		
    		com.djt.cvpp.ota.vadr.mapper.dto.Application dependentApplicationDto = dependentApplicationIterator.next();
    		com.djt.cvpp.ota.vadr.model.Application dependentApplicationEntity = mapApplicationDtoToApplicationEntity(dependentApplicationDto);
    		dependentApplications.add(dependentApplicationEntity);
    	}
    	
    	com.djt.cvpp.ota.vadr.model.Region regionEntity = new com.djt.cvpp.ota.vadr.model.Region
    		.RegionBuilder()
    		.withRegionName(applicationDto.getRegion())
    		.build();
    	
    	ApplicationNecessity applicationNecessity = null;
    	String appNecessity = applicationDto.getApplicationNecessity();
    	if (appNecessity != null) {
    		applicationNecessity = ApplicationNecessity.valueOf(appNecessity);
    	}
    	
    	ReleaseState releaseState = null;
    	String appReleaseState = applicationDto.getReleaseState();
    	if (appReleaseState != null) {
    		releaseState = ReleaseState.valueOf(appReleaseState);
    	}
        
    	com.djt.cvpp.ota.vadr.model.Application applicationEntity = new com.djt.cvpp.ota.vadr.model.Application
            .ApplicationBuilder()
            .withAppId(applicationDto.getAppId())
            .withAppName(applicationDto.getAppName())
            .withAppVersion(applicationDto.getAppVersion())
            .withRegion(regionEntity)
            .withDescription(applicationDto.getDescription())
            .withOwnerGroup(applicationDto.getOwnerGroup())
            .withOwnerKeyContact(applicationDto.getOwnerKeyContact())
            .withRequiresSubscription(applicationDto.getRequiresSubscription())
            .withApplicationNecessity(applicationNecessity)
            .withReleaseState(releaseState)
            .withBinaryMetadatas(binaryMetadatas)
            .withApplicationDependencies(dependentApplications)
            .build();
    	
    	return applicationEntity;
    }
    
    private com.djt.cvpp.ota.vadr.model.BinaryMetadata mapBinaryMetadataDtoToBinaryMetadataEntity(com.djt.cvpp.ota.vadr.mapper.dto.BinaryMetadata binaryMetadataDto) {
    	
        Set<com.djt.cvpp.ota.vadr.model.ProgramLogicalBlock> programLogicalBlocks = new TreeSet<>();
        Iterator<com.djt.cvpp.ota.vadr.mapper.dto.ProgramLogicalBlock> programLogicalBlockIterator = binaryMetadataDto.getProgramLogicalBlocks().iterator();
        while (programLogicalBlockIterator.hasNext()) {
        	
        	com.djt.cvpp.ota.vadr.mapper.dto.ProgramLogicalBlock programLogicalBlockDto = programLogicalBlockIterator.next();
        	com.djt.cvpp.ota.vadr.model.ProgramLogicalBlock programLogicalBlock = new com.djt.cvpp.ota.vadr.model.ProgramLogicalBlock
                .ProgramLogicalBlockBuilder()
                .withStartAddress(programLogicalBlockDto.getStartAddress())
                .withLength(programLogicalBlockDto.getLength())
                .build();
            programLogicalBlocks.add(programLogicalBlock);
        }
        
        Set<com.djt.cvpp.ota.vadr.model.EraseLogicalBlock> eraseLogicalBlocks = new TreeSet<>();
        Iterator<com.djt.cvpp.ota.vadr.mapper.dto.EraseLogicalBlock> eraseLogicalBlockIterator = binaryMetadataDto.getEraseLogicalBlocks().iterator();
        while (eraseLogicalBlockIterator.hasNext()) {
        	
        	com.djt.cvpp.ota.vadr.mapper.dto.EraseLogicalBlock eraseLogicalBlockDto = eraseLogicalBlockIterator.next();
        	com.djt.cvpp.ota.vadr.model.EraseLogicalBlock eraseLogicalBlock = new com.djt.cvpp.ota.vadr.model.EraseLogicalBlock
                .EraseLogicalBlockBuilder()
                .withStartAddress(eraseLogicalBlockDto.getStartAddress())
                .withLength(eraseLogicalBlockDto.getLength())
                .build();
            eraseLogicalBlocks.add(eraseLogicalBlock);
        }

        Set<com.djt.cvpp.ota.vadr.model.VerificationLogicalBlock> verificationLogicalBlocks = new TreeSet<>();
        Iterator<com.djt.cvpp.ota.vadr.mapper.dto.VerificationLogicalBlock> verificationLogicalBlockIterator = binaryMetadataDto.getVerificationLogicalBlocks().iterator();
        while (verificationLogicalBlockIterator.hasNext()) {
        	
        	com.djt.cvpp.ota.vadr.mapper.dto.VerificationLogicalBlock verificationLogicalBlockDto = verificationLogicalBlockIterator.next();
        	com.djt.cvpp.ota.vadr.model.VerificationLogicalBlock verificationLogicalBlock = new com.djt.cvpp.ota.vadr.model.VerificationLogicalBlock
                .VerificationLogicalBlockBuilder()
                .withStartAddress(verificationLogicalBlockDto.getStartAddress())
                .withLength(verificationLogicalBlockDto.getLength())
                .withRootHash(verificationLogicalBlockDto.getRootHash())
                .build();
            verificationLogicalBlocks.add(verificationLogicalBlock);
        }
        
        SoftwareCategory softwareCategory = null;
        String swCategory = binaryMetadataDto.getSwCategory();
        if (swCategory != null) {
        	softwareCategory = SoftwareCategory.valueOf(swCategory);
        }
        
        SoftwareClassification softwareClassification = null;
        String swClassification = binaryMetadataDto.getSwClassification();
        if (swClassification != null) {
        	softwareClassification = SoftwareClassification.valueOf(swClassification);
        }
        
        FileFormat fileFormat = null;
        String binaryFileFormat = binaryMetadataDto.getFileFormat();
        if (binaryFileFormat != null) {
        	fileFormat = FileFormat.valueOf(binaryFileFormat);
        }
    	
        com.djt.cvpp.ota.vadr.model.BinaryMetadata binaryMetadataEntity = new com.djt.cvpp.ota.vadr.model.BinaryMetadata
            .BinaryMetadataBuilder()
            .withPersistentIdentity(Long.valueOf(binaryMetadataDto.getFileId()))
            .withEcuAcronymName(binaryMetadataDto.getEcuAcronymName())
            .withEcuLogicalAddress(binaryMetadataDto.getEcuLogicalAddress())
            .withPublicKeyHash(binaryMetadataDto.getPublicKeyHash())
            .withSwPartNumber(binaryMetadataDto.getSwPartNumber())
            .withLegacySwPartType(binaryMetadataDto.getLegacySwPartType())
            .withSwCategory(softwareCategory)
            .withSwClassification(softwareClassification)
            .withDidAddress(binaryMetadataDto.getDidAddress())
            .withIsOvtp(binaryMetadataDto.getIsOvtp())
            .withProgramLogicalBlocks(programLogicalBlocks)
            .withEraseLogicalBlocks(eraseLogicalBlocks)
            .withVerificationLogicalBlocks(verificationLogicalBlocks)
            .withAutonomicUrl(binaryMetadataDto.getAutonomicUrl())
            .withFileName(binaryMetadataDto.getFileName())
            .withFileFormat(fileFormat)
            .withDescription(binaryMetadataDto.getDescription())
            .withUnzippedFileSize(binaryMetadataDto.getUnzippedFileSize())
            .withZippedFileSize(binaryMetadataDto.getZippedFileSize())
            .withVbfVersion(binaryMetadataDto.getVbfVersion())
            .withDataFormatIdentifier(binaryMetadataDto.getDataFormatIdentifier())
            .withFrameFormat(binaryMetadataDto.getFrameFormat())
            .withCallStartAddress(binaryMetadataDto.getCallStartAddress())
            .withFileChecksum(binaryMetadataDto.getFileChecksum())
            .withFileFingerprint(binaryMetadataDto.getFileFingerprint())
            .withProgramProtected(binaryMetadataDto.getProgramProtected())
            .withFirmwareInstallType(binaryMetadataDto.getFirmwareInstallType())
            .withCreatedBy("") // TODO: TDM: Need to add createdBy
            //.withCreatedTimestamp(binaryMetadataDto.getCreatedTimestamp()) // TODO: TDM: Need to deal with this
            .withLastModifiedBy("") // TODO: TDM: Need to add lastModifiedBy
            //.withLastModifiedTimestamp(binaryMetadataDto.getLastModifiedTimestamp()) // TODO: TDM: Need to deal with this
            .build();

        return binaryMetadataEntity;
    }    
}
