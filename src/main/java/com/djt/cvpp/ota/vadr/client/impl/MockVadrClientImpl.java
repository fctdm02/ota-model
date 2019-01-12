/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.vadr.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.repository.impl.AbstractMockRepository;
import com.djt.cvpp.ota.vadr.client.VadrClient;
import com.djt.cvpp.ota.vadr.exception.VadrException;
import com.djt.cvpp.ota.vadr.mapper.DomainDtoMapper;
import com.djt.cvpp.ota.vadr.mapper.DomainJsonConverter;
import com.djt.cvpp.ota.vadr.model.Domain;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockVadrClientImpl extends AbstractMockRepository implements VadrClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MockVadrClientImpl.class);
	
	
	private DomainJsonConverter domainJsonConverter = new DomainJsonConverter();
	private DomainDtoMapper domainDtoMapper = new DomainDtoMapper();
	

	public void reset() {
		throw new UnsupportedOperationException("Not supported");
	}
	
	public Domain retrieveDomainInstanceLineage(
		String parentDomainName,
		String targetDomainInstanceName,
		String targetDomainInstanceVersion,
		Integer numberOfPriorDomainInstancesToRetrieve,
		String productionState,
		String releaseState)
	throws 
		VadrException {

		LOGGER.debug("MockVadrClientImpl.retrieveDomainInstanceLineage(): parentDomainName: {}, targetDomainInstanceName: {}, targetDomainInstanceVersion: {}, numberOfPriorDomainInstancesToRetrieve: {}, productionState: {}, releaseState: {}",
			parentDomainName, 
			targetDomainInstanceName, 
			targetDomainInstanceVersion, 
			numberOfPriorDomainInstancesToRetrieve, 
			productionState, 
			releaseState);

		try {
			String json = this.loadTestData("/vadr/" + parentDomainName + "__" + targetDomainInstanceName + "__" + targetDomainInstanceVersion + ".json");
			return this.domainJsonConverter.unmarshallFromJsonToEntity(json);
		} catch (Exception e) {
			throw new VadrException("Could not retrieve domain instance lineage for target domain: ["
				+ parentDomainName
				+ "], target domain instance: ["
				+ targetDomainInstanceName
				+ " "
				+ targetDomainInstanceVersion
				+ "] because of reason: ["
				+ e.getMessage()
				+ "]"
				, e);
		}
	}
	
	public DomainJsonConverter getDomainJsonConverter() {
		return domainJsonConverter;
	}

	public DomainDtoMapper getDomainDtoMapper() {
		return domainDtoMapper;
	}
	
	public AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity) {
		throw new UnsupportedOperationException("This method is not supported by VadrClient");
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		throw new UnsupportedOperationException("This method is not supported by VadrClient");
	}
	
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		throw new UnsupportedOperationException("This method is not supported by VadrClient");
	}
}
