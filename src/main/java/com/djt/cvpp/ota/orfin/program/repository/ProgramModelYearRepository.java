/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.program.repository;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.repository.EntityRepository;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public interface ProgramModelYearRepository extends EntityRepository {
	
	/**
	 * 
	 * @param programCode
	 * @param modelYear
	 * @param policySetActions
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	ProgramModelYear createProgramModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @return
	 */
	List<ProgramModelYear> getAllProgramModelYears();
	
	/**
	 * 
	 * @param programCode
	 * @param modelYear
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	ProgramModelYear getProgramModelYearByNaturalIdentity(
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;
}
