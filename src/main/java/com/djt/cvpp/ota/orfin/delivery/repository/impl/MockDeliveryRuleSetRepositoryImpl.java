/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.delivery.repository.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.repository.impl.AbstractMockRepository;
import com.djt.cvpp.ota.orfin.delivery.mapper.DeliveryRuleSetJsonConverter;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.delivery.model.enums.ConsentType;
import com.djt.cvpp.ota.orfin.delivery.repository.DeliveryRuleSetRepository;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockDeliveryRuleSetRepositoryImpl extends AbstractMockRepository implements DeliveryRuleSetRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MockDeliveryRuleSetRepositoryImpl.class);

	
	private Map<String, DeliveryRuleSet> deliveryRuleSetMap = new TreeMap<>();

	private static DeliveryRuleSetRepository INSTANCE = new MockDeliveryRuleSetRepositoryImpl();
	public static DeliveryRuleSetRepository getInstance() {
		return INSTANCE;
	}

	private MockDeliveryRuleSetRepositoryImpl() {
	}
	
	public void reset() {
		this.deliveryRuleSetMap.clear();
	}
	
	public DeliveryRuleSet createDeliveryRuleSet(
		String deliveryRuleSetName,
		String authorizedBy,
		String messageToConsumer,
		ConsentType consentType,
		Timestamp scheduledRolloutDate)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		DeliveryRuleSet deliveryRuleSetCheck = (DeliveryRuleSet)getEntityByNaturalIdentityNullIfNotFound(deliveryRuleSetName);
		if (deliveryRuleSetCheck != null) {
			
			throw new EntityAlreadyExistsException("Cannot create delivery rule set with name: [" + deliveryRuleSetName + "] because it already exists");
		}
		
		DeliveryRuleSet deliveryRuleSet = new DeliveryRuleSet
			.DeliveryRuleSetBuilder()
			.withDeliveryRuleSetName(deliveryRuleSetName)
			.withAuthorizedBy(authorizedBy)
			.withMessageToConsumer(messageToConsumer)
			.withConsentType(consentType)
			.withScheduledRolloutDate(scheduledRolloutDate)
			.build();
		
		this.deliveryRuleSetMap.put(deliveryRuleSet.getNaturalIdentity(), deliveryRuleSet);
		
		return deliveryRuleSet;
	}
		
	public List<DeliveryRuleSet> getAllDeliveryRuleSets() {
		
		List<DeliveryRuleSet> list = new ArrayList<>();
		list.addAll(this.deliveryRuleSetMap.values());
		return list;
	}
	
	public DeliveryRuleSet getDeliveryRuleSetByName(
        String deliveryRuleSetName)
	throws 
		EntityDoesNotExistException {
		
		DeliveryRuleSet deliveryRuleSet = (DeliveryRuleSet)getEntityByNaturalIdentityNullIfNotFound(deliveryRuleSetName);
		
		if (deliveryRuleSet == null) {
			
			// As a last resort, see if we can load this entity from "testdata" on the file system.
			try {
				String json = this.loadTestData(deliveryRuleSetName);
				deliveryRuleSet = new DeliveryRuleSetJsonConverter().unmarshallFromJsonToEntity(json);
			} catch (Exception e) {
				LOGGER.error("Could not load from testdata area as file does not exist", e);
				deliveryRuleSet = null;
			}
		}
		
		if (deliveryRuleSet == null) {
			throw new EntityDoesNotExistException("DeliveryRuleSet with name: [" + deliveryRuleSetName + "] does not exist.");
		}
		return deliveryRuleSet;
	}
	
	public DeliveryRuleSet renameDeliveryRuleSet(
		String oldDeliveryRuleSetName, 
		String newDeliveryRuleSetName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException {
		
		DeliveryRuleSet deliveryRuleSetCheck = (DeliveryRuleSet)getEntityByNaturalIdentityNullIfNotFound(newDeliveryRuleSetName);
		if (deliveryRuleSetCheck != null) {
			throw new EntityAlreadyExistsException("DeliveryRuleSet with name: [" + newDeliveryRuleSetName + "] already exists.");			
		}
		
		DeliveryRuleSet deliveryRuleSet = (DeliveryRuleSet)getEntityByNaturalIdentityNullIfNotFound(oldDeliveryRuleSetName);
		if (deliveryRuleSet == null) {
			throw new EntityDoesNotExistException("DeliveryRuleSet with name: [" + oldDeliveryRuleSetName + "] does not exist.");			
		}
		
		deliveryRuleSet.setDeliveryRuleSetName(newDeliveryRuleSetName);
		
		this.deliveryRuleSetMap.put(oldDeliveryRuleSetName, null);
		this.deliveryRuleSetMap.put(newDeliveryRuleSetName, deliveryRuleSet);
		
		return deliveryRuleSet;
	}

	public AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity) {
		
		if (naturalIdentity == null || naturalIdentity.trim().isEmpty()) {
			throw new FenixRuntimeException("naturalIdentity must be specified.");
		}
		
		DeliveryRuleSet deliveryRuleSet = this.deliveryRuleSetMap.get(naturalIdentity);
		return deliveryRuleSet;
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		
		if (entity instanceof DeliveryRuleSet == false) {
			throw new RuntimeException("Expected an instance of DeliveryRuleSet, but instead was: " + entity.getClassAndIdentity());
		}
		
		this.deliveryRuleSetMap.put(entity.getNaturalIdentity(), (DeliveryRuleSet)entity);
		return entity;
	}
	
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		
		if (entity instanceof DeliveryRuleSet == false) {
			throw new RuntimeException("Expected an instance of DeliveryRuleSet, but instead was: " + entity.getClassAndIdentity());
		}
		
		return this.deliveryRuleSetMap.remove(entity.getNaturalIdentity());
	}
	
	public String loadTestData(String filename) {
		return super.loadTestData("/orfin/delivery/" + filename);
	}
}
