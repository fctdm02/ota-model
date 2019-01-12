/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.rollout.repository.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.flare.rollout.model.AbstractRollout;
import com.djt.cvpp.ota.flare.rollout.repository.RolloutRepository;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public class MockRolloutRepositoryImpl implements RolloutRepository {
	
	private Map<String, AbstractRollout> rolloutMap = new TreeMap<>();
	
	public void reset() {
		this.rolloutMap.clear();
	}
	
	public MockRolloutRepositoryImpl() {
	}
	
	public void saveRollout(AbstractRollout rollout)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		if (rollout == null) {
			throw new ValidationException("rollout", "cannot be null.");
		}
		
		String naturalIdentity = rollout.getNaturalIdentity();
		AbstractRollout check = getRolloutByNameNullIfNotFound(naturalIdentity);
		if (check != null) {
			throw new EntityAlreadyExistsException("Rollout with name: [" + naturalIdentity + "] already exists.");
		}
		
		this.rolloutMap.put(naturalIdentity, rollout);
	}
	
	public List<AbstractRollout> getAllRollouts() {
		
		List<AbstractRollout> list = new ArrayList<>();
		list.addAll(this.rolloutMap.values());
		return list;
	}
	
	public AbstractRollout getRolloutByName(
        String rolloutName)
	throws 
		EntityDoesNotExistException {
		
		AbstractRollout abstractRollout = getRolloutByNameNullIfNotFound(rolloutName);
		if (abstractRollout == null) {
			throw new EntityDoesNotExistException("AbstractRollout with name: [" + rolloutName + "] does not exist.");
		}
		return abstractRollout;
	}
	
	private AbstractRollout getRolloutByNameNullIfNotFound(String rolloutName) {
		
		if (rolloutName == null || rolloutName.trim().isEmpty()) {
			throw new FenixRuntimeException("rolloutName must be specified.");
		}
		
		Iterator<AbstractRollout> iterator = this.rolloutMap.values().iterator();
		while (iterator.hasNext()) {
			
			AbstractRollout abstractRollout = iterator.next();
			if (abstractRollout.getNaturalIdentity().equalsIgnoreCase(rolloutName.trim())) {
				return abstractRollout; 
			}
		}
		return null;
	}
	
	public AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity) {
		
		if (naturalIdentity == null || naturalIdentity.trim().isEmpty()) {
			throw new FenixRuntimeException("naturalIdentity must be specified.");
		}
		
		AbstractRollout abstractRollout = this.rolloutMap.get(naturalIdentity);
		return abstractRollout;
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		
		if (entity instanceof AbstractRollout == false) {
			throw new RuntimeException("Expected an instance of AbstractRollout, but instead was: " + entity.getClassAndIdentity());
		}
		
		this.rolloutMap.put(entity.getNaturalIdentity(), (AbstractRollout)entity);
		return entity;
	}
	
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		
		if (entity instanceof AbstractRollout == false) {
			throw new RuntimeException("Expected an instance of AbstractRollout, but instead was: " + entity.getClassAndIdentity());
		}
		
		return this.rolloutMap.remove(entity.getNaturalIdentity());
	}
}
