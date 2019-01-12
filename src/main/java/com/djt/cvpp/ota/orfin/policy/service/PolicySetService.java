/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.service;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.service.EntityService;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEventPublisher;
import com.djt.cvpp.ota.orfin.policy.mapper.PolicySetDtoMapper;
import com.djt.cvpp.ota.orfin.policy.mapper.PolicySetJsonConverter;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.orfin.policy.model.PolicySetHistoryEvent;
import com.djt.cvpp.ota.orfin.policy.model.region.Region;
import com.djt.cvpp.ota.orfin.policy.repository.PolicySetRepository;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

/**
 * 
 * This service interfaces deals with basic CRUD operations for the PolicySet
 * aggregate root, as well as all the "child" entities (and specializations of)
 * </p>
 * This interface, takes as input, primitives from a higher layer, e.g. a Controller
 * or Application Service, performs whatever operation requested (which for this 
 * interfaces, includes publishing policy set events), and returns the created, updated
 * or requested domain objects.  
 * </p>
 * For any information being transferred to another layer, it is recommended that the JSON
 * Converter, which is available in this interfae, be used to map entities to DTOs, and/or
 * map these DTOs to JSON. 
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface PolicySetService extends EntityService, OrfinPolicySetEventPublisher {
	
	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "ORFIN";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "POLICY";
	
	/** This is the name of the global policy set. */
	String GLOBAL_POLICY_SET_NAME = PolicySet.GLOBAL_POLICY_SET_NAME;

	/** policyValue and any overrides must be strings. Constraints specify min/max length as comma separated values. */
	String POLICY_VALUE_TYPE_STRING = "STRING";
	
	/** policyValue and any overrides must be numeric. Constraints specify min/max values as comma separated values. */
	String POLICY_VALUE_TYPE_NUMERIC = "NUMERIC";

	/** policyValue and any overrides must be numeric. Constraints specify min/max values as comma separated values. */
	String POLICY_VALUE_TYPE_BOOLEAN = "BOOLEAN";
	
	/** policyValue and any overrides must be string, but FROM the set of strings that exist as comma separated values in policyValueConstraints. */
	String POLICY_VALUE_TYPE_ENUM = "ENUM";
	
	
	/**
	 * Creates a policy set with the given name.  There can only be one policy set that can be 
	 * considered "global" and its name must be "GLOBAL".
	 * </p>   
	 * A "non-global" policy set is able to be associated to one or more <code>ProgramModelYear</code> instances.
	 * </p>
	 * The "global" policy set cannot be associated with any <code>ProgramModelYear</code> instances.  However,
	 * it is the only policy set that can have "program level" policy value overrides.  
	 * </p>
	 * In addition, "non-global" policy sets can only have region and vehicle level policy overrides associated
	 * with its child policies. (Only the global policy set can)
	 * 
	 * @param policySetName The unique name of the policy set, "GLOBAL" to create the global policy set.
	 * 
	 * @return The newly created policy set
	 * 
	 * @throws EntityAlreadyExistsException ORFIN-POLICY-1001: Could not create policy set: [policySetName] because it already exists
	 * @throws ValidationException ORFIN-POLICY-1002: Could not create policy set because policy set name: [policySetName] is not valid 
	 */
	PolicySet createPolicySet(
		String policySetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @return All policy sets that have been created.
	 */
	List<PolicySet> getAllPolicySets();

	/**
	 * 
	 * @param policySetName The unique name of the policy set. Use "GLOBAL" to retrieve the global policy set.
	 * 
	 * @return The policy set with the name specified by <code>policySetName</code>
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1003: Could not retrieve policy set: [policySetName] because it does not exist
	 * @throws ValidationException ORFIN-POLICY-1060: Could not retrieve policy set: [policySetName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet getPolicySetByName(
        String policySetName)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * @param policyType Either 'VehiclePolicy' or 'CloudPolicy'
	 * @param parentPolicySetName The unique name of the parent policy set ("GLOBAL" for the global policy set.)
	 * @param policyName The unique name of the policy to be created
	 * @param policyDescription The description of the policy
	 * @param policyValue The value of the policy
	 * 
	 * @param allowRegionalChangeable Specifies whether or not this policy's value can be overridden at the regional level  
	 * @param allowUserChangeable Specifies whether or not this policy's value can be overridden by the consumer. This resolves to the "vehicle level"
	 * @param allowServiceChangeable Specifies whether or not this policy's value can be overridden by service/dealership. This also resolves to the "vehicle level".
	 * @param allowCustomerFeedback Specifies whether or not this policy allows for customer feedback
	 * 
	 * @param hmi An enumeration value that specifies whether the user can (R)ead, Read&Write (RW) or None (N) on the HMI (human machine interface) in the vehicle.
	 * @param vehicleHmiFields A CSV line that contains an ID, a "vehicle HMI description", and a pipe-delimited set of button names (e.g. "Cancel"|"Allow")
	 * (Assumes that <code>allowUserChangeable</code> is true AND that <code>hmi</code> is either R or RW) 
	 * 
	 * @param phone An enumeration that says whether or not a phone number is displayed ("NONE" means that no phone number is displayed)
	 * @param otaFunction An enumeration that specifies which high level OTA Function is being dealt with (e.g. OTA_MANAGER, OTA_STATUS_MANAGER, IVSU_TRIGGER, etc.)
	 * 
	 * @param policyValueType Specifies the type of policy value.  It must be STRING, NUMERIC or ENUM.
	 * @param policyValueConstraints If <code>policyValueType</code> is ENUM, then constraints contain a comma separated list of enum choices.  If PolicyValueType is NUMERIC, then the constraints are the lower and upperbound. 
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1004: Could not create policy: [policyName] because parent policy set: [policySetName] does not exist 
	 * @throws EntityAlreadyExistsException ORFIN-POLICY-1005: Could not create policy: [policyName] because it already exists for parent policy set: [parentPolicySet]
	 * @throws ValidationException ORFIN-POLICY-1006: Could not create policy: [policyName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet createPolicy(
		String policyType,	
		String parentPolicySetName,	
		String policyName,
		String policyDescription,
		Object policyValue,
		Boolean allowRegionalChangeable,
		Boolean allowUserChangeable,
		Boolean allowServiceChangeable,
		Boolean allowCustomerFeedback,
		String hmi,
		String vehicleHmiFile,
		String phone,
		String otaFunction,
		String policyValueType,
		String policyValueConstraints)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * NOTES: 
	 * <ol>
	 *   <li>Only Policies that belong to the "global" policy set can have program level overrides</li>
	 *   <li>The policy value must be of the same type as specified in the parent policy's <code>policyValueType</code> field</li>
	 * </ol>
	 * 
	 * @param parentPolicyName The unique name of the parent policy
	 * @param programModelYear The program model year to create the policy override for
	 * @param policyValue The value to use for the override 
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1007: Could not create program level policy override for policy: [policyName] because parent policy set: [policySetName] or program model year: [programCode + modelYear] does not exist 
	 * @throws EntityAlreadyExistsException ORFIN-POLICY-1008: Could not create program level policy override for policy: [policyName] because it already exists for parent policy set: [parentPolicySet]
	 * @throws ValidationException ORFIN-POLICY-1009: Could not create program level policy override because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet createProgramLevelPolicyOverride(
		String parentPolicyName,	
		ProgramModelYear programModelYear,
		Object policyValue)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * 
	 * @param parentPolicySetName The unique name of the parent policy set ("GLOBAL" for the global policy set.)
	 * @param parentPolicyName The unique name of the parent policy
	 * @param parentRegionCode The unique code of the parent region
	 * @param policyValue The value to use for the override 
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1011: Could not create region level policy override for policy: [parentPolicyName] because parent policy set: [parentPolicySetName] does not exist 
	 * @throws EntityAlreadyExistsException ORFIN-POLICY-1012: Could not create region level policy override for policy: [parentPolicyName] because it already exists for parent policy set: [parentPolicySetName]
	 * @throws ValidationException ORFIN-POLICY-1013: Could not create region level policy override because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet createRegionLevelPolicyOverride(
		String parentPolicySetName,
		String parentPolicyName,
		String parentRegionCode,	
		Object policyValue)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * NOTE: THIS OPERATION IS TRANSIENT AND DOES NOT UPDATE/STORE ANYTHING IN THE ORFIN DATABASE!
	 * </p>
	 * The returned policy set with the addition of the vehicle level policy override is strictly to be used by the "render policy table JSON" method, as no vehicle information is stored in the ORFIN DB. 
	 * 
	 * @param parentPolicySetName The unique name of the parent policy set ("GLOBAL" for the global policy set.)
	 * @param parentPolicyName The unique name of the parent policy
	 * @param parentVin The unique code of the parent vehicle
	 * @param policyValue The value to use for the override 
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1014: Could not create vehicle level policy override for policy: [parentPolicyName] because parent policy set: [parentPolicySetName] does not exist 
	 * @throws EntityAlreadyExistsException ORFIN-POLICY-1015: Could not create vehicle level policy override for policy: [parentPolicyName] because it already exists for parent policy set: [parentPolicySetName]
	 * @throws ValidationException ORFIN-POLICY-1016: Could not create vehicle level policy override because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet createVehicleLevelPolicyOverride(
		String parentPolicySetName,
		String parentPolicyName,
		String parentVin,
		Object policyValue)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * NOTE: The "global" policy set cannot be renamed, it can only be updated.
	 * 
	 * @param oldPolicySetName The unique name of the policy set to change the name of
	 * @param newPolicySetName The new unique name of the policy set that is to be changed.
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1022: Could not rename policy set: [oldPolicySetName] because it doesn't exist
	 * @throws EntityAlreadyExistsException ORFIN-POLICY-1023: Could not rename policy set: [oldPolicySetName] because there already exists a policy set with name: [newPolicySetName]
	 * @throws ValidationException ORFIN-POLICY-1061: Could not rename policy set: [oldPolicySetName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet renamePolicySet(
		String oldPolicySetName, 
		String newPolicySetName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;

	/**
	 * @param parentPolicySetName The unique name of the parent policy set ("GLOBAL" for the global policy set.)
	 * @param oldPolicySetName The unique name of the policy set to change the name of
	 * @param newPolicySetName The new unique name of the policy set that is to be changed
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1024: Could not rename policy: [oldPolicyName] with parent policy set: [parentPolicySetName] because it doesn't exist
	 * @throws EntityAlreadyExistsException ORFIN-POLICY-1025: Could not rename policy: [oldPolicyName] with parent policy set: [parentPolicySetName] because it already has a policy with name: [newPolicySetName]
	 * @throws ValidationException ORFIN-POLICY-1010: Could not rename policy: [policySetName] because attribute: [attributeName] was invalid for reason: [reason] 
	 */
	PolicySet renamePolicy(
		String parentPolicySetName,
		String oldPolicyName, 
		String newPolicyName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * NOTE: policyValue must agree with the value specified by policy value type.
	 * 
	 * @param parentPolicySetName The unique name of the parent policy set ("GLOBAL" for the global policy set.)
	 * @param policyName
	 * @param policyDescription
	 * @param policyValue
	 * @param allowRegionalChangeable
	 * @param allowUserChangeable
	 * @param allowServiceChangeable
	 * @param allowCustomerFeedback
	 * @param hmi
	 * @param vehicleHmiFile
	 * @param phone
	 * @param otaFunction
	 * @param policyValueType Specifies the type of policy value.  It must be STRING, NUMERIC or ENUM.
	 * @param policyValueConstraints If <code>policyValueType</code> is ENUM, then constraints contain a comma separated list of enum choices.  If PolicyValueType is NUMERIC, then the constraints are the lower and upperbound. 
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1026: Could not update policy: [policyName] because it does not exist 
	 * @throws ValidationException ORFIN-POLICY-1027: Could not update policy: [policyName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet updateVehiclePolicy(
		String parentPolicySetName,	
		String policyName,
		String policyDescription,
		Object policyValue,
		Boolean allowRegionalChangeable,
		Boolean allowUserChangeable,
		Boolean allowServiceChangeable,
		Boolean allowCustomerFeedback,
		String hmi,
		String vehicleHmiFile,
		String phone,
		String otaFunction,
		String policyValueType,
		String policyValueConstraints)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * NOTE: policyValue must agree with the value specified by policy value type.
	 * 
	 * @param parentPolicySetName The unique name of the parent policy set ("GLOBAL" for the global policy set.)
	 * @param policyName
	 * @param policyDescription
	 * @param policyValue
	 * @param policyValueType Specifies the type of policy value.  It must be STRING, NUMERIC or ENUM.
	 * @param policyValueConstraints If <code>policyValueType</code> is ENUM, then constraints contain a comma separated list of enum choices.  If PolicyValueType is NUMERIC, then the constraints are the lower and upperbound. 
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1026: Could not update policy: [policyName] because it does not exist 
	 * @throws ValidationException ORFIN-POLICY-1027: Could not update policy: [policyName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet updateCloudPolicy(
		String parentPolicySetName,	
		String policyName,
		String policyDescription,
		Object policyValue,
		String policyValueType,
		String policyValueConstraints)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * NOTE: policyValue must agree with the value specified by policy value type.
	 * 
	 * @param parentPolicyName
	 * @param ModelYear
	 * @param policyValue
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1028: Could not update program level policy override because either it does not exist, the global policy set does not exist, the parent policy: [parentPolicyName] does not exist or the parent program model year: [programCode + modelYear] does not exist
	 * @throws ValidationException ORFIN-POLICY-1029: Could not update program level policy override with: [parentPolicyName] and parent program model year: [programCode + modelYear] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet updateProgramLevelPolicyOverride(
		String parentPolicyName,
		ProgramModelYear programModelYear,
		Object policyValue)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * NOTE: policyValue must agree with the value specified by policy value type.
	 *
	 * @param parentPolicySetName The unique name of the parent policy set ("GLOBAL" for the global policy set.)
	 * @param parentPolicyName
	 * @param parentRegionCode
	 * @param policyValue
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1030: Could not update region level policy override because either it does not exist, parent policy set: [parentPolicySetName] does not exist, the parent policy: [parentPolicyName] does not exist or the parent region: [parentRegionCode] does not exist
	 * @throws ValidationException ORFIN-POLICY-1031: Could not update region level policy override with: [parentPolicyName], parent policy set: [parentPolicySetName] and parent region: [parentRegionCode] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet updateRegionLevelPolicyOverride(
		String parentPolicySetName,	
		String parentPolicyName,	
		String regionCode,
		Object policyValue)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * 
	 * @param policySetName The name of the policy set to delete ("GLOBAL" for the global policy set.)
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1034: Could not delete policy set: [policySetName] because it does not exist
	 * @throws ValidationException ORFIN-POLICY-1035: Could not delete policy set: [policySetName] because the specified policy set name is invalid
	 */
	void deletePolicySet(
		String policySetName) 
	throws 
		EntityDoesNotExistException,
		ValidationException; 

	/**
	 * 
	 * @param parentPolicySetName The unique name of the parent policy set ("GLOBAL" for the global policy set.)
	 * @param policyName The name of the policy to delete
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1036: Could not delete policy: [policyName] because either it or parent policy set: [parentPolicySetName] does not exist
	 * @throws ValidationException ORFIN-POLICY-1037: Could not delete policy: [policyName] with parent policy set: [parentPolicySetName] because the specified policy name is invalid
	 */
	PolicySet deletePolicy(
		String parentPolicySetName,	
		String policyName)
	throws
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param parentPolicySetName The unique name of the parent policy set (and assumes that this is for the global policy set)
	 * @param parentPolicyName The name of the parent policy to delete the policy override for
	 * @param programModelYear The program to delete the policy override for
	 * 
	 * @return The affected policy set (Global policy set)
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1038: Could not delete program level global policy override for policy: [parentPolicyName] because either it, the parent global policy set: [parentPolicySetName] or the program: [programCode + modelYear] does not exist
	 * @throws ValidationException ORFIN-POLICY-1039: Could not delete program level global policy override for policy: [parentPolicyName] with parent global policy set: [parentPolicySetName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet deleteProgramLevelPolicyOverride(
		String parentPolicyName,
		ProgramModelYear programModelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 *
	 * @param parentPolicySetName The unique name of the parent policy set ("GLOBAL" for the global policy set.)
	 * @param parentPolicyName The name of the parent policy to delete the policy override for
	 * @param parentRegionCode The region code for the region to delete the policy override for
	 * 
	 * @return The affected policy set
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1040: Could not delete region level policy override for policy: [parentPolicyName] because either it, the parent policy set: [parentPolicySetName] or the region: [parentRegionCode] does not exist
	 * @throws ValidationException ORFIN-POLICY-1041: Could not delete region level policy override for policy: [parentPolicyName] with parent policy set: [parentPolicySetName] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	PolicySet deleteRegionLevelPolicyOverride(
		String parentPolicySetName,	
		String parentPolicyName,	
		String parentRegionCode)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	
	
	
	// REGIONAL RELATED CRUD OPERATIONS
	/**
	 * 
	 * @param regionCode The unique code of the region to create
	 * @param countryName The name of the region to create
	 * 
	 * @return The newly created Region
	 * 
	 * @throws EntityAlreadyExistsException ORFIN-POLICY-1042: Could not create region: [regionCode] because it already exists
	 * @throws ValidationException ORFIN-POLICY-1043: Could not create region because attribute: [attribute] was invalid for reason: [reason]
	 *  
	 */
	Region createRegion(
		String regionCode,
		String countryName)
	throws 
		EntityAlreadyExistsException,
		ValidationException;
	
	/**
	 * 
	 * @return All regions that have been created
	 */
	List<Region> getAllRegions();
	
	/**
	 * 
	 * @param regionCode The unique code for the region
	 * 
	 * @return The region identitied by <code>regionCode</code>
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1044: Could not retrieve region: [regionCode] because it does not exist
	 */
	Region getRegionByCode(
        String regionCode)
	throws 
		EntityDoesNotExistException;
	
	/**
	 * 
	 * @param oldRegionCode The unique code of the region to change
	 * @param newPolicySetName The new unique code of the region that is to be changed.
	 * 
	 * @return The affected region
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1045: Could not rename region: [oldRegionCode] because it doesn't exist
	 * @throws EntityAlreadyExistsException ORFIN-POLICY-1046: Could not rename region: [oldRegionCode] because there already exists a region with code: [newRegionCode]
	 * 
	 */
	Region renameRegion(
		String oldRegionCode, 
		String newRegionCode)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException;

	/**
	 * 
	 * @param regionCode The unique code of the region to change the name for
	 * @param countryName The new country name
	 * @return
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1047: Could not update region: [regionCode] because it does not exist 
	 * @throws ValidationException ORFIN-POLICY-1048: Could not update region: [regionCode] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	Region updateRegion(
		String regionCode, 
		String countryName)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param regionCode The unique code of the region to delete
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1056: Could not delete region: [regionCode] because it does not exist 
	 * @throws ValidationException ORFIN-POLICY-1057: Could not delete region: [regionCode] because attribute: [attributeName] was invalid for reason: [reason]
	 * 
	 */
	void deleteRegion(
		String regionCode)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	

	
	// PURE BUSINESS BEHAVIORS (NON CRUD OPERATIONS)
	/**
	 * NOTE: It is assumed that the vehicle is able to acquire its vehicle level policy overrides and re-apply them (where applicable) to this policy table JSON
	 * 
	 * @param programModelYear The program, model year and region to render the PolicyTable JSON for delivery to the vehicle
	 * @param regionCode The region code for the region to render the region specific policy table JSON for
	 * 
	 *</p>
	 *The general workflow is as follows:
	 *
	 *<ol>
	 *
	 *  <li> Initialize an empty set of policies (i.e. "running set of policies")
	 *  
	 *  <li> Get the set of global policies and add its set of policies to our set of policies
	 *  
	 *  <li> Get the set of policies for the given program model year and add its set of policies to our running set of policies  (it is assumed that there are no name clashes, but if there were, the program level policy would replace the global policy)
	 *  
	 *  <li> Initialize a set of PolicyTableEntry DTO objects (that represent the JSON to be delivered to the vehicle)
	 *  
	 *  <li> Iterate through this running set of policies and call the "getOverriddenPolicyValue()" business method for each policy, which will return a policy value that is determined by the following hierarchy of policy overrides (if they exist).
	 *  <pre>
			NOTE: This logic comes from requirement "###R_F_PUC_00002### Overlaying of policies" from the "AbstractPolicy Update Cloud" spec
			Rank   AbstractPolicy Type
			--------------------
			1      Vehicle NOTE: There won't exist any of these for this use case
			2      Region
			3      Program
			4      Global NOTE: This "level" is not in the spec, but was added after the fact from meetings with PD
	 *  </pre>
	 *  
	 *  <li>Create the PolicyTable container DTO (containing the PolicyTableEntry DTOS) so that we can marshall it to JSON
	 *  
	 *  <li> Return the generated JSON to the caller, which is suitable for upload to TMC ByteStream (and needs to be uniquely named, preferably with the combination of rollout code, program code, model year and vin that this JSON is for.
	 *  
	 *  NOTE: Every PolicyTableEntry DTO needs to have a "default value" that comes either from the global policy (if a global policy), or from the program level policy.
	 *  If the program level policy does NOT have any value specified, then the default value MUST come from a regional policy override that MUST exist 
	 *  (an exception will be thrown otherwise, as a "default value" is needed for the JSON)
	 * 
	 * @return Program/Region generic JSON that is suitable for uploading to TMC ByteStream to be consumed by the vehicle (assumed that vehicle is able to re-apply its vehicle level overrides).
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1049: Could not render policy table JSON for program model year: [programCode + modelYear] and region: [regionCode]
	 * @throws ValidationException ORFIN-POLICY-1050: Could not render policy table JSON for program model year: [programCode + modelYear] and region: [regionCode] because attribute: [attributeName] was invalid for reason: [reason]
	 */
	String renderGenericPolicyTableJsonForProgramAndRegion(
		ProgramModelYear programModelYear,
		String regionCode)
	throws 
		EntityDoesNotExistException,
		ValidationException;

	/**
	 * NOTE: It is assumed that the vehicle is able to acquire its vehicle level policy overrides and re-apply them (where applicable) to this policy table JSON
	 * 
	 * @param programModelYear The model year for the program to render the region specific policy table JSON for
	 * @param regionCode The region code for the region to render the region specific policy table JSON for
	 * 
	 *</p>
	 *The general workflow is as follows:
	 *
	 *<ol>
	 *
	 *  <li> Initialize an empty set of policies (i.e. "running set of policies")
	 *  
	 *  <li> Get the set of global policies and add its set of policies to our set of policies
	 *  
	 *  <li> Initialize a set of PolicyTableEntry DTO objects (that represent the JSON to be delivered to the vehicle)
	 *  
	 *  <li>Create the PolicyTable container DTO (containing the PolicyTableEntry DTOS) so that we can marshall it to JSON
	 *  
	 *  <li> Return the generated JSON to the caller, which is suitable for upload to TMC ByteStream (and needs to be uniquely named, preferably with the combination of rollout code, program code, model year and vin that this JSON is for.
	 *  
	 *  NOTE: Every PolicyTableEntry DTO needs to have a "default value" that comes either from the global policy (if a global policy), or from the program level policy.
	 *  If the program level policy does NOT have any value specified, then the default value MUST come from a regional policy override that MUST exist 
	 *  (an exception will be thrown otherwise, as a "default value" is needed for the JSON)
	 * 
	 * @return Global generic JSON that is suitable for uploading to TMC ByteStream to be consumed by the vehicle (assumed that vehicle is able to re-apply its vehicle level overrides).
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1054: Could not render global policy table JSON because the global policy set doesn't exist
	 * @throws ValidationException ORFIN-POLICY-1055: Could not render global policy table JSON because attribute: [attributeName] was invalid for reason: [reason]
	 */
	String renderGlobalPolicyTableJson()
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param regionCode The region code for the region to render the region specific policy table JSON for
	 * 
	 * @return Global generic JSON (with region overrides) that is suitable for uploading to TMC ByteStream to be consumed by the vehicle (assumed that vehicle is able to re-apply its vehicle level overrides).
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1058: Could not render global policy table with region overrides JSON because the global policy set or region: [regionCode] doesn't exist 
	 * @throws ValidationException ORFIN-POLICY-1059: Could not render global policy table with region overrides JSON because attribute: [attributeName] was invalid for reason: [reason]
	 */
	String renderGlobalPolicyTableJsonForRegion(String regionCode)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	/**
	 * 
	 * @param policySetName
	 * 
	 * @return
	 * 
	 * @throws EntityDoesNotExistException ORFIN-POLICY-1062: Could not retrieve policy set history for policy set: [policySetName] because it doesn't exist 
	 * @throws ValidationException ORFIN-POLICY-1063: Could not retrieve policy set history because attribute: [attributeName] was invalid for reason: [reason]
	 */
	List<PolicySetHistoryEvent> getPolicySetHistoryEvents(
		String policySetName)
	throws 
		EntityDoesNotExistException,
		ValidationException;
	
	
	// NON-FUNCTIONAL RELATED METHODS
	/**
	 * Used for setter-based dependency injection
	 * 
	 * @param policySetRepository
	 */
	void setPolicySetRepository(PolicySetRepository policySetRepository);

	/**
	 * Used for setter-based dependency injection
	 * 
	 * @param orfinPolicySetEventPublisher
	 */
	void setOrfinPolicySetEventPublisher(OrfinPolicySetEventPublisher orfinPolicySetEventPublisher);

	/**
	 * Used to marshall entities to JSON (via DTO Mappers), and vice-versa, where applicable.
	 *  
	 * @return
	 */
	PolicySetJsonConverter getJsonConverter();
	
	/**
	 * 
	 * Used to marshall entities to DTOs.  It is expected that the application/controller layer will convert everything to DTOs at their level 
	 * and marshall everything to JSON for over the wire transfer
	 *  
	 * @return
	 */
	PolicySetDtoMapper getDtoMapper();
}
