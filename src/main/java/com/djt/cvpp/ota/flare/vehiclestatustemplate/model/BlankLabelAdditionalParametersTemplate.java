/*
 * Copyright (C) 2018, Digital Jukebox Technologies. LLC. Motor Company
 *
 */
package com.djt.cvpp.ota.flare.vehiclestatustemplate.model;

public class BlankLabelAdditionalParametersTemplate extends AdditionalParametersTemplate {

    BlankLabelAdditionalParametersTemplate(
            String dataTypeExpression,
            String dataLabelExpression,
            String additionalParametersDataValue) {
        super(
                dataTypeExpression,
                dataLabelExpression,
                additionalParametersDataValue);
    }

    /**
     * example:
     * <pre>
     * dataTypeExpression=str
     * dataLabelExpression=BLANK-LABEL
     * additionalParametersDataValue='OTAM Happy Path Engineering Message Goes Here'
     * </pre>
     *
     * @return For the above example: 'OTAM Happy Path Engineering Message Goes Here'
     */
    @Override
    public String generateAdditionalParameterNameValues() {
        return additionalParametersDataValue;
    }
}
