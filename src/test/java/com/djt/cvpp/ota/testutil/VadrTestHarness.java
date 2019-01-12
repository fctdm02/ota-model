package com.djt.cvpp.ota.testutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.vadr.model.Application;
import com.djt.cvpp.ota.vadr.model.BinaryMetadata;
import com.djt.cvpp.ota.vadr.model.Domain;
import com.djt.cvpp.ota.vadr.model.DomainInstance;
import com.djt.cvpp.ota.vadr.model.EcuHardware;
import com.djt.cvpp.ota.vadr.model.EcuNode;
import com.djt.cvpp.ota.vadr.model.EcuSoftware;
import com.djt.cvpp.ota.vadr.model.EraseLogicalBlock;
import com.djt.cvpp.ota.vadr.model.ProgramLogicalBlock;
import com.djt.cvpp.ota.vadr.model.Region;
import com.djt.cvpp.ota.vadr.model.VerificationLogicalBlock;
import com.djt.cvpp.ota.vadr.model.enums.ApplicationNecessity;
import com.djt.cvpp.ota.vadr.model.enums.FileFormat;
import com.djt.cvpp.ota.vadr.model.enums.ProductionState;
import com.djt.cvpp.ota.vadr.model.enums.ReleaseState;
import com.djt.cvpp.ota.vadr.model.enums.SoftwareCategory;
import com.djt.cvpp.ota.vadr.model.enums.SoftwareClassification;

public class VadrTestHarness {

    public Domain buildDomain(
    	int numberOfDomainInstancesToBuild,
		int numberOfEcuHardwaresPerDomainInstanceToBuild,
		int numberOfEcuSoftwaresPerDomainInstanceToBuild,
		int numberOfApplicationsPerDomainInstanceToBuild) 
	throws 
		JsonProcessingException,
		ValidationException,
		EntityAlreadyExistsException {

        Region northAmericaRegion = new Region
            .RegionBuilder()
            .withRegionName("NA")
            .build();


        int domainSequence = 100;
        int domainInstanceSequence = 200;
        int ecuHardwarePartNumberSequence = 300;
        int ecuSoftwarePartNumberSequence = 400;
        int applicationNameSequence = 500;
        int ecuAcronymSequence = 600;
        int ecuLogicalAddressSequence = 700;
        int binaryMetadataSequence = 800;
        
        domainSequence = domainSequence + 1;
        ecuAcronymSequence = ecuAcronymSequence + 1;
        ecuLogicalAddressSequence = ecuLogicalAddressSequence + 1;
        
        String domainId = Integer.toString(domainSequence);
        String ecuAcronymName = "ecuAcronymName" + Integer.toString(ecuAcronymSequence);
        String ecuLogicalAddress = "ecuLogicalAddress" + Integer.toString(ecuLogicalAddressSequence);

        Set<EcuNode> ecuNodes = new TreeSet<>();
        EcuNode ecuNode = new EcuNode
            .EcuNodeBuilder()
            .withEcuAcronymName(ecuAcronymName)
            .withEcuLogicalAddress(ecuLogicalAddress)
            .build();
        ecuNodes.add(ecuNode);

        
        Domain domain = new Domain
            .DomainBuilder()
            .withDomainName("domainName" + domainId)
            .withDomainDescription("domainDescription" + domainId)
            .withEcuNodes(ecuNodes)
            .build();


        for (int i=1; i <= numberOfDomainInstancesToBuild; i++) {

        	String domainInstanceId = Integer.toString(domainInstanceSequence++);
            
        	
            Set<EcuHardware> ecuHardwares = new TreeSet<>();
            for (int j=1; j <= numberOfEcuHardwaresPerDomainInstanceToBuild; j++) {

                String ecuHardwarePartNumberId = Integer.toString(ecuHardwarePartNumberSequence++);
                EcuHardware ecuHardware = new EcuHardware
                    .EcuHardwareBuilder()
                    .withEcuNode(ecuNode)
                    .withEcuHardwarePartNumber(ecuHardwarePartNumberId)
                    .build();        
                ecuHardwares.add(ecuHardware);
            }

                       
            Set<EcuSoftware> ecuSoftwares = new TreeSet<>();
            for (int j=1; j <= numberOfEcuHardwaresPerDomainInstanceToBuild; j++) {
            	
            	String ecuSoftwarePartNumberId = Integer.toString(ecuSoftwarePartNumberSequence++);
                Set<ProgramLogicalBlock> softwareProgramLogicalBlocks = new TreeSet<>();
                ProgramLogicalBlock softwareProgramLogicalBlock = new ProgramLogicalBlock
                    .ProgramLogicalBlockBuilder()
                    .withStartAddress("programStartAddress" + domainInstanceId)
                    .withLength("32")
                    .build();
                softwareProgramLogicalBlocks.add(softwareProgramLogicalBlock);


                Set<EraseLogicalBlock> softwareEraseLogicalBlocks = new TreeSet<>();
                EraseLogicalBlock softwareEraseLogicalBlock = new EraseLogicalBlock
                    .EraseLogicalBlockBuilder()
                    .withStartAddress("eraseStartAddress" + domainInstanceId)
                    .withLength("32")
                    .build();
                softwareEraseLogicalBlocks.add(softwareEraseLogicalBlock);


                Set<VerificationLogicalBlock> softwareVerificationLogicalBlocks = new TreeSet<>();
                VerificationLogicalBlock softwareVerificationLogicalBlock = new VerificationLogicalBlock
                    .VerificationLogicalBlockBuilder()
                    .withStartAddress("verificationStartAddress" + domainInstanceId)
                    .withLength("32")
                    .withRootHash("rootHash" + domainInstanceId)
                    .build();
                softwareVerificationLogicalBlocks.add(softwareVerificationLogicalBlock);


                Long binaryMetadataId = Long.valueOf(binaryMetadataSequence++);
                BinaryMetadata softwareBinaryMetadata = new BinaryMetadata
                    .BinaryMetadataBuilder()
                    .withPersistentIdentity(binaryMetadataId)
                    .withEcuAcronymName(ecuAcronymName)
                    .withEcuLogicalAddress(ecuLogicalAddress)
                    .withPublicKeyHash("publicKeyHash" + domainInstanceId)
                    .withSwPartNumber(ecuSoftwarePartNumberId)
                    .withLegacySwPartType("legacySwPartType" + domainInstanceId)
                    .withSwCategory(SoftwareCategory.BINARY_APPLICATION)
                    .withSwClassification(SoftwareClassification.PRODUCTION)
                    .withDidAddress("didAddress" + domainInstanceId)
                    .withIsOvtp(Boolean.TRUE)
                    .withProgramLogicalBlocks(softwareProgramLogicalBlocks)
                    .withEraseLogicalBlocks(softwareEraseLogicalBlocks)
                    .withVerificationLogicalBlocks(softwareVerificationLogicalBlocks)
                    .withAutonomicUrl("autonomicUrl" + domainInstanceId)
                    .withFileName("fileName" + domainInstanceId)
                    .withFileFormat(FileFormat.APPLICATION_BINARY)
                    .withDescription("description" + domainInstanceId)
                    .withUnzippedFileSize(1000L)
                    .withZippedFileSize(750L)
                    .withVbfVersion("4.0")
                    .withDataFormatIdentifier("dataFormatIdentifier" + domainInstanceId)
                    .withFrameFormat("frameFormat")
                    .withCallStartAddress("callStartAddress")
                    .withFileChecksum("fileChecksum")
                    .withFileFingerprint("fileFingerprint")
                    .withProgramProtected(Boolean.TRUE.booleanValue())
                    .withFirmwareInstallType("firmwareInstallType")
                    .withCreatedBy("createdBy")
                    .withCreatedTimestamp(new Timestamp(AbstractEntity.getTimeKeeper().getCurrentTimeInMillis()))
                    .withLastModifiedBy("lastModifiedBy")
                    .withLastModifiedTimestamp(new Timestamp(AbstractEntity.getTimeKeeper().getCurrentTimeInMillis()))
                    .build();


                
                EcuSoftware ecuSoftware = new EcuSoftware
                    .EcuSoftwareBuilder()
                    .withEcuNode(ecuNode)
                    .withEcuSoftwarePartNumber(ecuSoftwarePartNumberId)
                    .withSoftwareName("softwareName" + ecuSoftwarePartNumberId)
                    .withSoftwareVersionNumber("softwareVersionNumber" + ecuSoftwarePartNumberId)
                    .withCategory("category")
                    .withDescription("description")
                    .withBinaryMetadata(softwareBinaryMetadata)
                    .build();
                ecuSoftwares.add(ecuSoftware);
            }
            

            Set<Application> applications = new TreeSet<>();
            for (int j=1; j <= numberOfEcuHardwaresPerDomainInstanceToBuild; j++) {
            	
            	String appId = Integer.toString(applicationNameSequence++);
                Set<ProgramLogicalBlock> applicationProgramLogicalBlocks = new TreeSet<>();
                ProgramLogicalBlock applicationProgramLogicalBlock = new ProgramLogicalBlock
                    .ProgramLogicalBlockBuilder()
                    .withStartAddress("programStartAddress" + domainInstanceId)
                    .withLength("32")
                    .build();
                applicationProgramLogicalBlocks.add(applicationProgramLogicalBlock);


                Set<EraseLogicalBlock> applicationEraseLogicalBlocks = new TreeSet<>();
                EraseLogicalBlock applicationEraseLogicalBlock = new EraseLogicalBlock
                    .EraseLogicalBlockBuilder()
                    .withStartAddress("eraseStartAddress" + domainInstanceId)
                    .withLength("32")
                    .build();
                applicationEraseLogicalBlocks.add(applicationEraseLogicalBlock);


                Set<VerificationLogicalBlock> applicationVerificationLogicalBlocks = new TreeSet<>();
                VerificationLogicalBlock applicationVerificationLogicalBlock = new VerificationLogicalBlock
                    .VerificationLogicalBlockBuilder()
                    .withStartAddress("verificationStartAddress" + domainInstanceId)
                    .withLength("32")
                    .withRootHash("rootHash" + domainInstanceId)
                    .build();
                applicationVerificationLogicalBlocks.add(applicationVerificationLogicalBlock);

                
                Set<BinaryMetadata> applicationBinaryMetadatas = new TreeSet<>();
                
                Long binaryMetadataId = Long.valueOf(binaryMetadataSequence++);
                BinaryMetadata applicationBinaryMetadata = new BinaryMetadata
                    .BinaryMetadataBuilder()
                    .withPersistentIdentity(binaryMetadataId)
                    .withEcuAcronymName(ecuAcronymName)
                    .withEcuLogicalAddress(ecuLogicalAddress)
                    .withPublicKeyHash("publicKeyHash")
                    .withSwPartNumber("applicationSwPartNumber" + appId)
                    .withLegacySwPartType("legacySwPartType")
                    .withSwCategory(SoftwareCategory.BINARY_APPLICATION)
                    .withSwClassification(SoftwareClassification.PRODUCTION)
                    .withDidAddress("didAddress")
                    .withIsOvtp(Boolean.TRUE)
                    .withProgramLogicalBlocks(applicationProgramLogicalBlocks)
                    .withEraseLogicalBlocks(applicationEraseLogicalBlocks)
                    .withVerificationLogicalBlocks(applicationVerificationLogicalBlocks)
                    .withAutonomicUrl("autonomicUrl" + appId)
                    .withFileName("fileName" + appId)
                    .withFileFormat(FileFormat.APPLICATION_BINARY)
                    .withDescription("description")
                    .withUnzippedFileSize(1000L)
                    .withZippedFileSize(750L)
                    .withVbfVersion("4.0")
                    .withDataFormatIdentifier("dataFormatIdentifier")
                    .withFrameFormat("frameFormat")
                    .withCallStartAddress("callStartAddress")
                    .withFileChecksum("fileChecksum")
                    .withFileFingerprint("fileFingerprint")
                    .withProgramProtected(Boolean.TRUE.booleanValue())
                    .withFirmwareInstallType("firmwareInstallType")
                    .withCreatedBy("createdBy")
                    .withCreatedTimestamp(new Timestamp(AbstractEntity.getTimeKeeper().getCurrentTimeInMillis()))
                    .withLastModifiedBy("lastModifiedBy")
                    .withLastModifiedTimestamp(new Timestamp(AbstractEntity.getTimeKeeper().getCurrentTimeInMillis()))
                    .build();
                applicationBinaryMetadatas.add(applicationBinaryMetadata);


                
                Application application = new Application
                    .ApplicationBuilder()
                    .withAppId("appId" + appId)
                    .withAppName("appName" + appId)
                    .withAppVersion("appVersion" + appId)
                    .withRegion(northAmericaRegion)
                    .withDescription("description")
                    .withOwnerGroup("ownerGroup")
                    .withOwnerKeyContact("ownerKeyContact")
                    .withRequiresSubscription(Boolean.FALSE)
                    .withApplicationNecessity(ApplicationNecessity.OPTIONAL)
                    .withReleaseState(ReleaseState.RELEASED)
                    .withBinaryMetadatas(applicationBinaryMetadatas)
                    .build();
                applications.add(application);
            }
            

            new DomainInstance
                .DomainInstanceBuilder()
                .withParentDomain(domain)
                .withDomainInstanceName("domainInstanceName" + domainInstanceId)
                .withDomainInstanceVersion("domainInstanceVersion" + domainInstanceId)
                .withDomainInstanceDescription("domainInstanceDescription" + domainInstanceId)
                .withReleasedDate(new Timestamp(AbstractEntity.getTimeKeeper().getCurrentTimeInMillis()))
                .withReleaseState(ReleaseState.RELEASED)
                .withProductionState(ProductionState.PRODUCTION)
                .withEcuHardwares(ecuHardwares)
                .withEcuSoftwares(ecuSoftwares)
                .withApplications(applications)
                .build();
        }
        
        domain.assertValid();

        return domain;
    }
    
	public String loadTestData(String filename) {
		
        try (InputStream inputStream = VadrTestHarness.class.getResourceAsStream(filename)) {
        	return this.loadTestData(inputStream);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to load file: [" + filename + "], error: " + ioe.getMessage(), ioe);
        }
	}
	
    public String loadTestData(InputStream inputStream) throws IOException {

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {
            Iterator<String> iterator = reader.lines().iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
            }
        }
        return sb.toString();
    }	    
}
