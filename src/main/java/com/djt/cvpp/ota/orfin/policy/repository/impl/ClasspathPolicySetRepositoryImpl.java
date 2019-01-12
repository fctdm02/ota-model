/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.repository.impl;

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
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.policy.model.AbstractPolicy;
import com.djt.cvpp.ota.orfin.policy.model.CloudPolicy;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.orfin.policy.model.VehiclePolicy;
import com.djt.cvpp.ota.orfin.policy.model.enums.OtaFunction;
import com.djt.cvpp.ota.orfin.policy.model.enums.PolicyValueType;
import com.djt.cvpp.ota.orfin.policy.model.override.AbstractPolicyOverride;
import com.djt.cvpp.ota.orfin.policy.model.override.PolicyRegionOverride;
import com.djt.cvpp.ota.orfin.policy.model.region.Region;
import com.djt.cvpp.ota.orfin.policy.model.value.AbstractPolicyValue;
import com.djt.cvpp.ota.orfin.policy.model.value.NumericValue;
import com.djt.cvpp.ota.orfin.policy.model.value.StringValue;
import com.djt.cvpp.ota.orfin.policy.repository.PolicySetRepository;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class ClasspathPolicySetRepositoryImpl extends MockPolicySetRepositoryImpl {
	
	public static final String GLOBAL_POLICY_SET_DEFNITIONS_FILENAME = "/com/djt/cvpp/ota/orfin/policy/repository/OTA_Global_Policies.csv";
	public static final String GLOBAL_POLICY_REGIONAL_OVERRIDES_AND_REGION_DEFINITIONS_FILENAME = "/com/djt/cvpp/ota/orfin/policy/repository/OTA_Global_Policy_Region_Overrides.csv";
	

    private static final String COMMENT = "--";
    private static final String COMMA = ",";
    private static final String UNDERSCORE = "_";
    private static final String Y = "Y";
    private static final String N = "N";

	private static PolicySetRepository INSTANCE = new ClasspathPolicySetRepositoryImpl();
	public static PolicySetRepository getInstance() {
		return INSTANCE;
	}
	
	private ClasspathPolicySetRepositoryImpl() {
		loadProductDevelopmentDefinedGlobalPoliciesRegionsAndRegionalOverrides();
	}
    
    private void loadProductDevelopmentDefinedGlobalPoliciesRegionsAndRegionalOverrides() {
    	try {
    		
    		PolicySet globalPolicySet = createPolicySet(PolicySet.GLOBAL_POLICY_SET_NAME);
    		Set<AbstractPolicy> globalPolicies = loadGlobalPolicies(globalPolicySet);
    		globalPolicySet.addPolicies(globalPolicies);
    		updatePolicySet(globalPolicySet);
    		
    	} catch (IOException | ValidationException | EntityAlreadyExistsException e) {
    		throw new FenixRuntimeException("Unable to load global policy set definitions from classpath resource: [" + GLOBAL_POLICY_SET_DEFNITIONS_FILENAME + "], error: [" + e.getMessage(), e);
    	}    	
    	
    	try {
    		
    		PolicySet globalPolicySet = getPolicySetByName(PolicySet.GLOBAL_POLICY_SET_NAME);
    		loadRegionsAndGlobalPolicyRegionalOverrides(globalPolicySet);
    		updatePolicySet(globalPolicySet);
    		
    	} catch (IOException | ValidationException | EntityAlreadyExistsException | EntityDoesNotExistException e) {
    		throw new FenixRuntimeException("Unable to load global policy set regions and regional override definitions from classpath resource: [" + GLOBAL_POLICY_REGIONAL_OVERRIDES_AND_REGION_DEFINITIONS_FILENAME + "], error: [" + e.getMessage(), e);
    	}    	
    }
    
	public void reset() {
		super.reset();
		loadProductDevelopmentDefinedGlobalPoliciesRegionsAndRegionalOverrides();
	}
    
	private Set<AbstractPolicy> loadGlobalPolicies(PolicySet parentPolicySet) throws EntityAlreadyExistsException, ValidationException, IOException {

		/* 
		 * TODO: TDM: These should be in the database
		 *  		
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
		List<List<String>> policyDataList = loadGlobalPolicyDefinitionsFromClassPath();		
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
				
				policyValue = new StringValue(policyData.get(4).trim());
				
			} else if (policyValueType == PolicyValueType.NUMERIC) {
				
				policyValue = new NumericValue(Long.parseLong(policyData.get(4).trim()));
				
			} else if (policyValueType == PolicyValueType.ENUM) {
				
				String strPolicyValue = policyData.get(4).trim();
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
	
	private Boolean parseBooleanValue(String attribute, String value, List<String> line) {
		
		Boolean booleanValue = null;
		
		if (value != null && !value.trim().isEmpty()) {
			
			value = value.toUpperCase().trim();
			
			if (value.equals(Y)) {
				booleanValue = Boolean.TRUE;
			} else if (value.equals(N)) {
				booleanValue = Boolean.FALSE;
			} else {
				throw new RuntimeException("Expected 'Y' or 'N' for " + attribute + " but encountered: [" + value + "] for line: " + line + ".");
			}
		} else {
			booleanValue = Boolean.FALSE;
		}
		
		return booleanValue;
   }
	
	private void loadRegionsAndGlobalPolicyRegionalOverrides(PolicySet globalPolicySet) throws EntityAlreadyExistsException, ValidationException, IOException, EntityDoesNotExistException {
		
		AbstractPolicy level1AuthorizationPolicy = globalPolicySet.getPolicyByName("Level1Authorization");
	    AbstractPolicy level2AuthorizationPolicy = globalPolicySet.getPolicyByName("Level2Authorization");
	    AbstractPolicy level3AuthorizationPolicy = globalPolicySet.getPolicyByName("Level3Authorization");
	    AbstractPolicy level4AuthorizationPolicy = globalPolicySet.getPolicyByName("Level4Authorization");
	    AbstractPolicy oneTimeAuthorizationPolicy = globalPolicySet.getPolicyByName("OneTimeAuthorization");
	    AbstractPolicy defaultScheduledSettingPolicy = globalPolicySet.getPolicyByName("DefaultScheduledSetting");
		
	    /*
	     * <pre>
	     * -- Column Number:
	     * --  0 - Code
	     * --  1 - Country
	     * --  2 - Level1Authorization
	     * --  3 - Level2Authorization
	     * --  4 - Level3Authorization
	     * --  5 - Level4Authorization
	     * --  6 - OneTimeAuthorization
	     * --  7 - DefaultScheduledSetting
	     * --  8 - VehicleHMIDescription
	     * -- ===================================================
	  		  ad,Andorra,UPDATE,UPDATE,UPDATE,UPDATE,ALLOW,ALLOW,
	     * </pre>
	     */
		List<List<String>> regionsAndRegionalOverridesDataList = loadRegionsAndGlobalPolicyRegionalOverrideDefinitionsFromClassPath();		
		Iterator<List<String>> iterator = regionsAndRegionalOverridesDataList.iterator();
		while (iterator.hasNext()) {
			
			List<String> data = iterator.next();
			
			String regionCode = data.get(0);
			String countryName = data.get(1);
			
			createRegion(regionCode, countryName);
			
			String level1AuthorizationPolicyRegionalOverrideValue = data.get(2);
			String level2AuthorizationPolicyRegionalOverrideValue = data.get(3);
			String level3AuthorizationPolicyRegionalOverrideValue = data.get(4);
		    String level4AuthorizationPolicyRegionalOverrideValue = data.get(5);
		    String oneTimeAuthorizationPolicyRegionalOverrideValue = data.get(6);
		    String defaultScheduledSettingPolicyRegionalOverrideValue = data.get(7);
		    
		    createRegionLevelPolicyOverride(
		    	globalPolicySet.getPolicySetName(), 
		    	level1AuthorizationPolicy.getPolicyName(), 
		    	regionCode, 
		    	new StringValue(level1AuthorizationPolicyRegionalOverrideValue));
		    
		    createRegionLevelPolicyOverride(
		    	globalPolicySet.getPolicySetName(), 
		    	level2AuthorizationPolicy.getPolicyName(), 
		    	regionCode, 
		    	new StringValue(level2AuthorizationPolicyRegionalOverrideValue));

		    createRegionLevelPolicyOverride(
		    	globalPolicySet.getPolicySetName(), 
		    	level3AuthorizationPolicy.getPolicyName(), 
		    	regionCode, 
		    	new StringValue(level3AuthorizationPolicyRegionalOverrideValue));

		    createRegionLevelPolicyOverride(
		    	globalPolicySet.getPolicySetName(), 
		    	level4AuthorizationPolicy.getPolicyName(), 
		    	regionCode, 
		    	new StringValue(level4AuthorizationPolicyRegionalOverrideValue));

		    createRegionLevelPolicyOverride(
		    	globalPolicySet.getPolicySetName(), 
		    	oneTimeAuthorizationPolicy.getPolicyName(), 
		    	regionCode, 
		    	new StringValue(oneTimeAuthorizationPolicyRegionalOverrideValue));

		    createRegionLevelPolicyOverride(
		    	globalPolicySet.getPolicySetName(), 
		    	defaultScheduledSettingPolicy.getPolicyName(), 
		    	regionCode, 
		    	new StringValue(defaultScheduledSettingPolicyRegionalOverrideValue));
		
		}
	}
	
   private List<List<String>> loadGlobalPolicyDefinitionsFromClassPath() throws IOException, ValidationException, EntityAlreadyExistsException {

       try (InputStream inputStream = MockPolicySetRepositoryImpl.class.getResourceAsStream(GLOBAL_POLICY_SET_DEFNITIONS_FILENAME)) {
    	   return loadGlobalPolicyDefinitions(inputStream);
       }
   }

	private AbstractPolicyOverride createRegionLevelPolicyOverride(
		String parentPolicySetName,
		String parentPolicyName,
		String regionCode,
		AbstractPolicyValue policyValueOverride) 
	throws 
		ValidationException, 
		EntityAlreadyExistsException, EntityDoesNotExistException {
		
		Region region = getRegionByCode(regionCode);
		PolicySet parentPolicySet = getPolicySetByName(parentPolicySetName);
		AbstractPolicy parentPolicy = parentPolicySet.getPolicyByName(parentPolicyName);
		
		AbstractPolicyOverride regionLevelPolicyOverride = new PolicyRegionOverride
			.PolicyRegionOverrideBuilder()
			.withParentPolicy(parentPolicy)
			.withRegion(region)
			.withPolicyOverrideValue(policyValueOverride)
			.build();
		
		parentPolicy.addPolicyOverride(regionLevelPolicyOverride);
		
		return regionLevelPolicyOverride;
	}
   
   private List<List<String>> loadRegionsAndGlobalPolicyRegionalOverrideDefinitionsFromClassPath() throws IOException, ValidationException, EntityAlreadyExistsException {

       try (InputStream inputStream = MockPolicySetRepositoryImpl.class.getResourceAsStream(GLOBAL_POLICY_REGIONAL_OVERRIDES_AND_REGION_DEFINITIONS_FILENAME)) {
    	   return loadRegionsAndGlobalPolicyRegionalOverrideDefinitions(inputStream);
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
    private List<List<String>> loadGlobalPolicyDefinitions(InputStream inputStream) throws IOException {

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
    
    /*
     * <pre>
     * -- Column Number:
     * --  0 - Code
     * --  1 - Country
     * --  2 - Level1Authorization
     * --  3 - Level2Authorization
     * --  4 - Level3Authorization
     * --  5 - Level4Authorization
     * --  6 - OneTimeAuthorization
     * --  7 - DefaultScheduledSetting
     * --  8 - VehicleHMIDescription
     * -- ===================================================
  		  ad,Andorra,UPDATE,UPDATE,UPDATE,UPDATE,ALLOW,ALLOW,
     * </pre>
     */
    private List<List<String>> loadRegionsAndGlobalPolicyRegionalOverrideDefinitions(InputStream inputStream) throws IOException {

    	List<List<String>> policiesList = new ArrayList<>();
    	
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {

            Iterator<String> iterator = reader.lines().iterator();
            while (iterator.hasNext()) {

                String line = iterator.next();
                if (!line.trim().isEmpty() && !line.startsWith(COMMENT)) {

                    List<String> list = new ArrayList<>(Arrays.asList(line.split(COMMA)));
                    
                    if (list.size() != 8) {
                    	throw new RuntimeException("Expected a comma delimited list of size 8 but was: [" + list.size() + "], line: [" + line + "].");
                    }
                    
                    policiesList.add(list);
                }
            }
        }
        return policiesList;
    }	    
}
