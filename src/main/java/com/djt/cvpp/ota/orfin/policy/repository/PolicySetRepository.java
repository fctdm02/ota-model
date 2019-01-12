/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.repository;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.repository.EntityRepository;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.orfin.policy.model.region.Region;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public interface PolicySetRepository extends EntityRepository {

	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "ORFIN";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "POLICY";
	
	/**
	 * 
	 * @param policySetName
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	PolicySet createPolicySet(
		String policySetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @return
	 */
	List<PolicySet> getAllPolicySets();
		
	/**
	 * 
	 * @param policySetName
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	PolicySet getPolicySetByName(
        String policySetName)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param oldPolicySetName
	 * @param newPolicySetName
	 * @return
	 * @throws EntityDoesNotExistException If the policy set identified by <code>oldPolicySetName</code> doesn't exist 
	 * @throws EntityAlreadyExistsException If a policy set identified by <code>newPolicySetName</code> already exists
	 * @throws ValidationException
	 */
	PolicySet renamePolicySet(
		String oldPolicySetName, 
		String newPolicySetName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @param regionCode
	 * @param countryName
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	Region createRegion(
		String regionCode,
		String countryName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @return
	 */
	List<Region> getAllRegions();
	
	/**
	 * 
	 * @param regionCode
	 * @return
	 * @throws EntityDoesNotExistException
	 */
	Region getRegionByCode(
        String regionCode)
	throws 
		EntityDoesNotExistException;
	
	/**
	 * 
	 * @param oldRegionCode
	 * @param newRegionCode
	 * @return
	 * @throws EntityDoesNotExistException If the policy set identified by <code>oldRegionCode</code> doesn't exist 
	 * @throws EntityAlreadyExistsException If a policy set identified by <code>newRegionCode</code> already exists
	 */
	Region renameRegion(
		String oldRegionCode, 
		String newRegionCode)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException;

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws ValidationException
	 */
	PolicySet updatePolicySet(PolicySet entity) throws ValidationException;
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	PolicySet deletePolicySet(PolicySet entity);
	
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws ValidationException
	 */
	Region updateRegion(Region entity) throws ValidationException;
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	Region deleteRegion(Region entity);
}
