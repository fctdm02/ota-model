/*
 * Copyright (C) 2018, Digital Jukebox Technologies. LLC. Motor Company
 *
 */
package com.djt.cvpp.ota.flare.vehiclestatustemplate.model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch;
import com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessage;
import com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessageAdditionalParameter;

/**
 * @author tmyers1@yahoo.com (Tom Myers)
 */
public class VehicleStatusMessageTemplate extends AbstractEntity {

	private static final long serialVersionUID = 1L;


    private static final String UNDERSCORE = "_";
    private static final String LEFT_PAREN = "(";
    private static final String RIGHT_PAREN_PLUS = ")+";
    private static final String PIPE = " | ";
    private static final String BLANK_LABEL = "BLANK-LABEL";

    private String otaFunction;
    private String prefix;
    private String lookupCode;
    private String additionalParametersDataTypeExpression;
    private String additionalParametersLabelExpression;
    private String exampleValue;
    
    public VehicleStatusMessageTemplate(
        String otaFunction,
        String prefix,
        String lookupCode,
        String additionalParametersDataTypeExpression,
        String additionalParametersLabelExpression,
        String exampleValue) {
    	
    	super();
    	this.otaFunction = otaFunction;
    	this.prefix = prefix;
    	this.lookupCode = lookupCode;
        this.additionalParametersDataTypeExpression = additionalParametersDataTypeExpression;
        this.additionalParametersLabelExpression = additionalParametersLabelExpression;
        this.exampleValue = exampleValue;
    }
    
	public String getNaturalIdentity() {
		return VehicleStatusMessageTemplate.buildVehicleStatusMessageTemplateNaturalIdentity(
			this.otaFunction, 
			this.prefix, 
			this.lookupCode);
	}
	
	public void validate(List<String> validationMessages) {

		validateNotNull(validationMessages, "otaFunction", otaFunction);
		validateNotNull(validationMessages, "prefix", prefix);
		validateNotNull(validationMessages, "lookupCode", lookupCode);
	}
	
    
    public String getOtaFunction() {
		return otaFunction;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getLookupCode() {
		return lookupCode;
	}

	public String getAdditionalParametersDataTypeExpression() {
		return additionalParametersDataTypeExpression;
	}

	public String getAdditionalParametersLabelExpression() {
		return additionalParametersLabelExpression;
	}

	public String getExampleValue() {
		return exampleValue;
	}

	/**
     * example:
     * OTAM,S,1001,num_num_num,triggerType_authorizationLevel_expirationTimeinHours,OTAM_S1001_1_2_720
     * <pre>
     * otaFunction=OTAM
     * prefix=S
     * lookupCode=1001
     * dataTypeExpression=num_num_num
     * dataLabelExpression=triggerType_authorizationLevel_expirationTimeinHours
     * additionalParametersDataValue=1_2_720
     * </pre>
     * 
     * @param parentVehicleCampaignBatch
     * @param vehicleStatusMessageExpression
     *
     * @return The above example would return a TmcVehicleStatusMessage with the appropriate number of "name/value"
     * additional parameters, corresponding to the May 2018 MVP behavior of creating this string: 
     * "triggerType: 1 | authorizationLevel: 2 | expirationTimeinHours: 720"
     */
    public VehicleStatusMessage buildVehicleStatusMessage(VehicleCampaignBatch parentVehicleCampaignBatch, String vehicleStatusMessageExpression) {
    	
        List<AdditionalParametersTemplate> additionalParametersTemplates = new ArrayList<>();
    	
        if (additionalParametersDataTypeExpression != null && additionalParametersLabelExpression != null && vehicleStatusMessageExpression != null) {

            String key = otaFunction + UNDERSCORE + prefix + lookupCode + UNDERSCORE;
            String additionalParametersDataValueOnly = vehicleStatusMessageExpression.replace(key, "");

            if (!additionalParametersDataTypeExpression.contains(LEFT_PAREN) && !additionalParametersDataTypeExpression.contains(RIGHT_PAREN_PLUS)) {

                if (additionalParametersLabelExpression.equals(BLANK_LABEL)) {
                    additionalParametersTemplates.add(new BlankLabelAdditionalParametersTemplate(
                            additionalParametersDataTypeExpression,
                            additionalParametersLabelExpression,
                            additionalParametersDataValueOnly));
                } else {
                    additionalParametersTemplates.add(new AdditionalParametersTemplate(
                            additionalParametersDataTypeExpression,
                            additionalParametersLabelExpression,
                            additionalParametersDataValueOnly));
                }

            } else {

                int index = additionalParametersDataTypeExpression.indexOf(LEFT_PAREN);
                if (index == 0) {

                    additionalParametersDataTypeExpression = additionalParametersDataTypeExpression.replace(LEFT_PAREN, "");
                    additionalParametersDataTypeExpression = additionalParametersDataTypeExpression.replace(RIGHT_PAREN_PLUS, "");

                    additionalParametersLabelExpression = additionalParametersLabelExpression.replace(LEFT_PAREN, "");
                    additionalParametersLabelExpression = additionalParametersLabelExpression.replace(RIGHT_PAREN_PLUS, "");

                    additionalParametersTemplates.add(new RepeatableAdditionalParametersTemplate(
                            additionalParametersDataTypeExpression,
                            additionalParametersLabelExpression,
                            additionalParametersDataValueOnly));

                } else {

                    String additionalParametersDataTypeExpression1 = additionalParametersDataTypeExpression.substring(0, index);

                    int index2 = additionalParametersLabelExpression.indexOf(LEFT_PAREN);
                    String additionalParametersLabelExpression1 = additionalParametersLabelExpression.substring(0, index2);

                    // This is a hack.  We know that there's only one status message with both non repeating and non-repeating (OTAM_E1008) with pattern reasonCode_(ecu!N!)+
                    int index3 = additionalParametersDataValueOnly.indexOf(UNDERSCORE);

                    additionalParametersTemplates.add(new AdditionalParametersTemplate(
                            additionalParametersDataTypeExpression1,
                            additionalParametersLabelExpression1,
                            additionalParametersDataValueOnly.substring(0, index3)));

                    String additionalParametersDataTypeExpression2 = additionalParametersDataTypeExpression.substring(index);
                    String additionalParametersLabelExpression2 = additionalParametersLabelExpression.substring(index2);

                    additionalParametersDataTypeExpression2 = additionalParametersDataTypeExpression2.replace(LEFT_PAREN, "");
                    additionalParametersDataTypeExpression2 = additionalParametersDataTypeExpression2.replace(RIGHT_PAREN_PLUS, "");

                    additionalParametersLabelExpression2 = additionalParametersLabelExpression2.replace(LEFT_PAREN, "");
                    additionalParametersLabelExpression2 = additionalParametersLabelExpression2.replace(RIGHT_PAREN_PLUS, "");

                    additionalParametersTemplates.add(new RepeatableAdditionalParametersTemplate(
                            additionalParametersDataTypeExpression2,
                            additionalParametersLabelExpression2,
                            additionalParametersDataValueOnly.substring(index3 + 1)));
                }
            }
        }
        
        VehicleStatusMessage vehicleStatusMessage = new VehicleStatusMessage(
        	parentVehicleCampaignBatch,
        	AbstractEntity.getTimeKeeper().getCurrentTimestamp(),
        	this.otaFunction + UNDERSCORE + this.prefix + this.lookupCode);
        
        List<VehicleStatusMessageAdditionalParameter> vehicleStatusMessageAdditionalParameters =  buildVehicleStatusMessageAdditionalParameters(vehicleStatusMessage, additionalParametersTemplates);
        
        vehicleStatusMessage.setVehicleStatusMessageAdditionalParameters(vehicleStatusMessageAdditionalParameters);
        
        return vehicleStatusMessage;
    }

    /**
     *
     * @param parentVehicleStatusMessage
     * @param 
     * 
     * dataTypeExpression: num, num, num, num, num
     * dataLabelExpression: timeForTriggerCampaignInSecs, timeforPostVilInSecs, timeForInstallationInSecs, timeForActivationRollbackInSecs, otaTotalWakeupTimeInSecs
     * additionalParametersDataValue: 100_234_456_234_2969
     * 
     * Results in:
     * <pre>
     * timeForTriggerCampaignInSecs: 100 | timeforPostVilInSecs: 234 | timeForInstallationInSecs: 456 | timeForActivationRollbackInSecs: 234 | otaTotalWakeupTimeInSecs: 2969
     * </pre>
     * 
     * @param additionalParametersTemplates
     * @return
     */
    public List<VehicleStatusMessageAdditionalParameter> buildVehicleStatusMessageAdditionalParameters(VehicleStatusMessage parentVehicleStatusMessage, List<AdditionalParametersTemplate> additionalParametersTemplates) {

    	List<VehicleStatusMessageAdditionalParameter> vehicleStatusMessageAdditionalParameters = new ArrayList<>();
    	
    	Integer additionalParameterSequenceNumber = Integer.valueOf(1);
    	Iterator<AdditionalParametersTemplate> iterator = additionalParametersTemplates.iterator();
    	while (iterator.hasNext()) {
    		
    		AdditionalParametersTemplate additionalParametersTemplate = iterator.next();
    		
    		List<VehicleStatusMessageAdditionalParameter> list = additionalParametersTemplate.generateAdditionalParameters(parentVehicleStatusMessage, additionalParameterSequenceNumber);
    		additionalParameterSequenceNumber = Integer.valueOf(additionalParameterSequenceNumber.intValue() + list.size());
    		
    		vehicleStatusMessageAdditionalParameters.addAll(list);
    	}

        return vehicleStatusMessageAdditionalParameters;
    }
    
    /**
     *
     * Given the following:
     * <pre>
     * dataTypeExpression: num, num, num, num, num
     * dataLabelExpression: timeForTriggerCampaignInSecs, timeforPostVilInSecs, timeForInstallationInSecs, timeForActivationRollbackInSecs, otaTotalWakeupTimeInSecs
     * additionalParametersDataValue: 100_234_456_234_2969
     * </pre>
     * 
     * @param additionalParametersTemplates
     * 
     * @return A pipe delimited set of name/value pairs, e.g. the above will result in:
     * <pre> 
     * timeForTriggerCampaignInSecs: 100 | timeforPostVilInSecs: 234 | timeForInstallationInSecs: 456 | timeForActivationRollbackInSecs: 234 | otaTotalWakeupTimeInSecs: 2969
     * </pre> 
     */
    public String generateAdditionalParametersMessage(List<AdditionalParametersTemplate> additionalParametersTemplates) {

        StringBuilder sb = new StringBuilder();
        int size = additionalParametersTemplates.size();
        if (size == 1) {

            sb.append(additionalParametersTemplates.get(0).generateAdditionalParameterNameValues());

        } else if (size == 2) {

            sb.append(additionalParametersTemplates.get(0).generateAdditionalParameterNameValues());
            sb.append(PIPE);
            sb.append(additionalParametersTemplates.get(1).generateAdditionalParameterNameValues());

        }

        return sb.toString();
    }
    
    /**
     * example:
     * OTAM,S,1001,num_num_num,triggerType_authorizationLevel_expirationTimeinHours,OTAM_S1001_1_2_720
     * <pre>
     * otaFunction=OTAM
     * prefix=S
     * lookupCode=1001
     * dataTypeExpression=num_num_num
     * dataLabelExpression=triggerType_authorizationLevel_expirationTimeinHours
     * additionalParametersDataValue=1_2_720
     * </pre>
     *
     * @return The above example would return: "OTAM_S1001"
     */
    public String generateRolloutDataAccessorLookupCode() {

    	return buildVehicleStatusMessageTemplateNaturalIdentity(
    		this.otaFunction,
    		this.prefix,
    		this.lookupCode);
    }
    
    /**
     * 
     * @param otaFunction
     * @param prefix
     * @param lookupCode
     * @return
     */
	public static String buildVehicleStatusMessageTemplateNaturalIdentity(
		String otaFunction,
		String prefix,
		String lookupCode) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(otaFunction);
		sb.append(UNDERSCORE);
		sb.append(prefix);		
		sb.append(lookupCode);
		return sb.toString();
	}    
}
