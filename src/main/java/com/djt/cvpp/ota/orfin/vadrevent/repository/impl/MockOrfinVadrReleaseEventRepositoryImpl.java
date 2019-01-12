/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.vadrevent.repository.impl;

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
import com.djt.cvpp.ota.orfin.vadrevent.mapper.VadrReleaseEventJsonConverter;
import com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease;
import com.djt.cvpp.ota.orfin.vadrevent.repository.OrfinVadrReleaseEventRepository;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockOrfinVadrReleaseEventRepositoryImpl extends AbstractMockRepository implements OrfinVadrReleaseEventRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MockOrfinVadrReleaseEventRepositoryImpl.class);
	

	private Map<String, VadrRelease> vadrReleaseMap = new TreeMap<>();
	
	public void reset() {
		this.vadrReleaseMap.clear();
	}
	
	public MockOrfinVadrReleaseEventRepositoryImpl() {
	}
	
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

		VadrRelease vadrRelease = new VadrRelease
			.VadrReleaseBuilder()
			.withDomainName(domainName)
			.withDomainInstanceName(domainInstanceName)
			.withDomainInstanceDescription(domainInstanceDescription)
			.withDomainInstanceVersion(domainInstanceVersion)
			.withAppId(appId)
			.withAppVersion(appVersion)
			.withProductionState(productionState)
			.withProductionState(productionState)
			.withReleaseDate(releaseDate)
			.withSoftwarePriorityLevel(softwarePriorityLevel)
			.build();
		
		String naturalIdentity = vadrRelease.getNaturalIdentity();
		
		/* TODO: TDM: Temporary, as the mock VADR release event publisher keeps on processing the same file even though we tell it to ignore it
		VadrRelease vadrReleaseCheck = (VadrRelease)getEntityByNaturalIdentityNullIfNotFound(naturalIdentity);
		if (vadrReleaseCheck != null) {
			
			throw new EntityAlreadyExistsException("Cannot create vadrRelease: [" + naturalIdentity + "] because it already exists");
		}
		*/
		
		this.vadrReleaseMap.put(naturalIdentity, vadrRelease);
		
		return vadrRelease;
	}
		
	public List<VadrRelease> getAllVadrReleases() {
		
		List<VadrRelease> list = new ArrayList<>();
		list.addAll(this.vadrReleaseMap.values());
		return list;
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
		EntityDoesNotExistException {
		
		String naturalIdentity = AbstractEntity.buildNaturalIdentity(
			domainName,
			domainInstanceName,
			domainInstanceVersion,
			appId,
			appVersion,
			productionState,
			releaseDate);
		
		VadrRelease vadrRelease = (VadrRelease)getEntityByNaturalIdentityNullIfNotFound(naturalIdentity);
		if (vadrRelease == null) {
			
			// As a last resort, see if we can load this entity from "testdata" on the file system.
			try {
				String json = this.loadTestData(naturalIdentity);
				vadrRelease = new VadrReleaseEventJsonConverter().unmarshallFromJsonToEntity(json);
			} catch (Exception e) {
				LOGGER.error("Could not load from testdata area as file does not exist", e);
				vadrRelease = null;
			}
		}
		
		if (vadrRelease == null) {
			throw new EntityDoesNotExistException("VadrRelease with name: [" + naturalIdentity + "] does not exist.");
		}
		return vadrRelease;
	}
		
	public AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity) {
		
		if (naturalIdentity == null || naturalIdentity.trim().isEmpty()) {
			throw new FenixRuntimeException("naturalIdentity must be specified.");
		}
		
		VadrRelease vadrRelease = this.vadrReleaseMap.get(naturalIdentity);
		return vadrRelease;
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		
		if (entity instanceof VadrRelease == false) {
			throw new RuntimeException("Expected an instance of VadrRelease, but instead was: " + entity.getClassAndIdentity());
		}
		
		this.vadrReleaseMap.put(entity.getNaturalIdentity(), (VadrRelease)entity);
		return entity;
	}
	
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		
		if (entity instanceof VadrRelease == false) {
			throw new RuntimeException("Expected an instance of VadrRelease, but instead was: " + entity.getClassAndIdentity());
		}
		
		return this.vadrReleaseMap.remove(entity.getNaturalIdentity());
	}
	
	public String loadTestData(String filename) {
		return super.loadTestData("/orfin/vadrevent/" + filename);
	}
}
