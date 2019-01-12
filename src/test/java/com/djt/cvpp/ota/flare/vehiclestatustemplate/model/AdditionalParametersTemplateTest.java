package com.djt.cvpp.ota.flare.vehiclestatustemplate.model;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.djt.cvpp.ota.flare.vehiclestatustemplate.model.AdditionalParametersTemplate;

public class AdditionalParametersTemplateTest {

    @Test
    public void constructor_nullDataTypeExpression() {

        // STEP 1: ARRANGE
        String additionalParametersDataTypeExpression = null;
        String additionalParametersLabelExpression = "triggerType_authorizationLevel_expirationTimeinHours";
        String additionalParametersDataValue = "1_2_720";

        try {


            // STEP 2: ACT
            new AdditionalParametersTemplate(
                    additionalParametersDataTypeExpression,
                    additionalParametersLabelExpression,
                    additionalParametersDataValue);
            fail("Expected exception not thrown");


        } catch (Exception e) {


            // STEP 3: ASSERT
            Assert.assertEquals("CORE-1000: dataTypeExpression: [null], dataLabelExpression: [triggerType_authorizationLevel_expirationTimeinHours] and additionalParametersDataValue: [1_2_720] must all be non-null and non-empty", e.getMessage());
        }
    }

    @Test
    public void constructor_nullLabelExpression() {

        // STEP 1: ARRANGE
        String additionalParametersDataTypeExpression = "num_num_num";
        String additionalParametersLabelExpression = null;
        String additionalParametersDataValue = "1_2_720";

        try {


            // STEP 2: ACT
            new AdditionalParametersTemplate(
                    additionalParametersDataTypeExpression,
                    additionalParametersLabelExpression,
                    additionalParametersDataValue);
            fail("Expected exception not thrown");


        } catch (Exception e) {


            // STEP 3: ASSERT
            Assert.assertEquals("CORE-1000: dataTypeExpression: [num_num_num], dataLabelExpression: [null] and additionalParametersDataValue: [1_2_720] must all be non-null and non-empty", e.getMessage());
        }
    }

    @Test
    public void constructor_nullDataValueExpression() {

        // STEP 1: ARRANGE
        String additionalParametersDataTypeExpression = "num_num_num";
        String additionalParametersLabelExpression = "triggerType_authorizationLevel_expirationTimeinHours";
        String additionalParametersDataValue = null;

        try {


            // STEP 2: ACT
            new AdditionalParametersTemplate(
                    additionalParametersDataTypeExpression,
                    additionalParametersLabelExpression,
                    additionalParametersDataValue);
            fail("Expected exception not thrown");


        } catch (Exception e) {


            // STEP 3: ASSERT
            Assert.assertEquals("CORE-1000: dataTypeExpression: [num_num_num], dataLabelExpression: [triggerType_authorizationLevel_expirationTimeinHours] and additionalParametersDataValue: [null] must all be non-null and non-empty", e.getMessage());
        }
    }

    @Test
    public void generateAdditionalParameters_invalidNumberDataType() {

        // STEP 1: ARRANGE
        String additionalParametersDataTypeExpression = "num_num_num";
        String additionalParametersLabelExpression = "triggerType_authorizationLevel_expirationTimeinHours";
        String additionalParametersDataValue = "A_2_3";

        try {


            // STEP 2: ACT
            AdditionalParametersTemplate additionalParametersTemplate = new AdditionalParametersTemplate(
                    additionalParametersDataTypeExpression,
                    additionalParametersLabelExpression,
                    additionalParametersDataValue);

            additionalParametersTemplate.generateAdditionalParameterNameValues();
            fail("Expected exception not thrown");


        } catch (Exception e) {


            // STEP 3: ASSERT
            Assert.assertEquals("CORE-1000: Invalid data type encountered specified for data value; [A].  Expected a value of type: [num]", e.getMessage());
        }
    }

    @Test
    public void generateAdditionalParameters_invalidBooleanDataType() {

        // STEP 1: ARRANGE
        String additionalParametersDataTypeExpression = "bool_num_num";
        String additionalParametersLabelExpression = "triggerType_authorizationLevel_expirationTimeinHours";
        String additionalParametersDataValue = "A_2_3";

        try {


            // STEP 2: ACT
            AdditionalParametersTemplate additionalParametersTemplate = new AdditionalParametersTemplate(
                    additionalParametersDataTypeExpression,
                    additionalParametersLabelExpression,
                    additionalParametersDataValue);

            additionalParametersTemplate.generateAdditionalParameterNameValues();
            fail("Expected exception not thrown");


        } catch (Exception e) {


            // STEP 3: ASSERT
            Assert.assertEquals("CORE-1000: Invalid data type encountered specified for data value; [A].  Expected a value of type: [bool]", e.getMessage());
        }
    }



}
