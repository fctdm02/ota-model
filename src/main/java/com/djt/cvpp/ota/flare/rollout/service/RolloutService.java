/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.rollout.service;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.service.EntityService;
import com.djt.cvpp.ota.flare.rollout.mapper.RolloutDtoMapper;
import com.djt.cvpp.ota.flare.rollout.mapper.RolloutJsonConverter;
import com.djt.cvpp.ota.flare.rollout.mapper.dto.RolloutProgress;
import com.djt.cvpp.ota.flare.rollout.model.AbstractRollout;
import com.djt.cvpp.ota.flare.rollout.model.AddApplicationRollout;
import com.djt.cvpp.ota.flare.rollout.model.ApplicationUpdateRollout;
import com.djt.cvpp.ota.flare.rollout.model.DirectConfigurationOnlyRollout;
import com.djt.cvpp.ota.flare.rollout.model.OdlUpdateRollout;
import com.djt.cvpp.ota.flare.rollout.model.PolicyTableUpdateRollout;
import com.djt.cvpp.ota.flare.rollout.model.RemoveApplicationRollout;
import com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout;
import com.djt.cvpp.ota.flare.rollout.model.SoftwareWithDirectConfigurationRollout;
import com.djt.cvpp.ota.flare.rollout.model.SuspendSoftwareRollout;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEventSubscriber;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEventSubscriber;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEventSubscriber;
import com.djt.cvpp.ota.tmc.bytestream.exception.TmcByteStreamException;
import com.djt.cvpp.ota.tmc.deployment.exception.TmcDeploymentException;
import com.djt.cvpp.ota.tmc.vehiclediscovery.exception.TmcResourceException;
import com.djt.cvpp.ota.tmc.vehiclestatus.event.TmcVehicleStatusMessageEventSubscriber;
import com.djt.cvpp.ota.vadr.exception.VadrException;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface RolloutService extends 
	EntityService, 
	TmcVehicleStatusMessageEventSubscriber,
	OrfinDeliveryRuleSetEventSubscriber, 
	OrfinOdlEventSubscriber, 
	OrfinPolicySetEventSubscriber {
	
	
	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "FLARE";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "ROLLOUT";
	
		
	/**
	 * 
	 * @param rolloutName The unique name of the rollout to create, specified by <code>rolloutName</code>
	 * @param deliveryRuleSetName The name of the delivery rule set which contains the software to be delivered, with the delivery rules/constraints 
	 * 
	 * @return The newly created rollout
	 * 
	 * @throws EntityAlreadyExistsException FLARE-ROLLOUT-1001: Could not create rollout: [rolloutName] because it already exists
	 * @throws EntityDoesNotExistException FLARE-ROLLOUT-1002: Could not create rollout: [rolloutName] because rollout action with UUID: [rolloutActionUUID] does not exist
	 * @throws ValidationException FLARE-ROLLOUT-1003: Could not create rollout because attribute: [attributeName] was invalid for reason: [reason] 
	 */
	SoftwareUpdateRollout createSoftwareUpdateRollout(
		String rolloutName,
		String deliveryRuleSetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param rolloutName
	 * @param deliveryRuleSetName
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	ApplicationUpdateRollout createApplicationUpdateRollout(
		String rolloutName,
		String deliveryRuleSetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param rolloutName
	 * @param deliveryRuleSetName
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	AddApplicationRollout createAddApplicationRollout(
		String rolloutName,
		String deliveryRuleSetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param rolloutName
	 * @param deliveryRuleSetName
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	RemoveApplicationRollout createRemoveApplicationRollout(
		String rolloutName,
		String deliveryRuleSetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param rolloutName
	 * @param deliveryRuleSetName
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	SuspendSoftwareRollout createSuspendSoftwareRollout(
		String rolloutName,
		String deliveryRuleSetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @param rolloutName
	 * @param deliveryRuleSetName
	 * @param nodeAcronym
	 * @param nodeAddress
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	SoftwareWithDirectConfigurationRollout createSoftwareWithDirectConfigurationRollout(
		String rolloutName,
		String deliveryRuleSetName,
		String nodeAcronym,
		String nodeAddress)
	throws 
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param rolloutName
	 * @param nodeAcronym
	 * @param nodeAddress
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	DirectConfigurationOnlyRollout createDirectConfigurationOnlyRollout(
		String rolloutName,
		String nodeAcronym,
		String nodeAddress)
	throws 
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @param rolloutName
	 * @param programCode
	 * @param modelYear
	 * @param regionCode
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	OdlUpdateRollout createOdlUpdateRollout(
		String rolloutName,
		String programCode,
		Integer modelYear,
		String regionCode)
	throws 
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param rolloutName
	 * @param programCode
	 * @param modelYear
	 * @param regionCode
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	PolicyTableUpdateRollout createPolicyTableUpdateRollout(
		String rolloutName,
		String programCode,
		Integer modelYear,
		String regionCode)
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
	 * @param rolloutName The unique name of the rollout
	 * 
	 * @return The requested rollout
	 * 
	 * @throws EntityDoesNotExistException FLARE-ROLLOUT-1004: Could not retrieve rollout: [rolloutName] because it does not exist
	 */
	AbstractRollout getRolloutByName(
        String rolloutName)
	throws 
		EntityDoesNotExistException;

	/**
	 * 
	 * @param rolloutName
	 * @return
	 * @throws EntityDoesNotExistException
	 */
	RolloutProgress getRolloutProgress(
        String rolloutName)
	throws 
		EntityDoesNotExistException;
	
	/**
	 * 
	 * @param rolloutName
	 * @param the delivery rule set associated with the specfied rollout
	 * 
	 * @throws EntityDoesNotExistException FLARE-ROLLOUT-1005: Could not perform pre-processing on rollout: [rolloutName] because it does not exist
	 * @throws ValidationException FLARE-ROLLOUT-1006: Could not perform pre-processing on rollout: [rolloutName] because attribute: [attributeName] was invalid for reason: [reason]
	 * @throws VadrException
	 * @throws TmcResourceException
	 */
	void performPreProcessing(
		String rolloutName,
		com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet deliveryRuleSet) 
	throws
		EntityDoesNotExistException,
		ValidationException,
		VadrException,
		TmcResourceException;

	/**
	 * Generated Content can be one of the following:
	 * <ul>
	 *    <li>Generic Manifests (campaign level)</li>
	 *    <li>Signed Commands (vehicle level)</li>
	 *    <li>OTA AbstractPolicy Tables (program/region level)</li>
	 *    <li>ODL Updates (of program level)</li>
	 *  </ul>  
	 * 
	 * @param rolloutName The unique name of the rollout
	 * 
	 * @throws EntityDoesNotExistException FLARE-ROLLOUT-1007: Could not upload generated content for rollout: [rolloutName] because it does not exist
	 * @throws TmcByteStreamException FLARE-ROLLOUT-1008: Could not upload generated content for rollout: [rolloutName] because of TMC Byte Stream error: [tmcByteStreamException]
	 */
	void uploadGeneratedContentToTmcByteStream(
		String rolloutName)
	throws
		EntityDoesNotExistException,
		TmcByteStreamException,
		ValidationException;

	/**
	 * 
	 * @param rolloutName
	 * @param initiatedBy
	 * 
	 * @throws EntityDoesNotExistException FLARE-ROLLOUT-1009: Could not initiate rollout: [rolloutName] because it does not exist
	 * @throws ValidationException FLARE-ROLLOUT-1010: Could not initiate rollout: [rolloutName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	void initiateRollout(
		String rolloutName,
		String initiatedBy) 
	throws 
		EntityDoesNotExistException, 
		ValidationException;

	/**
	 * 
	 * @param rolloutName The unique name of the rollout
	 * 
	 * @throws EntityDoesNotExistException FLARE-ROLLOUT-1011: Could not evaluate state for rollout: [rolloutName] because it does not exist
	 * @throws ValidationException FLARE-ROLLOUT-1012: Could not evaluate state for rollout: [rolloutName] because attribute: [attributeName] was invalid for reason: [reason]
	 * @throws TmcDeploymentException
	 */
	void evaluateRolloutState(
		String rolloutName) 
	throws 
		EntityDoesNotExistException, 
		ValidationException,
		TmcDeploymentException;
	
	/**
	 * 
	 * @param rolloutName The unique name of the rollout
	 * 
	 * @throws EntityDoesNotExistException FLARE-ROLLOUT-1013: Could not pause rollout: [rolloutName] because it does not exist
	 * @throws ValidationException FLARE-ROLLOUT-1014: Could not pause rollout: [rolloutName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	void pauseRollout(
		String rolloutName) 
	throws 
		EntityDoesNotExistException, 
		ValidationException;

	/**
	 * 
	 * @param rolloutName The unique name of the rollout
	 * 
	 * @throws EntityDoesNotExistException FLARE-ROLLOUT-1015: Could not resume rollout: [rolloutName] because it does not exist
	 * @throws ValidationException FLARE-ROLLOUT-1016: Could not resume rollout: [rolloutName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	void resumeRollout(
		String rolloutName) 
	throws 
		EntityDoesNotExistException, 
		ValidationException;

	/**
	 * 
	 * @param rolloutName The unique name of the rollout
	 * 
	 * @throws EntityDoesNotExistException FLARE-ROLLOUT-1017: Could not cancel rollout: [rolloutName] because it does not exist
	 */
	void cancelRollout(
		String rolloutName) 
	throws 
		EntityDoesNotExistException;
	
	/**
	 * 
	 * @return
	 */
	RolloutJsonConverter getJsonConverter();
	
	/**
	 * 
	 * Used to marshall entities to DTOs.  It is expected that the application/controller layer will convert everything to DTOs at their level 
	 * and marshall everything to JSON for over the wire transfer
	 *  
	 * @return
	 */
	RolloutDtoMapper getDtoMapper();
}
