/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.delivery.service;

import java.sql.Timestamp;
import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.service.EntityService;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEventPublisher;
import com.djt.cvpp.ota.orfin.delivery.mapper.DeliveryRuleSetDtoMapper;
import com.djt.cvpp.ota.orfin.delivery.mapper.DeliveryRuleSetJsonConverter;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.delivery.model.enums.ConnectionType;
import com.djt.cvpp.ota.orfin.delivery.model.enums.ConsentType;
import com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryAudience;
import com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryMethod;
import com.djt.cvpp.ota.orfin.delivery.repository.DeliveryRuleSetRepository;
import com.djt.cvpp.ota.orfin.vadrevent.event.OrfinVadrReleaseEventSubscriber;

/**
 * 
 * This service interfaces deals with basic CRUD operations for the DeliveryRuleSet
 * aggregate root, as well as all the "child" entities (and specializations of)
 * </p>
 * This interface, takes as input, primitives from a higher layer, e.g. a Controller
 * or Application Service, performs whatever operation requested (which for this 
 * interfaces, includes publishing delivery rule set events), and returns the created, updated
 * or requested domain objects.  
 * </p>
 * For any information being transferred to another layer, it is recommended that the JSON
 * Converter, which is available in this interfae, be used to map entities to DTOs, and/or
 * map these DTOs to JSON. 
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface DeliveryRuleSetService extends EntityService, OrfinDeliveryRuleSetEventPublisher, OrfinVadrReleaseEventSubscriber {
	
	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "ORFIN";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "DELIVERY";

	/**
	 * 
	 * @param deliveryRuleSetName The unique name of the delivery rule set, specified by <code>deliveryRuleSetName</code>
	 * @param authorizedBy
	 * @param messageToConsumer
	 * @param consentType
	 * @param scheduledRolloutDate
	 * 
	 * @return The newly created delivery rule set
	 * 
	 * @throws EntityAlreadyExistsException ORFIN-DELIVERY-1001: Could not create delivery rule set: [deliveryRuleSetName] because it already exists
	 * @throws ValidationException ORFIN-DELIVERY-1002: Could not create delivery rule set because attribute: [attributeName] was invalid for reason: [reason] 
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
	 * @param parentDeliveryRuleSetName
	 * @param allowable
	 * @param precedenceLevel
	 * @param deliveryAudience
	 * @param deliveryMethod
	 * @param connectionType
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	DeliveryRuleSet createDeliveryRule(
		String parentDeliveryRuleSetName,
		Boolean allowable,
		Integer precedenceLevel,
		DeliveryAudience deliveryAudience,
		DeliveryMethod deliveryMethod,
		ConnectionType connectionType)
	throws 
		EntityAlreadyExistsException,
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * @param parentDeliveryRuleSetName
	 * @param complexConditionName
	 * @param complexConditionValue
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	DeliveryRuleSet createComplexCondition(
		String parentDeliveryRuleSetName,
		String complexConditionName,
		String complexConditionValue)
	throws 
		EntityAlreadyExistsException,
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param parentDeliveryRuleSetName
	 * @param domainName
	 * @param domainInstanceName
	 * @param domainInstanceDescription
	 * @param domainInstanceVersion
	 * @param appId
	 * @param appVersion
	 * @param productionState
	 * @param releaseDate in MM-DD-YYYY format
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	DeliveryRuleSet associateVadrReleaseToDeliveryRuleSet(
		String parentDeliveryRuleSetName,
		String domainName,
		String domainInstanceName,
		String domainInstanceDescription,
		String domainInstanceVersion,
		String appId,
		String appVersion,
		String productionState,
		String releaseDate)
	throws 
		EntityAlreadyExistsException,
		EntityDoesNotExistException,
		ValidationException;
		
	/**
	 * 
	 * @return All delivery rule sets that have been created.
	 */
	List<DeliveryRuleSet> getAllDeliveryRuleSets();

	/**
	 * 
	 * @param deliveryRuleSetName The unique name of the delivery rule set.
	 * 
	 * @return The delivery rule set with the name specified by <code>deliveryRuleSetName</code>
	 * 
	 * @throws EntityDoesNotExistException ORFIN-DELIVERY-1003: Could not retrieve delivery rule set: [deliveryRuleSetName] because it does not exist
	 * @throws ValidationException ORFIN-DELIVERY-1004: Could not retrieve delivery rule set: [deliveryRuleSetName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	DeliveryRuleSet getDeliveryRuleSetByName(
        String deliveryRuleSetName)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * It is assumed that there is only ONE delivery rule set that is marked as default.  Otherwise, the first one is returned.
	 * 
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	DeliveryRuleSet getDefaultDeliveryRuleSet()
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * @param oldDeliveryRuleSetName The unique name of the delivery rule set to change the name of
	 * @param newDeliveryRuleSetName The new unique name of the delivery rule set that is to be changed.
	 * 
	 * @return The affected delivery rule set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-DELIVERY-1010: Could not rename delivery rule set: [oldDeliveryRuleSetName] because it doesn't exist
	 * @throws EntityAlreadyExistsException ORFIN-DELIVERY-1011: Could not rename delivery rule set: [oldDeliveryRuleSetName] because there already exists a delivery rule set with name: [newDeliveryRuleSetName]
	 */
	DeliveryRuleSet renameDeliveryRuleSet(
		String oldDeliveryRuleSetName, 
		String newDeliveryRuleSetName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException;
	
	/**
	 * 
	 * @param deliveryRuleSetName The unique name of the delivery rule set to update
	 * @param authorizedBy
	 * @param messageToConsumer
	 * @param consentType
	 * @param scheduledRolloutDate
	 * @param isDefault
	 * 
	 * @return The affected delivery rule set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-DELIVERY-1012: Could not update delivery rule set: [deliveryRuleSetName] because it does not exist 
	 * @throws ValidationException ORFIN-DELIVERY-1013: Could not update delivery rule set: [deliveryRuleSetName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	DeliveryRuleSet updateDeliveryRuleSet(
		String deliveryRuleSetName,
		String authorizedBy,
		String messageToConsumer,
		ConsentType consentType,
		Timestamp scheduledRolloutDate,
		Boolean isDefault)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * @param deliveryRuleSetName The name of the delivery rule set to delete
	 * 
	 * @throws EntityDoesNotExistException ORFIN-DELIVERY-1014: Could not delete delivery rule set: [deliveryRuleSetName] because it does not exist
	 * @throws ValidationException ORFIN-DELIVERY-1015: Could not delete delivery rule set: [deliveryRuleSetName] because the specified delivery rule set name is invalid
	 */
	void deleteDeliveryRuleSet(
		String deliveryRuleSetName) 
	throws 
		EntityDoesNotExistException,
		ValidationException; 

	
	
	// NON-FUNCTIONAL RELATED METHODS
	/**
	 * Used for setter-based dependency injection
	 * 
	 * @param deliveryRuleSetRepository
	 */
	void setDeliveryRuleSetRepository(DeliveryRuleSetRepository deliveryRuleSetRepository);

	/**
	 * Used for setter-based dependency injection
	 * 
	 * @param orfinDeliveryRuleSetEventPublisher
	 */
	void setOrfinDeliveryRuleSetEventPublisher(OrfinDeliveryRuleSetEventPublisher orfinDeliveryRuleSetEventPublisher);

	/**
	 * Used to marshall entities to JSON (via DTO Mappers), and vice-versa, where applicable.
	 *  
	 * @return
	 */
	DeliveryRuleSetJsonConverter getJsonConverter();
	
	/**
	 * 
	 * Used to marshall entities to DTOs.  It is expected that the application/controller layer will convert everything to DTOs at their level 
	 * and marshall everything to JSON for over the wire transfer
	 *  
	 * @return
	 */
	DeliveryRuleSetDtoMapper getDtoMapper();
}
