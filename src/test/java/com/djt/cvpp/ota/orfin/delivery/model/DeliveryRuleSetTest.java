/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.delivery.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.orfin.delivery.mapper.DeliveryRuleSetJsonConverter;
import com.djt.cvpp.ota.testutil.OrfinDeliveryRuleSetTestHarness;

public class DeliveryRuleSetTest {

	private OrfinDeliveryRuleSetTestHarness orfinDeliveryRuleSetTestHarness = new OrfinDeliveryRuleSetTestHarness();
	private DeliveryRuleSetJsonConverter deliveryRuleSetJsonConverter = new DeliveryRuleSetJsonConverter();

	@BeforeClass
	public static void beforeClass() {

		String env = System.getProperty(TimeKeeper.ENV);
		if (env == null) {
			System.setProperty(TimeKeeper.ENV, TimeKeeper.TEST);
		}

		AbstractEntity.setTimeKeeper(new TestTimeKeeperImpl(TestTimeKeeperImpl.DEFAULT_TEST_EPOCH_MILLIS_01_01_2020));
	}
	
	@Test
	public void buildDeliveryRuleSet() throws Exception {

		// STEP 1: ARRANGE
		DeliveryRuleSet deliveryRuleSet = orfinDeliveryRuleSetTestHarness.buildDeliveryRuleSet();


		// STEP 2: ACT
		String json = deliveryRuleSetJsonConverter.marshallFromEntityToJson(deliveryRuleSet);


		// STEP 3: ASSERT
		Assert.assertNotNull("json is null", json);
		System.out.println(json);
	}
}
