/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.vehiclestatustemplate.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.model.VehicleStatusMessageTemplate;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.repository.impl.ClasspathVehicleStatusMessageTemplateRepositoryImpl;

/**
 * @author tmyers28 (Tom Myers)
 *
 * The following are the various permutations of VehicleStatusMessages (with respect to Additional Parameters)
 * 1. No additional parameters
 *
 *
 * 2. One Non-Repeating only
 *      a. normal data label expression (matches cardinality of data type expression)
 *      OTAM,S,1001,num_num_num,triggerType_authorizationLevel_expirationTimeinHours,OTAM_S1001_1_2_720
 *
 *      b. BLANK-LABEL data label expression (only one string for additional parameters and there is no label)
 *
 *
 * 3. One Repeating only
 *
 *
 * 4. One Non-repeating followed by one repeating
 *
 */
public class ClasspathVehicleStatusMessageTemplateRepositoryImplTest {
	
	private ClasspathVehicleStatusMessageTemplateRepositoryImpl classpathVehicleStatusMessageTemplateRepositoryImpl;
	private VehicleStatusMessageTemplate vehicleStatusMessageTemplate;
	private String otaFunction;
	private String prefix;
	private String lookupCode;    	
		
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

		classpathVehicleStatusMessageTemplateRepositoryImpl = new ClasspathVehicleStatusMessageTemplateRepositoryImpl();
		vehicleStatusMessageTemplate = null;
	}
	
	
    @Test
    public void testGetVehicleStatusMessageTemplate_firstVehicleStatusMessageTemplate() throws EntityDoesNotExistException {
    	
		// STEP 1: ARRANGE
    	// OTAM,S,1001,num_num_num,triggerType_authorizationLevel_expirationTimeinHours,OTAM_S1001_1_2_720
    	otaFunction = "OTAM";
    	prefix = "S";
    	lookupCode = "1001";    	


		// STEP 2: ACT
    	vehicleStatusMessageTemplate = classpathVehicleStatusMessageTemplateRepositoryImpl.getVehicleStatusMessageTemplate(otaFunction, prefix, lookupCode);
    	

		// STEP 3: ASSERT
    	Assert.assertNotNull("vehicleStatusMessageTemplate is null", vehicleStatusMessageTemplate);
    }	
}
