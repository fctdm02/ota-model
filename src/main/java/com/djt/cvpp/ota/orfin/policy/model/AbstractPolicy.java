/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType;
import com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyProgramModelYearOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyRegionOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyVehicleOverride;
import com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractPolicy extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;


	public static final String PARENT_POLICY_SET_ERROR = "parentPolicySet must be specified.";
	public static final String POLICY_NAME_ERROR = "policyName must be specified.";
	public static final String POLICY_VALUE_ERROR = "policyValue must be specified.";
	
	private PolicySet parentPolicySet;
	private String policyName;
	private String policyDescription;
	private AbstractPolicyValue policyValue;
	private PolicyValueType policyValueType = PolicyValueType.STRING; // If STRING, then policyValue and any overrides need to be strings.
	private String policyValueConstraints = ""; // If PolicyValueType is ENUM, then constraints contain a comma separated list of enum choices.  If PolicyValueType is NUMERIC, then the constraints are the lower and upperbound. 
	private Set<AbstractPolicyOverride> policyOverrides = new TreeSet<>();
	
	protected AbstractPolicy(
		PolicySet parentPolicySet,
		String policyName,
		String policyDescription,
		AbstractPolicyValue policyValue,
		PolicyValueType policyValueType,
		String policyValueConstraints,
		Set<AbstractPolicyOverride> policyOverrides) throws ValidationException {
		
		this.parentPolicySet = parentPolicySet;
		if (this.parentPolicySet == null) {
			throw new ValidationException(this.getClassAndIdentity(), PARENT_POLICY_SET_ERROR);	
		}
		
		this.policyName = policyName;
		if (this.policyName == null || this.policyName.isEmpty()) {
			throw new ValidationException(this.getClassAndIdentity(), POLICY_NAME_ERROR);	
		}

		this.policyValue = policyValue;
		if (this.policyValue == null) {
			throw new ValidationException(this.getClassAndIdentity(), POLICY_VALUE_ERROR);	
		}
		this.policyValue.setParentPolicy(this);
		
		this.policyDescription = policyDescription;
		
		this.policyValueType = policyValueType;
		this.policyValueConstraints = policyValueConstraints;
		
		this.policyOverrides.addAll(policyOverrides);
	}
	
	public String getNaturalIdentity() {

		return AbstractEntity.buildNaturalIdentity(parentPolicySet, policyName);
	}

	public void validate(List<String> validationMessages) {
		
		validateNotNull(validationMessages, "parentPolicySet", parentPolicySet);
		validateNotNull(validationMessages, "policyName", policyName);
		validateNotNull(validationMessages, "policyValue", policyValue);
		validateNotNull(validationMessages, "policyValueType", policyValueType);
		
		if (policyOverrides != null && !policyOverrides.isEmpty()) {
			Iterator<AbstractPolicyOverride> iterator = policyOverrides.iterator();
			while (iterator.hasNext()) {

				AbstractPolicyOverride policyOverride = iterator.next();
				policyOverride.validate(validationMessages);
			}
		}
	}
	
	protected void setParentPolicySet(PolicySet parentPolicySet) {
		this.parentPolicySet = parentPolicySet;
	}
	
	public PolicySet getParentPolicySet() {
		return this.parentPolicySet;
	}
	
	public String getPolicyName() {
		return policyName;
	}

	public String getPolicyDescription() {
		return policyDescription;
	}

	public AbstractPolicyValue getPolicyValue() {
		return policyValue;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public void setPolicyDescription(String policyDescription) {
		this.policyDescription = policyDescription;
	}

	public void setPolicyValue(AbstractPolicyValue policyValue) {
		this.policyValue = policyValue;
	}

	public PolicyValueType getPolicyValueType() {
		return policyValueType;
	}

	public String getPolicyValueConstraints() {
		return policyValueConstraints;
	}

	public void setPolicyValueType(PolicyValueType policyValueType) {
		this.policyValueType = policyValueType;
	}

	public void setPolicyValueConstraints(String policyValueConstraints) {
		this.policyValueConstraints = policyValueConstraints;
	}

	public boolean addPolicyOverride(AbstractPolicyOverride policyOverride) throws EntityAlreadyExistsException, ValidationException {
		return this.policyOverrides.add(policyOverride);
	}
	
	public boolean removePolicyOverride(AbstractPolicyOverride policyOverride) throws EntityDoesNotExistException {
		
		if (!this.policyOverrides.contains(policyOverride)) {
			throw new EntityDoesNotExistException("AbstractPolicy: [" 
				+ this 
				+ "] does not have a policy override with type: [" 
				+ policyOverride.getClass().getSimpleName() 
				+ "] and identity: [" 
				+ policyOverride 
				+ "], so it cannot be removed.");
		}
		
		if (policyOverride instanceof PolicyProgramModelYearOverride && !this.parentPolicySet.getPolicySetName().equals(PolicySet.GLOBAL_POLICY_SET_NAME)) {
			throw new FenixRuntimeException("Only the global PolicySet can be associated with ProgramModelYear overrides.");
		}
		
		return this.policyOverrides.remove(policyOverride);
	}
	
	public AbstractPolicyOverride getProgramLevelPolicyOverride(String programCode, Integer modelYear) throws EntityDoesNotExistException {

		Iterator<AbstractPolicyOverride> iterator = policyOverrides.iterator();
		while (iterator.hasNext()) {

			AbstractPolicyOverride policyOverride = iterator.next();
			
			if (policyOverride instanceof PolicyProgramModelYearOverride && !this.parentPolicySet.getPolicySetName().equals(PolicySet.GLOBAL_POLICY_SET_NAME)) {
				throw new FenixRuntimeException("Only the global PolicySet can be associated with ProgramModelYear overrides.");
			}
			
			if (policyOverride instanceof PolicyProgramModelYearOverride) {
				
				PolicyProgramModelYearOverride policyProgramModelYearOverride = (PolicyProgramModelYearOverride)policyOverride;
				ProgramModelYear programModelYear = policyProgramModelYearOverride.getProgramModelYear();
				if (programModelYear.getParentProgram().getProgramCode().equals(programCode) && programModelYear.getParentModelYear().getModelYear().equals(modelYear)) {
					return policyProgramModelYearOverride;
				}
			}
		}
		throw new EntityDoesNotExistException("AbstractPolicy: [" 
			+ this 
			+ "] does not have a program level policy override with program code: [" 
			+ programCode 
			+ "] and model year: [" 
			+ modelYear 
			+ "].");
	}

	public AbstractPolicyOverride getRegionLevelPolicyOverride(String regionCode) throws EntityDoesNotExistException {

		Iterator<AbstractPolicyOverride> iterator = policyOverrides.iterator();
		while (iterator.hasNext()) {

			AbstractPolicyOverride policyOverride = iterator.next();
			
			if (policyOverride instanceof PolicyProgramModelYearOverride && !this.parentPolicySet.getPolicySetName().equals(PolicySet.GLOBAL_POLICY_SET_NAME)) {
				throw new FenixRuntimeException("Only the global PolicySet can be associated with ProgramModelYear overrides.");
			}
			
			if (policyOverride instanceof PolicyRegionOverride) {
				
				PolicyRegionOverride policyRegionOverride = (PolicyRegionOverride)policyOverride;
				if (policyRegionOverride.getRegion().getRegionCode().equals(regionCode)) {
					return policyRegionOverride;
				}
			}
		}
		throw new EntityDoesNotExistException("AbstractPolicy: [" 
			+ this 
			+ "] does not have a region level policy override with region code: [" 
			+ regionCode 
			+ "].");
	}

	public AbstractPolicyOverride getVehicleLevelPolicyOverride(String vin) throws EntityDoesNotExistException {

		Iterator<AbstractPolicyOverride> iterator = policyOverrides.iterator();
		while (iterator.hasNext()) {

			AbstractPolicyOverride policyOverride = iterator.next();
			
			if (policyOverride instanceof PolicyProgramModelYearOverride && !this.parentPolicySet.getPolicySetName().equals(PolicySet.GLOBAL_POLICY_SET_NAME)) {
				throw new FenixRuntimeException("Only the global PolicySet can be associated with ProgramModelYear overrides.");
			}
			
			if (policyOverride instanceof PolicyVehicleOverride) {
				
				PolicyVehicleOverride policyVehicleOverride = (PolicyVehicleOverride)policyOverride;
				if (policyVehicleOverride.getVehicle().getVin().equals(vin)) {
					return policyVehicleOverride;
				}
			}
		}
		throw new EntityDoesNotExistException("AbstractPolicy: [" 
			+ this 
			+ "] does not have a vehicle level policy override with vin: [" 
			+ vin 
			+ "].");
	}
	
	public List<AbstractPolicyOverride> getPolicyOverrides() {
		
		List<AbstractPolicyOverride> list = new ArrayList<>();
		list.addAll(policyOverrides);
		return list;
	}

	public void setPolicyOverrides(Set<AbstractPolicyOverride> policyOverrides) {
		this.policyOverrides = policyOverrides;
	}
	
	// BUSINESS METHODS
	public AbstractPolicyValue getOverriddenPolicyValue(
		String programCode, 
		Integer modelYear) {
		
		AbstractPolicyValue overriddenPolicyValue = this.getPolicyValue();
		Iterator<AbstractPolicyOverride> iterator = this.policyOverrides.iterator();
		while (iterator.hasNext()) {

			AbstractPolicyOverride policyOverride = iterator.next();
			
			if (policyOverride instanceof PolicyProgramModelYearOverride) {
				
				if (!this.parentPolicySet.getPolicySetName().equals(PolicySet.GLOBAL_POLICY_SET_NAME)) {
					throw new FenixRuntimeException("Only the global PolicySet can be associated with ProgramModelYear overrides.");
				}
				
				PolicyProgramModelYearOverride policyProgramModelYearOverride = (PolicyProgramModelYearOverride)policyOverride;
				if (policyProgramModelYearOverride.getProgramModelYear().getParentProgram().getProgramCode().equals(programCode)
					&& policyProgramModelYearOverride.getProgramModelYear().getParentModelYear().getModelYear().equals(modelYear)) {
					
					overriddenPolicyValue = policyProgramModelYearOverride.getPolicyOverrideValue();
				}
			}
		}
		
		return overriddenPolicyValue;
	}
	
	public AbstractPolicyValue getOverriddenPolicyValue(
		String programCode, 
		Integer modelYear, 
		String regionCode) {
		
		AbstractPolicyValue overriddenPolicyValue = this.getPolicyValue();
		Iterator<AbstractPolicyOverride> iterator = this.policyOverrides.iterator();
		while (iterator.hasNext()) {

			AbstractPolicyOverride policyOverride = iterator.next();
			
			if (policyOverride instanceof PolicyRegionOverride) {
				
				PolicyRegionOverride policyRegionOverride = (PolicyRegionOverride)policyOverride;
				if (policyRegionOverride.getRegion().getRegionCode().equals(regionCode)) {
					overriddenPolicyValue = policyRegionOverride.getPolicyOverrideValue();
				}
				
			} else if (policyOverride instanceof PolicyProgramModelYearOverride) {
				
				if (!this.parentPolicySet.getPolicySetName().equals(PolicySet.GLOBAL_POLICY_SET_NAME)) {
					throw new FenixRuntimeException("Only the global PolicySet can be associated with ProgramModelYear overrides.");
				}
				
				PolicyProgramModelYearOverride policyProgramModelYearOverride = (PolicyProgramModelYearOverride)policyOverride;
				if (policyProgramModelYearOverride.getProgramModelYear().getParentProgram().getProgramCode().equals(programCode)
					&& policyProgramModelYearOverride.getProgramModelYear().getParentModelYear().getModelYear().equals(modelYear)) {
					
					overriddenPolicyValue = policyProgramModelYearOverride.getPolicyOverrideValue();
				}
			}
		}
		
		return overriddenPolicyValue;
	}
	
	public AbstractPolicyValue getOverriddenPolicyValue(
		String programCode, 
		Integer modelYear, 
		String regionCode, 
		String vin) {
		
		AbstractPolicyValue overriddenPolicyValue = this.getPolicyValue();
		Iterator<AbstractPolicyOverride> iterator = this.policyOverrides.iterator();
		while (iterator.hasNext()) {

			AbstractPolicyOverride policyOverride = iterator.next();
						
			if (policyOverride instanceof PolicyVehicleOverride) {
				
				PolicyVehicleOverride policyVehicleOverride = (PolicyVehicleOverride)policyOverride;
				if (policyVehicleOverride.getVehicle().getVin().equals(vin)) {
					overriddenPolicyValue = policyVehicleOverride.getPolicyOverrideValue();
					break;
				}
				
			} else if (policyOverride instanceof PolicyRegionOverride) {
				
				PolicyRegionOverride policyRegionOverride = (PolicyRegionOverride)policyOverride;
				if (policyRegionOverride.getRegion().getRegionCode().equals(regionCode)) {
					overriddenPolicyValue = policyRegionOverride.getPolicyOverrideValue();
					break;
				}
				
			} else if (policyOverride instanceof PolicyProgramModelYearOverride) {
				
				if (!this.parentPolicySet.getPolicySetName().equals(PolicySet.GLOBAL_POLICY_SET_NAME)) {
					throw new FenixRuntimeException("Only the global PolicySet can be associated with ProgramModelYear overrides.");
				}
				
				PolicyProgramModelYearOverride policyProgramModelYearOverride = (PolicyProgramModelYearOverride)policyOverride;
				if (policyProgramModelYearOverride.getProgramModelYear().getParentProgram().getProgramCode().equals(programCode)
					&& policyProgramModelYearOverride.getProgramModelYear().getParentModelYear().getModelYear().equals(modelYear)) {
					
					overriddenPolicyValue = policyProgramModelYearOverride.getPolicyOverrideValue();
					break;
				}
			}
		}
		
		return overriddenPolicyValue;
	}

	public AbstractPolicyValue getOverriddenPolicyValue(String regionCode) {
		
		AbstractPolicyValue overriddenPolicyValue = this.getPolicyValue();
		Iterator<AbstractPolicyOverride> iterator = this.policyOverrides.iterator();
		while (iterator.hasNext()) {

			AbstractPolicyOverride policyOverride = iterator.next();
			
			if (policyOverride instanceof PolicyRegionOverride) {
				
				PolicyRegionOverride policyRegionOverride = (PolicyRegionOverride)policyOverride;
				if (policyRegionOverride.getRegion().getRegionCode().equals(regionCode)) {
					overriddenPolicyValue = policyRegionOverride.getPolicyOverrideValue();
					break;
				}
			}
		}
		
		return overriddenPolicyValue;
	}
}
