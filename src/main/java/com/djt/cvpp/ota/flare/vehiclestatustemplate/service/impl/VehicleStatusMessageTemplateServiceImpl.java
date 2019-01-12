/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.vehiclestatustemplate.service.impl;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.model.VehicleStatusMessageTemplate;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.repository.VehicleStatusMessageTemplateRepository;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.repository.impl.ClasspathVehicleStatusMessageTemplateRepositoryImpl;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.service.VehicleStatusMessageTemplateService;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VehicleStatusMessageTemplateServiceImpl implements VehicleStatusMessageTemplateService {
	
	private VehicleStatusMessageTemplateRepository vehicleStatusMessageTemplateRepository;
	
	public VehicleStatusMessageTemplateServiceImpl() {
		this.vehicleStatusMessageTemplateRepository = new ClasspathVehicleStatusMessageTemplateRepositoryImpl(); 
	}
	
	public VehicleStatusMessageTemplateServiceImpl(VehicleStatusMessageTemplateRepository vehicleStatusMessageTemplateRepository) {
		this.vehicleStatusMessageTemplateRepository = vehicleStatusMessageTemplateRepository;
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
		
		return this.vehicleStatusMessageTemplateRepository.createVehicleStatusMessageTemplate(
			otaFunction, 
			prefix, 
			lookupCode,
			additionalParametersDataTypeExpression,
			additionalParametersLabelExpression,
			exampleValue);
	}
	
	public VehicleStatusMessageTemplate getVehicleStatusMessageTemplate(
		String otaFunction,
		String prefix,
		String lookupCode)
	throws 
		EntityDoesNotExistException {
		
		return this.vehicleStatusMessageTemplateRepository.getVehicleStatusMessageTemplate(
			otaFunction, 
			prefix, 
			lookupCode);
	}
	
	public VehicleStatusMessageTemplate getVehicleStatusMessageTemplate(
		String vehicleStatusMessageExpression)
	throws 
		EntityDoesNotExistException {
		
		return this.vehicleStatusMessageTemplateRepository.getVehicleStatusMessageTemplate(vehicleStatusMessageExpression);
	}
}
