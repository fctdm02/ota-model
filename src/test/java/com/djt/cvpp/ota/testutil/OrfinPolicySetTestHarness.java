package com.djt.cvpp.ota.testutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy;
import com.djt.cvpp.ota.orfin.policy.model.CloudPolicy;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
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
import com.djt.cvpp.ota.orfin.program.model.ModelYear;
import com.djt.cvpp.ota.orfin.program.model.Program;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

public class OrfinPolicySetTestHarness {

	public static final String POLICY_TABLE_FILENAME = "/testdata/orfin/policy/OTA_Policy_Table_1_2.csv";
	

    private static final String COMMENT = "--";
    private static final String COMMA = ",";
    private static final String UNDERSCORE = "_";
    
    
	public PolicySet buildGlobalPolicySet() throws ValidationException, EntityAlreadyExistsException, IOException {
		
		PolicySet policySetEntity = new PolicySet
			.PolicySetBuilder()
			.withPolicySetName(PolicySet.GLOBAL_POLICY_SET_NAME)
			.build();
		
		Set<AbstractPolicy> abstractPolicies = buildPolicies(policySetEntity);
		
		policySetEntity.addPolicies(abstractPolicies);
		
		policySetEntity.assertValid();
		
		return policySetEntity;		
	}

	public PolicySet buildPolicySet() throws ValidationException, EntityAlreadyExistsException, IOException {
		
		Set<ProgramModelYear> programModelYears = new TreeSet<>();
		Program programEntity = new Program.ProgramBuilder()
			.withProgramCode("C344N")
			.build();  
		
		ModelYear modelYearEntity = new ModelYear
			.ModelYearBuilder()
			.withModelYearValue(Integer.valueOf("2017"))
			.build();
		
		ProgramModelYear programModelYearEntity = new ProgramModelYear
			.ProgramModelYearBuilder()
			.withParentProgram(programEntity)
			.withParentModelYear(modelYearEntity)
			.build();
		programModelYears.add(programModelYearEntity);

		PolicySet policySetEntity = new PolicySet
			.PolicySetBuilder()
			.withProgramModelYears(programModelYears)
			.withPolicySetName("policySetName1")
			.build();
		
		policySetEntity.addPolicies(buildPolicies(policySetEntity));
		
		policySetEntity.assertValid();
		
		return policySetEntity;		
	}

	public PolicySet buildUnassociatedPolicySet() throws ValidationException, EntityAlreadyExistsException, IOException {
		
		PolicySet policySetEntity = new PolicySet
			.PolicySetBuilder()
			.withPolicySetName("policySetName1")
			.build();
		
		policySetEntity.addPolicies(buildPolicies(policySetEntity));
		
		policySetEntity.assertValid();
		
		return policySetEntity;		
	}
	
	public Set<AbstractPolicy> buildPolicies(PolicySet parentPolicySet) throws EntityAlreadyExistsException, ValidationException, IOException {
		
		/*
		-- GLOBAL OTA CLOUD AND VEHICLE POLICIES
		--
		-- Column Number:
		--  0 - policyType: either 'VehiclePolicy' or 'CloudPolicy'
		--  1 - globalPolicyName: unique name for policy
		--  2 - globalPolicyDescription: description of policy
		--  3 - allowRegionalChangeable: Vehicle Policy Only: Whether or not the policy can be overridden at the regional level
		--  4 - policyValue: The 'default value' for the policy (the data type is determined by the 
		--  5 - hmi: Vehicle Policy Only: Whether or not the policy can be overridden at the regional level
		--  6 - phone: Vehicle Policy Only: Whether or not the policy can be overridden at the regional level
		--  7 - allowUserChangeable: Vehicle Policy Only: Whether or not the policy can be overridden at the regional level
		--  8 - allowServiceChangeable: Vehicle Policy Only: Whether or not the policy can be overridden at the regional level
		--  9 - allowCustomerFeedback: Vehicle Policy Only: Whether or not the policy can be overridden at the regional level
		-- 10 - vehicleHmiFile: Vehicle Policy Only: Whether or not the policy can be overridden at the regional level
		-- 11 - otaFunction: Vehicle Policy Only: Whether or not the policy can be overridden at the regional level
		-- 12 - policyValueType: An enum value that is either STRING, NUMERIC, BOOLEAN or ENUM
		-- 13 - policyValueConstraints: (if Type is 'ENUM', then Constraints contains a "_" (underscore symbol) delimited list of possible values)
		--			
		--			Example Vehicle Policy:
		--			VehiclePolicy,Level1Authorization,Safety and security updates. No additional consent provided by customer,Y,RegionCountryFile,Read/Write,None,Y,Y,N,,OTA MANAGER,
		--			
		--			Example Cloud Policy:
		--			CloudPolicy,unbreakableManifestTime,Time for the vehicle to download/install what is in the manifest as a unit, beyond this time, the vehicle can break up the rollout, NULL, 14, NULL, NULL, NULL, NULL, NULL, NULL,NULL, NUMERIC, 0_60
		-- 
		--policyType, globalPolicyName, globalPolicyDescription, allowRegionalChangeable, policyValue, hmi, phone, allowUserChangeable, allowServiceChangeable, allowCustomerFeedback, vehicleHmiFile, otaFunction, policyValueType, policyValueConstraints
		-- --------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		 */
		Set<AbstractPolicy> abstractPolicies = new TreeSet<>();
		List<List<String>> policyDataList = loadPoliciesFromClassPath();		
		Iterator<List<String>> iterator = policyDataList.iterator();
		while (iterator.hasNext()) {
			
			List<String> policyData = iterator.next();

			PolicyValueType policyValueType = PolicyValueType.STRING;
			String type = policyData.get(12);
			if (type != null && !type.trim().isEmpty() && !type.equals("NULL")) {
				
				policyValueType = PolicyValueType.valueOf(type.trim());
			}
			
			String strOtaFunction = policyData.get(11);
			OtaFunction otaFunction = OtaFunction.OTA_MANAGER;
			if (strOtaFunction != null && !strOtaFunction.trim().isEmpty() && !strOtaFunction.equals("NULL")) {
				otaFunction = OtaFunction.valueOf(strOtaFunction.trim());
			}
			
			String policyValueConstraints = policyData.get(13);
			
			AbstractPolicyValue policyValue = null;
			if (policyValueType == PolicyValueType.STRING) {
				
				policyValue = new StringValue(policyData.get(4));
				
			} else if (policyValueType == PolicyValueType.NUMERIC) {
				
				policyValue = new NumericValue(Long.parseLong(policyData.get(4)));
				
			} else if (policyValueType == PolicyValueType.ENUM) {
				
				String strPolicyValue = policyData.get(4);
				policyValue = new StringValue(strPolicyValue);
				String[] enumValuesArray = policyValueConstraints.split(UNDERSCORE);
				boolean isValid = false;
				if (enumValuesArray != null && enumValuesArray.length > 0) {
					for (int i=0; i < enumValuesArray.length; i++) {
						
						String enumValue = enumValuesArray[i];
						if (enumValue.equals(strPolicyValue)) {
							isValid = true;
							break;
						}
					}
				}
				
				if (!isValid) {
					throw new RuntimeException("policyValue: [" + policyValue + "] must be one of the following enum values: [" + policyValueConstraints + "] for line: " + policyData);
				}
			}
			
			// See what type of policy this is (vehicle or cloud)
			String policyType = policyData.get(0);
			if (policyType.equals("VehiclePolicy")) {
				VehiclePolicy vehiclePolicy = new VehiclePolicy
					.VehiclePolicyBuilder()
					.withParentPolicySet(parentPolicySet)
					.withPolicyName(policyData.get(1))
					.withPolicyDescription(policyData.get(2))
					.withPolicyValue(policyValue)
					.withAllowRegionalChangeable(parseBooleanValue("Regional", policyData.get(3), policyData))
					.withAllowUserChangeable(parseBooleanValue("User Changeable", policyData.get(7), policyData))
					.withAllowServiceChangeable(parseBooleanValue("Service Changeable", policyData.get(8), policyData))
					.withAllowCustomerFeedback(parseBooleanValue("CustomerFeedback", policyData.get(9), policyData))
					.withHmi(policyData.get(5))
					.withVehicleHmiFile(policyData.get(10))
					.withPhone(policyData.get(6))
					.withOtaFunction(otaFunction)
					.withPolicyValueType(policyValueType)
					.withPolicyValueConstraints(policyValueConstraints)
					.build();
					abstractPolicies.add(vehiclePolicy);
			} else if (policyType.equals("CloudPolicy")) {
				CloudPolicy cloudPolicy = new CloudPolicy
					.CloudPolicyBuilder()
					.withParentPolicySet(parentPolicySet)
					.withPolicyName(policyData.get(1))
					.withPolicyDescription(policyData.get(2))
					.withPolicyValue(policyValue)
					.withPolicyValueType(policyValueType)
					.withPolicyValueConstraints(policyValueConstraints)
					.build();
					abstractPolicies.add(cloudPolicy);
			} else {
				throw new RuntimeException("policyType: [" + policyType + "] must either be 'VehiclePolicy' or 'CloudPolicy', but was: [" + policyType + "] for line: " + policyData);
			}
		}
		
		return abstractPolicies;
	}
	
	Boolean parseBooleanValue(String attribute, String value, List<String> line) {
		
		Boolean booleanValue = null;
		
		if (value != null && !value.trim().isEmpty()) {
			
			value = value.toUpperCase().trim();
			
			if (value.equals("Y")) {
				booleanValue = Boolean.TRUE;
			} else if (value.equals("N")) {
				booleanValue = Boolean.FALSE;
			} else {
				throw new RuntimeException("Expected 'Y' or 'N' for " + attribute + " but encountered: [" + value + "] for line: " + line + ".");
			}
		} else {
			booleanValue = Boolean.FALSE;
		}
		
		return booleanValue;
	}

	public AbstractPolicyOverride buildProgramLevelPolicyOverride(AbstractPolicy parentPolicy) throws ValidationException {
		
		Program programEntity = new Program.ProgramBuilder()
			.withProgramCode("C344N")
			.build();  
		
		ModelYear modelYearEntity = new ModelYear
			.ModelYearBuilder()
			.withModelYearValue(Integer.valueOf("2017"))
			.build();
		
		ProgramModelYear programModelYearEntity = new ProgramModelYear
			.ProgramModelYearBuilder()
			.withParentProgram(programEntity)
			.withParentModelYear(modelYearEntity)
			.build();
		
		AbstractPolicyValue stringValue = new StringValue("program_level_override_value");
		
		AbstractPolicyOverride programLevelPolicyOverride = new PolicyProgramModelYearOverride
			.PolicyProgramModelYearOverrideBuilder()
			.withParentPolicy(parentPolicy)
			.withProgramModelYear(programModelYearEntity)
			.withPolicyOverrideValue(stringValue)
			.build();
		
		return programLevelPolicyOverride;
	}
	
	public AbstractPolicyOverride buildRegionLevelPolicyOverride(AbstractPolicy parentPolicy) throws ValidationException {
		
		Region region = new Region
			.RegionBuilder()
			.withCountryName("United States")
			.withRegionCode("US")
			.build();
		
		AbstractPolicyValue stringValue = new StringValue("region_level_override_value");
		
		AbstractPolicyOverride regionLevelPolicyOverride = new PolicyRegionOverride
			.PolicyRegionOverrideBuilder()
			.withParentPolicy(parentPolicy)
			.withRegion(region)
			.withPolicyOverrideValue(stringValue)
			.build();
		
		return regionLevelPolicyOverride;
	}
	
	public AbstractPolicyOverride buildVehicleLevelPolicyOverride(AbstractPolicy parentPolicy) throws ValidationException {

		Region region = new Region
			.RegionBuilder()
			.withCountryName("United States")
			.withRegionCode("US")
			.build();
		
		Vehicle vehicle = new Vehicle
			.VehicleBuilder()
			.withRegion(region)
			.withVin("1FA6P8TH4J5000000")
			.build();
		
		AbstractPolicyValue stringValue = new StringValue("vehicle_level_override_value");
		
		AbstractPolicyOverride vehicleLevelPolicyOverride = new PolicyVehicleOverride
			.PolicyVehicleOverrideBuilder()
			.withParentPolicy(parentPolicy)
			.withVehicle(vehicle)
			.withPolicyOverrideValue(stringValue)
			.build();
		
		return vehicleLevelPolicyOverride;
	}
	
	
   private List<List<String>> loadPoliciesFromClassPath() throws IOException, ValidationException, EntityAlreadyExistsException {

       try (InputStream inputStream = OrfinPolicySetTestHarness.class.getResourceAsStream(POLICY_TABLE_FILENAME)) {
    	   return loadPolicies(inputStream);
       }
   }
	
    /*
     * <pre>
     * -- Column Number:
     * --  1 - Name
     * --  2 - Description
     * --  3 - Regional
     * --  4 - Default Value
     * --  5 - HMI
     * --  6 - Phone
     * --  7 - User Changeable
     * --  8 - Service Changeable
     * --  9 - CustomerFeedback
     * -- 10 - VehicleHMIFile
     * -- 11 - OTA Function
     * -- 12 - Type 
     * --
     * -- =============================================================================================================================================================================================================================================================
		Level1Authorization,Safety and security updates. No additional consent provided by customer,Y,RegionCountryFile,Read/Write,None,Y,Y,N,,OTA MANAGER,
     * </pre>
     */
    private List<List<String>> loadPolicies(InputStream inputStream) throws IOException {

    	List<List<String>> policiesList = new ArrayList<>();
    	
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {

            Iterator<String> iterator = reader.lines().iterator();
            while (iterator.hasNext()) {

                String line = iterator.next();
                if (!line.trim().isEmpty() && !line.startsWith(COMMENT)) {

                    List<String> list = new ArrayList<>(Arrays.asList(line.split(COMMA)));
                    
                    if (list.size() != 14) {
                    	throw new RuntimeException("Expected a comma delimited list of size 13, but was: [" + list.size() + "], line: [" + line + "].");
                    }
                    
                    policiesList.add(list);
                }
            }
        }
        return policiesList;
    }	    
}
