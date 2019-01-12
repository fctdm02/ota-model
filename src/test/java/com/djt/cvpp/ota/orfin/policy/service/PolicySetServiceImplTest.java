/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.policy.service;

import java.io.IOException;
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
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEventPublisher;
import com.djt.cvpp.ota.orfin.delivery.event.impl.MockOrfinDeliveryRuleSetEventPublisher;
import com.djt.cvpp.ota.orfin.delivery.repository.DeliveryRuleSetRepository;
import com.djt.cvpp.ota.orfin.delivery.repository.impl.MockDeliveryRuleSetRepositoryImpl;
import com.djt.cvpp.ota.orfin.delivery.service.DeliveryRuleSetService;
import com.djt.cvpp.ota.orfin.delivery.service.impl.DeliveryRuleSetServiceImpl;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEventPublisher;
import com.djt.cvpp.ota.orfin.odl.event.impl.MockOrfinOdlEventPublisher;
import com.djt.cvpp.ota.orfin.odl.repository.OptimizedDataListRepository;
import com.djt.cvpp.ota.orfin.odl.repository.impl.MockOptimizedDataListRepositoryImpl;
import com.djt.cvpp.ota.orfin.odl.service.OptimizedDataListService;
import com.djt.cvpp.ota.orfin.odl.service.impl.OptimizedDataListServiceImpl;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEvent;
import com.djt.cvpp.ota.orfin.policy.event.impl.MockOrfinPolicySetEventPublisher;
import com.djt.cvpp.ota.orfin.policy.event.impl.MockOrfinPolicySetEventSubscriberImpl;
import com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.orfin.policy.model.VehiclePolicy;
import com.djt.cvpp.ota.orfin.policy.model.enums.OtaFunction;
import com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType;
import com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride;
import com.djt.cvpp.ota.orfin.policy.model.region.Region;
import com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue;
import com.djt.cvpp.ota.orfin.policy.repository.PolicySetRepository;
import com.djt.cvpp.ota.orfin.policy.repository.impl.ClasspathPolicySetRepositoryImpl;
import com.djt.cvpp.ota.orfin.policy.repository.impl.MockPolicySetRepositoryImpl;
import com.djt.cvpp.ota.orfin.policy.service.impl.PolicySetServiceImpl;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;
import com.djt.cvpp.ota.orfin.program.repository.ProgramModelYearRepository;
import com.djt.cvpp.ota.orfin.program.repository.impl.MockProgramModelYearRepositoryImpl;
import com.djt.cvpp.ota.orfin.program.service.ProgramModelYearService;
import com.djt.cvpp.ota.orfin.program.service.impl.ProgramModelYearServiceImpl;
import com.djt.cvpp.ota.orfin.vadrevent.repository.OrfinVadrReleaseEventRepository;
import com.djt.cvpp.ota.orfin.vadrevent.repository.impl.MockOrfinVadrReleaseEventRepositoryImpl;
import com.djt.cvpp.ota.orfin.vadrevent.service.OrfinVadrReleaseEventService;
import com.djt.cvpp.ota.orfin.vadrevent.service.impl.OrfinVadrReleaseEventServiceImpl;

public class PolicySetServiceImplTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PolicySetServiceImplTest.class);

	private OrfinVadrReleaseEventRepository orfinVadrReleaseEventRepository;
	private OrfinVadrReleaseEventService orfinVadrReleaseEventService;
	
	private OrfinDeliveryRuleSetEventPublisher orfinDeliveryRuleSetEventPublisher;
	private DeliveryRuleSetRepository deliveryRuleSetRepository;
	private DeliveryRuleSetService deliveryRuleSetService;

	private OrfinOdlEventPublisher orfinOdlEventPublisher;
	private OptimizedDataListRepository optimizedDataListRepository;
	private OptimizedDataListService optimizedDataListService;

	private PolicySetRepository policySetRepository;
	private MockOrfinPolicySetEventPublisher orfinPolicySetEventPublisher;
	private MockOrfinPolicySetEventSubscriberImpl orfinPolicySetEventSubscriber;
	
	private ProgramModelYearRepository programModelYearRepository;
	private ProgramModelYearService programModelYearService;
	
	// Aggregate root for service under test
	private PolicySet policySet;
	
	// Service under test
	private PolicySetService policySetService;
	
		
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

		this.orfinVadrReleaseEventRepository = new MockOrfinVadrReleaseEventRepositoryImpl();
		this.orfinVadrReleaseEventService = new OrfinVadrReleaseEventServiceImpl(this.orfinVadrReleaseEventRepository);
		
		this.orfinDeliveryRuleSetEventPublisher = MockOrfinDeliveryRuleSetEventPublisher.getInstance();
		this.deliveryRuleSetRepository = MockDeliveryRuleSetRepositoryImpl.getInstance();
		this.deliveryRuleSetRepository.reset();
		this.deliveryRuleSetService = new DeliveryRuleSetServiceImpl(
			this.deliveryRuleSetRepository,
			this.orfinVadrReleaseEventService,
			this.orfinDeliveryRuleSetEventPublisher);

		
		this.orfinOdlEventPublisher = MockOrfinOdlEventPublisher.getInstance();
		this.optimizedDataListRepository = MockOptimizedDataListRepositoryImpl.getInstance();
		this.optimizedDataListRepository.reset();
		this.optimizedDataListService = new OptimizedDataListServiceImpl(
			this.optimizedDataListRepository,
			this.orfinOdlEventPublisher);
		
		
		this.orfinPolicySetEventSubscriber = MockOrfinPolicySetEventSubscriberImpl.getInstance();
		this.orfinPolicySetEventSubscriber.clearEvents();
		this.orfinPolicySetEventPublisher = MockOrfinPolicySetEventPublisher.getInstance();
		this.orfinPolicySetEventPublisher.clearSubscribers();		
		this.policySetRepository = new MockPolicySetRepositoryImpl();
		this.policySetService = new PolicySetServiceImpl();
		this.policySetService.setPolicySetRepository(this.policySetRepository);
		this.policySetService.setOrfinPolicySetEventPublisher(this.orfinPolicySetEventPublisher);
		this.policySetService = new PolicySetServiceImpl(
			this.policySetRepository,
			this.orfinPolicySetEventPublisher);
		this.policySetService.subscribe(this.orfinPolicySetEventSubscriber);
		
		
		this.programModelYearRepository = MockProgramModelYearRepositoryImpl.getInstance();
		this.programModelYearRepository.reset();
		this.programModelYearService = new ProgramModelYearServiceImpl(
			this.programModelYearRepository,
			this.deliveryRuleSetService,
			this.optimizedDataListService,
			this.policySetService);
	}
	
	@Test
	public void subscribe() throws Exception {

		// STEP 1: ARRANGE
		String policySetName = "test_policy_set";
		this.policySetService.createPolicySet(policySetName);

		
		// STEP 2: ACT
		this.policySetService.subscribe(this.orfinPolicySetEventSubscriber);
		
		

		// STEP 3: ASSERT
	}

	@Test
	public void unsubscribe() throws Exception {

		// STEP 1: ARRANGE
		String policySetName = "test_policy_set";
		this.policySetService.createPolicySet(policySetName);

		
		// STEP 2: ACT
		this.policySetService.unsubscribe(this.orfinPolicySetEventSubscriber);
		

		// STEP 3: ASSERT
	}

	@Test
	public void publishOrfinPolicySetEvent() throws ValidationException, EntityAlreadyExistsException {

		// STEP 1: ARRANGE
		String owner = "tmyers28";
		String policySetName = "test_policy_set";
		this.policySetService.createPolicySet(policySetName);
		
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf("2017");
		String regionCode = "us";
		this.programModelYearService.createProgramModelYear(programCode, modelYear);

		
		// STEP 2: ACT
		this.policySetService.publishOrfinPolicySetEvent(
			owner, 
			programCode, 
			modelYear,
			regionCode,
			policySetName);
		

		// STEP 3: ASSERT
		List<OrfinPolicySetEvent> events = this.orfinPolicySetEventSubscriber.getOrfinPolicySetEvents();
		Assert.assertNotNull("events is null", events);
		Assert.assertEquals("events size is incorrect", "1", Integer.toString(events.size()));
	}
	
	@Test
	public void createGlobalPolicySet() throws Exception {

		// STEP 1: ARRANGE

		
		// STEP 2: ACT
		this.policySet = this.policySetService.createPolicySet(PolicySet.GLOBAL_POLICY_SET_NAME);
		

		// STEP 3: ASSERT
		Assert.assertNotNull("policySet is null", this.policySet);
		Assert.assertEquals("policySetName is incorrect", PolicySet.GLOBAL_POLICY_SET_NAME, policySet.getPolicySetName());
		String json = this.policySetService.getJsonConverter().marshallFromEntityToJson(this.policySet);
		Assert.assertNotNull("json is null", json);
		//LOGGER.info(json);
	}

	@Test(expected=EntityAlreadyExistsException.class)
	public void createGlobalPolicySet_alreadyExists() throws Exception {

		// STEP 1: ARRANGE

		
		// STEP 2: ACT
		this.policySetService.createPolicySet(PolicySet.GLOBAL_POLICY_SET_NAME);
		this.policySetService.createPolicySet(PolicySet.GLOBAL_POLICY_SET_NAME);
		

		// STEP 3: ASSERT
	}
	
	@Test
	public void createPolicySet() throws Exception {

		// STEP 1: ARRANGE
		String policySetName = "test_policy_set";

		
		// STEP 2: ACT
		this.policySet = this.policySetService.createPolicySet(policySetName);
		

		// STEP 3: ASSERT
		Assert.assertNotNull("policySet is null", this.policySet);
		Assert.assertEquals("policySetName is incorrect", policySetName, policySet.getPolicySetName());
		String json = this.policySetService.getJsonConverter().marshallFromEntityToJson(this.policySet);
		Assert.assertNotNull(json);
		//LOGGER.info(json);
	}

	@Test(expected=EntityAlreadyExistsException.class)
	public void createPolicySet_alreadyExists() throws Exception {

		// STEP 1: ARRANGE
		String policySetName = "test_policy_set";
		this.policySetService.createPolicySet(policySetName);

		
		// STEP 2: ACT
		this.policySetService.createPolicySet(policySetName);
		

		// STEP 3: ASSERT
	}

	@Test
	public void getAllPolicySets() throws Exception {

		// STEP 1: ARRANGE
		this.policySetService.createPolicySet("test_policy_set1");
		this.policySetService.createPolicySet("test_policy_set2");
		this.policySetService.createPolicySet("test_policy_set3");

		
		// STEP 2: ACT
		List<PolicySet> policySets = this.policySetService.getAllPolicySets();
		
		

		// STEP 3: ASSERT
		Assert.assertNotNull("policySets is null", policySets);
		Assert.assertEquals("policySets size is incorrect", "3", Integer.toString(policySets.size()));
	}

	@Test
	public void getPolicySetByName() throws Exception {

		// STEP 1: ARRANGE
		String policySetName = "test_policy_set";
		this.policySetService.createPolicySet(policySetName);

		
		// STEP 2: ACT
		this.policySet = this.policySetService.getPolicySetByName(policySetName);
		
		

		// STEP 3: ASSERT
		Assert.assertNotNull("policySet is null", this.policySet);
		Assert.assertEquals("policySetName is incorrect", policySetName, policySet.getPolicySetName());
	}

	@Test(expected=EntityDoesNotExistException.class)
	public void getPolicySetByName_doesNotExist() throws Exception {

		// STEP 1: ARRANGE
		String policySetName = "test_policy_set";
		this.policySetService.createPolicySet(policySetName);

		
		// STEP 2: ACT
		this.policySet = this.policySetService.getPolicySetByName("dummy");
		
		

		// STEP 3: ASSERT
	}

	@Test
	public void createPolicy() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";

		
		// STEP 2: ACT
		this.policySet = this.policySetService.createPolicy(
			PolicySet.VEHICLE_POLICY,
			parentPolicySetName, 
			policyName, 
			policyDescription, 
			policyValue, 
			allowRegionalChangeable, 
			allowUserChangeable, 
			allowServiceChangeable, 
			allowCustomerFeedback, 
			hmi, 
			vehicleHmiFile,
			phone, 
			otaFunction, 
			policyValueType, 
			policyValueConstraints);
		
		
		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		Assert.assertEquals("policyDescription is incorrect", policyDescription, abstractPolicy.getPolicyDescription());
		Assert.assertEquals("policyValue is incorrect", policyValue.toString(), abstractPolicy.getPolicyValue().getPolicyValue().toString());
		Assert.assertEquals("allowRegionalChangeable is incorrect", allowRegionalChangeable, abstractPolicy.getAllowRegionalChangeable());
		Assert.assertEquals("allowUserChangeable is incorrect", allowUserChangeable, abstractPolicy.getAllowUserChangeable());
		Assert.assertEquals("allowServiceChangeable is incorrect", allowServiceChangeable, abstractPolicy.getAllowServiceChangeable());
		Assert.assertEquals("allowCustomerFeedback is incorrect", allowCustomerFeedback, abstractPolicy.getAllowCustomerFeedback());
		Assert.assertEquals("hmi is incorrect", hmi, abstractPolicy.getHmi());
		Assert.assertEquals("vehicleHmiFile is vehicleHmiFile", vehicleHmiFile, abstractPolicy.getVehicleHmiFile());
		Assert.assertEquals("phone is incorrect", phone, abstractPolicy.getPhone());
		Assert.assertEquals("otaFunction is incorrect", otaFunction, abstractPolicy.getOtaFunction().toString());
		Assert.assertEquals("policyValueType is incorrect", policyValueType, abstractPolicy.getPolicyValueType().toString());
		Assert.assertEquals("policyValueConstraints is incorrect", policyValueConstraints, abstractPolicy.getPolicyValueConstraints());
	}

	@Test(expected=ValidationException.class)
	public void createPolicy_undefinedValue_noAllowRegionalOverride() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = PolicySet.UNDEFINED; 
		Boolean allowRegionalChangeable = Boolean.FALSE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";

		
		// STEP 2: ACT
		this.policySetService.createPolicy(
			PolicySet.VEHICLE_POLICY,
			parentPolicySetName, 
			policyName, 
			policyDescription, 
			policyValue, 
			allowRegionalChangeable, 
			allowUserChangeable, 
			allowServiceChangeable, 
			allowCustomerFeedback, 
			hmi, 
			vehicleHmiFile,
			phone, 
			otaFunction, 
			policyValueType, 
			policyValueConstraints);
	}
	
	@Test
	public void createProgramLevelPolicyOverride() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		String programLevelPolicyOverrideValue = "program_level_policy_override_value";
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		
		
		// STEP 2: ACT
		this.policySetService.createProgramLevelPolicyOverride(
			policyName, 	
			programModelYear, 
			programLevelPolicyOverrideValue);
		
		
		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		AbstractPolicyValue programLevelPolicyOverride = abstractPolicy.getOverriddenPolicyValue(programCode, modelYear);
		Assert.assertNotNull("programLevelPolicyOverride is null", programLevelPolicyOverride);
		Assert.assertEquals("programLevelPolicyOverrideValue is incorrect", programLevelPolicyOverrideValue, programLevelPolicyOverride.getPolicyValue().toString());
	}
	
	@Test
	public void createRegionLevelPolicyOverride() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2018);
		createProgramModelYear(programCode, modelYear);
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		String programLevelPolicyOverrideValue = "program_level_policy_override_value";
		this.policySetService.createProgramLevelPolicyOverride(
			policyName, 
			programModelYear, 
			programLevelPolicyOverrideValue);
		
		String regionCode = "us";
		String countryName = "United States";
		createRegion(regionCode, countryName);
		String regionLevelPolicyOverrideValue = "region_level_policy_override_value";
		
		
		// STEP 2: ACT
		this.policySetService.createRegionLevelPolicyOverride(
			parentPolicySetName,
			policyName, 
			regionCode, 
			regionLevelPolicyOverrideValue);
		
		
		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		AbstractPolicyValue regionLevelPolicyOverride = abstractPolicy.getOverriddenPolicyValue(programCode, modelYear, regionCode);
		Assert.assertNotNull("regionLevelPolicyOverride is null", regionLevelPolicyOverride);
		Assert.assertEquals("regionLevelPolicyOverrideValue is incorrect", regionLevelPolicyOverrideValue, regionLevelPolicyOverride.getPolicyValue().toString());
	}

	@Test(expected=ValidationException.class)
	public void createRegionLevelPolicyOverride_notRegionalChangeable() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.FALSE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		String programLevelPolicyOverrideValue = "program_level_policy_override_value";
		this.policySetService.createProgramLevelPolicyOverride(
			policyName, 
			programModelYear, 
			programLevelPolicyOverrideValue);
		
		String regionCode = "us";
		String countryName = "United States";
		createRegion(regionCode, countryName);
		String regionLevelPolicyOverrideValue = "region_level_policy_override_value";
		
		
		// STEP 2: ACT
		this.policySetService.createRegionLevelPolicyOverride(
			parentPolicySetName,
			policyName, 
			regionCode, 
			regionLevelPolicyOverrideValue);
		
		
		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		AbstractPolicyValue regionLevelPolicyOverride = abstractPolicy.getOverriddenPolicyValue(programCode, modelYear, regionCode);
		Assert.assertNotNull("regionLevelPolicyOverride is null", regionLevelPolicyOverride);
		Assert.assertEquals("regionLevelPolicyOverrideValue is incorrect", regionLevelPolicyOverrideValue, regionLevelPolicyOverride.getPolicyValue().toString());
	}
	
	@Test
	public void createVehicleLevelPolicyOverride() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		String programLevelPolicyOverrideValue = "program_level_policy_override_value";
		this.policySetService.createProgramLevelPolicyOverride(
			policyName, 	
			programModelYear, 
			programLevelPolicyOverrideValue);
		
		String regionCode = "us";
		String countryName = "United States";
		createRegion(regionCode, countryName);
		String regionLevelPolicyOverrideValue = "region_level_policy_override_value";
		this.policySetService.createRegionLevelPolicyOverride(
			parentPolicySetName,
			policyName, 
			regionCode, 			
			regionLevelPolicyOverrideValue);
		String parentVin = "1FA6P8TH4J5000000";
		String vehicleLevelPolicyOverrideValue = "vehicle_level_policy_override_value";
		
		
		// STEP 2: ACT
		this.policySet = this.policySetService.createVehicleLevelPolicyOverride(
			parentPolicySetName,
			policyName,
			parentVin, 			 
			vehicleLevelPolicyOverrideValue);
		
		
		// STEP 3: ASSERT
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		AbstractPolicyValue vehicleLevelPolicyOverride = abstractPolicy.getOverriddenPolicyValue(programCode, modelYear, regionCode, parentVin);
		Assert.assertNotNull("vehicleLevelPolicyOverride is null", vehicleLevelPolicyOverride);
		Assert.assertEquals("regionLevelPolicyOverrideValue is incorrect", vehicleLevelPolicyOverrideValue, vehicleLevelPolicyOverride.getPolicyValue().toString());
	}

	@Test(expected=ValidationException.class)
	public void createVehicleLevelPolicyOverride_userNotChangeable() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.FALSE;
		Boolean allowServiceChangeable = Boolean.FALSE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		String programLevelPolicyOverrideValue = "program_level_policy_override_value";
		this.policySetService.createProgramLevelPolicyOverride(
			policyName, 	
			programModelYear, 
			programLevelPolicyOverrideValue);
		
		String regionCode = "us";
		String countryName = "United States";
		createRegion(regionCode, countryName);
		String regionLevelPolicyOverrideValue = "region_level_policy_override_value";
		this.policySetService.createRegionLevelPolicyOverride(
			parentPolicySetName,
			policyName, 
			regionCode, 			
			regionLevelPolicyOverrideValue);
		String parentVin = "1FA6P8TH4J5000000";
		String vehicleLevelPolicyOverrideValue = "vehicle_level_policy_override_value";
		
		
		// STEP 2: ACT
		this.policySet = this.policySetService.createVehicleLevelPolicyOverride(
			parentPolicySetName,
			policyName,
			parentVin, 			 
			vehicleLevelPolicyOverrideValue);
		
		
		// STEP 3: ASSERT
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		AbstractPolicyValue vehicleLevelPolicyOverride = abstractPolicy.getOverriddenPolicyValue(programCode, modelYear, regionCode, parentVin);
		Assert.assertNotNull("vehicleLevelPolicyOverride is null", vehicleLevelPolicyOverride);
		Assert.assertEquals("regionLevelPolicyOverrideValue is incorrect", vehicleLevelPolicyOverrideValue, vehicleLevelPolicyOverride.getPolicyValue().toString());
	}
	
	
	@Test
	public void renamePolicySet() throws Exception {

		// STEP 1: ARRANGE
		String oldPolicySetName = "old_policy_set";
		String newPolicySetName = "new_policy_set";
		createPolicySet(oldPolicySetName);

		
		// STEP 2: ACT
		this.policySet = this.policySetService.renamePolicySet(oldPolicySetName, newPolicySetName);
		
		

		// STEP 3: ASSERT
		Assert.assertNotNull("policySet is null", this.policySet);
	}

	@Test(expected=EntityDoesNotExistException.class)
	public void renamePolicySet_entityDoesNotExist() throws Exception {

		// STEP 1: ARRANGE
		String oldPolicySetName = "old_policy_set";
		String newPolicySetName = "new_policy_set";
		createPolicySet(oldPolicySetName);

		
		// STEP 2: ACT
		this.policySet = this.policySetService.renamePolicySet("dummy", newPolicySetName);
		
		

		// STEP 3: ASSERT
	}
	
	@Test(expected=EntityAlreadyExistsException.class)
	public void renamePolicySet_entityAlreadyExists() throws Exception {

		// STEP 1: ARRANGE
		String oldPolicySetName = "old_policy_set";
		String newPolicySetName = "new_policy_set";
		createPolicySet(oldPolicySetName);
		createPolicySet(newPolicySetName);

		
		// STEP 2: ACT
		this.policySet = this.policySetService.renamePolicySet(oldPolicySetName, newPolicySetName);
		
		

		// STEP 3: ASSERT
	}

	@Test
	public void renamePolicy() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String oldPolicyName = "old_policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, oldPolicyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		String newPolicyName = "new_policy_name";
		
		
		// STEP 2: ACT
		this.policySetService.renamePolicy(
			parentPolicySetName, 
			oldPolicyName, 
			newPolicyName);
		
		
		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		Assert.assertNotNull("policySet is null", policySet);
		AbstractPolicy abstractPolicy = this.policySet.getPolicyByName(newPolicyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
	}

	@Test
	public void updatePolicy() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "old_policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		policyDescription = "new_policy_description";
		policyValue = "policy_value"; 
		allowRegionalChangeable = Boolean.FALSE;
		allowUserChangeable = Boolean.FALSE;
		allowServiceChangeable = Boolean.FALSE;
		allowCustomerFeedback = Boolean.FALSE;
		hmi = "new_hmi_value";
		vehicleHmiFile = "new_vehicle_hmi_file";
		phone = "new_phone";
		otaFunction = OtaFunction.OTA_MANAGER_HMI.toString();
		
		
		
		// STEP 2: ACT
		this.policySetService.updateVehiclePolicy(
			parentPolicySetName, 
			policyName, 
			policyDescription, 
			policyValue, 
			allowRegionalChangeable, 
			allowUserChangeable, 
			allowServiceChangeable, 
			allowCustomerFeedback, 
			hmi, 
			vehicleHmiFile, 
			phone, 
			otaFunction, 
			policyValueType, 
			policyValueConstraints);
		
		
		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		Assert.assertEquals("policyDescription is incorrect", policyDescription, abstractPolicy.getPolicyDescription());
		Assert.assertEquals("policyValue is incorrect", policyValue.toString(), abstractPolicy.getPolicyValue().getPolicyValue().toString());
		Assert.assertEquals("allowRegionalChangeable is incorrect", allowRegionalChangeable, abstractPolicy.getAllowRegionalChangeable());
		Assert.assertEquals("allowUserChangeable is incorrect", allowUserChangeable, abstractPolicy.getAllowUserChangeable());
		Assert.assertEquals("allowServiceChangeable is incorrect", allowServiceChangeable, abstractPolicy.getAllowServiceChangeable());
		Assert.assertEquals("allowCustomerFeedback is incorrect", allowCustomerFeedback, abstractPolicy.getAllowCustomerFeedback());
		Assert.assertEquals("hmi is incorrect", hmi, abstractPolicy.getHmi());
		Assert.assertEquals("vehicleHmiFile is vehicleHmiFile", vehicleHmiFile, abstractPolicy.getVehicleHmiFile());
		Assert.assertEquals("phone is incorrect", phone, abstractPolicy.getPhone());
		Assert.assertEquals("otaFunction is incorrect", otaFunction, abstractPolicy.getOtaFunction().toString());
		Assert.assertEquals("policyValueType is incorrect", policyValueType, abstractPolicy.getPolicyValueType().toString());
		Assert.assertEquals("policyValueConstraints is incorrect", policyValueConstraints, abstractPolicy.getPolicyValueConstraints());
	}
	
	@Test
	public void updateProgramLevelPolicyOverride() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		String programLevelPolicyOverrideValue = "program_level_policy_override_value";
		this.policySetService.createProgramLevelPolicyOverride(policyName, programModelYear, programLevelPolicyOverrideValue);
		programLevelPolicyOverrideValue = "new_program_level_policy_override_value";
		
		
		// STEP 2: ACT
		this.policySetService.updateProgramLevelPolicyOverride(
			policyName, 
			programModelYear, 
			programLevelPolicyOverrideValue);
		
		
		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		AbstractPolicyValue programLevelPolicyOverride = abstractPolicy.getOverriddenPolicyValue(programCode, modelYear);
		Assert.assertNotNull("programLevelPolicyOverride is null", programLevelPolicyOverride);
		Assert.assertEquals("programLevelPolicyOverrideValue is incorrect", programLevelPolicyOverrideValue, programLevelPolicyOverride.getPolicyValue().toString());
	}
	
	@Test
	public void updateRegionLevelPolicyOverride() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		String programLevelPolicyOverrideValue = "program_level_policy_override_value";
		this.policySetService.createProgramLevelPolicyOverride(
			policyName, 			
			programModelYear, 
			programLevelPolicyOverrideValue);
		
		String regionCode = "us";
		String countryName = "United States";
		createRegion(regionCode, countryName);
		String regionLevelPolicyOverrideValue = "region_level_policy_override_value";
		this.policySetService.createRegionLevelPolicyOverride(
				parentPolicySetName,
				policyName, 
				regionCode, 				
				regionLevelPolicyOverrideValue);
		regionLevelPolicyOverrideValue = "new_region_level_policy_override_value";
		
		
		// STEP 2: ACT
		this.policySetService.updateRegionLevelPolicyOverride(
			parentPolicySetName,
			policyName,
			regionCode, 
			regionLevelPolicyOverrideValue);
		
		
		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		AbstractPolicyValue regionLevelPolicyOverride = abstractPolicy.getOverriddenPolicyValue(programCode, modelYear, regionCode);
		Assert.assertNotNull("regionLevelPolicyOverride is null", regionLevelPolicyOverride);
		Assert.assertEquals("regionLevelPolicyOverrideValue is incorrect", regionLevelPolicyOverrideValue, regionLevelPolicyOverride.getPolicyValue().toString());
	}

	@Test(expected=EntityDoesNotExistException.class)
	public void deletePolicySet() throws Exception {

		// STEP 1: ARRANGE
		String policySetName = "test_policy_set";
		createPolicySet(policySetName);

		
		// STEP 2: ACT
		this.policySetService.deletePolicySet(policySetName);
		

		// STEP 3: ASSERT
		this.policySetService.getPolicySetByName(policySetName);
	}

	@Test(expected=EntityDoesNotExistException.class)
	public void deletePolicy() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);

		
		// STEP 2: ACT
		this.policySetService.deletePolicy(
			parentPolicySetName, 
			policyName);
		

		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(parentPolicySetName);
		this.policySet.getPolicyByName(policyName);
	}
	
	@Test
	public void deleteProgramLevelPolicyOverride() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		String programLevelPolicyOverrideValue = "program_level_policy_override_value";
		this.policySetService.createProgramLevelPolicyOverride(policyName, programModelYear, programLevelPolicyOverrideValue);
		
		
		// STEP 2: ACT
		this.policySetService.deleteProgramLevelPolicyOverride(
			policyName, 
			programModelYear);
		
		
		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		List<AbstractPolicyOverride> list = abstractPolicy.getPolicyOverrides();
		Assert.assertNotNull("list is null", list);
		Assert.assertEquals("list size is incorrect", "0", Integer.toString(list.size()));
	}
	
	@Test
	public void deleteRegionLevelPolicyOverride() throws Exception {

		// STEP 1: ARRANGE
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		createPolicySet(parentPolicySetName);
		String policyName = "policy_name";
		String policyDescription = "policy_description";
		Object policyValue = "policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(parentPolicySetName, policyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		String regionCode = "us";
		String countryName = "United States";
		createRegion(regionCode, countryName);
		String regionLevelPolicyOverrideValue = "region_level_policy_override_value";
		this.policySetService.createRegionLevelPolicyOverride(
				parentPolicySetName,
				policyName, 
				regionCode, 				
				regionLevelPolicyOverrideValue);
		
		
		// STEP 2: ACT
		this.policySetService.deleteRegionLevelPolicyOverride(
			parentPolicySetName,
			policyName,
			regionCode);
		
		
		// STEP 3: ASSERT
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		Assert.assertNotNull("policySet is null", policySet);
		VehiclePolicy abstractPolicy = this.policySet.getVehiclePolicyByName(policyName);
		Assert.assertNotNull("policy is null", abstractPolicy);
		List<AbstractPolicyOverride> list = abstractPolicy.getPolicyOverrides();
		Assert.assertNotNull("list is null", list);
		Assert.assertEquals("list size is incorrect", "0", Integer.toString(list.size()));
	}
	
	@Test
	public void createRegion() throws EntityAlreadyExistsException, ValidationException, EntityDoesNotExistException {

		// STEP 1: ARRANGE
		String regionCode = "us";
		String countryName = "United States";

		
		// STEP 2: ACT
		this.policySetService.createRegion(regionCode, countryName);
		

		// STEP 3: ASSERT
		Region region = this.policySetService.getRegionByCode(regionCode);
		Assert.assertNotNull("region is null", region);
		Assert.assertEquals("regionCode is incorrect", regionCode, region.getRegionCode());
		Assert.assertEquals("countryName is incorrect", countryName, region.getCountryName());
	}

	@Test
	public void getAllRegions() throws EntityAlreadyExistsException, ValidationException, EntityDoesNotExistException {

		// STEP 1: ARRANGE
		createRegion("us", "United States");
		createRegion("ca", "Canada");

		
		// STEP 2: ACT
		List<Region> regionList = this.policySetService.getAllRegions();
		

		// STEP 3: ASSERT
		Assert.assertNotNull("regionList is null", regionList);
		Assert.assertEquals("regionList size is incorrect", "2",Integer.toString(regionList.size()));
	}
	
	@Test
	public void getRegionByCode() throws EntityAlreadyExistsException, ValidationException, EntityDoesNotExistException {

		// STEP 1: ARRANGE
		String regionCode = "us";
		String countryName = "United States";
		this.createRegion(regionCode, countryName);

		
		// STEP 2: ACT
		Region region = this.policySetService.getRegionByCode(regionCode);
		

		// STEP 3: ASSERT
		Assert.assertNotNull("region is null", region);
		Assert.assertEquals("regionCode is incorrect", regionCode, region.getRegionCode());
		Assert.assertEquals("countryName is incorrect", countryName, region.getCountryName());
	}

	@Test
	public void renameRegion() throws EntityAlreadyExistsException, ValidationException, EntityDoesNotExistException {

		// STEP 1: ARRANGE
		String oldRegionCode = "XX";
		String countryName = "United States";
		this.createRegion(oldRegionCode, countryName);
		String newRegionCode = "us";
		
		
		// STEP 2: ACT
		this.policySetService.renameRegion(
			oldRegionCode, 
			newRegionCode);
		

		// STEP 3: ASSERT
		Region region = this.policySetService.getRegionByCode(newRegionCode);
		Assert.assertNotNull("region is null", region);
		Assert.assertEquals("regionCode is incorrect", newRegionCode, region.getRegionCode());
		Assert.assertEquals("countryName is incorrect", countryName, region.getCountryName());
	}

	@Test
	public void updateRegion() throws EntityAlreadyExistsException, ValidationException, EntityDoesNotExistException {

		// STEP 1: ARRANGE
		String regionCode = "us";
		String countryName = "UnitedStatesOfAmerica";
		this.createRegion(regionCode, countryName);
		countryName = "United States";
		
		
		// STEP 2: ACT
		this.policySetService.updateRegion(
			regionCode, 
			countryName);
		

		// STEP 3: ASSERT
		Region region = this.policySetService.getRegionByCode(regionCode);
		Assert.assertNotNull("region is null", region);
		Assert.assertEquals("regionCode is incorrect", regionCode, region.getRegionCode());
		Assert.assertEquals("countryName is incorrect", countryName, region.getCountryName());
	}

	@Test(expected=EntityDoesNotExistException.class)
	public void deleteRegion() throws EntityAlreadyExistsException, ValidationException, EntityDoesNotExistException {

		// STEP 1: ARRANGE
		String regionCode = "us";
		String countryName = "UnitedStatesOfAmerica";
		this.createRegion(regionCode, countryName);
		
		
		// STEP 2: ACT
		this.policySetService.deleteRegion(regionCode);
		

		// STEP 3: ASSERT
		this.policySetService.getRegionByCode(regionCode);
	}
	
	
	// ****************************************************************************************************************
	// ***   PURE BUSINESS METHODS   **********************************************************************************
	// ****************************************************************************************************************
	
	
	// SCENARIO 1: Global policy table that has policies for ALL regions, but NO regional override information.
	@Test
	public void renderGlobalPolicyTableJson() throws EntityAlreadyExistsException, ValidationException, EntityDoesNotExistException, IOException {

		// STEP 1: ARRANGE
		this.policySetRepository = ClasspathPolicySetRepositoryImpl.getInstance();
		this.policySetRepository.reset();
		this.policySetService.setPolicySetRepository(this.policySetRepository);

		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		String programPolicyRegionLevelPolicyOverrideValue = "program_level_policy_override_value_for_global_policy";
		String globalPolicyName = "Battery SOC";
		createProgramLevelPolicyOverride(globalPolicyName, programCode, modelYear, programPolicyRegionLevelPolicyOverrideValue);

		
		// STEP 2: ACT
		String renderedGlobalPolicyTableJson = this.policySetService.renderGlobalPolicyTableJson();
		

		// STEP 3: ASSERT
		Assert.assertNotNull("renderedGlobalPolicyTableJson is null", renderedGlobalPolicyTableJson);
		//LOGGER.info("renderedGlobalPolicyTableJson VehicleJSON ==>" + renderedGlobalPolicyTableJson);
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		String serviceJson = this.policySetService.getJsonConverter().marshallFromEntityToJson(this.policySet);
		LOGGER.info("renderedGlobalPolicyTable ServiceJSON ==>" + serviceJson);
	}
	
	
	// SCENARIO 2: Global policy table that has policies for ALL regions, but it DOES HAVE regional override information.
	@Test
	public void renderGlobalPolicyTableJsonForRegion() throws EntityDoesNotExistException, ValidationException {
		
		// STEP 1: ARRANGE
		this.policySetRepository = ClasspathPolicySetRepositoryImpl.getInstance();
		this.policySetRepository.reset();
		this.policySetService.setPolicySetRepository(this.policySetRepository);
		String regionCode = "us";
		
		
		// STEP 2: ACT
		String renderedGlobalPolicyTableJsonForRegion = this.policySetService.renderGlobalPolicyTableJsonForRegion(regionCode);
		

		// STEP 3: ASSERT
		Assert.assertNotNull("renderedGlobalPolicyTableJsonForRegion is null", renderedGlobalPolicyTableJsonForRegion);
		//LOGGER.info("renderedGlobalPolicyTableJsonForRegion VehicleJSON ==>" + renderedGlobalPolicyTableJsonForRegion);
		/*
		this.policySet = this.policySetService.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
		String serviceJson = this.policySetService.getJsonConverter().marshallFromEntityToJson(this.policySet);
		LOGGER.info("renderedGlobalPolicyTableJsonForRegion ServiceJSON ==>" + serviceJson);
		*/
	}
	
	
	// SCENARIO 3: Program-specific policy table (i.e. non-global policies) that DO HAVE regional override information, 
	// for the given program code, model year and region.  This policy table also includes all GLOBAL polices, along with their 
	// regional overrides for the given region
	@Test
	public void renderGenericPolicyTableJsonForProgramAndRegion() throws Exception {

		// STEP 1: ARRANGE
		this.policySetRepository = ClasspathPolicySetRepositoryImpl.getInstance();
		this.policySetRepository.reset();
		this.policySetService.setPolicySetRepository(this.policySetRepository);
		String regionCode = "us";
		
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);

		String programSpecificPolicySetName = programCode + "_" + modelYear + "_PolicySet";
		createPolicySet(programSpecificPolicySetName);
		associateWithProgramModelYear(programSpecificPolicySetName, programCode, modelYear);
		
		String programPolicyName = "program_policy_name";
		String policyDescription = "program_policy_description";
		Object policyValue = "program_policy_value"; 
		Boolean allowRegionalChangeable = Boolean.TRUE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		createVehiclePolicy(programSpecificPolicySetName, programPolicyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		
		String regionLevelPolicyOverrideValue = "region_level_policy_override_value_for_program";
		createRegionLevelPolicyOverride(programSpecificPolicySetName, programPolicyName, regionCode, regionLevelPolicyOverrideValue);
		
		
		// STEP 2: ACT
		String renderedGenericPolicyTableJsonForProgramAndRegion = this.policySetService.renderGenericPolicyTableJsonForProgramAndRegion(
			programModelYear, 
			regionCode);
		
		
		// STEP 3: ASSERT
		Assert.assertNotNull("renderedGenericPolicyTableJsonForProgramAndRegion is null", renderedGenericPolicyTableJsonForProgramAndRegion);
		LOGGER.info("renderedGenericPolicyTableJsonForProgramAndRegion VehicleJSON ==>" + renderedGenericPolicyTableJsonForProgramAndRegion);
		LOGGER.info(renderedGenericPolicyTableJsonForProgramAndRegion);
		
		this.policySet = this.policySetService.getPolicySetByName(programSpecificPolicySetName);
		String serviceJson = this.policySetService.getJsonConverter().marshallFromEntityToJson(this.policySet);
		LOGGER.info(" renderedGenericPolicyTableJsonForProgramAndRegion ServiceJSON ==>" + serviceJson);
	}

	// SCENARIO 3: Program-specific policy table (i.e. non-global policies) that DO HAVE regional override information, 
	// for the given program code, model year and region.  This policy table also includes all GLOBAL polices, along with their 
	// regional overrides for the given region
	@Test(expected=ValidationException.class)
	public void renderGenericPolicyTableJsonForProgramAndRegion_undefinedPolicyValue() throws Exception {

		// STEP 1: ARRANGE
		this.policySetRepository = ClasspathPolicySetRepositoryImpl.getInstance();
		this.policySetRepository.reset();
		this.policySetService.setPolicySetRepository(this.policySetRepository);
		String regionCode = "us";
		
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		createProgramModelYear(programCode, modelYear);
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);

		String programSpecificPolicySetName = programCode + "_" + modelYear + "_PolicySet";
		createPolicySet(programSpecificPolicySetName);
		associateWithProgramModelYear(programSpecificPolicySetName, programCode, modelYear);
		
		// Create a global policy that has an UNDEFINED value and no regional override was created.  In this scenario, we expect a validation error.
		String parentPolicySetName = PolicySet.GLOBAL_POLICY_SET_NAME;
		String policyName = "policy_name_with_undefined_default_value";
		String policyDescription = "policy_description";
		Object policyValue = PolicySet.UNDEFINED; 
		Boolean allowRegionalChangeable = Boolean.FALSE;
		Boolean allowUserChangeable = Boolean.TRUE;
		Boolean allowServiceChangeable = Boolean.TRUE;
		Boolean allowCustomerFeedback = Boolean.TRUE;
		String hmi = "hmi_value";
		String vehicleHmiFile = "vehicle_hmi_file";
		String phone = "phone";
		String otaFunction = OtaFunction.OTA_MANAGER.toString();
		String policyValueType = PolicyValueType.STRING.toString();
		String policyValueConstraints = "NONE";
		this.policySetService.createPolicy(
			PolicySet.VEHICLE_POLICY,
			parentPolicySetName, 
			policyName, 
			policyDescription, 
			policyValue, 
			allowRegionalChangeable, 
			allowUserChangeable, 
			allowServiceChangeable, 
			allowCustomerFeedback, 
			hmi, 
			vehicleHmiFile,
			phone, 
			otaFunction, 
			policyValueType, 
			policyValueConstraints);

		// Create a program level policy
		String programPolicyName = "program_policy_name";
		policyDescription = "program_policy_description";
		policyValue = "program_policy_value"; 
		allowRegionalChangeable = Boolean.TRUE;
		allowUserChangeable = Boolean.TRUE;
		allowServiceChangeable = Boolean.TRUE;
		allowCustomerFeedback = Boolean.TRUE;
		hmi = "hmi_value";
		vehicleHmiFile = "vehicle_hmi_file";
		phone = "phone";
		otaFunction = OtaFunction.OTA_MANAGER.toString();
		policyValueType = PolicyValueType.STRING.toString();
		policyValueConstraints = "NONE";
		createVehiclePolicy(programSpecificPolicySetName, programPolicyName, policyDescription, policyValue, allowRegionalChangeable, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, hmi, vehicleHmiFile, phone, otaFunction, policyValueType, policyValueConstraints);
		
		String regionLevelPolicyOverrideValue = "region_level_policy_override_value_for_program";
		createRegionLevelPolicyOverride(programSpecificPolicySetName, programPolicyName, regionCode, regionLevelPolicyOverrideValue);
		
		
		// STEP 2: ACT
		String renderedGenericPolicyTableJsonForProgramAndRegion = this.policySetService.renderGenericPolicyTableJsonForProgramAndRegion(
			programModelYear, 
			regionCode);
		
		
		// STEP 3: ASSERT
		Assert.assertNotNull("renderedGenericPolicyTableJsonForProgramAndRegion is null", renderedGenericPolicyTableJsonForProgramAndRegion);
		LOGGER.info("renderedGenericPolicyTableJsonForProgramAndRegion VehicleJSON ==>" + renderedGenericPolicyTableJsonForProgramAndRegion);
		LOGGER.info(renderedGenericPolicyTableJsonForProgramAndRegion);
		
		this.policySet = this.policySetService.getPolicySetByName(programSpecificPolicySetName);
		String serviceJson = this.policySetService.getJsonConverter().marshallFromEntityToJson(this.policySet);
		LOGGER.info(" renderedGenericPolicyTableJsonForProgramAndRegion ServiceJSON ==>" + serviceJson);
	}
	
	// ****************************************************************************************************************
	// ***   TEST HARNESS METHODS   ***********************************************************************************
	// ****************************************************************************************************************
	private void createPolicySet(String policySetName) throws EntityAlreadyExistsException, ValidationException {
		
		this.policySetService.createPolicySet(policySetName);
	}
	
	private void createVehiclePolicy(
		String parentPolicySetName,	
		String policyName,
		String policyDescription,
		Object policyValue,
		Boolean allowRegionalChangeable,
		Boolean allowUserChangeable,
		Boolean allowServiceChangeable,
		Boolean allowCustomerFeedback,
		String hmi,
		String vehicleHmiFile,
		String phone,
		String otaFunction,
		String policyValueType,
		String policyValueConstraints) throws EntityDoesNotExistException, EntityAlreadyExistsException, ValidationException {
		  
		this.policySetService.createPolicy(
			PolicySet.VEHICLE_POLICY,	
			parentPolicySetName, 
			policyName, 
			policyDescription, 
			policyValue, 
			allowRegionalChangeable, 
			allowUserChangeable, 
			allowServiceChangeable, 
			allowCustomerFeedback, 
			hmi, 
			vehicleHmiFile,
			phone, 
			otaFunction, 
			policyValueType, 
			policyValueConstraints);
	 
	}
	
	private ProgramModelYear createProgramModelYear(String programCode, Integer modelYear) throws EntityAlreadyExistsException, ValidationException {
		
		return this.programModelYearService.createProgramModelYear(programCode, modelYear);
	}
	
	private PolicySet associateWithProgramModelYear(String policySetName, String programCode, Integer modelYear) throws EntityDoesNotExistException, EntityAlreadyExistsException, ValidationException {
		
		this.programModelYearService.associatePolicySetToProgramModelYear(policySetName, programCode, modelYear);
		return this.programModelYearService.getPolicySetByProgramCodeAndModelYear(programCode, modelYear);
	}
	
	private void createRegion(String regionCode, String countryName) throws EntityAlreadyExistsException, ValidationException {
		
		this.policySetService.createRegion(regionCode, countryName);
	}
	
	private PolicySet createProgramLevelPolicyOverride(
		String parentPolicyName,	
		String programCode,
		Integer modelYear,
		Object policyValue)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		ProgramModelYear programModelYear = this.programModelYearService.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		
		return this.policySetService.createProgramLevelPolicyOverride(
			parentPolicyName,	
			programModelYear, 
			policyValue);
	}
	
	private PolicySet createRegionLevelPolicyOverride(
		String parentPolicySetName,
		String parentPolicyName,
		String regionCode,
		Object policyValue)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		return this.policySetService.createRegionLevelPolicyOverride(
			parentPolicySetName, 
			parentPolicyName, 
			regionCode, 
			policyValue);
	}
}
