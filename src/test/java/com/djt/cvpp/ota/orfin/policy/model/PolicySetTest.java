/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.policy.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.orfin.policy.mapper.PolicySetJsonConverter;
import com.djt.cvpp.ota.testutil.OrfinPolicySetTestHarness;

public class PolicySetTest {

	private PolicySet policySet;
	private OrfinPolicySetTestHarness orfinPolicySetTestHarness = new OrfinPolicySetTestHarness();
	private PolicySetJsonConverter policySetJsonConverter = new PolicySetJsonConverter();

	@BeforeClass
	public static void beforeClass() {

		String env = System.getProperty(TimeKeeper.ENV);
		if (env == null) {
			System.setProperty(TimeKeeper.ENV, TimeKeeper.TEST);
		}

		AbstractEntity.setTimeKeeper(new TestTimeKeeperImpl(TestTimeKeeperImpl.DEFAULT_TEST_EPOCH_MILLIS_01_01_2020));
	}

	@Test
	public void buildGlobalPolicySet() throws Exception {

		// STEP 1: ARRANGE
		policySet = orfinPolicySetTestHarness.buildGlobalPolicySet();


		// STEP 2: ACT
		String json = policySetJsonConverter.marshallFromEntityToJson(policySet);


		// STEP 3: ASSERT
		Assert.assertNotNull("json is null", json);
		System.out.println(json);
		policySet = policySetJsonConverter.unmarshallFromJsonToEntity(json);
		policySet.assertValid();
	}
	
	@Test
	public void buildPolicySet() throws Exception {

		// STEP 1: ARRANGE
		policySet = orfinPolicySetTestHarness.buildPolicySet();


		// STEP 2: ACT
		String json = policySetJsonConverter.marshallFromEntityToJson(policySet);


		// STEP 3: ASSERT
		Assert.assertNotNull("json is null", json);
		System.out.println(json);
	}
	
	@Test
	public void marshallAndUnMarshallPolicySet() throws Exception {

		// STEP 1: ARRANGE
		policySet = orfinPolicySetTestHarness.buildPolicySet();


		// STEP 2: ACT
		String json = policySetJsonConverter.marshallFromEntityToJson(policySet);


		// STEP 3: ASSERT
		Assert.assertNotNull("json is null", json);
		policySet = policySetJsonConverter.unmarshallFromJsonToEntity(json);
		policySet.assertValid();
	}
}
