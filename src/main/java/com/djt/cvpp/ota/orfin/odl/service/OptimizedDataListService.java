/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.service;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.service.EntityService;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEventPublisher;
import com.djt.cvpp.ota.orfin.odl.mapper.OdlDtoMapper;
import com.djt.cvpp.ota.orfin.odl.mapper.OdlJsonConverter;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.odl.repository.OptimizedDataListRepository;
import com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear;

/**
 * 
 * This service interfaces deals with basic CRUD operations for the VadrRelease
 * aggregate root, as well as all the "child" entities (and specializations of)
 * </p>
 * This interface, takes as input, primitives from a higher layer, e.g. a Controller
 * or Application Service, performs whatever operation requested (which for this 
 * interfaces, includes publishing odl events), and returns the created, updated
 * or requested domain objects.  
 * </p>
 * For any information being transferred to another layer, it is recommended that the JSON
 * Converter, which is available in this interfae, be used to map entities to DTOs, and/or
 * map these DTOs to JSON. 
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface OptimizedDataListService extends EntityService, OrfinOdlEventPublisher {
	
	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "ORFIN";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "ODL";

	/**
	 * 
	 * @param odlName The unique name of the odl, specified by <code>odlName</code>
	 * 
	 * @return The newly created odl
	 * 
	 * @throws EntityAlreadyExistsException ORFIN-ODL-1001: Could not create odl: [odlName] because it already exists
	 * @throws ValidationException ORFIN-ODL-1002: Could not create odl because attribute: [attributeName] was invalid for reason: [reason] 
	 */
	Odl createOdl(
		String odlName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param odlName
	 * @param networkName
	 * @param protocol
	 * @param dataRate
	 * @param dclName
	 * @param networkPins
	 * 
	 * @return
	 * 
	 * @throws EntityDoesNotExistException
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	Odl createNetwork(
		String odlName,
		String networkName,
		String protocol,
		String dataRate,
		String dclName,
		String networkPins)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param odlName
	 * @param networkName
	 * @param nodeAcronym
	 * @param nodeAddress
	 * @param gatewayNodeId
	 * @param gatewayType
	 * @param hasConditionBasedOnDtc
	 * @param isOvtp
	 * @param ovtpDestinationAddress
	 * @param specificationCategoryType
	 * @param diagnosticSpecificationResponse
	 * @param activationTime
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	Odl createNode(
		String odlName,
		String networkName,
		String nodeAcronym,
		String nodeAddress,
		String gatewayNodeId,
		String gatewayType,
		Boolean hasConditionBasedOnDtc,
		Boolean isOvtp,
		String ovtpDestinationAddress,
		String specificationCategoryType,
		Integer diagnosticSpecificationResponse,
		Integer activationTime)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param odlName
	 * @param networkName
	 * @param nodeAcronym
	 * @param nodeAddress
	 * @param ignoredDids
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	Odl addIgnoredDidsToNode(
		String odlName,
		String networkName,
		String nodeAcronym,
		String nodeAddress,
		List<String> ignoredDids)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param odlName
	 * @param networkName
	 * @param nodeAcronym
	 * @param nodeAddress
	 * @param didName
	 * @param description
	 * @param vinSpecificDidFlag
	 * @param directConfigurationDidFlag
	 * @param privateNetworkDidFlag
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	Odl createDid(
		String odlName,
		String networkName,
		String nodeAcronym,
		String nodeAddress,
		String didName,
		String description,
		Boolean vinSpecificDidFlag,
		Boolean directConfigurationDidFlag,
		Boolean privateNetworkDidFlag)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param odlName
	 * @param ecgSignalName
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	Odl addEcgSignalToOdl(
		String odlName,
		String ecgSignalName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param odlName
	 * @param customOdlName
	 * @param customOdlNodeList A list of nodes where each node is of the form: <code>nodeAcronym_nodeAddress</code> and node *must* already be associated to the parent, or master, ODL
	 * e.g. AA_00,BB_01,CC_02
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws EntityAlreadyExistsException
	 * @throws ValidationException
	 */
	Odl addCustomOdlToOdl(
		String odlName,
		String customOdlName,
		List<String> customOdlNodeList)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @return All odls that have been created.
	 */
	List<Odl> getAllOdls();

	/**
	 * 
	 * @param odlName The unique name of the odl.
	 * 
	 * @return The odl with the name specified by <code>odlName</code>
	 * 
	 * @throws EntityDoesNotExistException ORFIN-ODL-1003: Could not retrieve odl: [odlName] because it does not exist
	 * @throws ValidationException ORFIN-ODL-1004: Could not retrieve odl: [odlName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	Odl getOdlByName(
        String odlName)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The odl with the name specified by <code>programCode</code> and <code>modelYear</code>
	 * 
	 * @throws EntityDoesNotExistException ORFIN-ODL-1003: Could not retrieve odl by programCodeModelYear: [programCode+modelYear] because it does not exist
	 * @throws ValidationException ORFIN-ODL-1004: Could not retrieve odl by programCodeModelYear: [programCode+modelYear] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	Odl getOdlByProgramCodeAndModelYear(
        String programCode,
        Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * @param oldOdlName The unique name of the odl to change the name of
	 * @param newOdlName The new unique name of the odl that is to be changed.
	 * 
	 * @return The affected odl
	 * 
	 * @throws EntityDoesNotExistException ORFIN-ODL-1010: Could not rename odl: [oldOdlName] because it doesn't exist
	 * @throws EntityAlreadyExistsException ORFIN-ODL-1011: Could not rename odl: [oldOdlName] because there already exists a odl with name: [newOdlName]
	 */
	Odl renameOdl(
		String oldOdlName, 
		String newOdlName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException;
	
	/**
	 * @param odlName The unique name of the odl to update
	 * 
	 * @return The affected odl
	 * 
	 * @throws EntityDoesNotExistException ORFIN-ODL-1012: Could not update odl: [odlName] because it does not exist 
	 * @throws ValidationException ORFIN-ODL-1013: Could not update odl: [odlName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	Odl updateOdl(
		String odlName)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * @param odlName The name of the odl to delete
	 * 
	 * @throws EntityDoesNotExistException ORFIN-ODL-1014: Could not delete odl: [odlName] because it does not exist
	 * @throws ValidationException ORFIN-ODL-1015: Could not delete odl: [odlName] because the specified odl name is invalid
	 */
	void deleteOdl(
		String odlName) 
	throws 
		EntityDoesNotExistException,
		ValidationException; 
	
	/**
	 * 
	 * Convenience method for the other "render" operation, with *all* nodes processed and *all* defined ECG signals included.
	 * 
	 * @param programModelYear The program and model year to render the ODL JSON for delivery to the vehicle
	 * 
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	String renderFullOdlWithEcgSignalsForProgram(
		ProgramModelYear programModelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * @param programModelYear The program and model year to render the ODL JSON for delivery to the vehicle
	 * @param customOdlName If specified, then the rendered ODL JSON will only include those networks/nodes that 
	 * specified by the "custom ODL", identified by <code>customOdlName</code>
	 * @param includeEcgSignals If <code>true</code>, then the rendered ODL JSON will include ECG signal information.
	 * 
	 * @return
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 */
	String renderOdlForProgram(
		ProgramModelYear programModelYear,
		String customOdlName,
		Boolean includeEcgSignals)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	
	// NON-FUNCTIONAL RELATED METHODS
	/**
	 * Used for setter-based dependency injection
	 * 
	 * @param orfinVadrReleaseEventRepository
	 */
	void setOdlRepository(OptimizedDataListRepository optimizedDataListRepository);

	/**
	 * Used for setter-based dependency injection
	 * 
	 * @param orfinOdlEventPublisher
	 */
	void setOrfinOdlEventPublisher(OrfinOdlEventPublisher orfinOdlEventPublisher);

	/**
	 * Used to marshall entities to JSON (via DTO Mappers), and vice-versa, where applicable.
	 *  
	 * @return
	 */
	OdlJsonConverter getJsonConverter();
	
	/**
	 * 
	 * Used to marshall entities to DTOs.  It is expected that the application/controller layer will convert everything to DTOs at their level 
	 * and marshall everything to JSON for over the wire transfer
	 *  
	 * @return
	 */
	OdlDtoMapper getDtoMapper();
}
