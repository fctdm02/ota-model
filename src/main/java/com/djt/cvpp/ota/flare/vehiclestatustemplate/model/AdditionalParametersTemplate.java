/*
 * Copyright (C) 2018, Digital Jukebox Technologies. LLC. Motor Company
 *
 */
package com.djt.cvpp.ota.flare.vehiclestatustemplate.model;

import java.util.ArrayList;
import java.util.List;

import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.BooleanAdditionalParameter;
import com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.NumericAdditionalParameter;
import com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.StringAdditionalParameter;
import com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessage;
import com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessageAdditionalParameter;

/**
 * @author tmyers1@yahoo.com (Tom Myers)
 */
public class AdditionalParametersTemplate {

    //private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalParametersTemplate.class);

    static final String UNDERSCORE = "_";
    static final String COLON_SPACE = ": ";
    static final String PIPE = " | ";
    private static final String NUM = "num";
    private static final String STR = "str";
    private static final String BOOL = "bool";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String N = "!N!";

    protected String[] dataTypeExpressionArray;
    protected String[] dataLabelExpressionArray;
    protected String additionalParametersDataValue;

    protected AdditionalParametersTemplate(
        String dataTypeExpression,
        String dataLabelExpression,
        String additionalParametersDataValue) {

        if (dataTypeExpression == null || dataLabelExpression == null || dataTypeExpression.trim().isEmpty() || additionalParametersDataValue == null || dataLabelExpression.trim().isEmpty() || additionalParametersDataValue.isEmpty()) {
            throw new FenixRuntimeException("dataTypeExpression: [" + dataTypeExpression + "], dataLabelExpression: [" + dataLabelExpression + "] and additionalParametersDataValue: [" + additionalParametersDataValue + "] must all be non-null and non-empty");
        }

        this.dataTypeExpressionArray = dataTypeExpression.split(UNDERSCORE);
        this.dataLabelExpressionArray = dataLabelExpression.split(UNDERSCORE);
        this.additionalParametersDataValue = additionalParametersDataValue;

        if (this.dataTypeExpressionArray.length != this.dataLabelExpressionArray.length) {
            throw new FenixRuntimeException("dataTypeExpression: [" + dataTypeExpression + "] does not match dataLabelExpression: [" + dataLabelExpression + "] in terms of underscore separated values.");
        }

        for (int i = 0; i < this.dataTypeExpressionArray.length; i++) {

            String dataType = this.dataTypeExpressionArray[i];
            if (!dataType.equals(NUM) && !dataType.equals(STR) && !dataType.equals(BOOL)) {
                throw new FenixRuntimeException("Invalid dataType encountered: [" + dataType + "] at index: [" + i + "] in dataTypeExpression: [" + dataTypeExpression + "].  Valid values are 'num', 'str' or 'bool'.");
            }
        }
    }

    /**
     * example:
     * <pre>
     * dataTypeExpression=num_num_num
     * dataLabelExpression=triggerType_authorizationLevel_expirationTimeinHours
     * additionalParametersDataValue=1_2_720
     * </pre>
     *
     * @return For the above example: "triggerType: 1 | authorizationLevel: 2 | expirationTimeinHours: 720"
     */
    public String generateAdditionalParameterNameValues() {

        String[] additionalParametersDataValueArray = additionalParametersDataValue.split(UNDERSCORE);

        if (additionalParametersDataValueArray.length != dataLabelExpressionArray.length) {
            throw new FenixRuntimeException("additionalParametersDataValue: [" + additionalParametersDataValue + "] size mismatch.  Expected an underscore delimited size of: [" + this.dataLabelExpressionArray.length + "] but was: [" + additionalParametersDataValueArray.length + "].");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.dataTypeExpressionArray.length; i++) {

            String dataType = this.dataTypeExpressionArray[i];
            String dataLabel = this.dataLabelExpressionArray[i];
            String dataValue = additionalParametersDataValueArray[i];

            validateDataType(dataType, dataValue);

            sb.append(dataLabel);
            sb.append(COLON_SPACE);
            sb.append(dataValue);
            if (i < this.dataTypeExpressionArray.length - 1) {
                sb.append(PIPE);
            }
        }
        String s = sb.toString();
        s = s.replaceAll("BLANK-LABEL: ", "");
        return s;
    }

    public List<VehicleStatusMessageAdditionalParameter> generateAdditionalParameters(VehicleStatusMessage parentVehicleStatusMessage, Integer additionalParameterSequenceNumber) {

    	List<VehicleStatusMessageAdditionalParameter> vehicleStatusMessageAdditionalParameters = new ArrayList<>();
    	
        String[] additionalParametersDataValueArray = additionalParametersDataValue.split(UNDERSCORE);
        
        int mod = additionalParametersDataValueArray.length % dataLabelExpressionArray.length;
        int div = additionalParametersDataValueArray.length / dataLabelExpressionArray.length;
        if (mod != 0) {
            throw new FenixRuntimeException("additionalParametersDataValue: [" + additionalParametersDataValue + "] size mismatch.  Expected an underscore delimited string in multiples of: [" + this.dataLabelExpressionArray.length + "] but was: [" + additionalParametersDataValueArray.length + "].");
        }

         
        int dataTypeExpressionArrayLength = dataTypeExpressionArray.length;
        for (int j = 0; j < div; j++) {
            for (int i = 0; i < dataTypeExpressionArrayLength; i++) {

                String dataType = this.dataTypeExpressionArray[i];
                String dataLabel = this.dataLabelExpressionArray[i];
                dataLabel = dataLabel.replaceAll(N, Integer.toString(j+1));
                int idx = i + (j * dataTypeExpressionArrayLength);
                String dataValue = additionalParametersDataValueArray[idx];

                validateDataType(dataType, dataValue);
                
                if (dataType.equals(NUM)) {
                	vehicleStatusMessageAdditionalParameters.add(new NumericAdditionalParameter(parentVehicleStatusMessage, additionalParameterSequenceNumber, dataLabel, Long.valueOf(dataValue)));
                } else if (dataType.equals(BOOL)) {
                	vehicleStatusMessageAdditionalParameters.add(new BooleanAdditionalParameter(parentVehicleStatusMessage, additionalParameterSequenceNumber, dataLabel, Boolean.valueOf(dataValue)));
                } else {
                	vehicleStatusMessageAdditionalParameters.add(new StringAdditionalParameter(parentVehicleStatusMessage, additionalParameterSequenceNumber, dataLabel, dataValue));
                }
                
                additionalParameterSequenceNumber = Integer.valueOf(additionalParameterSequenceNumber.intValue() + 1);
            }
        }
        return vehicleStatusMessageAdditionalParameters;
    }
    
    void validateDataType(String dataType, String dataValue) {

        String errorMessage = "Invalid data type encountered specified for data value; [" + dataValue + "].  Expected a value of type: [" + dataType + "]";
        if (dataType.equals(NUM)) {
            try {
            	Long.valueOf(dataValue);
                //Long l = Long.valueOf(dataValue);
                //LOGGER.trace("Parsed long value: {}", l);
            } catch (NumberFormatException nfe) {
                throw new FenixRuntimeException(errorMessage, nfe);
            }
        } else if (dataType.equals(BOOL)) {
            if (dataValue != null && !dataValue.equalsIgnoreCase(TRUE) && !dataValue.equalsIgnoreCase(FALSE)) {
                throw new FenixRuntimeException(errorMessage);
            }
            Boolean.valueOf(dataValue);
            //Boolean b = Boolean.valueOf(dataValue);
            //LOGGER.trace("Parsed boolean value: {}", b);
        }
    }
}
