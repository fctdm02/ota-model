/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.delivery.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ExceptionIdentity;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.service.impl.AbstractService;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEvent;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEventPublisher;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEventSubscriber;
import com.djt.cvpp.ota.orfin.delivery.mapper.DeliveryRuleSetDtoMapper;
import com.djt.cvpp.ota.orfin.delivery.mapper.DeliveryRuleSetJsonConverter;
import com.djt.cvpp.ota.orfin.delivery.model.ComplexCondition;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRule;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.delivery.model.enums.ConnectionType;
import com.djt.cvpp.ota.orfin.delivery.model.enums.ConsentType;
import com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryAudience;
import com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryMethod;
import com.djt.cvpp.ota.orfin.delivery.repository.DeliveryRuleSetRepository;
import com.djt.cvpp.ota.orfin.delivery.service.DeliveryRuleSetService;
import com.djt.cvpp.ota.orfin.vadrevent.event.OrfinVadrReleaseEvent;
import com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease;
import com.djt.cvpp.ota.orfin.vadrevent.service.OrfinVadrReleaseEventService;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class DeliveryRuleSetServiceImpl extends AbstractService implements DeliveryRuleSetService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryRuleSetServiceImpl.class);

	
	// For domain services and repositories relating to ORFIN-DELIVERY	
	private DeliveryRuleSetJsonConverter deliveryRuleSetJsonConverter = new DeliveryRuleSetJsonConverter();
	private DeliveryRuleSetDtoMapper deliveryRuleSetDtoMapper = new DeliveryRuleSetDtoMapper();
	private OrfinVadrReleaseEventService orfinVadrReleaseEventService;
	private DeliveryRuleSetRepository deliveryRuleSetRepository;
	
	// For dealing with publishing delivery rule set events
	private OrfinDeliveryRuleSetEventPublisher delegate;

	
	public DeliveryRuleSetServiceImpl() {
	}

	public DeliveryRuleSetServiceImpl(
		DeliveryRuleSetRepository deliveryRuleSetRepository,
		OrfinVadrReleaseEventService orfinVadrReleaseEventService,
		OrfinDeliveryRuleSetEventPublisher orfinDeliveryRuleSetEventPublisher) {
		
		this.deliveryRuleSetRepository = deliveryRuleSetRepository;
		this.orfinVadrReleaseEventService = orfinVadrReleaseEventService;
		this.delegate = orfinDeliveryRuleSetEventPublisher;
	}

	public DeliveryRuleSetJsonConverter getJsonConverter() {
		return this.deliveryRuleSetJsonConverter;
	}

	public DeliveryRuleSetDtoMapper getDtoMapper() {
		return this.deliveryRuleSetDtoMapper;
	}
	
	public void setDeliveryRuleSetRepository(DeliveryRuleSetRepository deliveryRuleSetRepository) {
		this.deliveryRuleSetRepository = deliveryRuleSetRepository;
	}
	
	public void setOrfinDeliveryRuleSetEventPublisher(OrfinDeliveryRuleSetEventPublisher orfinDeliveryRuleSetEventPublisher) {
		this.delegate = orfinDeliveryRuleSetEventPublisher;
	}
	
	// EVENT BASED BEHAVIORS
	public void subscribe(OrfinDeliveryRuleSetEventSubscriber orfinDeliveryRuleSetEventSubscriber) {
		this.delegate.subscribe(orfinDeliveryRuleSetEventSubscriber);
	}
	
	public void unsubscribe(OrfinDeliveryRuleSetEventSubscriber orfinDeliveryRuleSetEventSubscriber) {
		this.delegate.unsubscribe(orfinDeliveryRuleSetEventSubscriber);
	}
	
	public OrfinDeliveryRuleSetEvent publishOrfinDeliveryRuleSetEvent(
		String owner, 
		String updateAction,
		String deliveryRuleSetName,
		String nodeAcronym,
		String nodeAddress)
	throws 
		ValidationException {
		
		return this.delegate.publishOrfinDeliveryRuleSetEvent(
			owner,
			updateAction,
			deliveryRuleSetName,
			nodeAcronym,
			nodeAddress);
	}
	
	public void handleOrfinVadrReleaseEvent(OrfinVadrReleaseEvent orfinVadrReleaseEvent) throws ValidationException {
		
		LOGGER.info("Creating VADR Release for VADR Release Event: [" + orfinVadrReleaseEvent + "]");
		VadrRelease vadrRelease = null;
		try {
			
			vadrRelease = this.orfinVadrReleaseEventService.createVadrRelease(
				orfinVadrReleaseEvent.getDomainName(), 
				orfinVadrReleaseEvent.getDomainInstanceName(), 
				orfinVadrReleaseEvent.getDomainInstanceDescription(), 
				orfinVadrReleaseEvent.getDomainInstanceVersion(), 
				orfinVadrReleaseEvent.getAppId(), 
				orfinVadrReleaseEvent.getAppVersion(), 
				orfinVadrReleaseEvent.getProductionState(), 
				orfinVadrReleaseEvent.getReleaseDate(),
				orfinVadrReleaseEvent.getSoftwarePriorityLevel());
		} catch (EntityAlreadyExistsException eaee) {
			throw new FenixRuntimeException("Unable to create VADR Release from VADR Release Event because it already exists, error: " + eaee.getMessage(), eaee);
		}
			
		// If this is a critical software release, get the default delivery set, clone it, associate this VADR release to it, then publish to FENIX.
		if (vadrRelease != null && vadrRelease.getSoftwarePriorityLevel().trim().equalsIgnoreCase("CRITICAL_PRIORITY")) {
			
			DeliveryRuleSet defaultDeliveryRuleSet = null;
			try {
				defaultDeliveryRuleSet = this.getDefaultDeliveryRuleSet();
			} catch (EntityDoesNotExistException ednee) {
				throw new FenixRuntimeException("Unable to create VADR Release from VADR Release Event because the 'DEFAULT' delivery rule set does not exist, error: " + ednee.getMessage(), ednee);
			}
			
			if (defaultDeliveryRuleSet != null) {

				Set<VadrRelease> vadrReleases = new TreeSet<>();
				vadrReleases.add(vadrRelease);
				
				DeliveryRuleSet deliveryRuleSet = new DeliveryRuleSet
					.DeliveryRuleSetBuilder()
					.withDeliveryRuleSetName(vadrRelease.getNaturalIdentity())
					.withAuthorizedBy(defaultDeliveryRuleSet.getAuthorizedBy())
					.withMessageToConsumer(defaultDeliveryRuleSet.getMessageToConsumer())
					.withConsentType(defaultDeliveryRuleSet.getConsentType())
					.withComplexConditions(defaultDeliveryRuleSet.getComplexConditions())
					.withDeliveryRules(defaultDeliveryRuleSet.getDeliveryRules())
					.withProgramModelYears(defaultDeliveryRuleSet.getProgramModelYears())
					.withScheduledRolloutDate(AbstractEntity.getTimeKeeper().getCurrentTimestamp())
					.withVadrReleases(vadrReleases)
					.build();
				this.deliveryRuleSetRepository.updateEntity(deliveryRuleSet);
				
				this.publishOrfinDeliveryRuleSetEvent("Digital Jukebox Technologies. LLC.", OrfinDeliveryRuleSetEvent.SOFTWARE_UPDATE, deliveryRuleSet.getDeliveryRuleSetName(), null, null);				
			}
		}
	}
	
	
	// INHERITED BUSINESS BEHAVIORS
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		return this.deliveryRuleSetRepository.updateEntity((DeliveryRuleSet)entity);
	}
		
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		return this.deliveryRuleSetRepository.deleteEntity((DeliveryRuleSet)entity);
	}
			

	// BUSINESS BEHAVIORS
	public DeliveryRuleSet createDeliveryRuleSet(
		String deliveryRuleSetName,
		String authorizedBy,
		String messageToConsumer,
		ConsentType consentType,
		Timestamp scheduledRolloutDate)
	throws 
		EntityAlreadyExistsException, 
		ValidationException {
		
		try {
			
			LOGGER.debug("DeliveryRuleSetServiceImpl::createDeliveryRuleSet(): deliveryRuleSetName: [{}].", deliveryRuleSetName);
			
			return this.deliveryRuleSetRepository.createDeliveryRuleSet(
				deliveryRuleSetName, 
				authorizedBy, 
				messageToConsumer, 
				consentType, 
				scheduledRolloutDate);
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create delivery rule set: [" + deliveryRuleSetName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1002", "Could not create delivery rule set: [" + deliveryRuleSetName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public DeliveryRuleSet createDeliveryRule(
		String parentDeliveryRuleSetName,
		Boolean allowable,
		Integer precedenceLevel,
		DeliveryAudience deliveryAudience,
		DeliveryMethod deliveryMethod,
		ConnectionType connectionType)
	throws 
		EntityAlreadyExistsException,
		EntityDoesNotExistException,
		ValidationException {

		try {
			
			LOGGER.debug("DeliveryRuleSetServiceImpl::createDeliveryRule(): parentDeliveryRuleSetName: [{}].", parentDeliveryRuleSetName);
			
			DeliveryRuleSet deliveryRuleSet = this.getDeliveryRuleSetByName(parentDeliveryRuleSetName);
			
			DeliveryRule deliveryRule = new DeliveryRule
				.DeliveryRuleBuilder()
				.withDeliveryAudience(deliveryAudience)
				.withDeliveryMethod(deliveryMethod)
				.withConnectionType(connectionType)
				.withAllowable(allowable)
				.withPrecedenceLevel(precedenceLevel)
				.withParentDeliveryRuleSet(deliveryRuleSet)
				.build();

			deliveryRuleSet.addDeliveryRule(deliveryRule);
			
			this.deliveryRuleSetRepository.updateEntity(deliveryRuleSet);
			
			return deliveryRuleSet;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create delivery rule for delivery rule set: [" + parentDeliveryRuleSetName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1002", "Could not create delivery rule for delivery rule set: [" + parentDeliveryRuleSetName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public DeliveryRuleSet createComplexCondition(
		String parentDeliveryRuleSetName,
		String complexConditionName,
		String complexConditionValue)
	throws 
		EntityAlreadyExistsException,
		EntityDoesNotExistException,
		ValidationException {

		try {
			
			LOGGER.debug("DeliveryRuleSetServiceImpl::createComplexCondition(): parentDeliveryRuleSetName: [{}].", parentDeliveryRuleSetName);
			
			DeliveryRuleSet deliveryRuleSet = this.getDeliveryRuleSetByName(parentDeliveryRuleSetName);
			
			ComplexCondition complexCondition = new ComplexCondition
				.ComplexConditionBuilder()
				.withComplexConditionName(complexConditionName)
				.withComplexConditionValue(complexConditionValue)
				.withParentDeliveryRuleSet(deliveryRuleSet)
				.build();

			deliveryRuleSet.addComplexCondition(complexCondition);
			
			this.deliveryRuleSetRepository.updateEntity(deliveryRuleSet);
			
			return deliveryRuleSet;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create complex condition for delivery rule set: [" + parentDeliveryRuleSetName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1002", "Could not create complex condition for delivery rule set: [" + parentDeliveryRuleSetName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public DeliveryRuleSet associateVadrReleaseToDeliveryRuleSet(
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
		ValidationException {

		try {
			
			LOGGER.debug("DeliveryRuleSetServiceImpl::createComplexCondition(): associateVadrReleaseToDeliveryRuleSet: [{}].", parentDeliveryRuleSetName);
			
			DeliveryRuleSet deliveryRuleSet = this.getDeliveryRuleSetByName(parentDeliveryRuleSetName);
			
			VadrRelease vadrRelease = this.orfinVadrReleaseEventService.getVadrReleaseByName(
				domainName, 
				domainInstanceName, 
				domainInstanceVersion, 
				appId, 
				appVersion, 
				productionState, 
				releaseDate);
			
			deliveryRuleSet.addVadrRelease(vadrRelease);
			
			this.deliveryRuleSetRepository.updateEntity(deliveryRuleSet);
			
			return deliveryRuleSet;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not associate VADR release to delivery rule set: [" + parentDeliveryRuleSetName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1002", "Could not associate VADR release to delivery rule set: [" + parentDeliveryRuleSetName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public List<DeliveryRuleSet> getAllDeliveryRuleSets() {
		
		return this.deliveryRuleSetRepository.getAllDeliveryRuleSets();
	}

	public DeliveryRuleSet getDeliveryRuleSetByName(String deliveryRuleSetName) throws EntityDoesNotExistException, ValidationException {
		
		try {
			return this.deliveryRuleSetRepository.getDeliveryRuleSetByName(deliveryRuleSetName);	
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1003", "Could not retrieve delivery rule set: [" + deliveryRuleSetName + "] because it does not exist");
			throw ednee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1004", "Could not retrieve delivery rule set: [" + deliveryRuleSetName + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public DeliveryRuleSet getDefaultDeliveryRuleSet()
	throws 
		EntityDoesNotExistException,
		ValidationException {

		return this.deliveryRuleSetRepository.getDeliveryRuleSetByName(DeliveryRuleSet.DEFAULT_DELIVERY_RULE_SET_NAME);
	}
	
	public DeliveryRuleSet renameDeliveryRuleSet(
		String oldDeliveryRuleSetName, 
		String newDeliveryRuleSetName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException {
		
		try {

			return this.deliveryRuleSetRepository.renameDeliveryRuleSet(oldDeliveryRuleSetName, newDeliveryRuleSetName);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1010", "Could not rename delivery rule set: [" + oldDeliveryRuleSetName + "] because it doesn't exist");
			throw ednee;
	
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1011", "Could not rename delivery rule set: [" + oldDeliveryRuleSetName + "] because there already exists a delivery rule set with name: [" + newDeliveryRuleSetName + "]");
			throw eaee;
		}
	}
	
	public DeliveryRuleSet updateDeliveryRuleSet(
		String deliveryRuleSetName,
		String authorizedBy,
		String messageToConsumer,
		ConsentType consentType,
		Timestamp scheduledRolloutDate,
		Boolean isDefault)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		try {

			DeliveryRuleSet deliveryRuleSet = this.getDeliveryRuleSetByName(deliveryRuleSetName);
			
			deliveryRuleSet.setAuthorizedBy(authorizedBy);
			deliveryRuleSet.setMessageToConsumer(messageToConsumer);
			deliveryRuleSet.setConsentType(consentType);
			deliveryRuleSet.setScheduledRolloutDate(scheduledRolloutDate);

			LOGGER.debug("DeliveryRuleSetServiceImpl::updateDeliveryRuleSet(): deliveryRuleSetName: [{}].", deliveryRuleSetName);
			this.deliveryRuleSetRepository.updateEntity(deliveryRuleSet);
			
			return deliveryRuleSet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1012", "Could not update delivery rule set: [" + deliveryRuleSetName + "] because it doesn't exist");
			throw ednee;
	
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1013", "Could not update delivery rule set: [" + deliveryRuleSetName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public void deleteDeliveryRuleSet(
		String deliveryRuleSetName)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		try {

			LOGGER.debug("DeliveryRuleSetServiceImpl::deleteDeliveryRuleSet(): deliveryRuleSetName: [{}].", deliveryRuleSetName);
			DeliveryRuleSet deliveryRuleSet = this.getDeliveryRuleSetByName(deliveryRuleSetName);
			this.deliveryRuleSetRepository.deleteEntity(deliveryRuleSet);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1014", "Could not delete delivery rule set: [" + deliveryRuleSetName + "] because it does not exist");
			throw ednee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1015", "Could not delete delivery rule set: [" + deliveryRuleSetName + "] because the specified delivery rule set name is invalid");
			throw ve;
		}
	}

	private void setExceptionIdentity(ExceptionIdentity exceptionIdentity, String uniqueErrorCode, String messageOverride) {
		
		exceptionIdentity.setBoundedContextName(BOUNDED_CONTEXT_NAME);
		exceptionIdentity.setServiceName(SERVICE_NAME);
		exceptionIdentity.setUniqueErrorCode(uniqueErrorCode);
		exceptionIdentity.setMessageOverride(messageOverride);
	}
}
