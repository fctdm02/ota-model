/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.program.service;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.service.EntityService;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.delivery.service.DeliveryRuleSetService;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.odl.service.OptimizedDataListService;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.orfin.policy.service.PolicySetService;
import com.djt.cvpp.ota.orfin.program.mapper.ProgramModelYearDtoMapper;
import com.djt.cvpp.ota.orfin.program.mapper.ProgramModelYearJsonConverter;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;
import com.djt.cvpp.ota.orfin.program.repository.ProgramModelYearRepository;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface ProgramModelYearService extends EntityService {
	
	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "ORFIN";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "PROGRAM";
	
	/**
	 * 
	 * @param programModelYearRepository
	 */
	void setProgramModelYearRepository(ProgramModelYearRepository programModelYearRepository);

	/**
	 * 
	 * @param deliveryRuleSetService
	 */
	void setDeliveryRuleSetService(DeliveryRuleSetService deliveryRuleSetService);

	/**
	 * 
	 * @param optimizedDataListService
	 */
	void setOdlService(OptimizedDataListService optimizedDataListService);

	/**
	 * 
	 * @param policySetService
	 */
	void setPolicySetService(PolicySetService policySetService);
	
	/**
	 * 
	 * @return
	 */
	ProgramModelYearJsonConverter getJsonConverter();
	
	/**
	 * 
	 * @return
	 */
	ProgramModelYearDtoMapper getDtoMapper();

	/**
	 * 
	 * @param programCode The parent program code of the ProgramModelYear to create
	 * @param modelYear The parent model year of the ProgramModelYear to create 
	 * 
	 * @return The newly created ProgramModelYear
	 * 
	 * @throws EntityAlreadyExistsException ORFIN-PROGRAM-1001: Could not create program model Year: [programCode + modelYear] because it already exists
	 * @throws ValidationException ORFIN-PROGRAM-1002: Could not create program model year because attribute: [attributeName] was invalid for reason: [reason] 
	 */
	ProgramModelYear createProgramModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @return
	 */
	List<ProgramModelYear> getAllProgramModelYears();
	
	/**
	 * 
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The requested ProgramModelYear
	 * 
	 * @throws EntityDoesNotExistException ORFIN-PROGRAM-1003: Program model year: [programCode + modelYear] does not exist
	 * @throws ValidationException ORFIN-PROGRAM-1004: Could not retrieve program model year: [programCode + modelYear] because attribute: [attribute] was invalid for reason: [reason]
	 */
	ProgramModelYear getProgramModelYearByProgramCodeAndModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The requested ODL
	 * 
	 * @throws EntityDoesNotExistException ORFIN-PROGRAM-1005: There does not exist an ODL that is associated with Program model year: [programCode + modelYear]
	 * @throws ValidationException ORFIN-PROGRAM-1006: Could not retrieve ODL associated with program model year: [programCode + modelYear] because attribute: [attribute] was invalid for reason: [reason]
	 */
	Odl getOdlByProgramCodeAndModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The requested PolicySet
	 * 
	 * @throws EntityDoesNotExistException ORFIN-PROGRAM-1007: There does not exist a PolicySet that is associated with Program model year: [programCode + modelYear]
	 * @throws ValidationException ORFIN-PROGRAM-1008: Could not retrieve PolicySet associated with program model year: [programCode + modelYear] because attribute: [attribute] was invalid for reason: [reason]
	 */
	PolicySet getPolicySetByProgramCodeAndModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The requested DeliveryRuleSet
	 * 
	 * @throws EntityDoesNotExistException ORFIN-PROGRAM-1008: There does not exist a DeliveryRuleSet that is associated with Program model year: [programCode + modelYear]
	 * @throws ValidationException ORFIN-PROGRAM-1009: Could not retrieve DeliveryRuleSet associated with program model year: [programCode + modelYear] because attribute: [attribute] was invalid for reason: [reason]
	 */
	DeliveryRuleSet getDeliveryRuleSetByProgramCodeAndModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * @param odlName
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The affected ProgramModelYear
	 * 
	 * @throws EntityDoesNotExistException ORFIN-PROGRAM-1010: Could not associate ODL with name: [odlName] to program model year: [programCode + modelYear] because either of the two does not exist
	 * @throws ValidationException ORFIN-PROGRAM-1011: Could not associate ODL with name: [odlName] to program model year: [programCode + modelYear] because attribute: [attribute] was invalid for reason: [reason]
	 * @throws EntityAlreadyExistsException
	 */
	ProgramModelYear associateOdlToProgramModelYear(
		String odlName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException,
		EntityAlreadyExistsException;

	/**
	 * 
	 * @param odlName
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The affected ProgramModelYear
	 * 
	 * @throws EntityDoesNotExistException ORFIN-PROGRAM-1012: Could not disassociate ODL with name: [odlName] from program model year: [programCode + modelYear] because either of the two does not exist
	 * @throws ValidationException ORFIN-PROGRAM-1013: Could not disassociate ODL with name: [odlName] from program model year: [programCode + modelYear] because attribute: [attribute] was invalid for reason: [reason]
	 */
	ProgramModelYear disassociateOdlFromProgramModelYear(
		String odlName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param policySetName
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The affected ProgramModelYear
	 * 
	 * @throws EntityDoesNotExistException ORFIN-PROGRAM-1014: Could not associate PolicySet with name: [policySetName] to program model year: [programCode + modelYear] because either of the two does not exist
	 * @throws ValidationException ORFIN-PROGRAM-1015: Could not associate PolicySet with name: [policySetName] to program model year: [programCode + modelYear] because attribute: [attribute] was invalid for reason: [reason]
	 * @throws EntityAlreadyExistsException
	 */
	ProgramModelYear associatePolicySetToProgramModelYear(
		String policySetName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException,
		EntityAlreadyExistsException;
	
	/**
	 * 
	 * @param policySetName
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The affected ProgramModelYear
	 * 
	 * @throws EntityDoesNotExistException ORFIN-PROGRAM-1016: Could not disassociate PolicySet with name: [policySetName] from program model year: [programCode + modelYear] because either of the two does not exist
	 * @throws ValidationException ORFIN-PROGRAM-1017: Could not disassociate PolicySet with name: [policySetName] from program model year: [programCode + modelYear] because attribute: [attribute] was invalid for reason: [reason]
	 */
	ProgramModelYear disassociatePolicySetFromProgramModelYear(
		String policySetName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param deliveryRuleSetName
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The affected ProgramModelYear
	 * 
	 * @throws EntityDoesNotExistException ORFIN-PROGRAM-1018: Could not add DeliveryRuleSet with name: [deliveryRuleSetName] to program model year: [programCode + modelYear] because either of the two does not exist
	 * @throws ValidationException ORFIN-PROGRAM-1019: Could not add DeliverySet with name: [deliveryRuleSetName] to program model year: [programCode + modelYear] because attribute: [attribute] was invalid for reason: [reason]
	 */
	ProgramModelYear addDeliveryRuleSetToProgramModelYear(
		String deliveryRuleSetName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;
		
	/**
	 * 
	 * @param deliveryRuleSetName
	 * @param programCode
	 * @param modelYear
	 * 
	 * @return The affected ProgramModelYear
	 * 
	 * @throws EntityDoesNotExistException ORFIN-PROGRAM-1020: Could not remove DeliveryRuleSet with name: [deliveryRuleSetName] from program model year: [programCode + modelYear] because either of the two does not exist
	 * @throws ValidationException ORFIN-PROGRAM-1021: Could not remove DeliverySet with name: [deliveryRuleSetName] from program model year: [programCode + modelYear] because attribute: [attribute] was invalid for reason: [reason]
	 */
	ProgramModelYear removeDeliveryRuleSetFromProgramModelYear(
		String deliveryRuleSetName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;
}
