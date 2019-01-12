/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.delivery.repository;

import java.sql.Timestamp;
import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.repository.EntityRepository;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.delivery.model.enums.ConsentType;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface DeliveryRuleSetRepository extends EntityRepository {

	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "ORFIN";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "DELIVERY";

	/**
	 * 
	 * @param deliveryRuleSetName
	 * @param authorizedBy
	 * @param messageToConsumer
	 * @param consentType
	 * @param scheduledRolloutDate
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	DeliveryRuleSet createDeliveryRuleSet(
		String deliveryRuleSetName,
		String authorizedBy,
		String messageToConsumer,
		ConsentType consentType,
		Timestamp scheduledRolloutDate)
	throws 
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @return
	 */
	List<DeliveryRuleSet> getAllDeliveryRuleSets();
		
	/**
	 * 
	 * @param deliveryRuleSetName
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	DeliveryRuleSet getDeliveryRuleSetByName(
        String deliveryRuleSetName)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param oldDeliveryRuleSetName
	 * @param newDeliveryRuleSetName
	 * @return
	 * @throws EntityDoesNotExistException If the delivery rule set identified by <code>oldDeliveryRuleSetName</code> doesn't exist 
	 * @throws EntityAlreadyExistsException If a delivery rule set identified by <code>newDeliveryRuleSetName</code> already exists
	 */
	DeliveryRuleSet renameDeliveryRuleSet(
		String oldDeliveryRuleSetName, 
		String newDeliveryRuleSetName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException;
}
