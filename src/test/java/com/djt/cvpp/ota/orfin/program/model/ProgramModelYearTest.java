/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.program.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.orfin.program.mapper.ProgramModelYearJsonConverter;
import com.djt.cvpp.ota.testutil.ProgramModelYearTestHarness;

public class ProgramModelYearTest {

	private ProgramModelYear programModelYear;
	private ProgramModelYearTestHarness programModelYearTestHarness = new ProgramModelYearTestHarness();
	private ProgramModelYearJsonConverter programModelYearJsonConverter = new ProgramModelYearJsonConverter();

	@BeforeClass
	public static void beforeClass() {

		String env = System.getProperty(TimeKeeper.ENV);
		if (env == null) {
			System.setProperty(TimeKeeper.ENV, TimeKeeper.TEST);
		}

		AbstractEntity.setTimeKeeper(new TestTimeKeeperImpl(TestTimeKeeperImpl.DEFAULT_TEST_EPOCH_MILLIS_01_01_2020));
	}
	
	@Test
	public void buildProgramModelYear() throws Exception {

		// STEP 1: ARRANGE
		programModelYear = programModelYearTestHarness.buildProgramModelYear();


		// STEP 2: ACT
		String json = programModelYearJsonConverter.marshallFromEntityToJson(programModelYear);


		// STEP 3: ASSERT
		Assert.assertNotNull("json is null", json);
		System.out.println(json);
	}
}
