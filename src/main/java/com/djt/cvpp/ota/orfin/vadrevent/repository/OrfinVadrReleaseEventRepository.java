/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.vadrevent.repository;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.repository.EntityRepository;
import com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface OrfinVadrReleaseEventRepository extends EntityRepository {

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
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
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
	 * @return
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
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
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
}
