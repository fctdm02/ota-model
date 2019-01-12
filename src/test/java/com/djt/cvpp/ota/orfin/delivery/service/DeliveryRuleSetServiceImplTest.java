/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.delivery.service;

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
import com.djt.cvpp.ota.orfin.delivery.event.impl.MockOrfinDeliveryRuleSetEventPublisher;
import com.djt.cvpp.ota.orfin.delivery.event.impl.MockOrfinDeliveryRuleSetEventSubscriberImpl;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.delivery.model.enums.ConsentType;
import com.djt.cvpp.ota.orfin.delivery.repository.DeliveryRuleSetRepository;
import com.djt.cvpp.ota.orfin.delivery.repository.impl.MockDeliveryRuleSetRepositoryImpl;
import com.djt.cvpp.ota.orfin.delivery.service.impl.DeliveryRuleSetServiceImpl;
import com.djt.cvpp.ota.orfin.vadrevent.repository.OrfinVadrReleaseEventRepository;
import com.djt.cvpp.ota.orfin.vadrevent.repository.impl.MockOrfinVadrReleaseEventRepositoryImpl;
import com.djt.cvpp.ota.orfin.vadrevent.service.OrfinVadrReleaseEventService;
import com.djt.cvpp.ota.orfin.vadrevent.service.impl.OrfinVadrReleaseEventServiceImpl;

public class DeliveryRuleSetServiceImplTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryRuleSetServiceImplTest.class);

	
	// Aggregate root for service under test
	private DeliveryRuleSet deliveryRuleSet;

	
	// Service under test
	private OrfinVadrReleaseEventRepository orfinVadrReleaseEventRepository;
	private OrfinVadrReleaseEventService orfinVadrReleaseEventService;
	private MockOrfinDeliveryRuleSetEventSubscriberImpl orfinDeliveryRuleSetEventSubscriber;
	private MockOrfinDeliveryRuleSetEventPublisher orfinDeliveryRuleSetEventPublisher;
	private DeliveryRuleSetRepository deliveryRuleSetRepository;
	private DeliveryRuleSetService deliveryRuleSetService;
	
		
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
		
		this.orfinDeliveryRuleSetEventSubscriber = MockOrfinDeliveryRuleSetEventSubscriberImpl.getInstance();
		this.orfinDeliveryRuleSetEventSubscriber.clearEvents();
		this.orfinDeliveryRuleSetEventPublisher = MockOrfinDeliveryRuleSetEventPublisher.getInstance();
		this.orfinDeliveryRuleSetEventPublisher.clearSubscribers();
		this.deliveryRuleSetRepository = MockDeliveryRuleSetRepositoryImpl.getInstance();
		this.deliveryRuleSetRepository.reset();
		this.deliveryRuleSetService = new DeliveryRuleSetServiceImpl(
			this.deliveryRuleSetRepository,
			this.orfinVadrReleaseEventService,
			this.orfinDeliveryRuleSetEventPublisher);
	}
	
	@Test
	public void createDefaultDeliveryRuleSet() throws Exception {

		// STEP 1: ARRANGE
		String deliveryRuleSetName = DeliveryRuleSet.DEFAULT_DELIVERY_RULE_SET_NAME;
		String authorizedBy = "authorized_by";
		String messageToConsumer = "message to consumer";
		ConsentType consentType = ConsentType.SAFETY_UPDATE_NOTICE;
		Timestamp scheduledRolloutDate = AbstractEntity.getTimeKeeper().getTimestampForDaysFromCurrent(30);

		
		// STEP 2: ACT
		this.deliveryRuleSet = this.deliveryRuleSetService.createDeliveryRuleSet(
			deliveryRuleSetName, 
			authorizedBy, 
			messageToConsumer, 
			consentType, 
			scheduledRolloutDate);
		

		// STEP 3: ASSERT
		Assert.assertNotNull("deliveryRuleSet is null", this.deliveryRuleSet);
		
		Assert.assertEquals("deliveryRuleSetName is incorrect", deliveryRuleSetName, deliveryRuleSet.getDeliveryRuleSetName());
		Assert.assertEquals("authorizedBy is incorrect", authorizedBy, deliveryRuleSet.getAuthorizedBy());
		Assert.assertEquals("messageToConsumer is incorrect", messageToConsumer, deliveryRuleSet.getMessageToConsumer());
		Assert.assertEquals("consentType is incorrect", consentType, deliveryRuleSet.getConsentType());
		Assert.assertEquals("scheduledRolloutDate is incorrect", scheduledRolloutDate, deliveryRuleSet.getScheduledRolloutDate());
		
		String json = this.deliveryRuleSetService.getJsonConverter().marshallFromEntityToJson(this.deliveryRuleSet);
		LOGGER.info(json);
	}
}
