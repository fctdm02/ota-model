/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.service;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface EntityService {
	
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
}
