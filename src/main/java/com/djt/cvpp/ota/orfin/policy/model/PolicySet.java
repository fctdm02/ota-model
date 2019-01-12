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
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyProgramModelYearOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyRegionOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyVehicleOverride;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public class PolicySet extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	// If the policy set name is 'GLOBAL', then it will be the *only* policy set 
	// that does *not* have any associations to any child ProgramModelYears. 
	public static final String GLOBAL_POLICY_SET_NAME = "GLOBAL";
	public static final String GLOBAL_POLICY_SET_NAME_ERROR = "The Global PolicySet cannot be associated with any ProgramModelYear instances.";
	public static final String POLICY_SET_NAME_ERROR = "policySetName must be specified.";
	
	
	public static final String VEHICLE_POLICY = "VehiclePolicy";
	public static final String CLOUD_POLICY = "CloudPolicy";
	
	
	// A policy may be created without a default value. In this case, the value will be "UNDEFINED". It is expected that the policy is overridable by region, 
	// so that a regional value can be specified. if a policy table is rendered that results in an UNDEFINED value, then an exception will be thrown.
	public static final String UNDEFINED = "UNDEFINED";
	
	
	private String policySetName;
	private Set<AbstractPolicy> abstractPolicies;
	private Set<ProgramModelYear> programModelYears;
	
	private PolicySet(PolicySetBuilder builder) throws ValidationException {
		
		this.policySetName = builder.policySetName;
		if (this.policySetName == null || this.policySetName.isEmpty()) {
			throw new ValidationException(this.getClassAndIdentity(), POLICY_SET_NAME_ERROR);	
		}
				
		this.abstractPolicies = new TreeSet<>();
		this.abstractPolicies.addAll(builder.abstractPolicies);
		Iterator<AbstractPolicy> policiesIterator = this.abstractPolicies.iterator();
		while (policiesIterator.hasNext()) {

			AbstractPolicy abstractPolicy = policiesIterator.next();
			abstractPolicy.setParentPolicySet(this);
		}
		
		this.programModelYears = new TreeSet<>();
		this.programModelYears.addAll(builder.programModelYears);
		Iterator<ProgramModelYear> programModelYearsIterator = this.programModelYears.iterator();
		while (programModelYearsIterator.hasNext()) {

			ProgramModelYear programModelYear = programModelYearsIterator.next();
			programModelYear.setPolicySet(this);
		}
	}
	
	public String getNaturalIdentity() {

		return this.policySetName;
	}

	public void validate(List<String> validationMessages) {

		if (abstractPolicies == null || abstractPolicies.isEmpty()) {
			validationMessages.add("At least one AbstractPolicy must be specified.");
		} else {
			Iterator<AbstractPolicy> policiesIterator = abstractPolicies.iterator();
			while (policiesIterator.hasNext()) {

				AbstractPolicy abstractPolicy = policiesIterator.next();
				abstractPolicy.validate(validationMessages);
			}
		}
		
		if (policySetName.equalsIgnoreCase(GLOBAL_POLICY_SET_NAME) && !programModelYears.isEmpty()) {
			validationMessages.add(GLOBAL_POLICY_SET_NAME_ERROR);
		} else if (!policySetName.equalsIgnoreCase(GLOBAL_POLICY_SET_NAME) && programModelYears.isEmpty()) {
			validationMessages.add("At least one ProgramModelYear must be specified.");
		}
	}
	
	public String getPolicySetName() {
		return policySetName;
	}

	public void setPolicySetName(String policySetName) {
		this.policySetName = policySetName;
	}
	
	public AbstractPolicy getPolicyByName(String policyName) throws EntityDoesNotExistException {

		Iterator<AbstractPolicy> policiesIterator = abstractPolicies.iterator();
		while (policiesIterator.hasNext()) {

			AbstractPolicy abstractPolicy = policiesIterator.next();
			if (abstractPolicy.getPolicyName().equals(policyName)) {
				return abstractPolicy;
			}
		}
		throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have any policy with name: " + policyName);
	}

	public VehiclePolicy getVehiclePolicyByName(String policyName) throws EntityDoesNotExistException {

		Iterator<AbstractPolicy> policiesIterator = abstractPolicies.iterator();
		while (policiesIterator.hasNext()) {

			AbstractPolicy abstractPolicy = policiesIterator.next();
			if (abstractPolicy.getPolicyName().equals(policyName) && abstractPolicy instanceof VehiclePolicy) {
				return (VehiclePolicy)abstractPolicy;
			}
		}
		throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have any vehicle policy with name: " + policyName);
	}

	public CloudPolicy getCloudPolicyByName(String policyName) throws EntityDoesNotExistException {

		Iterator<AbstractPolicy> policiesIterator = abstractPolicies.iterator();
		while (policiesIterator.hasNext()) {

			AbstractPolicy abstractPolicy = policiesIterator.next();
			if (abstractPolicy.getPolicyName().equals(policyName) && abstractPolicy instanceof CloudPolicy) {
				return (CloudPolicy)abstractPolicy;
			}
		}
		throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have any cloud policy with name: " + policyName);
	}
	
	public boolean hasPolicyByName(String policyName) {

		boolean hasPolicyByName = false;
		Iterator<AbstractPolicy> policiesIterator = abstractPolicies.iterator();
		while (policiesIterator.hasNext()) {

			AbstractPolicy abstractPolicy = policiesIterator.next();
			if (abstractPolicy.getPolicyName().equals(policyName)) {
				hasPolicyByName = true;
				break;
			}
		}
		return hasPolicyByName;
	}

	public boolean removePolicy(String policyName) throws EntityDoesNotExistException {

		AbstractPolicy policyToRemove = null;
		Iterator<AbstractPolicy> policiesIterator = abstractPolicies.iterator();
		while (policiesIterator.hasNext()) {

			AbstractPolicy abstractPolicy = policiesIterator.next();
			if (abstractPolicy.getPolicyName().equals(policyName)) {
				policyToRemove = abstractPolicy;
				break;
			}
		}
		if (policyToRemove == null) {
			throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have any policy with name: " + policyName);	
		} 
		return this.abstractPolicies.remove(policyToRemove);
	}
	
	public boolean removeProgramLevelPolicyOverride(String parentPolicyName, String programCode, Integer modelYear) throws EntityDoesNotExistException {
		
		AbstractPolicyOverride policyOverrideToRemove = null;
		AbstractPolicy policyToUpdate = null;
		Iterator<AbstractPolicy> policiesIterator = abstractPolicies.iterator();
		while (policiesIterator.hasNext()) {

			AbstractPolicy abstractPolicy = policiesIterator.next();
			if (abstractPolicy.getPolicyName().equals(parentPolicyName)) {
				
				policyToUpdate = abstractPolicy;
				Iterator<AbstractPolicyOverride> policyOverridesIterator = abstractPolicy.getPolicyOverrides().iterator();
				while (policyOverridesIterator.hasNext()) {
					
					AbstractPolicyOverride policyOverride = policyOverridesIterator.next();
					
					if (policyOverride instanceof PolicyProgramModelYearOverride) {
						
						PolicyProgramModelYearOverride policyProgramModelYearOverride = (PolicyProgramModelYearOverride)policyOverride;
						if (policyProgramModelYearOverride.getProgramModelYear().getParentProgram().getProgramCode().equals(programCode) 
							&& policyProgramModelYearOverride.getProgramModelYear().getParentModelYear().getModelYear().equals(modelYear)) {
						
							policyOverrideToRemove = policyProgramModelYearOverride;
							break;
						}
					}
				}
			}
		}
		if (policyOverrideToRemove == null) {
			throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have program level policy overide with name: " + parentPolicyName + " for program code: " + programCode + ", modelYear: " + modelYear);	
		}
		return policyToUpdate.removePolicyOverride(policyOverrideToRemove);
	}

	public boolean removeRegionLevelPolicyOverride(String parentPolicyName, String parentRegionCode) throws EntityDoesNotExistException {
		
		AbstractPolicyOverride policyOverrideToRemove = null;
		AbstractPolicy policyToUpdate = null;
		Iterator<AbstractPolicy> policiesIterator = abstractPolicies.iterator();
		while (policiesIterator.hasNext()) {

			AbstractPolicy abstractPolicy = policiesIterator.next();
			if (abstractPolicy.getPolicyName().equals(parentPolicyName)) {
				
				policyToUpdate = abstractPolicy;
				Iterator<AbstractPolicyOverride> policyOverridesIterator = abstractPolicy.getPolicyOverrides().iterator();
				while (policyOverridesIterator.hasNext()) {
					
					AbstractPolicyOverride policyOverride = policyOverridesIterator.next();
					
					if (policyOverride instanceof PolicyRegionOverride) {
						
						PolicyRegionOverride policyRegionOverride = (PolicyRegionOverride)policyOverride;
						if (policyRegionOverride.getRegion().getRegionCode().equals(parentRegionCode)) {
						
							policyOverrideToRemove = policyRegionOverride;
							break;
						}
					}
				}
			}
		}
		if (policyOverrideToRemove == null) {
			throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have region level policy overide with name: " + parentPolicyName + " for region code: " + parentRegionCode);	
		}
		return policyToUpdate.removePolicyOverride(policyOverrideToRemove);
	}

	public boolean removeVehicleLevelPolicyOverride(String parentPolicyName, String parentVin) throws EntityDoesNotExistException {
		
		AbstractPolicyOverride policyOverrideToRemove = null;
		AbstractPolicy policyToUpdate = null;
		Iterator<AbstractPolicy> policiesIterator = abstractPolicies.iterator();
		while (policiesIterator.hasNext()) {

			AbstractPolicy abstractPolicy = policiesIterator.next();
			if (abstractPolicy.getPolicyName().equals(parentPolicyName)) {
				
				policyToUpdate = abstractPolicy;
				Iterator<AbstractPolicyOverride> policyOverridesIterator = abstractPolicy.getPolicyOverrides().iterator();
				while (policyOverridesIterator.hasNext()) {
					
					AbstractPolicyOverride policyOverride = policyOverridesIterator.next();
					
					if (policyOverride instanceof PolicyVehicleOverride) {
						
						PolicyVehicleOverride policyVehicleOverride = (PolicyVehicleOverride)policyOverride;
						if (policyVehicleOverride.getVehicle().getVin().equals(parentVin)) {
						
							policyOverrideToRemove = policyVehicleOverride;
							break;
						}
					}
				}
			}
		}
		if (policyOverrideToRemove == null) {
			throw new EntityDoesNotExistException(this.getClassAndIdentity() + " does not have vehicle level policy overide with name: " + parentPolicyName + " for VIN: " + parentVin);	
		}
		return policyToUpdate.removePolicyOverride(policyOverrideToRemove);
	}
	
	public List<AbstractPolicy> getPolicies() {
		List<AbstractPolicy> list = new ArrayList<>();
		list.addAll(this.abstractPolicies);
		return list;
	}
	
	public List<ProgramModelYear> getProgramModelYears() {
		List<ProgramModelYear> list = new ArrayList<>();
		list.addAll(this.programModelYears);
		return list;
	}
	
	public void addPolicies(Set<AbstractPolicy> abstractPolicies) throws EntityAlreadyExistsException {
	
		Iterator<AbstractPolicy> policiesIterator = abstractPolicies.iterator();
		while (policiesIterator.hasNext()) {

			addPolicy(policiesIterator.next());
		}
	}

	public void addPolicy(AbstractPolicy abstractPolicy) throws EntityAlreadyExistsException {
		
		if (this.abstractPolicies.contains(abstractPolicy)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has policy: " + abstractPolicy.getNaturalIdentity());
		}
		this.abstractPolicies.add(abstractPolicy);
		abstractPolicy.setParentPolicySet(this);
	}
	
	public void addProgramModelYear(ProgramModelYear programModelYear) throws ValidationException, EntityAlreadyExistsException {
		
		if (policySetName.equalsIgnoreCase(GLOBAL_POLICY_SET_NAME)) {
			throw new IllegalStateException(this.getClassAndIdentity() + GLOBAL_POLICY_SET_NAME_ERROR);
		}
		
		if (this.programModelYears.contains(programModelYear)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has an association with programModelYear: " + programModelYear);
		}
		
		programModelYear.setPolicySet(this);
		this.programModelYears.add(programModelYear);
	}

	public void removeProgramModelYear(ProgramModelYear programModelYear) throws ValidationException {
		
		if (policySetName.equalsIgnoreCase(GLOBAL_POLICY_SET_NAME)) {
			throw new ValidationException(this.getClassAndIdentity(), GLOBAL_POLICY_SET_NAME_ERROR);
		}
		programModelYear.setPolicySet(null);
		this.programModelYears.remove(programModelYear);
	}
	
	public void addProgramModelYears(Set<ProgramModelYear> programModelYears) throws ValidationException {
		
		if (policySetName.equalsIgnoreCase(GLOBAL_POLICY_SET_NAME) && !programModelYears.isEmpty()) {
			throw new ValidationException(this.getClassAndIdentity(), GLOBAL_POLICY_SET_NAME_ERROR);
		}
		this.programModelYears.addAll(programModelYears);
	}
	
	public static final class PolicySetBuilder {

		private String policySetName;
		private Set<AbstractPolicy> abstractPolicies = new TreeSet<>();
		private Set<ProgramModelYear> programModelYears = new TreeSet<>();

		public PolicySetBuilder() {
		}
		
		public PolicySetBuilder withPolicySetName(String policySetName) {
			this.policySetName = policySetName;
			return this;
		}
		
		public PolicySetBuilder withPolicies(Set<AbstractPolicy> abstractPolicies) {
			this.abstractPolicies = abstractPolicies;
			return this;
		}

		public PolicySetBuilder withProgramModelYears(Set<ProgramModelYear> programModelYears) {
			this.programModelYears = programModelYears;
			return this;
		}
		
		public PolicySet build() throws ValidationException {
			return new PolicySet(this);
		}
	}	
}
