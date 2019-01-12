/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.rollout.repository;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.repository.EntityRepository;
import com.djt.cvpp.ota.flare.rollout.model.AbstractRollout;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public interface RolloutRepository extends EntityRepository {

	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "FLARE";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "ROLLOUT";

	/**
	 * 
	 * @param rollout
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	void saveRollout(AbstractRollout rollout)
	throws 
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @return
	 */
	List<AbstractRollout> getAllRollouts();
	
	/**
	 * 
	 * @param rolloutName
	 * @return
	 * @throws EntityDoesNotExistException
	 */
	AbstractRollout getRolloutByName(
        String rolloutName)
	throws 
		EntityDoesNotExistException;
}
