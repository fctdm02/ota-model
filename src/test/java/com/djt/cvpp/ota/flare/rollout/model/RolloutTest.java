/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.flare.rollout.model;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.flare.rollout.model.AbstractRollout;
import com.djt.cvpp.ota.testutil.FlareRolloutTestHarness;

public class RolloutTest {

	private FlareRolloutTestHarness flareRolloutTestHarness = new FlareRolloutTestHarness();
	
	private AbstractRollout abstractRollout;
		
	@BeforeClass
	public static void beforeClass() {

		String env = System.getProperty(TimeKeeper.ENV);
		if (env == null) {
			System.setProperty(TimeKeeper.ENV, TimeKeeper.TEST);
		}

		AbstractEntity.setTimeKeeper(new TestTimeKeeperImpl(TestTimeKeeperImpl.DEFAULT_TEST_EPOCH_MILLIS_01_01_2020));
	}
	
	@Test
	public void assertValid_sunnyDay() throws Exception {

		// STEP 1: ARRANGE
		abstractRollout = flareRolloutTestHarness.buildRollout();


		// STEP 2: ACT
		List<String> validationMessages = abstractRollout.validate();
		
		
		
		// STEP 3: ASSERT
		Assert.assertNotNull("validationMessages is null", validationMessages);
		Assert.assertEquals("validationMessages is incorrect", "[SoftwareUpdateRollout[rollout1]:  tmcVehicleDiscoveryQuery must be specified., At least one Campaign must be specified.]", validationMessages.toString());
	}
}
