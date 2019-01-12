/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.repository;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.repository.EntityRepository;
import com.djt.cvpp.ota.orfin.odl.model.Odl;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface OptimizedDataListRepository extends EntityRepository {

	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "ORFIN";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "ODL";

	/**
	 * 
	 * @param odlName
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	Odl createOdl(
		String odlName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @return
	 */
	List<Odl> getAllOdls();
		
	/**
	 * 
	 * @param odlName
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	Odl getOdlByName(
        String odlName)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param programCode
	 * @param modelYear
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	Odl getOdlByProgramCodeAndModelYear(
        String programCode,
        Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param oldOdlName
	 * @param newOdlName
	 * @return
	 * @throws EntityDoesNotExistException If the odl identified by <code>oldOdlName</code> doesn't exist 
	 * @throws EntityAlreadyExistsException If a odl identified by <code>newOdlName</code> already exists
	 */
	Odl renameOdl(
		String oldOdlName, 
		String newOdlName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException;
}
