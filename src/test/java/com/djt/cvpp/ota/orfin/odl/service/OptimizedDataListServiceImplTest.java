/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.odl.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.orfin.odl.event.impl.MockOrfinOdlEventPublisher;
import com.djt.cvpp.ota.orfin.odl.event.impl.MockOrfinOdlEventSubscriberImpl;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.odl.model.enums.SpecificationCategoryType;
import com.djt.cvpp.ota.orfin.odl.repository.OptimizedDataListRepository;
import com.djt.cvpp.ota.orfin.odl.repository.impl.MockOptimizedDataListRepositoryImpl;
import com.djt.cvpp.ota.orfin.odl.service.impl.OptimizedDataListServiceImpl;
import com.djt.cvpp.ota.orfin.program.mapper.ProgramModelYearDtoMapper;
import com.djt.cvpp.ota.orfin.program.model.ModelYear;
import com.djt.cvpp.ota.orfin.program.model.Program;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

public class OptimizedDataListServiceImplTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OptimizedDataListServiceImplTest.class);

	
	// Aggregate root for service under test
	private Odl odl;
	
	// Service under test
	private MockOrfinOdlEventSubscriberImpl orfinOdlEventSubscriber;
	private MockOrfinOdlEventPublisher orfinOdlEventPublisher;
	private OptimizedDataListRepository optimizedDataListRepository;
	private OptimizedDataListService optimizedDataListService;
	
		
	@BeforeClass
	public static void beforeClass() {

		String env = System.getProperty(TimeKeeper.ENV);
		if (env == null) {
			System.setProperty(TimeKeeper.ENV, TimeKeeper.TEST);
		}

		AbstractEntity.setTimeKeeper(new TestTimeKeeperImpl(TestTimeKeeperImpl.DEFAULT_TEST_EPOCH_MILLIS_01_01_2020));
	}
	
	@Before
	public void before() {

		this.orfinOdlEventSubscriber = MockOrfinOdlEventSubscriberImpl.getInstance();
		this.orfinOdlEventSubscriber.clearEvents();
		this.orfinOdlEventPublisher = MockOrfinOdlEventPublisher.getInstance();
		this.orfinOdlEventPublisher.clearSubscribers();
		this.optimizedDataListRepository = MockOptimizedDataListRepositoryImpl.getInstance();
		this.optimizedDataListRepository.reset();
		this.optimizedDataListService = new OptimizedDataListServiceImpl(
			this.optimizedDataListRepository,
			this.orfinOdlEventPublisher);
	}
	
	@Test
	public void createOdl() throws Exception {

		// STEP 1: ARRANGE
		String odlName = "odl_name";
		
		// STEP 2: ACT
		this.odl = this.optimizedDataListService.createOdl(odlName);
		

		// STEP 3: ASSERT
		Assert.assertNotNull("odl is null", this.odl);
		Assert.assertEquals("odlName is incorrect", odlName, odl.getOdlName());
		
		String json = this.optimizedDataListService.getJsonConverter().marshallFromEntityToJson(this.odl);
		LOGGER.info(json);
	}
	
	@Test
	public void createNetwork()
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		/*
		String odlName = "odl_name";
		String networkName = "network_name";
		String protocol = "protocol_name";
		String dataRate = "500";
		String dclName = "dcl_name";
		String networkPins = "1,3";
		*/		
	}

	@Test
	public void  createNode()
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {

		/*
		String odlName,
		String networkName,
		String nodeAcronym,
		String nodeAddress,
		String fesn,
		String publicKeyHash,
		String gatewayType,
		Boolean hasConditionBasedOnDtc,
		Boolean isOvtp,
		String ovtpDestinationAddress,
		String specificationCategoryType,
		Integer activationTime,
		Integer vehicleInhibitActivationTime 
		 */
		
	}

	@Test
	public void addIgnoredDidsToNode()
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
	
		/*
		String odlName,
		String networkName,
		String nodeAcronym,
		String nodeAddress,
		List<String> ignoredDids
		*/
	}

	@Test
	public void createDid()
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		/*
		String odlName,
		String networkName,
		String nodeAcronym,
		String nodeAddress,
		String didName,
		String format,
		Integer length,
		String description,
		String value,
		Boolean vinSpecificDidFlag,
		Boolean directConfigurationDidFlag,
		Boolean privateNetworkDidFlag,
		Boolean hasConditionBasedOnDtc 
		 */
		
	}

	@Test
	public void addEcgSignalToOdl()
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		/*
		String odlName,
		String ecgSignalName 
		 */
		
		
	}

	@Test
	public void addCustomOdlToOdl()
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		/*
		String odlName,
		String customOdlName,
		List<String> customOdlNodeList 
		 */
		
	}
	
	@Test
	public void getAllOdls() {
		
	}

	@Test
	public void getOdlByName()
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		/*
		 * String odlName
		 */
		
	}

	@Test
	public void getOdlByProgramCodeAndModelYear()
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		/*
		String programCode,
        Integer modelYear 
		 */
		
	}
	
	@Test
	public void renameOdl()
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException {
		
		/*
		 * String oldOdlName, 
		 * String newOdlName
		 */
		
	}
	
	@Test
	public void updateOdl()
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		/*
		 * String odlName
		 */
		
	}

	@Test
	public void deleteOdl() 
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		/*
		 * String odlName
		 */
		
	}
	
	@Test
	public void renderVehicleJson() throws EntityDoesNotExistException, ValidationException, EntityAlreadyExistsException {
		
		// STEP 1: ARRANGE
		String programCode = "CD391N";
		Integer modelYear = Integer.valueOf(2018);
		
		Program parentProgram = new Program.ProgramBuilder().withProgramCode(programCode).build();
		ModelYear parentModelYear = new ModelYear.ModelYearBuilder().withModelYearValue(modelYear).build();
		
		ProgramModelYear programModelYearEntity = new ProgramModelYear
			.ProgramModelYearBuilder()
			.withParentProgram(parentProgram)
			.withParentModelYear(parentModelYear)
			.build();
				 
		String odlName = "CD391N_2018_ODL_Full";
		Odl odl = this.optimizedDataListService.createOdl(odlName);
		programModelYearEntity.setOdl(odl);
		odl.addProgramModelYear(programModelYearEntity);
		
		ProgramModelYearDtoMapper programModelYearDtoMapper = new ProgramModelYearDtoMapper();
		com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear programModelYearDto = programModelYearDtoMapper.mapEntityToDto(programModelYearEntity);
		
		String networkName = "HS1";
		String protocol = "CAN";
		String dataRate = "500";
		String dclName = "SAE J1962";
		String networkPins = "6,14";		
		this.optimizedDataListService.createNetwork(odlName, networkName, protocol, dataRate, dclName, networkPins);

		// SYNC
		String nodeAcronym = "APIM";
		String nodeAddress = "7D0";
		String gatewayNodeId = null;
		String gatewayType = "NONE";
		Boolean hasConditionBasedOnDtc = Boolean.FALSE;
		Boolean isOvtp = Boolean.FALSE;
		String ovtpDestinationAddress = "https://autonomic.ai/bytestream/ovtp_destination_address";
		String specificationCategoryType = SpecificationCategoryType.PART2_SPEC.toString();
		Integer diagnosticSpecificationResponse = Integer.valueOf(3);
		Integer activationTime = Integer.valueOf(15);
		this.optimizedDataListService.createNode(odlName, networkName, nodeAcronym, nodeAddress, gatewayNodeId, gatewayType, hasConditionBasedOnDtc, isOvtp, ovtpDestinationAddress, specificationCategoryType, diagnosticSpecificationResponse, activationTime);
		
		List<String> ignoredDids = new ArrayList<>();
		ignoredDids.add("F10A");
		ignoredDids.add("F16B");
		this.optimizedDataListService.addIgnoredDidsToNode(odlName, networkName, nodeAcronym, nodeAddress, ignoredDids);
		
		String didName = "8033";
		String description = "Embedded Consumer Operating System Part Number";
		Boolean vinSpecificDidFlag = Boolean.FALSE;
		Boolean directConfigurationDidFlag = Boolean.FALSE;
		Boolean privateNetworkDidFlag = Boolean.FALSE;
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "8060";
		description = "Embedded Consumer Applications Part Numbers 1";
		vinSpecificDidFlag = Boolean.TRUE;
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "8061";
		description = "Embedded Consumer Applications Part Numbers 2";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F111";
		description = "ECU Core Assembly Number";
		vinSpecificDidFlag = Boolean.FALSE;
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);
		
		// TCU
		networkName = "HS2";
		networkPins = "3,11";		
		this.optimizedDataListService.createNetwork(odlName, networkName, protocol, dataRate, dclName, networkPins);
		
		nodeAcronym = "TCU";
		nodeAddress = "754";
		gatewayNodeId = "754";
		gatewayType = "Transparent";
		hasConditionBasedOnDtc = Boolean.FALSE;
		isOvtp = Boolean.FALSE;
		ovtpDestinationAddress = "https://autonomic.ai/bytestream/ovtp_destination_address";
		specificationCategoryType = SpecificationCategoryType.PART2_SPEC.toString();
		diagnosticSpecificationResponse = Integer.valueOf(3);
		this.optimizedDataListService.createNode(odlName, networkName, nodeAcronym, nodeAddress, gatewayNodeId, gatewayType, hasConditionBasedOnDtc, isOvtp, ovtpDestinationAddress, specificationCategoryType, diagnosticSpecificationResponse, activationTime);
		
		ignoredDids = new ArrayList<>();
		ignoredDids.add("F120");
		this.optimizedDataListService.addIgnoredDidsToNode(odlName, networkName, nodeAcronym, nodeAddress, ignoredDids);
		
		vinSpecificDidFlag = Boolean.FALSE;
		directConfigurationDidFlag = Boolean.FALSE;
		privateNetworkDidFlag = Boolean.FALSE;
		didName = "F10A";
		description = "ECU Cal-Config Part Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F188";
		description = "Vehicle Manufacturer ECU Software Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F111";
		description = "ECU Core Assembly Number";
		vinSpecificDidFlag = Boolean.FALSE;
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);
		
		// ECG
		networkName = "HS1";
		nodeAcronym = "ECG";
		nodeAddress = "716";
		gatewayNodeId = null;
		gatewayType = "NONE";
		hasConditionBasedOnDtc = Boolean.FALSE;
		isOvtp = Boolean.FALSE;
		ovtpDestinationAddress = "https://autonomic.ai/bytestream/ovtp_destination_address";
		specificationCategoryType = SpecificationCategoryType.PART2_SPEC.toString();
		diagnosticSpecificationResponse = Integer.valueOf(3);
		this.optimizedDataListService.createNode(odlName, networkName, nodeAcronym, nodeAddress, gatewayNodeId, gatewayType, hasConditionBasedOnDtc, isOvtp, ovtpDestinationAddress, specificationCategoryType, diagnosticSpecificationResponse, activationTime);
		
		ignoredDids = new ArrayList<>();
		ignoredDids.add("F10A");
		ignoredDids.add("F16B");
		this.optimizedDataListService.addIgnoredDidsToNode(odlName, networkName, nodeAcronym, nodeAddress, ignoredDids);
		
		vinSpecificDidFlag = Boolean.FALSE;
		directConfigurationDidFlag = Boolean.FALSE;
		privateNetworkDidFlag = Boolean.FALSE;
		didName = "8033";
		description = "Embedded Consumer Operating System Part Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "8060";
		description = "Embedded Consumer Applications Part Numbers 1";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "8061";
		description = "Embedded Consumer Applications Part Numbers 2";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F111";
		description = "ECU Core Assembly Number";
		vinSpecificDidFlag = Boolean.FALSE;
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		// PCM
		networkName = "HS1";
		nodeAcronym = "PCM";
		nodeAddress = "7E0";
		gatewayNodeId = null;
		gatewayType = "NONE";
		hasConditionBasedOnDtc = Boolean.FALSE;
		isOvtp = Boolean.FALSE;
		ovtpDestinationAddress = "https://autonomic.ai/bytestream/ovtp_destination_address";
		specificationCategoryType = SpecificationCategoryType.PART2_SPEC.toString();
		diagnosticSpecificationResponse = Integer.valueOf(3);
		this.optimizedDataListService.createNode(odlName, networkName, nodeAcronym, nodeAddress, gatewayNodeId, gatewayType, hasConditionBasedOnDtc, isOvtp, ovtpDestinationAddress, specificationCategoryType, diagnosticSpecificationResponse, activationTime);
		
		ignoredDids = new ArrayList<>();
		this.optimizedDataListService.addIgnoredDidsToNode(odlName, networkName, nodeAcronym, nodeAddress, ignoredDids);
		
		vinSpecificDidFlag = Boolean.FALSE;
		directConfigurationDidFlag = Boolean.FALSE;
		privateNetworkDidFlag = Boolean.FALSE;
		vinSpecificDidFlag = Boolean.FALSE;
		directConfigurationDidFlag = Boolean.FALSE;
		privateNetworkDidFlag = Boolean.FALSE;

		didName = "F188";
		description = "Vehicle Manufacturer ECU Software Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F111";
		description = "ECU Core Assembly Number";
		vinSpecificDidFlag = Boolean.FALSE;
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		// BCM
		networkName = "HS1";
		nodeAcronym = "PCM";
		nodeAddress = "726";
		gatewayNodeId = null;
		gatewayType = "NONE";
		hasConditionBasedOnDtc = Boolean.FALSE;
		isOvtp = Boolean.FALSE;
		ovtpDestinationAddress = "https://autonomic.ai/bytestream/ovtp_destination_address";
		specificationCategoryType = SpecificationCategoryType.PART2_SPEC.toString();
		diagnosticSpecificationResponse = Integer.valueOf(3);
		this.optimizedDataListService.createNode(odlName, networkName, nodeAcronym, nodeAddress, gatewayNodeId, gatewayType, hasConditionBasedOnDtc, isOvtp, ovtpDestinationAddress, specificationCategoryType, diagnosticSpecificationResponse, activationTime);
		
		ignoredDids = new ArrayList<>();
		this.optimizedDataListService.addIgnoredDidsToNode(odlName, networkName, nodeAcronym, nodeAddress, ignoredDids);
		
		vinSpecificDidFlag = Boolean.FALSE;
		directConfigurationDidFlag = Boolean.FALSE;
		privateNetworkDidFlag = Boolean.FALSE;
		vinSpecificDidFlag = Boolean.FALSE;
		directConfigurationDidFlag = Boolean.FALSE;
		privateNetworkDidFlag = Boolean.FALSE;

		didName = "F10A";
		description = "ECU Cal-Config Part Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F16B";
		description = "ECU Cal-Config #2 Part Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F16C";
		description = "ECU Cal-Config #3 Part Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F16D";
		description = "ECU Cal-Config #4 Part Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F16E";
		description = "ECU Cal-Config #5 Part Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F17D";
		description = "ECU Cal-Config #6 Part Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);
				
		didName = "F188";
		description = "Vehicle Manufacturer ECU Software Number";
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);

		didName = "F111";
		description = "ECU Core Assembly Number";
		vinSpecificDidFlag = Boolean.FALSE;
		this.optimizedDataListService.createDid(odlName, networkName, nodeAcronym, nodeAddress, didName, description, vinSpecificDidFlag, directConfigurationDidFlag, privateNetworkDidFlag);
		
		// Custom ODL (TDK nodes only)
		String customOdlName = "SYNC_TCU_ECG";
		List<String> customOdlNodeList = new ArrayList<>();
		customOdlNodeList.add("APIM_7D0");
		customOdlNodeList.add("TCU_754");
		customOdlNodeList.add("ECG_716");
		this.optimizedDataListService.addCustomOdlToOdl(odlName, customOdlName, customOdlNodeList);
		
		// ECG Signals
		String ecgSignalName = "OfbChrgGoTHr_T_Rq";
		this.optimizedDataListService.addEcgSignalToOdl(odlName, ecgSignalName);
		ecgSignalName = "ChrgGoTAllOn_B_Stat";
		this.optimizedDataListService.addEcgSignalToOdl(odlName, ecgSignalName);
		
		 
		// STEP 2: ACT
		String renderedOdlJson = this.optimizedDataListService.renderFullOdlWithEcgSignalsForProgram(programModelYearDto);
		System.out.println("Full ODL: [" + odlName + "] with ECG Signals - Vehicle JSON:");
		System.out.println(renderedOdlJson);

		Boolean includeEcgSignals = Boolean.TRUE;
		renderedOdlJson = this.optimizedDataListService.renderOdlForProgram(programModelYearDto, customOdlName, includeEcgSignals);
		System.out.println("Custom ODL: [" + customOdlName + "] with ECG Signals - Vehicle JSON:");
		System.out.println(renderedOdlJson);

		includeEcgSignals = Boolean.FALSE;
		renderedOdlJson = this.optimizedDataListService.renderOdlForProgram(programModelYearDto, customOdlName, includeEcgSignals);
		System.out.println("Custom ODL: [" + customOdlName + "] *without* ECG Signals - Vehicle JSON:");
		System.out.println(renderedOdlJson);
		
		
		// STEP 3: ASSERT
		Assert.assertNotNull("renderedOdlJson is null", renderedOdlJson);
	}

	@Test
	public void renderOdlForProgram()
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		/*
		ProgramModelYear programModelYear,
		String customOdlName,
		Boolean includeEcgSignals
		 */
	}
}
