/*
 * Copyright (C) 2018, Digital Jukebox Technologies. LLC. Motor Company
 *
 */
package com.djt.cvpp.ota.flare.vehiclestatustemplate.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.djt.cvpp.ota.common.exception.FenixRuntimeException;

/**
 * @author tmyers1@yahoo.com (Tom Myers)
 */
public class RepeatableAdditionalParametersTemplate extends AdditionalParametersTemplate {

    private static final String N = "!N!";

    RepeatableAdditionalParametersTemplate(
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
     * dataTypeExpression=(str_str_str_str)+
     * dataLabelExpression=(ecu{N}_filename{N}_softwarePartNumber{N}_softwareVersion{N})+
     * additionalParametersDataValue=SYNC_abc.vbf_fmel.tar.gz_14G43-23495-AA_ABS_efgh.vbf_2222.tar.gz_22222-22222-BB
     * </pre>
     *
     * @return For the above example: "ecu1: SYNC | filename1: abc.vbf | softwarePartNumber1: fmel.tar.gz | softwareVersion1: 14G43-23495-AA | ecu2: ABS | filename2: efgh.vbf | softwarePartNumber2: 2222.tar.gz | softwareVersion2: 22222-22222-BB"
     */
    @Override
    public String generateAdditionalParameterNameValues() {

        String repeatableAdditionalParametersDataValue = additionalParametersDataValue;
        String[] repeatableAdditionalParametersDataValueArray = repeatableAdditionalParametersDataValue.split(UNDERSCORE);
        if ((repeatableAdditionalParametersDataValueArray.length % dataLabelExpressionArray.length) != 0) {
            throw new FenixRuntimeException("additionalParametersDataValue: [" + additionalParametersDataValue + "] size mismatch.  Expected an underscore delimited size in multiples of: [" + this.dataLabelExpressionArray.length + "] but was: [" + repeatableAdditionalParametersDataValueArray.length + "].");
        }
        List<String> repeatableAdditionalParametersDataValueList = new ArrayList<>(Arrays.asList(repeatableAdditionalParametersDataValueArray));
        StringBuilder sb = new StringBuilder();

        int n = 1;
        while (!repeatableAdditionalParametersDataValueList.isEmpty()) {

            for (int i = 0; i < this.dataTypeExpressionArray.length; i++) {

                String dataType = this.dataTypeExpressionArray[i];
                String dataLabel = this.dataLabelExpressionArray[i];
                dataLabel = dataLabel.replaceAll(N, Integer.toString(n));
                String dataValue = repeatableAdditionalParametersDataValueList.get(0);
                repeatableAdditionalParametersDataValueList.remove(0);

                validateDataType(dataType, dataValue);

                sb.append(dataLabel);
                sb.append(COLON_SPACE);
                sb.append(dataValue);
                if (i < this.dataTypeExpressionArray.length - 1) {
                    sb.append(PIPE);
                }
            }
            n++;
            if (!repeatableAdditionalParametersDataValueList.isEmpty()) {
                sb.append(PIPE);
            }
        }

        return sb.toString();
    }
}
