/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.vadrevent.service;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.service.EntityService;
import com.djt.cvpp.ota.orfin.vadrevent.mapper.VadrReleaseEventDtoMapper;
import com.djt.cvpp.ota.orfin.vadrevent.mapper.VadrReleaseEventJsonConverter;
import com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease;
import com.djt.cvpp.ota.orfin.vadrevent.repository.OrfinVadrReleaseEventRepository;

/**
 * 
 * This service interfaces deals with basic CRUD operations for the VadrRelease
 * aggregate root, as well as all the "child" entities (and specializations of)
 * </p>
 * This interface, takes as input, primitives from a higher layer, e.g. a Controller
 * or Application Service, performs whatever operation requested (which for this 
 * interfaces, includes publishing vadrRelease events), and returns the created, updated
 * or requested domain objects.  
 * </p>
 * For any information being transferred to another layer, it is recommended that the JSON
 * Converter, which is available in this interfae, be used to map entities to DTOs, and/or
 * map these DTOs to JSON. 
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface OrfinVadrReleaseEventService extends EntityService {
	
	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "ORFIN";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "VADR-RELEASE";

	/**
	 * 
	 * @param domainName
	 * @param domainInstanceName
	 * @param domainInstanceDescription
	 * @param domainInstanceVersion
	 * @param appId
	 * @param appVersion
	 * @param productionState
	 * @param releaseDate
	 * @param softwarePriorityLevel
	 * 
	 * @return
	 * 
	 * @throws EntityAlreadyExistsException ORFIN-VADR-RELEASE-1001: Could not create vadrRelease: [vadrReleaseName] because it already exists
	 * @throws ValidationException ORFIN-VADR-RELEASE-1002: Could not create vadrRelease because attribute: [attributeName] was invalid for reason: [reason] 
	 */
	VadrRelease createVadrRelease(
		String domainName,
		String domainInstanceName,
		String domainInstanceDescription,
		String domainInstanceVersion,
		String appId,
		String appVersion,
		String productionState,
		String releaseDate,
		String softwarePriorityLevel)
	throws 
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @return All vadrReleases that have been created.
	 */
	List<VadrRelease> getAllVadrReleases();

	/**
	 * 
	 * @param domainName
	 * @param domainInstanceName
	 * @param domainInstanceVersion
	 * @param appId
	 * @param appVersion
	 * @param productionState
	 * @param releaseDate
	 * 
	 * @return
	 * 
	 * @throws EntityDoesNotExistException ORFIN-VADR-RELEASE-1003: Could not retrieve vadrRelease: [vadrReleaseName] because it does not exist
	 * @throws ValidationException ORFIN-VADR-RELEASE-1004: Could not retrieve vadrRelease: [vadrReleaseName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	VadrRelease getVadrReleaseByName(
		String domainName,
		String domainInstanceName,
		String domainInstanceVersion,
		String appId,
		String appVersion,
		String productionState,
		String releaseDate)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * @param vadrReleaseName The unique name of the vadrRelease to update
	 * 
	 * @return The affected vadrRelease
	 * 
	 * @throws EntityDoesNotExistException ORFIN-VADR-RELEASE-1012: Could not update vadrRelease: [vadrReleaseName] because it does not exist 
	 * @throws ValidationException ORFIN-VADR-RELEASE-1013: Could not update vadrRelease: [vadrReleaseName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	VadrRelease updateVadrRelease(
		String vadrReleaseName)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * @param vadrReleaseName The name of the vadrRelease to delete
	 * 
	 * @throws EntityDoesNotExistException ORFIN-VADR-RELEASE-1014: Could not delete vadrRelease: [vadrReleaseName] because it does not exist
	 * @throws ValidationException ORFIN-VADR-RELEASE-1015: Could not delete vadrRelease: [vadrReleaseName] because the specified vadrRelease name is invalid
	 */
	void deleteVadrRelease(
		String vadrReleaseName) 
	throws 
		EntityDoesNotExistException,
		ValidationException; 

	
	
	// NON-FUNCTIONAL RELATED METHODS
	/**
	 * Used for setter-based dependency injection
	 * 
	 * @param orfinVadrReleaseEventRepository
	 */
	void setVadrReleaseRepository(OrfinVadrReleaseEventRepository orfinVadrReleaseEventRepository);

	/**
	 * Used to marshall entities to JSON (via DTO Mappers), and vice-versa, where applicable.
	 *  
	 * @return
	 */
	VadrReleaseEventJsonConverter getJsonConverter();
	
	/**
	 * 
	 * Used to marshall entities to DTOs.  It is expected that the application/controller layer will convert everything to DTOs at their level 
	 * and marshall everything to JSON for over the wire transfer
	 *  
	 * @return
	 */
	VadrReleaseEventDtoMapper getDtoMapper();
}
