/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.vehiclestatustemplate.repository;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.model.VehicleStatusMessageTemplate;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface VehicleStatusMessageTemplateRepository {

	/**
	 * 
	 * @param otaFunction
	 * @param prefix
	 * @param lookupCode
	 * @param additionalParametersDataTypeExpression
	 * @param additionalParametersLabelExpression
	 * @param exampleValue
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
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
	 * @return
	 * @throws EntityDoesNotExistException
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
	 * @return
	 * @throws EntityDoesNotExistException
	 */
	VehicleStatusMessageTemplate getVehicleStatusMessageTemplate(
		String vehicleStatusMessageExpression)
	throws 
		EntityDoesNotExistException;
}
