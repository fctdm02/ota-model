/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.vehiclestatustemplate.service;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.model.VehicleStatusMessageTemplate;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface VehicleStatusMessageTemplateService {

	
	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "FLARE";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "VEHICLE-STATUS-MESSAGE-TEMPLATE";
	
	
	/**
	 * 
	 * @param otaFunction
	 * @param prefix
	 * @param lookupCode
	 * @param additionalParametersDataTypeExpression
	 * @param additionalParametersLabelExpression
	 * @param exampleValue
	 * 
	 * @return The newly created vehicle status message template
	 * 
	 * @throws EntityAlreadyExistsException FLARE-VEHICLE-STATUS-MESSAGE-TEMPLATE-1001: Could not create vehicle status messsage template: [otaFunction + prefix + lookupCode] because it already exists
	 * @throws ValidationException FLARE-VEHICLE-STATUS-MESSAGE-TEMPLATE-1002: Could not create vehicle status messsage template: [otaFunction + prefix + lookupCode] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	VehicleStatusMessageTemplate createVehicleStatusMessageTemplate(
		String otaFunction,
        String prefix,
        String lookupCode,
        String additionalParametersDataTypeExpression,
        String additionalParametersLabelExpression,
        String exampleValue)
	throws
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @param otaFunction
	 * @param prefix
	 * @param lookupCode
	 * 
	 * @return The requested vehicle status message template
	 * 
	 * @throws EntityDoesNotExistException FLARE-VEHICLE-STATUS-MESSAGE-TEMPLATE-1003: Could not retrieve vehicle status messsage template: [otaFunction + prefix + lookupCode] because it does not exist
	 */
	VehicleStatusMessageTemplate getVehicleStatusMessageTemplate(
		String otaFunction,
		String prefix,
		String lookupCode)
	throws 
		EntityDoesNotExistException;
	
	/**
	 * 
	 * <pre>
	 * vehicleStatusMessageExpression = "OTAM_S1001_1_2_720";
	 * </pre>
	 * 
	 * @param vehicleStatusMessageExpression
	 * 
	 * @return The requested vehicle status message template
	 * 
	 * @throws EntityDoesNotExistException FLARE-VEHICLE-STATUS-MESSAGE-TEMPLATE-1004: Could not retrieve vehicle status messsage template by message expression: [vehicleStatusMessageExpression] because it does not exist
	 */
	VehicleStatusMessageTemplate getVehicleStatusMessageTemplate(
		String vehicleStatusMessageExpression)
	throws 
		EntityDoesNotExistException;	
}
