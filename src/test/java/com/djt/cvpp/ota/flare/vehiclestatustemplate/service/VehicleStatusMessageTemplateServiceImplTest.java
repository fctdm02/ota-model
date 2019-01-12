package com.djt.cvpp.ota.flare.vehiclestatustemplate.service;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TestTimeKeeperImpl;
import com.djt.cvpp.ota.flare.rollout.model.Campaign;
import com.djt.cvpp.ota.flare.rollout.model.CampaignBatch;
import com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout;
import com.djt.cvpp.ota.flare.rollout.model.AbstractRollout;
import com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch;
import com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessage;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.model.VehicleStatusMessageTemplate;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.service.VehicleStatusMessageTemplateService;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.service.impl.VehicleStatusMessageTemplateServiceImpl;

public class VehicleStatusMessageTemplateServiceImplTest {

	
	private String rolloutName = "testRollout1";
	private AbstractRollout abstractRollout;
	
    private String domainName = "domainName";
    private String domainInstanceName = "domainInstanceName";
    private String domainInstanceVersion = "domainInstanceVersion";

    private String programCode = "C344N";
    private Integer modelYear = Integer.valueOf(2017);
    private String odlPayload = "";
	private Campaign campaign;

	private String campaignBatchName = "testCampaignBatch1";
	private CampaignBatch campaignBatch;
	
	private String vin = "1FA6P8TH4J5107939";
	private String tmcVehicleId = "264b362c-f185-4984-8fae-46627a4430e1";
	private String tmcVehicleObjectPayload = "";	
	private VehicleCampaignBatch vehicleCampaignBatch;

    private VehicleStatusMessageTemplate vehicleStatusMessageTemplate;
    private VehicleStatusMessage vehicleStatusMessage;

    
	private VehicleStatusMessageTemplateService vehicleStatusMessageTemplateService;


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

		String deliveryRuleSetName = "deliveryRuleSet_MayHackathon-ECG";
		abstractRollout = new SoftwareUpdateRollout(rolloutName, deliveryRuleSetName);
		
		campaign = new Campaign(
			abstractRollout, 
			domainName,
			domainInstanceName,
			domainInstanceVersion,
			programCode, 
			modelYear,
			odlPayload);		
		
		campaignBatch = new CampaignBatch(
			campaign, 
			campaignBatchName);
				
		vehicleCampaignBatch = new VehicleCampaignBatch(
			campaignBatch, 
			vin,
			tmcVehicleId,
			tmcVehicleObjectPayload);
		
		vehicleStatusMessageTemplateService = new VehicleStatusMessageTemplateServiceImpl();
	}
    
    @Test
    public void generateRolloutDataAccessorLookupCode_WhenOtamS1001_ShouldHaveValidResponse() throws EntityDoesNotExistException {

        // STEP 1: ARRANGE
        String vehicleStatusMessageExpression = "OTAM_S1001_1_2_720";
        String expectedRolloutDataAccessorLookupCode = "OTAM_S1001";
        vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);


        // STEP 2: ACT
        String actualRolloutDataAccessorLookupCode = vehicleStatusMessageTemplate.generateRolloutDataAccessorLookupCode();


        // STEP 3: ASSERT
        Assert.assertEquals(expectedRolloutDataAccessorLookupCode, actualRolloutDataAccessorLookupCode);
    }

    @Test
    public void generateAdditionalParametersMessage_WhenOtamS1015NoAdditionalParameters_ShouldHaveValidResponse() throws EntityDoesNotExistException {

        // STEP 1: ARRANGE
        String vehicleStatusMessageExpression = "OTAM_S1015";
        String expectedAdditionalParametersMessage = "";
        vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);


        // STEP 2: ACT
        vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
        String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();


        // STEP 3: ASSERT
        Assert.assertEquals(expectedAdditionalParametersMessage, actualAdditionalParametersMessage);
    }

    @Test
    public void generateAdditionalParametersMessage_WhenOtamS1001NonRepeatingAdditionalParametersOnly_ShouldHaveValidResponse() throws EntityDoesNotExistException {

        // STEP 1: ARRANGE
        String vehicleStatusMessageExpression = "OTAM_S1001_1_2_720";
        String expectedAdditionalParametersMessage = "triggerType: 1 | authorizationLevel: 2 | expirationTimeinHours: 720";
        vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);


        // STEP 2: ACT
        vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
        String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();


        // STEP 3: ASSERT
        Assert.assertEquals(expectedAdditionalParametersMessage, actualAdditionalParametersMessage);
    }

    @Test
    public void generateAdditionalParametersMessage_WhenOtamS1004RepeatingAdditionalParametersOnly_ShouldHaveValidResponse() throws EntityDoesNotExistException {

        // STEP 1: ARRANGE
        String vehicleStatusMessageExpression = "OTAM_S1004_SYNC_fmel.tar.gz_14G43-23495-AA_ABS_efgh.vbf_22222-22222-BB";
        String expectedAdditionalParametersMessage = "ecu1: SYNC | filename1: fmel.tar.gz | softwarePartNumberOrSoftwareVersion1: 14G43-23495-AA | ecu2: ABS | filename2: efgh.vbf | softwarePartNumberOrSoftwareVersion2: 22222-22222-BB";
        vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);


        // STEP 2: ACT
        vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
        String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();


        // STEP 3: ASSERT
        Assert.assertEquals(expectedAdditionalParametersMessage, actualAdditionalParametersMessage);
    }

    @Test
    public void generateAdditionalParametersMessage_WhenOtamS1006RepeatingAdditionalParametersOnly_ShouldHaveValidResponse() throws EntityDoesNotExistException{

        // STEP 1: ARRANGE
    	// OTAM,S,1006,(str_num)+,(filename!N!_fileLength!N!InBytes)+,OTAM_S1006_abcd.vbf_2032_fmel.tar.gz_23498239522
        String vehicleStatusMessageExpression = "OTAM_S1006_abcd.vbf_2032_fmel.tar.gz_23498239522";
        String expectedAdditionalParametersMessage = "filename1: abcd.vbf | fileLength1InBytes: 2032 | filename2: fmel.tar.gz | fileLength2InBytes: 23498239522";
        vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);


        // STEP 2: ACT
        vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
        String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();


        // STEP 3: ASSERT
        Assert.assertEquals(expectedAdditionalParametersMessage, actualAdditionalParametersMessage);
    }
    
    @Test
    public void generateAdditionalParametersMessage_WhenOtamE1008BothRepeatingAndNonRepeatingAdditionalParameters_ShouldHaveValidResponse() throws EntityDoesNotExistException {

        // STEP 1: ARRANGE
    	// OTAM,E,1008,num_(str)+,reasonCode_(ecu!N!)+,OTAM_E1008_3_ABS_CLUSTER
        String vehicleStatusMessageExpression = "OTAM_E1008_3_ABS_CLUSTER";
        String expectedAdditionalParametersMessage = "reasonCode: 3 | ecu1: ABS | ecu2: CLUSTER";
        vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);


        // STEP 2: ACT
        vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
        String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();


        // STEP 3: ASSERT
        Assert.assertEquals(expectedAdditionalParametersMessage, actualAdditionalParametersMessage);
    }

    @Test
    public void buildVehicleStatusMessage_WhenGivenInvalidNonRepeatingAdditionalParametersOnly_ShouldThrowException() {

        // STEP 1: ARRANGE
        String expectedErrorMessage = "CORE-1000: additionalParametersDataValue: [1_2_720_DUMMY] size mismatch.  Expected an underscore delimited string in multiples of: [3] but was: [4].";
        String vehicleStatusMessageExpression = "OTAM_S1001_1_2_720_DUMMY";
        try {


            // STEP 2: ACT
        	vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);        	
            vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
            String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();
            fail("Expected exception not thrown, actualAdditionalParametersMessage was: " + actualAdditionalParametersMessage);

        } catch (Exception e) {

            // STEP 3: ASSERT
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
        }
    }

    @Test
    public void buildVehicleStatusMessage_WhenGivenInvalidRepeatingAdditionalParametersOnly_ShouldThrowException() {

        // STEP 1: ARRANGE
    	// OTAM,E,1007,(str_num)+,(filename!N!_mismatchTypeCode!N!)+,OTAM_E1007_ads-34.tar.gz_3_e2d.vbf_3
        String expectedErrorMessage = "CORE-1000: additionalParametersDataValue: [ads-34.tar.gz_3_e2d.vbf_3_DUMMY] size mismatch.  Expected an underscore delimited string in multiples of: [2] but was: [5].";
        String vehicleStatusMessageExpression = "OTAM_E1007_ads-34.tar.gz_3_e2d.vbf_3_DUMMY";
        try {


            // STEP 2: ACT
        	vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);
            vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
            String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();
            fail("Expected exception not thrown, actualAdditionalParametersMessage was: " + actualAdditionalParametersMessage);

        } catch (Exception e) {

            // STEP 3: ASSERT
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
        }
    }

    @Test
    public void buildVehicleStatusMessage_WhenGivenInvalidDataTypeLongInMessageExpression_ShouldThrowException() {

        // STEP 1: ARRANGE
        String expectedErrorMessage = "CORE-1000: Invalid data type encountered specified for data value; [DUMMY].  Expected a value of type: [num]";
        String vehicleStatusMessageExpression = "OTAM_E1008_DUMMY_ABS_CLUSTER";
        try {


            // STEP 2: ACT
        	vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);        	
            vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
            String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();
            fail("Expected exception not thrown, actualAdditionalParametersMessage was: " + actualAdditionalParametersMessage);

        } catch (Exception e) {

            // STEP 3: ASSERT
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
        }
    }

    @Test
    public void buildVehicleStatusMessage_WhenGivenInvalidDataTypeBooleanInMessageExpression_ShouldThrowException() {

        // STEP 1: ARRANGE
        String expectedErrorMessage = "CORE-1000: Invalid data type encountered specified for data value; [DUMMY].  Expected a value of type: [bool]";
        String vehicleStatusMessageExpression = "OTAM_S1002_2_DUMMY";
        try {


            // STEP 2: ACT
        	vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);        	
            vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
            String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();
            fail("Expected exception not thrown, actualAdditionalParametersMessage was: " + actualAdditionalParametersMessage);

        } catch (Exception e) {

            // STEP 3: ASSERT
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
        }
    }

    @Test
    public void buildVehicleStatusMessage_WhenGivenValidDataTypeBooleanInMessageExpression_ShouldHaveValidResponse() throws EntityDoesNotExistException {

        // STEP 1: ARRANGE
        String vehicleStatusMessageExpression = "OTAM_S1002_2_false";
        String expectedRolloutDataAccessorLookupCode = "OTAM_S1002";
        String expectedAdditionalParametersMessage = "consentResourceCode: 2 | result: false";


        // STEP 2: ACT
    	vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);
        vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
        

        // STEP 3: ASSERT
        String actualRolloutDataAccessorLookupCode = vehicleStatusMessage.generateRolloutDataAccessorLookupCode();
        String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();
        Assert.assertEquals(expectedRolloutDataAccessorLookupCode, actualRolloutDataAccessorLookupCode);
        Assert.assertEquals(expectedAdditionalParametersMessage, actualAdditionalParametersMessage);
    }

    @Test
    public void buildVehicleStatusMessage_WhenGivenOldMessageExpression_ShouldThrowException() {

        // STEP 1: ARRANGE
        String expectedErrorMessage = "CORE-0003: VehicleStatusMessageTemplate for expression: [OTAM_S0003] not found";
        String vehicleStatusMessageExpression = "OTAM_S0003";
        try {


            // STEP 2: ACT
        	vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);        	
            vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
            String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();
            fail("Expected exception not thrown, actualAdditionalParametersMessage was: " + actualAdditionalParametersMessage);

        } catch (Exception e) {


            // STEP 3: ASSERT
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
        }
    }

    @Test
    public void generateAdditionalParametersMessage_WhenSunnyDayAndLookupCodeIs0000_ShouldHaveValidResponse() throws EntityDoesNotExistException {

        // STEP 1: ARRANGE
        String vehicleStatusMessageExpression = "OTAM_S0000_'OTAM Happy Path Engineering Message Goes Here'";
        String expectedAdditionalParametersMessage = "'OTAM Happy Path Engineering Message Goes Here'";
        vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);


        // STEP 2: ACT
        vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
        String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();


        // STEP 3: ASSERT
        Assert.assertEquals(expectedAdditionalParametersMessage, actualAdditionalParametersMessage);
    }

    @Test
    public void buildVehicleStatusMessage_WhenGivenNullMessageExpression_ShouldThrowException() {

        // STEP 1: ARRANGE
        String expectedErrorMessage = "CORE-1000: vehicleStatusMessageExpression must be specified.";
        String vehicleStatusMessageExpression = null;
        try {


            // STEP 2: ACT
        	vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);
            vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
            String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();
            fail("Expected exception not thrown, actualAdditionalParametersMessage was: " + actualAdditionalParametersMessage);

        } catch (Exception e) {


            // STEP 3: ASSERT
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
        }
    }

    @Test
    public void buildVehicleStatusMessage_WhenGivenInvalidMessageExpression_ShouldThrowException() {

        // STEP 1: ARRANGE
        String expectedErrorMessage = "CORE-0003: VehicleStatusMessageTemplate for expression: [DUMMY] not found";
        String vehicleStatusMessageExpression = "DUMMY";
        try {


            // STEP 2: ACT
        	vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);
            vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
            String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();
            fail("Expected exception not thrown, actualAdditionalParametersMessage was: " + actualAdditionalParametersMessage);

        } catch (Exception e) {


            // STEP 3: ASSERT
            String actualErrorMessage = e.getMessage();
            Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
        }
    }

    @Test
    public void buildVehicleStatusMessage_WhenGivenInvalidMessagePrefix_ShouldThrowException() {

        // STEP 1: ARRANGE
        String expectedErrorMessage = "CORE-0003: VehicleStatusMessageTemplate for expression: [OTAM_X1002_2_false] not found";
        String vehicleStatusMessageExpression = "OTAM_X1002_2_false";
        try {


            // STEP 2: ACT
        	vehicleStatusMessageTemplate = vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);
            vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
            String actualAdditionalParametersMessage = vehicleStatusMessage.generateLabeledAdditionalParameters();
            fail("Expected exception not thrown, actualAdditionalParametersMessage was: " + actualAdditionalParametersMessage);

        } catch (Exception e) {


            // STEP 3: ASSERT
            String actualErrorMessage = e.getMessage();
            Assert.assertTrue(actualErrorMessage.startsWith(expectedErrorMessage));
        }
    }
}
