/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.common.mapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface JsonConverter<E> {

	/**
	 * This operation only makes sense for those adapters that need to
	 * take JSON from external systems, then unmarshall it to DTOs, then
	 * map them Entities, for say, the for the purposes of validation.
	 * Otherwise, an <code>UnsupportedOperationException</code> will be thrown. 
	 * 
	 * @param json
	 * @return e
	 */
	E unmarshallFromJsonToEntity(String json);	
	
	/**
	 * 
	 * @param entity
	 * @return json
	 */
	String marshallFromEntityToJson(E entity);
}
