/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.odl.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.orfin.odl.mapper.OdlJsonConverter;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.testutil.OrfinOdlTestHarness;

public class OdlTest {

	private Odl odl;
	private OrfinOdlTestHarness orfinOdlTestHarness = new OrfinOdlTestHarness();
	private OdlJsonConverter odlJsonConverter = new OdlJsonConverter();

	@BeforeClass
	public static void beforeClass() {

		String env = System.getProperty(TimeKeeper.ENV);
		if (env == null) {
			System.setProperty(TimeKeeper.ENV, TimeKeeper.TEST);
		}

		AbstractEntity.setTimeKeeper(new TestTimeKeeperImpl(TestTimeKeeperImpl.DEFAULT_TEST_EPOCH_MILLIS_01_01_2020));
	}
	
	@Test
	public void buildOdl() throws Exception {

		// STEP 1: ARRANGE
		odl = orfinOdlTestHarness.buildOdl();


		// STEP 2: ACT
		String json = odlJsonConverter.marshallFromEntityToJson(odl);


		// STEP 3: ASSERT
		Assert.assertNotNull("json is null", json);
		System.out.println(json);
	}
}
