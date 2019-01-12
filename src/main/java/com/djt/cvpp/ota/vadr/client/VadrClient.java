/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.vadr.client;

import com.djt.cvpp.ota.vadr.exception.VadrException;
import com.djt.cvpp.ota.vadr.mapper.DomainDtoMapper;
import com.djt.cvpp.ota.vadr.mapper.DomainJsonConverter;
import com.djt.cvpp.ota.vadr.model.Domain;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public interface VadrClient {

	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "VADR";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "COMMUNICATOR";
	
	/**
	 * 
	 * Retrieves a range of domain instances from the specified <code>domainName</code>.  The "target" domain instance contains the software that is 
	 * desired to be updated to in the vehicle.  The "prior" domain instances from the range specified by <code>numberOfPriorDomainInstancesToRetrieve</code> 
	 * represents the "out of date" software that is to be updated "from".
	 * 
	 * @param parentDomainName The name of the parent domain with which to retrieve the domain instance lineage from
	 *  
	 * @param targetDomainInstanceName The name of the target domain instance lineage to retrieve
	 * 
	 * @param targetDomainInstanceVersion The version of the target domain instance lineage to retrieve (this represents the version of the software to update vehicles to)
	 * 
	 * @param numberOfPriorDomainInstancesToRetrieve The number of prior/predecessor domain instance versions (not including the target domain instance version) to retrieve.  
	 * This represents the "out of date" software in the vehicles that need to be updated.
	 * 
	 * @param productionState The production state that each domain instance must be in.  For FENIX-FLARE, this will always be "PRODUCTION"
	 * 
	 * @param releaseState The release state that each domain instance must be in.  For FENIX-FLARE, this will always be "RELEASED"
	 * 
	 * @return The requested range of domain instances (ordered by version) going from the target version and having <code>numberOfPriorDomainInstancesToRetrieve</code>
	 * number of "prior" or "predecessor" older domains.  These predecessor domain instances are used in order to create campaign manifests and vehicle signed commands
	 * in order to be able to update vehicles with that software to the software versions specified by <code>targetDomainInstanceVersion</code>
	 * 
	 * @throws VadrException VADR-DOMAIN-1000: Could not retrieve domain instance lineage for parent domain: [parentDomainName], target domain instance: [targetDomainInstanceName + targetDomainInstanceVersion] because of reason: [reason] 
	 */
	Domain retrieveDomainInstanceLineage(
		String parentDomainName,
		String targetDomainInstanceName,
		String targetDomainInstanceVersion,
		Integer numberOfPriorDomainInstancesToRetrieve,
		String productionState,
		String releaseState)
	throws 
		VadrException;
	
	/**
	 * 
	 * @return
	 */
	DomainJsonConverter getDomainJsonConverter();

	/**
	 * 
	 * @return
	 */
	DomainDtoMapper getDomainDtoMapper();	
}
