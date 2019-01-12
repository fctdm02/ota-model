/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.client;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.tmc.vehiclediscovery.client.TmcVehicleClient;
import com.djt.cvpp.ota.tmc.vehiclediscovery.client.impl.MockTmcVehicleClientImpl;
import com.djt.cvpp.ota.tmc.vehiclediscovery.exception.TmcResourceException;
import com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle;

public class TmcVehicleClientTest {

	private TmcVehicleClient tmcVehicleClient;
	
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

		this.tmcVehicleClient = new MockTmcVehicleClientImpl();
	}
	
	@Test
	public void pushDeploymentToTmc() throws EntityDoesNotExistException, ValidationException {
		
		// STEP 1: ARRANGE

		
		// STEP 2: ACT


		// STEP 3: ASSERT
		
	}

	@Test
	public void getAllVehicles() throws EntityDoesNotExistException, ValidationException, TmcResourceException {

		List<TmcVehicle> vehicles = tmcVehicleClient.getAllVehicles();
		Assert.assertNotNull("vehicles is null", vehicles);
	}

	@Test
	public void createTmcReleaseArtifactRequestJsonPayload() {
		
	}
	
	@Test
	public void createTmcTargetBundleRequestJsonPayload() {
		
	}

	@Test
	public void createTmcReleaseRequestJsonPayload() {
		
	}
	
	@Test
	public void createTmcDeploymentRequestJsonPayload() {
		
	}
}
