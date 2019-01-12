/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.program.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEventPublisher;
import com.djt.cvpp.ota.orfin.delivery.event.impl.MockOrfinDeliveryRuleSetEventPublisher;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.delivery.repository.DeliveryRuleSetRepository;
import com.djt.cvpp.ota.orfin.delivery.repository.impl.MockDeliveryRuleSetRepositoryImpl;
import com.djt.cvpp.ota.orfin.delivery.service.DeliveryRuleSetService;
import com.djt.cvpp.ota.orfin.delivery.service.impl.DeliveryRuleSetServiceImpl;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEventPublisher;
import com.djt.cvpp.ota.orfin.odl.event.impl.MockOrfinOdlEventPublisher;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.odl.repository.OptimizedDataListRepository;
import com.djt.cvpp.ota.orfin.odl.repository.impl.MockOptimizedDataListRepositoryImpl;
import com.djt.cvpp.ota.orfin.odl.service.OptimizedDataListService;
import com.djt.cvpp.ota.orfin.odl.service.impl.OptimizedDataListServiceImpl;
import com.djt.cvpp.ota.orfin.policy.event.impl.MockOrfinPolicySetEventPublisher;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.orfin.policy.repository.PolicySetRepository;
import com.djt.cvpp.ota.orfin.policy.repository.impl.MockPolicySetRepositoryImpl;
import com.djt.cvpp.ota.orfin.policy.service.PolicySetService;
import com.djt.cvpp.ota.orfin.policy.service.impl.PolicySetServiceImpl;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;
import com.djt.cvpp.ota.orfin.program.repository.ProgramModelYearRepository;
import com.djt.cvpp.ota.orfin.program.repository.impl.MockProgramModelYearRepositoryImpl;
import com.djt.cvpp.ota.orfin.program.service.impl.ProgramModelYearServiceImpl;
import com.djt.cvpp.ota.orfin.vadrevent.repository.OrfinVadrReleaseEventRepository;
import com.djt.cvpp.ota.orfin.vadrevent.repository.impl.MockOrfinVadrReleaseEventRepositoryImpl;
import com.djt.cvpp.ota.orfin.vadrevent.service.OrfinVadrReleaseEventService;
import com.djt.cvpp.ota.orfin.vadrevent.service.impl.OrfinVadrReleaseEventServiceImpl;
import com.djt.cvpp.ota.testutil.OrfinDeliveryRuleSetTestHarness;
import com.djt.cvpp.ota.testutil.OrfinOdlTestHarness;
import com.djt.cvpp.ota.testutil.OrfinPolicySetTestHarness;

public class ProgramModelYearServiceImplTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgramModelYearServiceImplTest.class);

	
	private OrfinVadrReleaseEventRepository orfinVadrReleaseEventRepository;
	private OrfinVadrReleaseEventService orfinVadrReleaseEventService;
	
	private OrfinDeliveryRuleSetTestHarness orfinDeliveryRuleSetTestHarness = new OrfinDeliveryRuleSetTestHarness();
	private OrfinOdlTestHarness orfinOdlTestHarness = new OrfinOdlTestHarness();
	private OrfinPolicySetTestHarness orfinPolicySetTestHarness = new OrfinPolicySetTestHarness();


	private OrfinDeliveryRuleSetEventPublisher orfinDeliveryRuleSetEventPublisher;
	private DeliveryRuleSetRepository deliveryRuleSetRepository;
	private DeliveryRuleSetService deliveryRuleSetService;

	private OrfinOdlEventPublisher orfinOdlEventPublisher;
	private OptimizedDataListRepository optimizedDataListRepository;
	private OptimizedDataListService optimizedDataListService;
	
	private PolicySetRepository policySetRepository;
	private PolicySetService policySetService;
	private MockOrfinPolicySetEventPublisher orfinPolicySetEventPublisher;
	
	private ProgramModelYearRepository programModelYearRepository;
	private ProgramModelYearService programModelYearService;
	
		
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
		
		
		this.orfinPolicySetEventPublisher = MockOrfinPolicySetEventPublisher.getInstance();
		this.policySetRepository = new MockPolicySetRepositoryImpl();
		this.policySetService = new PolicySetServiceImpl();
		this.policySetService.setPolicySetRepository(this.policySetRepository);
		this.policySetService.setOrfinPolicySetEventPublisher(this.orfinPolicySetEventPublisher);
		this.policySetService = new PolicySetServiceImpl(
			this.policySetRepository,
			this.orfinPolicySetEventPublisher);
		
		
		this.programModelYearRepository = MockProgramModelYearRepositoryImpl.getInstance();
		this.programModelYearRepository.reset();
		this.programModelYearService = new ProgramModelYearServiceImpl(
			this.programModelYearRepository,
			this.deliveryRuleSetService,
			this.optimizedDataListService,
			this.policySetService);
	}
	
	@Test
	public void createProgramModelYearWithChildren() throws Exception {

		// STEP 1: ARRANGE
		String programCode = "C344N";
		Integer modelYear = Integer.valueOf(2017);
		ProgramModelYear programModelYear = this.programModelYearService.createProgramModelYear(programCode, modelYear);
		Odl odl = orfinOdlTestHarness.buildOdl();
		PolicySet policySet = this.orfinPolicySetTestHarness.buildPolicySet();
		DeliveryRuleSet deliveryRuleSet = this.orfinDeliveryRuleSetTestHarness.buildDeliveryRuleSet();
		programModelYear.setOdl(odl);
		programModelYear.setPolicySet(policySet);
		programModelYear.addDeliveryRuleSet(deliveryRuleSet);
		
		
		// STEP 2: ACT
		String json = this.programModelYearService.getJsonConverter().marshallFromEntityToJson(programModelYear);
		LOGGER.info(json);


		// STEP 3: ASSERT
		Assert.assertNotNull("programModelYear is null", programModelYear);
		Assert.assertEquals("programCode is incorrect", programCode, programModelYear.getParentProgram().getProgramCode());
		Assert.assertEquals("modelYear is incorrect", modelYear, programModelYear.getParentModelYear().getModelYear());
		Assert.assertNotNull("odl is null", programModelYear.getOdl());
		Assert.assertNotNull("policySet is null", programModelYear.getPolicySet());
		Assert.assertFalse("deliveryRuleSets is empty", programModelYear.getDeliveryRuleSets().isEmpty());
	}
}
