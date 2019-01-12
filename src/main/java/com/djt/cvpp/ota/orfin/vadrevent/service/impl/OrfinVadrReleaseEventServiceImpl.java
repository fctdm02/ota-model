/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.vadrevent.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ExceptionIdentity;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.service.impl.AbstractService;
import com.djt.cvpp.ota.orfin.vadrevent.mapper.VadrReleaseEventDtoMapper;
import com.djt.cvpp.ota.orfin.vadrevent.mapper.VadrReleaseEventJsonConverter;
import com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease;
import com.djt.cvpp.ota.orfin.vadrevent.repository.OrfinVadrReleaseEventRepository;
import com.djt.cvpp.ota.orfin.vadrevent.service.OrfinVadrReleaseEventService;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class OrfinVadrReleaseEventServiceImpl extends AbstractService implements OrfinVadrReleaseEventService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrfinVadrReleaseEventServiceImpl.class);

	
	private VadrReleaseEventJsonConverter vadrReleaseEventJsonConverter = new VadrReleaseEventJsonConverter();
	private VadrReleaseEventDtoMapper vadrReleaseEventDtoMapper = new VadrReleaseEventDtoMapper();
	private OrfinVadrReleaseEventRepository orfinVadrReleaseEventRepository;
	
	public OrfinVadrReleaseEventServiceImpl() {
	}

	public OrfinVadrReleaseEventServiceImpl(OrfinVadrReleaseEventRepository orfinVadrReleaseEventRepository) {
		
		this.orfinVadrReleaseEventRepository = orfinVadrReleaseEventRepository;
	}

	public VadrReleaseEventJsonConverter getJsonConverter() {
		return this.vadrReleaseEventJsonConverter;
	}

	public VadrReleaseEventDtoMapper getDtoMapper() {
		return this.vadrReleaseEventDtoMapper;
	}
	
	public void setVadrReleaseRepository(OrfinVadrReleaseEventRepository orfinVadrReleaseEventRepository) {
		this.orfinVadrReleaseEventRepository = orfinVadrReleaseEventRepository;
	}
	
	
	// INHERITED BUSINESS BEHAVIORS
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		return this.orfinVadrReleaseEventRepository.updateEntity((VadrRelease)entity);
	}
		
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		return this.orfinVadrReleaseEventRepository.deleteEntity((VadrRelease)entity);
	}
			

	// BUSINESS BEHAVIORS
	public VadrRelease createVadrRelease(
		String domainName,
		String domainInstanceName,
		String domainInstanceDescription,
		String domainInstanceVersion,
		String appId,
		String appVersion,
		String productionState,
		String releaseDate,
		String softwarePriorityLevel)
	throws 
		EntityAlreadyExistsException, 
		ValidationException {
		
		String naturalIdentity = AbstractEntity.buildNaturalIdentity(
			domainName,
			domainInstanceName,
			domainInstanceVersion,
			appId,
			appVersion,
			productionState,
			releaseDate);
		
		try {
			
			LOGGER.debug("OrfinVadrReleaseEventService::createVadrRelease(): naturalIdentity: [{}].", naturalIdentity);
			
			return this.orfinVadrReleaseEventRepository.createVadrRelease(
				domainName, 
				domainInstanceName,
				domainInstanceDescription,
				domainInstanceVersion, 
				appId, 
				appVersion, 
				productionState,
				releaseDate,
				softwarePriorityLevel);
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create vadrRelease: [" + naturalIdentity +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1002", "Could not create vadrRelease: [" + naturalIdentity + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public List<VadrRelease> getAllVadrReleases() {
		
		return this.orfinVadrReleaseEventRepository.getAllVadrReleases();
	}

	public VadrRelease getVadrReleaseByName(
		String domainName,
		String domainInstanceName,
		String domainInstanceVersion,
		String appId,
		String appVersion,
		String productionState,
		String releaseDate)
	throws 
		EntityDoesNotExistException, 
		ValidationException {
		
		String naturalIdentity = AbstractEntity.buildNaturalIdentity(
			domainName,
			domainInstanceName,
			domainInstanceVersion,
			appId,
			appVersion,
			productionState,
			releaseDate);
		
		try {
			return this.orfinVadrReleaseEventRepository.getVadrReleaseByName(
				domainName, 
				domainInstanceName, 
				domainInstanceVersion, 
				appId, 
				appVersion, 
				productionState, 
				releaseDate);	
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1003", "Could not retrieve vadrRelease: [" + naturalIdentity + "] because it does not exist");
			throw ednee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1004", "Could not retrieve vadrRelease: [" + naturalIdentity + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	
	public VadrRelease updateVadrRelease(
		String vadrReleaseName)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		throw new RuntimeException("Not supported");
	}

	public void deleteVadrRelease(
		String vadrReleaseName)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		throw new RuntimeException("Not supported");
	}

	private void setExceptionIdentity(ExceptionIdentity exceptionIdentity, String uniqueErrorCode, String messageOverride) {
		
		exceptionIdentity.setBoundedContextName(BOUNDED_CONTEXT_NAME);
		exceptionIdentity.setServiceName(SERVICE_NAME);
		exceptionIdentity.setUniqueErrorCode(uniqueErrorCode);
		exceptionIdentity.setMessageOverride(messageOverride);
	}
}
