/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.repository;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface EntityRepository {
	
	/**
	 * 
	 * @param naturalIdentity
	 * @return
	 */
	AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity);
		
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws ValidationException
	 */
	AbstractEntity updateEntity(
		AbstractEntity entity)
	throws 
		ValidationException;
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	AbstractEntity deleteEntity(AbstractEntity entity);
	
	
	/**
	 * Resets the state of the repository.
	 * <h1>
	 * This method is intended to be used for automated, integration tests ONLY 
	 * in a TEST environment.  Any attempt to call in a non TEST environment 
	 * will have an <code>UnsupportedOperationException</code> thrown.
	 * 
	 * @param newCurrentTimeInMillis
	 */
	void reset();	
}
