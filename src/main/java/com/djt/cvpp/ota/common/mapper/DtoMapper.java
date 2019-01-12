/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.common.mapper;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface DtoMapper<E, D> {

	/**
	 * 
	 * @param entity
	 * @return dto
	 */
	D mapEntityToDto(E entity);	

	/**
	 * 
	 * @param dto
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	E mapDtoToEntity(D dto) throws EntityAlreadyExistsException, EntityDoesNotExistException, ValidationException;
}
