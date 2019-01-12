/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.service;

import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.flare.rollout.model.AbstractRollout;
import com.djt.cvpp.ota.flare.rollout.model.enums.RolloutState;
import com.djt.cvpp.ota.flare.rollout.repository.RolloutRepository;
import com.djt.cvpp.ota.flare.rollout.repository.impl.MockRolloutRepositoryImpl;
import com.djt.cvpp.ota.flare.rollout.service.impl.RolloutServiceImpl;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.service.VehicleStatusMessageTemplateService;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.service.impl.VehicleStatusMessageTemplateServiceImpl;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEventPublisher;
import com.djt.cvpp.ota.orfin.delivery.event.impl.MockOrfinDeliveryRuleSetEventPublisher;
import com.djt.cvpp.ota.orfin.delivery.repository.DeliveryRuleSetRepository;
import com.djt.cvpp.ota.orfin.delivery.repository.impl.MockDeliveryRuleSetRepositoryImpl;
import com.djt.cvpp.ota.orfin.delivery.service.DeliveryRuleSetService;
import com.djt.cvpp.ota.orfin.delivery.service.impl.DeliveryRuleSetServiceImpl;
import com.djt.cvpp.ota.orfin.vadrevent.repository.OrfinVadrReleaseEventRepository;
import com.djt.cvpp.ota.orfin.vadrevent.repository.impl.MockOrfinVadrReleaseEventRepositoryImpl;
import com.djt.cvpp.ota.orfin.vadrevent.service.OrfinVadrReleaseEventService;
import com.djt.cvpp.ota.orfin.vadrevent.service.impl.OrfinVadrReleaseEventServiceImpl;
import com.djt.cvpp.ota.tmc.bytestream.client.TmcByteStreamClient;
import com.djt.cvpp.ota.tmc.bytestream.client.impl.MockTmcByteStreamClientImpl;
import com.djt.cvpp.ota.tmc.deployment.client.TmcDeploymentClient;
import com.djt.cvpp.ota.tmc.deployment.client.impl.MockTmcDeploymentClientImpl;
import com.djt.cvpp.ota.tmc.deployment.simulator.TmcVehicleSimulator;
import com.djt.cvpp.ota.tmc.vehiclediscovery.client.TmcVehicleClient;
import com.djt.cvpp.ota.tmc.vehiclediscovery.client.impl.MockTmcVehicleClientImpl;
import com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle;
import com.djt.cvpp.ota.tmc.vehiclestatus.event.TmcVehicleStatusMessageEventPublisher;
import com.djt.cvpp.ota.tmc.vehiclestatus.event.impl.MockTmcVehicleStatusMessageEventPublisher;
import com.djt.cvpp.ota.vadr.client.VadrClient;
import com.djt.cvpp.ota.vadr.client.impl.MockVadrClientImpl;

public class RolloutServiceImplTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RolloutServiceImplTest.class);

	
	// ORFIN
	private OrfinVadrReleaseEventRepository orfinVadrReleaseEventRepository;
	private OrfinVadrReleaseEventService orfinVadrReleaseEventService;
	private OrfinDeliveryRuleSetEventPublisher orfinDeliveryRuleSetEventPublisher;
	private DeliveryRuleSetRepository deliveryRuleSetRepository;
	private DeliveryRuleSetService deliveryRuleSetService;

	
	// VADR
	private VadrClient vadrClient;
	
	
	// TMC
	private TmcVehicleClient tmcVehicleClient;
	private TmcByteStreamClient tmcByteStreamClient;
	private TmcDeploymentClient tmcDeploymentClient;
	
	private TmcVehicleStatusMessageEventPublisher tmcVehicleStatusMessageEventPublisher;
	
	
	// FENIX-FLARE
	private VehicleStatusMessageTemplateService vehicleStatusMessageTemplateService;
	private RolloutRepository rolloutRepository;
	private RolloutService rolloutService;
	
		
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

		rolloutRepository = new MockRolloutRepositoryImpl();
		
		vehicleStatusMessageTemplateService = new VehicleStatusMessageTemplateServiceImpl();
				
		vadrClient = new MockVadrClientImpl();
		tmcByteStreamClient = new MockTmcByteStreamClientImpl();
		tmcVehicleClient = new MockTmcVehicleClientImpl();
		tmcDeploymentClient = new MockTmcDeploymentClientImpl();
		
		// Service under test, RolloutService.  It depends on all the adapters/services defined here.
		rolloutService = new RolloutServiceImpl(
			rolloutRepository,
			vehicleStatusMessageTemplateService,
			vadrClient,
			tmcVehicleClient,
			tmcByteStreamClient,
			tmcDeploymentClient);

		
		// To simulate external event publishers. The real implementations will be the actual external systems communicating events over some event bus.
		orfinDeliveryRuleSetEventPublisher = MockOrfinDeliveryRuleSetEventPublisher.getInstance();
		tmcVehicleStatusMessageEventPublisher = MockTmcVehicleStatusMessageEventPublisher.getInstance();
		
		orfinDeliveryRuleSetEventPublisher.subscribe(this.rolloutService);
		tmcVehicleStatusMessageEventPublisher.subscribe(this.rolloutService);
	}

	@Test
	public void simulateRollout_MayHackathonDomain_10000Vehicles() throws Exception {

		// STEP 1: ARRANGE

		// Let's "spin up" 10000 virtual vehicles, based on the 2018 Mustang vehicle template
		TmcVehicle djtMustang2018VehicleTemplate = this.tmcVehicleClient.getVehicleByVin(MockTmcVehicleClientImpl.MUSTANG_2018_VIN);
		
		// 10,000 vehicles takes 6 seconds.  100,000 vehicles takes about 10 minutes (with heap size increased to 2GB)
		int numberToProvision = 10000 - 2; // There are already two vehicles created.
		this.tmcVehicleClient.provisionVirtualVehiclesFromTemplateVehicle(djtMustang2018VehicleTemplate, numberToProvision);
		
		// Let's set the expectations for vehicle behavior (the numbers must all add up to 1.0)
		double expectedPercentageSuccess  = 0.5;
		double expectedPercentageFailure  = 0.4;
		double expectedPercentageComplete = expectedPercentageSuccess + expectedPercentageFailure; 
		double expectedPercentageOffline  = 0.1;
		
		String expectedFinalRolloutState = null;
		if (expectedPercentageOffline == 0.0) {
			expectedFinalRolloutState = "FINISHED";
		} else {
			expectedFinalRolloutState = "CANCELLED";	
		}
		
		TmcVehicleSimulator.setVehicleBehaviorExpectations(
			expectedPercentageSuccess, 
			expectedPercentageFailure, 
			expectedPercentageOffline);
		
		
		String rolloutName = "rollout1";
		String initiatedBy = "tmyers28";
		String deliveryRuleSetName = "deliveryRuleSet_MayHackathon-ECG";

		
		this.rolloutService.createSoftwareUpdateRollout(rolloutName, deliveryRuleSetName);
		AbstractRollout abstractRollout = this.rolloutService.getRolloutByName(rolloutName);

		
		// We can't initiate a rollout unless we have done pre-processing (which creates the rollout tree, 
		// as well as generating manifests/signed commands, i.e. performPreProcessing() and 
		// uploading of release artifacts to TMC first, i.e. uploadReleaseArtifactsToTmcByteStream().
		com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet deliveryRuleSetDto = this.deliveryRuleSetService.getDtoMapper().mapEntityToDto(this.deliveryRuleSetService.getDeliveryRuleSetByName(deliveryRuleSetName));
		this.rolloutService.performPreProcessing(rolloutName, deliveryRuleSetDto);
		this.rolloutService.evaluateRolloutState(rolloutName);
		this.rolloutService.uploadGeneratedContentToTmcByteStream(rolloutName);
		this.rolloutService.evaluateRolloutState(rolloutName);

		

		// STEP 2: ACT
		this.rolloutService.initiateRollout(rolloutName, initiatedBy);
		
		
		// Step forward in time and let the model "play out"
		Timestamp endDate = abstractRollout.getEndDate();
		Timestamp currentDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		while (currentDate.before(endDate) || currentDate.equals(endDate)) {
			
			this.rolloutService.evaluateRolloutState(rolloutName);
			
			AbstractEntity.getTimeKeeper().setCurrentTimeInMillisForwardByOneDay();
			currentDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		}

		

		// STEP 3: ASSERT
		abstractRollout.assertValid();
		
		// For large rollouts, this becomes very time consuming
		if (numberToProvision <= 100) {
 
			String json = this.rolloutService.getJsonConverter().marshallFromEntityToJson(abstractRollout);
			LOGGER.debug(json);
		}
		
		// TODO: TDM
		// Add any method that is invoked on the entity to be on the service interface.
		// Make service extend entity service so that it has the update entity method (same goes for repository interface)
		int maxNumberActiveCampaignBatches = abstractRollout.getMaxNumberActiveCampaignBatches();
		int maxNumberVehiclesPerCampaignBatch = abstractRollout.getMaxNumberVehiclesPerCampaignBatch();
		
		int totalNumberOfCampaigns = abstractRollout.getTotalNumberOfCampaigns();
		int totalNumberofBatches = abstractRollout.getTotalNumberOfBatches();
		int totalNumberOfVehicles = abstractRollout.getTotalNumberOfVehicles();
		int totalNumberOfVehicleMessages = abstractRollout.getTotalNumberOfVehicleMessages();
		
		RolloutState rolloutState = abstractRollout.getRolloutState();
		int numberComplete = abstractRollout.getNumberComplete();
		int numberSuccessful = abstractRollout.getNumberSuccessful();
		int numberFailure = abstractRollout.getNumberFailure();
		int numberIncomplete = abstractRollout.getNumberIncomplete();
		
		double percentageComplete = abstractRollout.getPercentageComplete();
		double percentageSuccessful = abstractRollout.getPercentageSuccessful();
		double percentageFailure = abstractRollout.getPercentageFailure();
		

		LOGGER.info("maxNumberActiveCampaignBatches: {}", maxNumberActiveCampaignBatches);
		LOGGER.info("maxNumberVehiclesPerCampaignBatch: {}", maxNumberVehiclesPerCampaignBatch);
		LOGGER.info("");
		LOGGER.info("totalNumberOfCampaigns: {}", totalNumberOfCampaigns);
		LOGGER.info("totalNumberofBatches: {}", totalNumberofBatches);
		LOGGER.info("totalNumberOfVehicles: {}", totalNumberOfVehicles);
		LOGGER.info("totalNumberOfVehicleMessages: {}", totalNumberOfVehicleMessages);
		LOGGER.info("");
		LOGGER.info("rolloutState: {}", rolloutState);
		
		LOGGER.info("numberComplete: {}", numberComplete);
		LOGGER.info("numberSuccessful: {}", numberSuccessful);
		LOGGER.info("numberFailure: {}", numberFailure);
		LOGGER.info("numberIncomplete: {}", numberIncomplete);
		LOGGER.info("");
		LOGGER.info("percentageComplete: {}", percentageComplete);
		LOGGER.info("percentageSuccessful: {}", percentageSuccessful);
		LOGGER.info("percentageFailure: {}", percentageFailure);
		
		Assert.assertEquals("totalNumberOfVehicles is incorrect", Integer.toString(numberToProvision + 2), Integer.toString(totalNumberOfVehicles));
		Assert.assertEquals("numberSuccessful is incorrect", Integer.toString((int)(totalNumberOfVehicles*expectedPercentageSuccess)), Integer.toString(numberSuccessful));
		Assert.assertEquals("numberFailure is incorrect", Integer.toString((int)(totalNumberOfVehicles*expectedPercentageFailure)), Integer.toString(numberFailure));
		Assert.assertEquals("percentageComplete is incorrect", Double.toString(expectedPercentageComplete*100), Double.toString(percentageComplete));
		Assert.assertEquals("rolloutState is incorrect", expectedFinalRolloutState, rolloutState.toString());
	}
}
