/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.client;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.testutil.VadrTestHarness;
import com.djt.cvpp.ota.tmc.vehiclediscovery.client.TmcVehicleClient;
import com.djt.cvpp.ota.tmc.vehiclediscovery.client.impl.MockTmcVehicleClientImpl;
import com.djt.cvpp.ota.tmc.vehiclediscovery.exception.TmcResourceException;
import com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle;
import com.djt.cvpp.ota.vadr.client.impl.MockVadrClientImpl;
import com.djt.cvpp.ota.vadr.exception.VadrException;
import com.djt.cvpp.ota.vadr.model.Domain;
import com.djt.cvpp.ota.vadr.model.DomainInstance;
import com.djt.cvpp.ota.vadr.model.enums.ProductionState;
import com.djt.cvpp.ota.vadr.model.enums.ReleaseState;

public class VadrClientTest {

	private VadrClient vadrClient;
	private Domain domain;
	private TmcVehicleClient tmcVehicleClient = new MockTmcVehicleClientImpl();

	@BeforeClass
	public static void beforeClass() {

		String env = System.getProperty(TimeKeeper.ENV);
		if (env == null) {
			System.setProperty(TimeKeeper.ENV, TimeKeeper.TEST);
		}

		AbstractEntity.setTimeKeeper(new TestTimeKeeperImpl(TestTimeKeeperImpl.DEFAULT_TEST_EPOCH_MILLIS_01_01_2020));
	}
	
	@Before
	public void before() throws Exception {

		this.vadrClient = new MockVadrClientImpl();
	}
	
	@Test
	public void testVehicleDiscovery() throws EntityDoesNotExistException, ValidationException, VadrException, TmcResourceException {
	
		// STEP 1: ARRANGE
		String targetDomainName = "MayHackathon-ECG";
		String targetDomainInstanceName = "ECG_Hack_02";
		String targetDomainInstanceVersion = "01.01.02";
		Integer numberOfPriorDomainInstancesToRetrieve = Integer.valueOf(3);
		String productionState = ProductionState.PRODUCTION.toString();
		String releaseState = ReleaseState.RELEASED.toString();
		Domain domain = vadrClient.retrieveDomainInstanceLineage(
			targetDomainName, 
			targetDomainInstanceName, 
			targetDomainInstanceVersion,
			numberOfPriorDomainInstancesToRetrieve,
			productionState,
			releaseState);
		
		// TODO: TDM: Need to have notion of "predecessor" domain instances and only do vehicle discovery on these "prior"
		// versions, as there is no need to query the target version, as those vehicles are up-to-date and nothing need be done.
		DomainInstance domainInstance = domain.getDomainInstance(targetDomainInstanceName, targetDomainInstanceVersion);

		// Inclusions take the form of: XXXX_YYYY where XXXX is the programCode and modelYear is the 4 digit model year
		Set<String> programCodeModelYearInclusions = new HashSet<>();
		
		

		// STEP 2: ACT
		List<TmcVehicle> qualifiedVehicleList = tmcVehicleClient.performVehicleDiscovery(programCodeModelYearInclusions, domainInstance);
		


		// STEP 3: ASSERT
		Assert.assertNotNull("qualifiedVehicleList is null", qualifiedVehicleList);
		Assert.assertEquals("qualifiedVehicleList size is incorrect", "2", Integer.toString(qualifiedVehicleList.size()));
	}
	
	@Test
	public void unmarshallFromJson_vadrMayHackathonData() throws Exception {

		// STEP 1: ARRANGE
		String targetDomainName = "MayHackathon-ECG";
		String targetDomainInstanceName = "ECG_Hack_02";
		String targetDomainInstanceVersion = "01.01.02";
		Integer numberOfPriorDomainInstancesToRetrieve = Integer.valueOf(3);
		String productionState = ProductionState.PRODUCTION.toString();
		String releaseState = ReleaseState.RELEASED.toString();


		// STEP 2: ACT
		domain = vadrClient.retrieveDomainInstanceLineage(
			targetDomainName, 
			targetDomainInstanceName, 
			targetDomainInstanceVersion,
			numberOfPriorDomainInstancesToRetrieve,
			productionState,
			releaseState);


		// STEP 3: ASSERT
		Assert.assertNotNull("domain is null", domain);
		String json2 = this.vadrClient.getDomainJsonConverter().marshallFromEntityToJson(domain);
		Assert.assertNotNull("json2 is null", json2);
		System.out.println(json2);
	}

	@Test
	public void unmarshallFromJson_VadrInfotainmentData() throws Exception {

		// STEP 1: ARRANGE
		String targetDomainName = "Infotainment";
		String targetDomainInstanceName = "Yoda";
		String targetDomainInstanceVersion = "01.01.00";
		Integer numberOfPriorDomainInstancesToRetrieve = Integer.valueOf(3);
		String productionState = ProductionState.PRODUCTION.toString();
		String releaseState = ReleaseState.RELEASED.toString();


		// STEP 2: ACT
		domain = vadrClient.retrieveDomainInstanceLineage(
			targetDomainName, 
			targetDomainInstanceName, 
			targetDomainInstanceVersion,
			numberOfPriorDomainInstancesToRetrieve,
			productionState,
			releaseState);


		// STEP 3: ASSERT
		Assert.assertNotNull("domain is null", domain);
		String json2 = this.vadrClient.getDomainJsonConverter().marshallFromEntityToJson(domain);
		Assert.assertNotNull("json2 is null", json2);
		System.out.println(json2);
	}	
	
    @Test
	public void marshallToJson_testData() throws IOException, ValidationException, EntityAlreadyExistsException {

    	// STEP 1: ARRANGE
		int numberOfDomainInstancesToBuild = 2;
		int numberOfEcuHardwaresPerDomainInstanceToBuild = 2;
		int numberOfEcuSoftwaresPerDomainInstanceToBuild = 2;
		int numberOfApplicationsPerDomainInstanceToBuild = 2;


		// STEP 2: ACT
		domain = new VadrTestHarness().buildDomain(
			numberOfDomainInstancesToBuild,
			numberOfEcuHardwaresPerDomainInstanceToBuild,
			numberOfEcuSoftwaresPerDomainInstanceToBuild,
			numberOfApplicationsPerDomainInstanceToBuild);


		// STEP 3: ASSERT
		String json = this.vadrClient.getDomainJsonConverter().marshallFromEntityToJson(domain);
		Assert.assertNotNull("json is null", json);
		System.out.println(json);
		
		
		Domain unmarshalledDomain = this.vadrClient.getDomainJsonConverter().unmarshallFromJsonToEntity(json);
		com.djt.cvpp.ota.vadr.mapper.dto.Domain unmarshalledDomainDto = this.vadrClient.getDomainDtoMapper().mapEntityToDto(unmarshalledDomain);
		Assert.assertNotNull("unmarshalledDomainDto is null", unmarshalledDomainDto);
		
		ObjectMapper mapper = new ObjectMapper();
		String json2 = mapper.writeValueAsString(unmarshalledDomainDto);
		System.out.println(json2);
		Assert.assertEquals("First JSON is not equal to second JSON", json, json2);
	}
}
