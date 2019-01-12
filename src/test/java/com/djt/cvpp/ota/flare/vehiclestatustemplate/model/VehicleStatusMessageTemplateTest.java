package com.djt.cvpp.ota.flare.vehiclestatustemplate.model;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.djt.cvpp.ota.flare.rollout.model.AbstractRollout;
import com.djt.cvpp.ota.flare.rollout.model.Campaign;
import com.djt.cvpp.ota.flare.rollout.model.CampaignBatch;
import com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout;
import com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch;

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
public class VehicleStatusMessageTemplateTest {
	
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
	
	@Before
	public void setup() {
		
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
	}

    @Test
    public void constructor_mismatchBetweenDataTypeAndLabelExpression() {

        // STEP 1: ARRANGE
        String otaFunction = "OTAM";
        String prefix = "S";
        String lookupCode = "1001";
        String additionalParametersDataTypeExpression = "num_num_num";
        String additionalParametersLabelExpression = "XXX_YYY";
        String additionalParametersDataValue = "1_2_720";
        String exampleVehicleStatusMessage = otaFunction + "_" + prefix + lookupCode + "_" + additionalParametersDataValue; 
        
        VehicleStatusMessageTemplate vehicleStatusMessageTemplate = new VehicleStatusMessageTemplate(
            	otaFunction,
                prefix,
                lookupCode,
                additionalParametersDataTypeExpression,
                additionalParametersLabelExpression,
                exampleVehicleStatusMessage);

        
        try {


        	
            // STEP 2: ACT
        	vehicleStatusMessageTemplate.buildVehicleStatusMessage(
        		vehicleCampaignBatch, 
        		exampleVehicleStatusMessage);
            fail("Expected exception not thrown");
            


        } catch (Exception e) {


            // STEP 3: ASSERT
            Assert.assertEquals("CORE-1000: dataTypeExpression: [num_num_num] does not match dataLabelExpression: [XXX_YYY] in terms of underscore separated values.", e.getMessage());
        }
    }

    @Test
    public void generateAdditionalParametersMessage_mismatchBetweenDataTypeAndDataValueExpression() {

        // STEP 1: ARRANGE
        String otaFunction = "OTAM";
        String prefix = "S";
        String lookupCode = "1001";
        String additionalParametersDataTypeExpression = "num_num_num";
        String additionalParametersLabelExpression = "triggerType_authorizationLevel_expirationTimeinHours";
        String additionalParametersDataValue = "1_2";
        String exampleVehicleStatusMessage = otaFunction + "_" + prefix + lookupCode + "_" + additionalParametersDataValue;
        
        VehicleStatusMessageTemplate vehicleStatusMessageTemplate = new VehicleStatusMessageTemplate(
            	otaFunction,
                prefix,
                lookupCode,
                additionalParametersDataTypeExpression,
                additionalParametersLabelExpression,
                exampleVehicleStatusMessage);

        try {


            // STEP 2: ACT
        	vehicleStatusMessageTemplate.buildVehicleStatusMessage(
            		vehicleCampaignBatch, 
            		exampleVehicleStatusMessage);
        	
            //vehicleStatusMessageTemplate.generateAdditionalParametersMessage();
            fail("Expected exception not thrown");


        } catch (Exception e) {


            // STEP 3: ASSERT
            Assert.assertEquals("CORE-1000: additionalParametersDataValue: [1_2] size mismatch.  Expected an underscore delimited string in multiples of: [3] but was: [2].", e.getMessage());
        }
    }

    @Test
    public void constructor_mismatchBetweenDataTypeAndDataValueExpression() {

        // STEP 1: ARRANGE
        String otaFunction = "OTAM";
        String prefix = "S";
        String lookupCode = "1001";
        String additionalParametersDataTypeExpression = "XXX_num_num";
        String additionalParametersLabelExpression = "triggerType_authorizationLevel_expirationTimeinHours";
        String additionalParametersDataValue = "1_2_720";
        String exampleVehicleStatusMessage = otaFunction + "_" + prefix + lookupCode + "_" + additionalParametersDataValue;
        
        VehicleStatusMessageTemplate vehicleStatusMessageTemplate = new VehicleStatusMessageTemplate(
            	otaFunction,
                prefix,
                lookupCode,
                additionalParametersDataTypeExpression,
                additionalParametersLabelExpression,
                exampleVehicleStatusMessage);
        

        try {


            // STEP 2: ACT
        	vehicleStatusMessageTemplate.buildVehicleStatusMessage(
            		vehicleCampaignBatch, 
            		exampleVehicleStatusMessage);


        } catch (Exception e) {


            // STEP 3: ASSERT
            Assert.assertEquals("CORE-1000: Invalid dataType encountered: [XXX] at index: [0] in dataTypeExpression: [XXX_num_num].  Valid values are 'num', 'str' or 'bool'.", e.getMessage());
        }
    }
}
