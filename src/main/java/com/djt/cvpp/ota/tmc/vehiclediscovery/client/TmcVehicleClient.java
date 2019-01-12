/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.client;

import java.util.List;
import java.util.Set;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.tmc.vehiclediscovery.exception.TmcResourceException;
import com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.VehicleDtoMapper;
import com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.VehicleJsonConverter;
import com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle;
import com.djt.cvpp.ota.vadr.model.DomainInstance;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface TmcVehicleClient {

	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "TMC";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "RESOURCE";
	
	/**
	 * NOTE: This method can only be invoked in a non-production environment, i.e. DEV/TEST, 
	 * otherwise, an <code>UnsupportedOperationException</code>
	 * 
	 * @param tmcVehicle
	 * @throws EntityAlreadyExistsException
	 */
	void provisionVirtualVehicle(TmcVehicle tmcVehicle) throws EntityAlreadyExistsException, TmcResourceException;

	/**
	 * NOTE: This method can only be invoked in a non-production environment, i.e. DEV/TEST, 
	 * otherwise, an <code>UnsupportedOperationException</code>
	 * 
	 * @param templateVehicle
	 * @param numberToProvision Since the "template" vehicles will have a VIN that has a sequenceNumber of 0,
	 * the <code>numberToProvision</code> can be from 1 to 999,998
	 *  
	 * @throws EntityAlreadyExistsException
	 */
	void provisionVirtualVehiclesFromTemplateVehicle(TmcVehicle templateVehicle, int numberToProvision) throws EntityAlreadyExistsException, TmcResourceException;
	
	/**
	 * 
	 * @param vin
	 * @return
	 * @throws EntityDoesNotExistException
	 */
	TmcVehicle getVehicleByVin(String vin) throws EntityDoesNotExistException, TmcResourceException;
	
	/**
	 * NOTE: This method may not be needed.
	 * 
	 * @return
	 */
	List<TmcVehicle> getAllVehicles() throws TmcResourceException;
		
	/**
	 * NOTE: This matches the current TMC API specs for a *single* synchronous call with *all* vehicles in the *single* response
	 * 
	 * @param programCodeModelYearInclusions Inclusions take the form of: XXXX_YYYY where XXXX is the programCode and modelYear is the 4 digit model year
	 * @param domainInstance
	 * @return
	 * @throws TmcResourceException
	 */
	List<TmcVehicle> performVehicleDiscovery(
		Set<String> programCodeModelYearInclusions,
		DomainInstance domainInstance)
	throws
		TmcResourceException;
	
	/**
	 * 
	 * @return
	 */
	VehicleJsonConverter getVehicleJsonConverter();

	/**
	 * 
	 * @return
	 */
	VehicleDtoMapper getVehicleDtoMapper();
}
