/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.vehiclestatustemplate.repository.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.model.VehicleStatusMessageTemplate;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.repository.VehicleStatusMessageTemplateRepository;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class ClasspathVehicleStatusMessageTemplateRepositoryImpl implements VehicleStatusMessageTemplateRepository {

	public static final String VEHICLE_STATUS_MESSAGE_TEMPLATE_FILENAME = "/com/djt/cvpp/ota/flare/vehiclestatustemplate/repository/FENIX_vehicle_status_listener_message_processing.txt";
	

    private static final String COMMENT = "--";
    private static final String COMMA = ",";
    private static final String SUNNY_PATH_PREFIX = "S";
    private static final String ERROR_PATH_PREFIX = "E";

    
	// The vehicle status message templates are keyed by their natural identity.
    private Map<String, VehicleStatusMessageTemplate> map = new TreeMap<>();
    
    public ClasspathVehicleStatusMessageTemplateRepositoryImpl() {
    	try {
    		this.loadFromClasspath();	
    	} catch (IOException | ValidationException | EntityAlreadyExistsException e) {
    		throw new FenixRuntimeException("Unable to load vehicle status message templates from classpath resource: [" + VEHICLE_STATUS_MESSAGE_TEMPLATE_FILENAME + "], error: [" + e.getMessage(), e);
    	}    	
    }

	public VehicleStatusMessageTemplate createVehicleStatusMessageTemplate(
		String otaFunction,
        String prefix,
        String lookupCode,
        String additionalParametersDataTypeExpression,
        String additionalParametersLabelExpression,
        String exampleValue)
	throws
		EntityAlreadyExistsException,
		ValidationException {
		
		VehicleStatusMessageTemplate vehicleStatusMessageTemplate = new VehicleStatusMessageTemplate(
			otaFunction,
	        prefix,
	        lookupCode,
	        additionalParametersDataTypeExpression,
	        additionalParametersLabelExpression,
	        exampleValue);
		
		vehicleStatusMessageTemplate.assertValid();
		
		this.map.put(vehicleStatusMessageTemplate.getNaturalIdentity(), vehicleStatusMessageTemplate);
		
		return vehicleStatusMessageTemplate;
	}
	
	public VehicleStatusMessageTemplate getVehicleStatusMessageTemplate(
		String otaFunction,
		String prefix,
		String lookupCode)
	throws 
		EntityDoesNotExistException {
		
		String naturalIdentity = VehicleStatusMessageTemplate.buildVehicleStatusMessageTemplateNaturalIdentity(otaFunction, prefix, lookupCode);
		VehicleStatusMessageTemplate vehicleStatusMessageTemplate = this.map.get(naturalIdentity);
		
		if (vehicleStatusMessageTemplate == null) {
			throw new EntityDoesNotExistException("VehicleStatusMessageTemplate: [" + naturalIdentity + "] not found in: [" + this.map.keySet() + "].");	
		}
		
		return vehicleStatusMessageTemplate;
	}
	
	/*
	 * 
	 * <pre>
	 * vehicleStatusMessageExpression = "OTAM_S1001_1_2_720";
	 * </pre>
	 * 
	 * @param vehicleStatusMessageExpression
	 * @return
	 * @throws EntityDoesNotExistException
	 */
	public VehicleStatusMessageTemplate getVehicleStatusMessageTemplate(
		String vehicleStatusMessageExpression)
	throws 
		EntityDoesNotExistException {
		
		if (vehicleStatusMessageExpression == null) {
			throw new FenixRuntimeException("vehicleStatusMessageExpression must be specified.");
		}
		
		for (Map.Entry<String, VehicleStatusMessageTemplate> entry: this.map.entrySet()) {

			if (vehicleStatusMessageExpression.startsWith(entry.getKey())) {
				
				return entry.getValue();
			}
		}
		
		throw new EntityDoesNotExistException("VehicleStatusMessageTemplate for expression: [" + vehicleStatusMessageExpression + "] not found");
	}
		
   private void loadFromClasspath() throws IOException, ValidationException, EntityAlreadyExistsException {

       try (InputStream inputStream = ClasspathVehicleStatusMessageTemplateRepositoryImpl.class.getResourceAsStream(VEHICLE_STATUS_MESSAGE_TEMPLATE_FILENAME)) {
           loadVehicleStatusMessageTemplates(inputStream);
       }
   }
	
    /*
     * See file FENIX_vehicle_status_listener_message_processing.txt, which is in this package.
     * NOTE: Any lines that are blank or start with -- should not be parsed.
     * <p>
     * <pre>
     * -- Column Number:
     * -- 1 - OTA function abbreviation
     * -- 2 - Prefix: either 'S' (for happy path) or 'E' (for error path)
     * -- 3 - Lookup code
     * -- 4 - Additional parameter types
     * -- 5 - Additional parameter names
     * -- 6 - Example sent from vehicle
     * --
     * -- =============================================================================================================================================================================================================================================================
     * OTAM,S,1001,num_num_num,triggerType_authorizationLevel_expirationTimeinHours,OTAM_S1001_1_2_720
     * OTAM,S,1002,num_bool,consentResourceCode_result,OTAM_S1002_2_true
     * OTAM,S,1003,num_num,responseType_responsetimeInMilliseconds,OTAM_S1003_2_40
     * OTAM,S,1004,(str_str_str)+,(ecu!N!_filename!N!_softwarePartNumberOrSoftwareVersion!N!)+,OTAM_S1004_SYNC_fmel.tar.gz_14G43-23495-AA_ABS_efgh.vbf_22222-22222-BB
     * .
     * .
     * .
     * OTAM,E,1009,num,reasonCode,OTAM_E1009_5
     *
     * -- WHERE:
     * -- (X)+ denotes a repeating group of 1 to N groups defined by "X" in the "Additional parameter types" column and parameter substitution is done on {N}
     * -- in the "Additional parameter names" column (where !N! is replaced with the appropriate value from the sequence 1...N)
     * --
     * -- For example, for status message SIM_E_1002, we have a pattern of: (str_str_num)+, so for example value:
     * -- SIM_E_1002_ABS_aaa.vbf_5_CLUSTER_bbb.vbf_4
     * --
     * -- we would get a rollout data accessor "message" value of:
     * -- ecu1 : ABS | filename1: aaa.vbf | failureCode1: 5 | ecu2: CLUSTER | filename2: bbb.vbf | failureCode2: 4
     * </pre>
     */
    private void loadVehicleStatusMessageTemplates(InputStream inputStream) throws IOException, ValidationException, EntityAlreadyExistsException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {

            Iterator<String> iterator = reader.lines().iterator();
            while (iterator.hasNext()) {

                String line = iterator.next();
                if (!line.trim().isEmpty() && !line.startsWith(COMMENT)) {

                    List<String> list = new ArrayList<>(Arrays.asList(line.split(COMMA)));
                    if (list.size() != 3 && list.size() != 6) {
                        throw new FenixRuntimeException("Invalid comma separated line encountered, expected size of 3 or 6, but was: " + list.size() + ", line: " + line);
                    }

                    String otaFunction = list.get(0);
                    String prefix = list.get(1);
                    String lookupCode = list.get(2);
                    String additionalParametersDataTypeExpression = null;
                    String additionalParametersLabelExpression = null;
                    String exampleValue = null;

                    if (list.size() == 6) {
                    	
                    	additionalParametersDataTypeExpression = list.get(3);
                    	additionalParametersLabelExpression = list.get(4);
                    	exampleValue = list.get(5);
                    }
                    
                    if (!prefix.equals(SUNNY_PATH_PREFIX) && !prefix.equals(ERROR_PATH_PREFIX)) {
                        throw new FenixRuntimeException("Invalid prefix encountered: [" + prefix + "].  Only 'S' and 'E' are supported for line: [" + line + "].");
                    }
                    this.createVehicleStatusMessageTemplate(
                    	otaFunction, 
                    	prefix, 
                    	lookupCode,
                    	additionalParametersDataTypeExpression,
                    	additionalParametersLabelExpression,
                    	exampleValue);
                }
            }
        }
    }	
}
