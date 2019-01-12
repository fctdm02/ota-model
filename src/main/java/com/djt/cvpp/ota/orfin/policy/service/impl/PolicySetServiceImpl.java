/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ExceptionIdentity;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.service.impl.AbstractService;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEvent;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEventPublisher;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEventSubscriber;
import com.djt.cvpp.ota.orfin.policy.mapper.PolicySetDtoMapper;
import com.djt.cvpp.ota.orfin.policy.mapper.PolicySetJsonConverter;
import com.djt.cvpp.ota.orfin.policy.mapper.dto.ecg.PolicyTable;
import com.djt.cvpp.ota.orfin.policy.mapper.dto.ecg.PolicyTableContainer;
import com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy;
import com.djt.cvpp.ota.orfin.policy.model.CloudPolicy;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.orfin.policy.model.PolicySetHistoryEvent;
import com.djt.cvpp.ota.orfin.policy.model.VehiclePolicy;
import com.djt.cvpp.ota.orfin.policy.model.enums.OtaFunction;
import com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType;
import com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyProgramModelYearOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyRegionOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyVehicleOverride;
import com.djt.cvpp.ota.orfin.policy.model.region.Region;
import com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue;
import com.djt.cvpp.ota.orfin.policy.model.value.NumericValue;
import com.djt.cvpp.ota.orfin.policy.model.value.StringValue;
import com.djt.cvpp.ota.orfin.policy.model.vehicle.Vehicle;
import com.djt.cvpp.ota.orfin.policy.repository.PolicySetRepository;
import com.djt.cvpp.ota.orfin.policy.service.PolicySetService;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class PolicySetServiceImpl extends AbstractService implements PolicySetService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PolicySetServiceImpl.class);

	
	// For domain services and repositories relating to ORFIN-POLICY	
	protected PolicySetJsonConverter policySetJsonConverter = new PolicySetJsonConverter();
	protected PolicySetDtoMapper policySetDtoMapper = new PolicySetDtoMapper();

	// For persisting our entities
	protected PolicySetRepository policySetRepository;
	
	// For dealing with publishing policy set events
	protected OrfinPolicySetEventPublisher delegate;

	
	public PolicySetServiceImpl() {
	}

	public PolicySetServiceImpl(
		PolicySetRepository policySetRepository,
		OrfinPolicySetEventPublisher orfinPolicySetEventPublisher) {
		
		this.policySetRepository = policySetRepository;
		this.delegate = orfinPolicySetEventPublisher;
	}

	public PolicySetJsonConverter getJsonConverter() {
		return this.policySetJsonConverter;
	}

	public PolicySetDtoMapper getDtoMapper() {
		return this.policySetDtoMapper;
	}
	
	public void setPolicySetRepository(PolicySetRepository policySetRepository) {
		this.policySetRepository = policySetRepository;
	}
	
	public void setOrfinPolicySetEventPublisher(OrfinPolicySetEventPublisher orfinPolicySetEventPublisher) {
		this.delegate = orfinPolicySetEventPublisher;
	}
	
	// EVENT BASED BEHAVIORS
	public void subscribe(OrfinPolicySetEventSubscriber orfinPolicySetEventSubscriber) {
		this.delegate.subscribe(orfinPolicySetEventSubscriber);
	}
	
	public void unsubscribe(OrfinPolicySetEventSubscriber orfinPolicySetEventSubscriber) {
		this.delegate.unsubscribe(orfinPolicySetEventSubscriber);
	}
	
	public OrfinPolicySetEvent publishOrfinPolicySetEvent(
		String owner,
		String programCode,
		Integer modelYear,
		String regionCode,
		String policySetName)
	throws 
		ValidationException {
		
		return this.delegate.publishOrfinPolicySetEvent(
			owner, 
			programCode, 
			modelYear, 
			regionCode,
			policySetName);
	}
	
	
	// INHERITED BUSINESS BEHAVIORS
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		if (entity instanceof PolicySet) {
			return this.policySetRepository.updatePolicySet((PolicySet)entity);
		} if (entity instanceof Region) {
			return this.policySetRepository.updateRegion((Region)entity);
		}
		throw new IllegalStateException("Expected instance of PolicySet or Region, but was: [" + entity.getClassAndIdentity() + "].");
	}
		
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		if (entity instanceof PolicySet) {
			return this.policySetRepository.deletePolicySet((PolicySet)entity);
		} if (entity instanceof Region) {
			return this.policySetRepository.deleteRegion((Region)entity);
		}
		throw new IllegalStateException("Expected instance of PolicySet or Region, but was: [" + entity.getClassAndIdentity() + "].");
	}
			

	// BUSINESS BEHAVIORS
	public PolicySet createPolicySet(String policySetName) throws EntityAlreadyExistsException, ValidationException {
		
		LOGGER.debug("PolicySetServiceImpl::createPolicySet(): policySetName: [{}].", policySetName);
		validateAttribute("1002", "policySetName", policySetName, "Could not create policy set because policy set name: [" + policySetName + "] is not valid");
		
		try {
			return this.policySetRepository.createPolicySet(policySetName);	
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create policy set: [" + policySetName +"] because it already exists");
			throw eaee;
		}
	}

	public List<PolicySet> getAllPolicySets() {
		
		return this.policySetRepository.getAllPolicySets();
	}

	public PolicySet getPolicySetByName(String policySetName) throws EntityDoesNotExistException, ValidationException {
		
		validateAttribute("1003", "policySetName", policySetName, "Could not retrieve policy set because given policy set name: [" + policySetName + "] is not valid");
		try {
			return this.policySetRepository.getPolicySetByName(policySetName);	
		} catch (EntityDoesNotExistException ednee) {
			
			this.setExceptionIdentity(ednee, "1003", "Could not retrieve policy set: [" + policySetName + "] because it does not exist");
			throw ednee;
		}
	}

	public PolicySet createPolicy(
		String policyType,	
		String parentPolicySetName,	
		String policyName,
		String policyDescription,
		Object objPolicyValue,
		Boolean allowRegionalChangeable,
		Boolean allowUserChangeable,
		Boolean allowServiceChangeable,
		Boolean allowCustomerFeedback,
		String hmi,
		String vehicleHmiFile,
		String phone,
		String strOtaFunction,
		String strPolicyValueType,
		String policyValueConstraints)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {

		try {
			PolicySet parentPolicySet = this.getPolicySetByName(parentPolicySetName);
			
			OtaFunction otaFunction = null;
			if (strOtaFunction != null) {
				otaFunction = OtaFunction.valueOf(strOtaFunction);
			}
			
			PolicyValueType policyValueType = buildPolicyValueType(strPolicyValueType);
			AbstractPolicyValue policyValue = buildPolicyValue(null, policyValueType, objPolicyValue, policyValueConstraints);

			if (policyType.equals(PolicySet.VEHICLE_POLICY)) {
				
				VehiclePolicy vehiclePolicy = new VehiclePolicy
					.VehiclePolicyBuilder()
					.withParentPolicySet(parentPolicySet)
					.withPolicyName(policyName)
					.withPolicyDescription(policyDescription)
					.withPolicyValue(policyValue)
					.withAllowRegionalChangeable(allowRegionalChangeable)
					.withAllowUserChangeable(allowUserChangeable)
					.withAllowServiceChangeable(allowServiceChangeable)
					.withAllowCustomerFeedback(allowCustomerFeedback)
					.withHmi(hmi)
					.withVehicleHmiFile(vehicleHmiFile)
					.withPhone(phone)
					.withOtaFunction(otaFunction)
					.withPolicyValueType(policyValueType)
					.withPolicyValueConstraints(policyValueConstraints)
					.build();
				policyValue.setParentPolicy(vehiclePolicy);
				parentPolicySet.addPolicy(vehiclePolicy);

			} else if (policyType.equals(PolicySet.CLOUD_POLICY)) {
				
				CloudPolicy cloudPolicy = new CloudPolicy
					.CloudPolicyBuilder()
					.withParentPolicySet(parentPolicySet)
					.withPolicyName(policyName)
					.withPolicyDescription(policyDescription)
					.withPolicyValue(policyValue)
					.withPolicyValueType(policyValueType)
					.withPolicyValueConstraints(policyValueConstraints)
					.build();
				policyValue.setParentPolicy(cloudPolicy);
				parentPolicySet.addPolicy(cloudPolicy);
				
			} else {
				throw new RuntimeException("policyType: [" + policyType + "] must either be 'VehiclePolicy' or 'CloudPolicy', but was: [" + policyType + "].");
			}
			
			
			this.policySetRepository.updatePolicySet(parentPolicySet);
			
			return parentPolicySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1004", "Could not create policy: [" + policyName + "] because parent policy set: [" + parentPolicySetName + "] does not exist");
			throw ednee;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1005", "Could not create policy: [" + policyName +"] because it already exists for parent policy set: [" + parentPolicySetName + "]");
			throw eaee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1006", "Could not create policy: [" + policyName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public PolicySet createProgramLevelPolicyOverride(
		String parentPolicyName,	
		ProgramModelYear programModelYear,
		Object objPolicyValue)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		PolicySet parentPolicySet = null;
		try {

			parentPolicySet = this.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
			
			AbstractPolicy parentPolicy = parentPolicySet.getPolicyByName(parentPolicyName);
			
			AbstractPolicyValue policyValue = buildPolicyValue(
				parentPolicy,	
				parentPolicy.getPolicyValueType(), 
				objPolicyValue, 
				parentPolicy.getPolicyValueConstraints());
			
			PolicyProgramModelYearOverride policyProgramOverride = new PolicyProgramModelYearOverride
				.PolicyProgramModelYearOverrideBuilder()
				.withParentPolicy(parentPolicy)
				.withPolicyOverrideValue(policyValue)
				.withProgramModelYear(programModelYear)
				.build();
			
			parentPolicy.addPolicyOverride(policyProgramOverride);
			
			this.policySetRepository.updatePolicySet(parentPolicySet);
			
			return parentPolicySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1007", "Could not create program level policy override for policy: [" + parentPolicyName + "] because parent policy set: [" + PolicySet.GLOBAL_POLICY_SET_NAME + "] or program model year: [" + programModelYear + "] does not exist");
			throw ednee;
	
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1008", "Could not create program level policy override for policy: [" + parentPolicyName + "] because it already exists for parent policy set: [" + PolicySet.GLOBAL_POLICY_SET_NAME + "]");
			throw eaee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1009", "Could not create program level policy override because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public PolicySet createRegionLevelPolicyOverride(
		String parentPolicySetName,	
		String parentPolicyName,
		String parentRegionCode,	
		Object objPolicyValue)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		try {

			PolicySet parentPolicySet = this.getPolicySetByName(parentPolicySetName);
			
			AbstractPolicy parentPolicy = parentPolicySet.getPolicyByName(parentPolicyName);
			
			AbstractPolicyValue policyValue = buildPolicyValue(
				parentPolicy,	
				parentPolicy.getPolicyValueType(), 
				objPolicyValue, 
				parentPolicy.getPolicyValueConstraints());
			
			
			Region region = this.policySetRepository.getRegionByCode(parentRegionCode);
			
			
			PolicyRegionOverride policyRegionOverride = new PolicyRegionOverride
				.PolicyRegionOverrideBuilder()
				.withParentPolicy(parentPolicy)
				.withPolicyOverrideValue(policyValue)
				.withRegion(region)
				.build();
			
			parentPolicy.addPolicyOverride(policyRegionOverride);
			
			this.policySetRepository.updatePolicySet(parentPolicySet);
			
			return parentPolicySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1011", "Could not create region level policy override for policy: [" + parentPolicyName + "] because parent policy set: [" + parentPolicySetName + "] does not exist");
			throw ednee;
	
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1012", "Could not create region level policy override for policy: [" + parentPolicyName + "] because it already exists for parent policy set: [" + parentPolicySetName + "]");
			throw eaee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1013", "Could not create region level policy override because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public PolicySet createVehicleLevelPolicyOverride(
		String parentPolicySetName,	
		String parentPolicyName,
		String parentVin,
		Object objPolicyValue)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		try {

			PolicySet parentPolicySet = this.getPolicySetByName(parentPolicySetName);
			
			AbstractPolicy parentPolicy = parentPolicySet.getPolicyByName(parentPolicyName);
			
			Vehicle vehicle = new Vehicle.VehicleBuilder()
				.withVin(parentVin)
				.build();
			
			AbstractPolicyValue policyValue = buildPolicyValue(
				parentPolicy,
				parentPolicy.getPolicyValueType(), 
				objPolicyValue, 
				parentPolicy.getPolicyValueConstraints());
			
			PolicyVehicleOverride policyVehicleOverride = new PolicyVehicleOverride
				.PolicyVehicleOverrideBuilder()
				.withParentPolicy(parentPolicy)
				.withPolicyOverrideValue(policyValue)
				.withVehicle(vehicle)
				.build();
			
			parentPolicy.addPolicyOverride(policyVehicleOverride);
			
			// As stated in the javadoc, vehicle level overrides are transient and not stored in the database
			//this.policySetRepository.updatePolicySet(parentPolicySet);
			
			return parentPolicySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1014", "Could not create vehicle level policy override for policy: [" + parentPolicyName + "] because parent policy set: [" + parentPolicySetName + "] does not exist");
			throw ednee;
	
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1015", "Could not create vehicle level policy override for policy: [" + parentPolicyName + "] because it already exists for parent policy set: [" + parentPolicySetName + "]");
			throw eaee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1016", "Could not create vehicle level policy override because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
		
	public PolicySet renamePolicySet(
		String oldPolicySetName, 
		String newPolicySetName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		try {

			if (oldPolicySetName.equals(PolicySet.GLOBAL_POLICY_SET_NAME) || newPolicySetName.equals(PolicySet.GLOBAL_POLICY_SET_NAME)) {
				throw new FenixRuntimeException("Cannot rename a policy to/from the global policy set name.");
			}
			
			return this.policySetRepository.renamePolicySet(oldPolicySetName, newPolicySetName);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1022", "Could not rename policy set: [" + oldPolicySetName + "] because it doesn't exist");
			throw ednee;
	
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1023", "Could not rename policy set: [" + oldPolicySetName + "] because there already exists a policy set with name: [" + newPolicySetName + "]");
			throw eaee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1021", "Could not rename policy set: [" + oldPolicySetName + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public PolicySet renamePolicy(
		String parentPolicySetName,
		String oldPolicyName, 
		String newPolicyName)
	throws
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		try {

			LOGGER.debug("PolicySetServiceImpl::renameGlobalPolicy(): oldPolicyName: [{}], newPolicyName: [{}]", oldPolicyName, newPolicyName);
			PolicySet policySet = this.getPolicySetByName(parentPolicySetName);

			AbstractPolicy abstractPolicy = policySet.getPolicyByName(oldPolicyName);
			
			if (policySet.hasPolicyByName(newPolicyName)) {
				throw new EntityAlreadyExistsException("AbstractPolicy with name: [" + newPolicyName + "] already exists.");			
			}
			
			abstractPolicy.setPolicyName(newPolicyName);
			this.policySetRepository.updatePolicySet(policySet);
			
			return policySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1024", "Could not rename policy: [" + oldPolicyName + "] with parent policy set: [" + parentPolicySetName + "] because it doesn't exist");
			throw ednee;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1025", "Could not rename policy: [" + oldPolicyName + "] with parent policy set: [" + parentPolicySetName + "] because it already has a policy with name: [newPolicySetName]");
			throw eaee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1010", "Could not rename policy: [" + oldPolicyName + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public PolicySet updateVehiclePolicy(
		String parentPolicySetName,	
		String policyName,
		String policyDescription,
		Object objPolicyValue,
		Boolean allowRegionalChangeable,
		Boolean allowUserChangeable,
		Boolean allowServiceChangeable,
		Boolean allowCustomerFeedback,
		String hmi,
		String vehicleHmiFile,
		String phone,
		String strOtaFunction,
		String strPolicyValueType,
		String policyValueConstraints)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		try {

			PolicySet parentPolicySet = this.getPolicySetByName(parentPolicySetName);
			VehiclePolicy vehiclePolicy = parentPolicySet.getVehiclePolicyByName(policyName);

			OtaFunction otaFunction = null;
			if (strOtaFunction != null) {
				otaFunction = OtaFunction.valueOf(strOtaFunction);
			}
			
			PolicyValueType policyValueType = buildPolicyValueType(strPolicyValueType);
			AbstractPolicyValue policyValue = buildPolicyValue(vehiclePolicy, policyValueType, objPolicyValue, policyValueConstraints);
			
			vehiclePolicy.setPolicyDescription(policyDescription);
			vehiclePolicy.setPolicyValue(policyValue);
			vehiclePolicy.setAllowRegionalChangeable(allowRegionalChangeable);
			vehiclePolicy.setAllowUserChangeable(allowUserChangeable);
			vehiclePolicy.setAllowServiceChangeable(allowServiceChangeable);
			vehiclePolicy.setAllowCustomerFeedback(allowCustomerFeedback);
			vehiclePolicy.setHmi(hmi);
			vehiclePolicy.setVehicleHmiFile(vehicleHmiFile);
			vehiclePolicy.setPhone(phone);		
			vehiclePolicy.setOtaFunction(otaFunction);
			vehiclePolicy.setPolicyValueType(policyValueType);
			vehiclePolicy.setPolicyValueConstraints(policyValueConstraints);

			LOGGER.debug("PolicySetServiceImpl::updateVehiclePolicy(): parentPolicySet: [{}], policyName: [{}].", parentPolicySet, policyName);
			this.policySetRepository.updatePolicySet(parentPolicySet);
			
			return parentPolicySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1026", "Could not update policy: [" + policyName + "] with parent policy set: [" + parentPolicySetName + "] because it doesn't exist");
			throw ednee;
	
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1027", "Could not update policy: [" + policyName + "] with parent policy set: [" + parentPolicySetName + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public PolicySet updateCloudPolicy(
		String parentPolicySetName,	
		String policyName,
		String policyDescription,
		Object objPolicyValue,
		String strPolicyValueType,
		String policyValueConstraints)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		try {

			PolicySet parentPolicySet = this.getPolicySetByName(parentPolicySetName);
			CloudPolicy abstractPolicy = parentPolicySet.getCloudPolicyByName(policyName);

			PolicyValueType policyValueType = buildPolicyValueType(strPolicyValueType);
			AbstractPolicyValue policyValue = buildPolicyValue(abstractPolicy, policyValueType, objPolicyValue, policyValueConstraints);
			
			abstractPolicy.setPolicyDescription(policyDescription);
			abstractPolicy.setPolicyValue(policyValue);
			abstractPolicy.setPolicyValueType(policyValueType);
			abstractPolicy.setPolicyValueConstraints(policyValueConstraints);

			LOGGER.debug("PolicySetServiceImpl::updateCloudPolicy(): parentPolicySet: [{}], policyName: [{}].", parentPolicySet, policyName);
			this.policySetRepository.updatePolicySet(parentPolicySet);
			
			return parentPolicySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1026", "Could not update policy: [" + policyName + "] with parent policy set: [" + parentPolicySetName + "] because it doesn't exist");
			throw ednee;
	
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1027", "Could not update policy: [" + policyName + "] with parent policy set: [" + parentPolicySetName + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public PolicySet updateProgramLevelPolicyOverride(
		String parentPolicyName,
		ProgramModelYear programModelYear,
		Object objPolicyValue)
	throws 
		EntityDoesNotExistException,
		ValidationException {

		try {

			PolicySet parentPolicySet = this.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
			AbstractPolicy parentPolicy = parentPolicySet.getPolicyByName(parentPolicyName);
			
			AbstractPolicyOverride policyOverride = parentPolicy.getProgramLevelPolicyOverride(programModelYear.getParentProgram().getProgramCode(), programModelYear.getParentModelYear().getModelYear());
			
			AbstractPolicyValue policyValue = buildPolicyValue(
				parentPolicy,	
				parentPolicy.getPolicyValueType(), 
				objPolicyValue, 
				parentPolicy.getPolicyValueConstraints());
			
			policyOverride.setPolicyOverrideValue(policyValue);
			
			LOGGER.debug("PolicySetServiceImpl::updateProgramLevelPolicyOverride(): parentPolicySet: [{}], policyName: [{}], programModelYear: [{}] and policyValue: [{}].", parentPolicySet, parentPolicyName, programModelYear, objPolicyValue);
			this.policySetRepository.updatePolicySet(parentPolicySet);
			
			return parentPolicySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1028", "Could not update program level policy override because either it does not exist, the global policy set does not exist, the parent policy: [" + parentPolicyName + "] does not exist or the parent program model year: [" + programModelYear + "] does not exist");
			throw ednee;
	
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1029", "Could not update program level policy override with: [" + parentPolicyName + "] and parent program model year: [" + programModelYear + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public PolicySet updateRegionLevelPolicyOverride(
		String parentPolicySetName,	
		String parentPolicyName,	
		String parentRegionCode,
		Object objPolicyValue)
	throws 
		EntityDoesNotExistException,
		ValidationException {
	
		try {

			PolicySet parentPolicySet = this.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
			AbstractPolicy parentPolicy = parentPolicySet.getPolicyByName(parentPolicyName);
			
			AbstractPolicyOverride policyOverride = parentPolicy.getRegionLevelPolicyOverride(parentRegionCode);
			
			AbstractPolicyValue policyValue = buildPolicyValue(
				parentPolicy,	
				parentPolicy.getPolicyValueType(), 
				objPolicyValue, 
				parentPolicy.getPolicyValueConstraints());
			
			policyOverride.setPolicyOverrideValue(policyValue);
			
			LOGGER.debug("PolicySetServiceImpl::updateRegionLevelPolicyOverride(): parentPolicySetName: [{}], policyName: [{}], parentRegionCode: [{}] and policyValue: [{}].", parentPolicySet, parentPolicyName, parentRegionCode, objPolicyValue);
			this.policySetRepository.updatePolicySet(parentPolicySet);
			
			return parentPolicySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1030", "Could not update region level policy override because either it does not exist, parent policy set: [" + parentPolicySetName + "] does not exist, the parent policy: [" + parentPolicyName + "] does not exist or the parent region: [" + parentRegionCode + "] does not exist");
			throw ednee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1031", "Could not update region level policy override with: [" + parentPolicyName + "], parent policy set: [" + parentPolicySetName + "] and parent region: [" + parentRegionCode + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public void deletePolicySet(
		String policySetName)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		try {

			LOGGER.debug("PolicySetServiceImpl::deletePolicySet(): policySetName: [{}].", policySetName);
			PolicySet policySet = this.getPolicySetByName(policySetName);
			this.policySetRepository.deletePolicySet(policySet);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1034", "Could not delete policy set: [" + policySetName + "] because it does not exist");
			throw ednee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1035", "Could not delete policy set: [" + policySetName + "] because the specified policy set name is invalid");
			throw ve;
		}
	}

	public PolicySet deletePolicy(
		String parentPolicySetName,	
		String policyName)
	throws
		EntityDoesNotExistException,
		ValidationException {

		try {

			LOGGER.debug("PolicySetServiceImpl::deletePolicy(): parentPolicySetName: [{}], policyName: [{}].", parentPolicySetName, policyName);
			PolicySet policySet = this.getPolicySetByName(parentPolicySetName);
			policySet.removePolicy(policyName);
			return policySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1036", "Could not delete policy: [" + policyName + "] because either it or parent policy set: [" + parentPolicySetName + "] does not exist");
			throw ednee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1037", "Could not delete policy: [" + policyName + "] with parent policy set: [" + parentPolicySetName + "] because the specified policy name is invalid");
			throw ve;
		}
	}

	public PolicySet deleteProgramLevelPolicyOverride(
		String parentPolicyName,
		ProgramModelYear programModelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {

		try {

			LOGGER.debug("PolicySetServiceImpl::deleteProgramLevelPolicyOverride(): parentPolicyName: [{}], programModelYear: [{}]", parentPolicyName, programModelYear);
			PolicySet policySet = this.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
			
			policySet.removeProgramLevelPolicyOverride(
				parentPolicyName, 
				programModelYear.getParentProgram().getProgramCode(), 
				programModelYear.getParentModelYear().getModelYear());
			
			return policySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1038", "Could not delete program level global policy override for policy: [" + parentPolicyName + "] because either it, the parent global policy set: [" + PolicySet.GLOBAL_POLICY_SET_NAME + "] or the program: [" + programModelYear + "] does not exist");
			throw ednee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1039", "Could not delete program level global policy override for policy: [" + parentPolicyName + "] with parent global policy set: [" + PolicySet.GLOBAL_POLICY_SET_NAME + "] because attribute: [" + ve.getReason() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public PolicySet deleteRegionLevelPolicyOverride(
		String parentPolicySetName,	
		String parentPolicyName,	
		String parentRegionCode)
	throws 
		EntityDoesNotExistException,
		ValidationException {

		try {

			LOGGER.debug("PolicySetServiceImpl::deleteRegionLevelPolicyOverride(): parentPolicyName: [{}], parentRegionCode: [{}]", parentPolicyName, parentRegionCode);
			PolicySet policySet = this.getPolicySetByName(parentPolicySetName);
			
			policySet.removeRegionLevelPolicyOverride(
				parentPolicyName, 
				parentRegionCode);
			
			return policySet;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1040", "Could not delete region level policy override for policy: [" + parentPolicyName + "] because either it, the parent policy set: [" + parentPolicySetName + "] or the region: [" + parentRegionCode + "] does not exist");
			throw ednee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1041", "Could not delete region level policy override for policy: [" + parentPolicyName + "] with parent policy set: [" + parentPolicySetName + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	
	// REGIONAL RELATED CRUD OPERATIONS
	public Region createRegion(
		String regionCode,
		String countryName)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		try {
			return this.policySetRepository.createRegion(regionCode, countryName);
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1042", "Could not create region: [" + regionCode + "] because it already exists");
			throw eaee;

		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1043", "Could not create region because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public List<Region> getAllRegions() {
		return this.policySetRepository.getAllRegions();
	}
	
	public Region getRegionByCode(
        String regionCode)
	throws 
		EntityDoesNotExistException {
		try {
			return this.policySetRepository.getRegionByCode(regionCode);	
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1044", "Could not retrieve region: [" + regionCode + "] because it does not exist");
			throw ednee;
		}
	}
	
	public Region renameRegion(
		String oldRegionCode, 
		String newRegionCode)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException {
		
		try {
			return this.policySetRepository.renameRegion(oldRegionCode, newRegionCode);
		} catch (EntityDoesNotExistException edee) {
			this.setExceptionIdentity(edee, "1045", "Could not rename region: [" + oldRegionCode + "] because it doesn't exist");
			throw edee;

		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1046", "Could not rename region: [" + oldRegionCode + "] because there already exists a region with code: [" + newRegionCode + "]");
			throw eaee;
		}
	}

	public Region updateRegion(
		String regionCode, 
		String countryName)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		try {
			Region region = this.policySetRepository.getRegionByCode(regionCode);
			region.setCountryName(countryName);
			return this.policySetRepository.updateRegion(region);	
		} catch (EntityDoesNotExistException edee) {
			this.setExceptionIdentity(edee, "1047", "Could not update region: [" + regionCode + "] because it doesn't exist");
			throw edee;

		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1048", "Could not update region: [" + regionCode + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public void deleteRegion(
		String regionCode)
	throws 
		EntityDoesNotExistException,
		ValidationException {

		try {
			validateAttribute("1057", "regionCode", regionCode, "Could not delete region because : [" + regionCode + "] is not valid");
			
			LOGGER.debug("PolicySetServiceImpl::deleteRegion(): regionCode: [{}].", regionCode);
			Region region = this.getRegionByCode(regionCode);
			this.policySetRepository.deleteRegion(region);
			
		} catch (EntityDoesNotExistException edee) {
			this.setExceptionIdentity(edee, "1056", "Could not delete region: [" + regionCode + "] because it doesn't exist");
			throw edee;
		}
	}
	
	public List<PolicySetHistoryEvent> getPolicySetHistoryEvents(
		String policySetName)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		throw new RuntimeException("Not implemented yet.");
	}
		
	// PURE BUSINESS BEHAVIORS (NON CRUD OPERATIONS)
	public String renderGenericPolicyTableJsonForProgramAndRegion(
		ProgramModelYear programModelYear,	
		String regionCode)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		try {
			
			// Initialize an empty set of policies (i.e. "running set of policies")
			Set<AbstractPolicy> runningPolicies = new TreeSet<AbstractPolicy>();
			

			// Get the set of global policies and add its set of policies to our set of policies
			PolicySet globalPolicySet = this.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
			runningPolicies.addAll(globalPolicySet.getPolicies());

			
			 // Get the set of policies for the given program model year and add its set of policies to our running set of policies 
			 // (it is assumed that there are no name clashes, but if there were, the program level policy would replace the global policy)
			PolicySet programPolicySet = programModelYear.getPolicySet();
			runningPolicies.addAll(programPolicySet.getPolicies());
			
			
			// Initialize a set of PolicyTableEntry DTO objects (that represent the JSON to be delivered to the vehicle)			
			List<PolicyTable> policyTableDtos = new ArrayList<>();
			
			
			// Iterate through this running set of policies and call the "getOverriddenPolicyValue()" business method for each policy,
			// which will return a policy value that is determined by the following hierarchy of policy overrides (if they exist).
			// 
			// NOTE: This logic comes from requirement "###R_F_PUC_00002### Overlaying of policies" from the "AbstractPolicy Update Cloud" spec
			// Rank   AbstractPolicy Type
			// --------------------
			// 1      TmcVehicle
			// 2      Region
			// 3      Program
			// 4      Global NOTE: This "level" is not in the spec, but was added after the fact from meetings with PD
			Iterator<AbstractPolicy> runningPoliciesIterator = runningPolicies.iterator();
			while (runningPoliciesIterator.hasNext()) {
				
				AbstractPolicy abstractPolicy = runningPoliciesIterator.next();
				AbstractPolicyValue policyValue = abstractPolicy.getOverriddenPolicyValue(
					programModelYear.getParentProgram().getProgramCode(),
					programModelYear.getParentModelYear().getModelYear(),
					regionCode);
				
				if (policyValue.getPolicyValue().toString().equals(PolicySet.UNDEFINED)) {
					
					throw new ValidationException("policyValue", "Policy: [" + abstractPolicy + "] must be overridden, as it has an UNDEFINED policy value");
				}
				
				policyTableDtos.add(maptoPolicyTableDto(abstractPolicy, policyValue));
			}

			
			// Create the PolicyTable container DTO (containing the PolicyTableEntry DTOS) so that we can marshall it to JSON
			PolicyTableContainer policyTableContainerDto = new PolicyTableContainer();
			policyTableContainerDto.setVIN("");
			policyTableContainerDto.setESN("");
			policyTableContainerDto.setPolicyTable(policyTableDtos);

			
			// Return the generated JSON to the caller, which is suitable for upload to TMC ByteStream (and needs to be uniquely named, preferably with the
			// combination of rollout code, program code, model year and vin that this JSON is for.
			return marshallFromPolicyTableContainerDtoToJson(policyTableContainerDto);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1049", "Could not render program/region generic policy table JSON for program model year: [" + programModelYear + "] and region: [" +  regionCode + "]");
			throw ednee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1050", "Could not render program/region generic policy table JSON for program model year: [" + programModelYear + "] and region: [" + regionCode + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
		
	public String renderGlobalPolicyTableJson()
	throws 
		EntityDoesNotExistException,
		ValidationException {

		try {
			
			// Initialize an empty set of policies (i.e. "running set of policies")
			Set<AbstractPolicy> runningPolicies = new TreeSet<AbstractPolicy>();
			

			// Get the set of global policies and add its set of policies to our set of policies
			PolicySet globalPolicySet = this.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
			runningPolicies.addAll(globalPolicySet.getPolicies());

			
			// Initialize a set of PolicyTableEntry DTO objects (that represent the JSON to be delivered to the vehicle)			
			List<PolicyTable> policyTableDtos = new ArrayList<>();
			
			
			// Iterate through this running set of policies and call the "getOverriddenPolicyValue()" business method for each policy,
			// which will return a policy value that is determined by the following hierarchy of policy overrides (if they exist).
			// 
			// NOTE: This logic comes from requirement "###R_F_PUC_00002### Overlaying of policies" from the "AbstractPolicy Update Cloud" spec
			// Rank   AbstractPolicy Type
			// --------------------
			// 1      TmcVehicle
			// 2      Region
			// 3      Program
			// 4      Global NOTE: This "level" is not in the spec, but was added after the fact from meetings with PD
			Iterator<AbstractPolicy> runningPoliciesIterator = runningPolicies.iterator();
			while (runningPoliciesIterator.hasNext()) {
				
				AbstractPolicy abstractPolicy = runningPoliciesIterator.next();
				AbstractPolicyValue policyValue = abstractPolicy.getPolicyValue();
				
				if (policyValue.getPolicyValue().toString().equals(PolicySet.UNDEFINED)) {
					
					throw new ValidationException("policyValue", "Policy: [" + abstractPolicy + "] must be overridden, as it has an UNDEFINED policy value");
				}
				
				policyTableDtos.add(maptoPolicyTableDto(abstractPolicy, policyValue));
			}

			
			// Create the PolicyTable container DTO (containing the PolicyTableEntry DTOS) so that we can marshall it to JSON
			PolicyTableContainer policyTableContainerDto = new PolicyTableContainer();
			policyTableContainerDto.setVIN("");
			policyTableContainerDto.setESN("");
			policyTableContainerDto.setPolicyTable(policyTableDtos);

			
			// Return the generated JSON to the caller, which is suitable for upload to TMC ByteStream (and needs to be uniquely named, preferably with the
			// combination of rollout code, program code, model year and vin that this JSON is for.
			return marshallFromPolicyTableContainerDtoToJson(policyTableContainerDto);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1054", "Could not render global generic policy table JSON because the global policy set does not exist");
			throw ednee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1055", "Could not render global generic policy table JSON because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public String renderGlobalPolicyTableJsonForRegion(String regionCode)
	throws 
		EntityDoesNotExistException,
		ValidationException {

		try {
			
			// Initialize an empty set of policies (i.e. "running set of policies")
			Set<AbstractPolicy> runningPolicies = new TreeSet<AbstractPolicy>();
			

			// Get the set of global policies and add its set of policies to our set of policies
			PolicySet globalPolicySet = this.getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
			runningPolicies.addAll(globalPolicySet.getPolicies());

			
			// Initialize a set of PolicyTableEntry DTO objects (that represent the JSON to be delivered to the vehicle)			
			List<PolicyTable> policyTableDtos = new ArrayList<>();
			
			
			// Iterate through this running set of policies and call the "getOverriddenPolicyValue()" business method for each policy,
			// which will return a policy value that is determined by the following hierarchy of policy overrides (if they exist).
			// 
			// NOTE: This logic comes from requirement "###R_F_PUC_00002### Overlaying of policies" from the "AbstractPolicy Update Cloud" spec
			// Rank   AbstractPolicy Type
			// --------------------
			// 1      TmcVehicle
			// 2      Region
			// 3      Program
			// 4      Global NOTE: This "level" is not in the spec, but was added after the fact from meetings with PD
			Iterator<AbstractPolicy> runningPoliciesIterator = runningPolicies.iterator();
			while (runningPoliciesIterator.hasNext()) {
				
				AbstractPolicy abstractPolicy = runningPoliciesIterator.next();
				AbstractPolicyValue policyValue = abstractPolicy.getOverriddenPolicyValue(regionCode);
				
				if (policyValue.getPolicyValue().toString().equals(PolicySet.UNDEFINED)) {
					
					throw new ValidationException("policyValue", "Policy: [" + abstractPolicy + "] must be overridden, as it has an UNDEFINED policy value");
				}
				
				policyTableDtos.add(maptoPolicyTableDto(abstractPolicy, policyValue));
			}

			
			// Create the PolicyTable container DTO (containing the PolicyTableEntry DTOS) so that we can marshall it to JSON
			PolicyTableContainer policyTableContainerDto = new PolicyTableContainer();
			policyTableContainerDto.setVIN("");
			policyTableContainerDto.setESN("");
			policyTableContainerDto.setPolicyTable(policyTableDtos);

			
			// Return the generated JSON to the caller, which is suitable for upload to TMC ByteStream (and needs to be uniquely named, preferably with the
			// combination of rollout code, program code, model year and vin that this JSON is for.
			return marshallFromPolicyTableContainerDtoToJson(policyTableContainerDto);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1054", "Could not render global generic policy table with region overrides JSON because the global policy set or region: [" + regionCode + "] does not exist");
			throw ednee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1055", "Could not render global generic policy table with region overrides JSON because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	private PolicyTable maptoPolicyTableDto(
		AbstractPolicy abstractPolicy, 
		AbstractPolicyValue defaultValue) {
		
		PolicyTable policyTableDto = new PolicyTable();
		
		policyTableDto.setName(abstractPolicy.getPolicyName());
		policyTableDto.setDescription(abstractPolicy.getPolicyDescription());
		
		if (abstractPolicy instanceof VehiclePolicy) {
			
			VehiclePolicy vehiclePolicy = (VehiclePolicy)abstractPolicy;

			policyTableDto.setRegional(mapPolicyTableBooleanToString(vehiclePolicy.getAllowRegionalChangeable()));
			policyTableDto.setUserChangeable(mapPolicyTableBooleanToString(vehiclePolicy.getAllowUserChangeable()));
			policyTableDto.setServiceChangeable(mapPolicyTableBooleanToString(vehiclePolicy.getAllowServiceChangeable()));
			policyTableDto.setCustomerFeedback(mapPolicyTableBooleanToString(vehiclePolicy.getAllowCustomerFeedback()));
			policyTableDto.setHMI(vehiclePolicy.getHmi());
			policyTableDto.setPhone(vehiclePolicy.getPhone());
			policyTableDto.setVehicleHMIFile(vehiclePolicy.getVehicleHmiFile());
			policyTableDto.setOTAFunction(vehiclePolicy.getOtaFunction().toString()); // TODO: TDM: Ask PD if we need to map this enum to their enum
		}
		
		policyTableDto.setDefaultValue(defaultValue.getPolicyValue().toString());
		policyTableDto.setUserValue(""); // This is only to be set when the policy is overridden by service/consumer (vehicle level)
		policyTableDto.setType(abstractPolicy.getPolicyValueType().toString());
    
		return policyTableDto;
	}
	
	private String mapPolicyTableBooleanToString(Boolean booleanValue) {

		String stringValue = "N";
		if (booleanValue) {
			stringValue = "Y";
		}
		return stringValue;
	}
	
	private String marshallFromPolicyTableContainerDtoToJson(PolicyTableContainer dto) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(dto);
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to marshall from ORFIN PolicyTableContainer DTO to json: [" + dto + "], error: [" + ioe.getMessage() + "].", ioe);
		}
	}
	
	private PolicyValueType buildPolicyValueType(String policyValueType) throws ValidationException {
		
		if (policyValueType == null || 
			(!policyValueType.equals(PolicyValueType.STRING.toString()) 
				&& !policyValueType.equals(PolicyValueType.NUMERIC.toString()) 
				&& !policyValueType.equals(PolicyValueType.ENUM.toString()))) {
			
			throw new ValidationException("policyValueType", "expected to be STRING, NUMERIC or ENUM, but instead was: [" + policyValueType + "].");
		}
		return PolicyValueType.valueOf(policyValueType);
	}
	
	private AbstractPolicyValue buildPolicyValue(
		AbstractPolicy parentPolicy,
		PolicyValueType policyValueType, 
		Object objPolicyValue, 
		String policyValueConstraints) 
	throws 
		ValidationException {

		String attribute = "policyValue";
		String message = "must be non-null and of type: [" + policyValueType + "] adhering to policyValueConstraints: [" + policyValueConstraints + "].";
		
		if (objPolicyValue == null) {
		
			throw new ValidationException(attribute, message);
		}
		
		AbstractPolicyValue policyValue = null;
		
		if (policyValueType.equals(PolicyValueType.STRING)) {
			
			// TODO: TDM: Implement policyValueConstraints
			policyValue = new StringValue(parentPolicy, objPolicyValue.toString());
			
		} else if (policyValueType.equals(PolicyValueType.NUMERIC)) {
			
			try {
				policyValue = new NumericValue(parentPolicy, (Number)objPolicyValue);
				// TODO: TDM: Implement policyValueConstraints
			} catch (NumberFormatException nfe) {
				throw new ValidationException(attribute, message, nfe);
			}
			
		} else {

			String strPolicyValue = objPolicyValue.toString();
			policyValue = new StringValue(parentPolicy, strPolicyValue);
			
			if (policyValueConstraints == null || policyValueConstraints.isEmpty()) {
				throw new ValidationException("policyValueConstraints", " must be a comma delimited set of enum choices for policy value.");
			}
			String[] enumChoiceArray = policyValueConstraints.split("_");
			Set<String> set = new HashSet<>();
			set.addAll(Arrays.asList(enumChoiceArray));
			if (!set.contains(strPolicyValue)) {
				throw new ValidationException("policyValue", "[" + strPolicyValue + "], must be one of the following choices: [" + set + "].");
			}
		}
		
		return policyValue;
	}
	
	private void validateAttribute(String uniqueErrorCode, String attributeName, String attributeValue, String validationMessage) throws ValidationException {
		
		if (attributeValue == null || attributeValue.isEmpty()) {
			throw new ValidationException(
				attributeName, 
				validationMessage,
				BOUNDED_CONTEXT_NAME,
				SERVICE_NAME,
				uniqueErrorCode);
		}
	}
	
	private void setExceptionIdentity(ExceptionIdentity exceptionIdentity, String uniqueErrorCode, String messageOverride) {
		
		exceptionIdentity.setBoundedContextName(BOUNDED_CONTEXT_NAME);
		exceptionIdentity.setServiceName(SERVICE_NAME);
		exceptionIdentity.setUniqueErrorCode(uniqueErrorCode);
		exceptionIdentity.setMessageOverride(messageOverride);
	}
}
