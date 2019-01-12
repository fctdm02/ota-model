/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.repository.impl.AbstractMockRepository;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.orfin.policy.model.region.Region;
import com.djt.cvpp.ota.orfin.policy.repository.PolicySetRepository;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockPolicySetRepositoryImpl extends AbstractMockRepository implements PolicySetRepository {

	//private static final Logger LOGGER = LoggerFactory.getLogger(MockPolicySetRepositoryImpl.class);
	
	
	private Map<String, PolicySet> policySetMap = new TreeMap<>();
	private Map<String, Region> regionMap = new TreeMap<>();
	
	public void reset() {
		this.policySetMap.clear();
		this.regionMap.clear();
	}
	
	public MockPolicySetRepositoryImpl() {
	}
	
	public PolicySet createPolicySet(
		String policySetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		PolicySet policySetCheck = getPolicySetByNameNullIfNotFound(policySetName);
		if (policySetCheck != null) {
			
			throw new EntityAlreadyExistsException("Cannot create policy set with name: [" + policySetName + "] because it already exists");
		}
		
		PolicySet policySet = new PolicySet.PolicySetBuilder().withPolicySetName(policySetName).build();
		
		this.policySetMap.put(policySet.getNaturalIdentity(), policySet);
		
		return policySet;
	}
		
	public List<PolicySet> getAllPolicySets() {
		
		List<PolicySet> list = new ArrayList<>();
		list.addAll(this.policySetMap.values());
		return list;
	}
	
	public PolicySet getPolicySetByName(
        String policySetName)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		PolicySet policySet = getPolicySetByNameNullIfNotFound(policySetName);
		if (policySet == null) {
			throw new EntityDoesNotExistException("PolicySet with name: [" + policySetName + "] does not exist.");
		}
		return policySet;
	}
		
	public PolicySet renamePolicySet(
		String oldPolicySetName, 
		String newPolicySetName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		PolicySet policySetCheck = getPolicySetByNameNullIfNotFound(newPolicySetName);
		if (policySetCheck != null) {
			throw new EntityAlreadyExistsException("PolicySet with name: [" + newPolicySetName + "] already exists.");			
		}
		
		PolicySet policySet = getPolicySetByNameNullIfNotFound(oldPolicySetName);
		if (policySet == null) {
			throw new EntityDoesNotExistException("PolicySet with name: [" + oldPolicySetName + "] does not exist.");			
		}
		
		policySet.setPolicySetName(newPolicySetName);
		
		this.policySetMap.put(oldPolicySetName, null);
		this.policySetMap.put(newPolicySetName, policySet);
		
		return policySet;
	}

	public Region createRegion(
		String regionCode,
		String countryName)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		Region regionCheck = getRegionByRegionCodeNullIfNotFound(regionCode);
		if (regionCheck != null) {
			
			throw new EntityAlreadyExistsException("Cannot create region with code: [" + regionCode + "] because it already exists");
		}
		
		Region region = new Region
			.RegionBuilder()
			.withRegionCode(regionCode)
			.withCountryName(countryName)
			.build();
		
		this.regionMap.put(region.getNaturalIdentity(), region);
		
		return region;
	}
		
	public List<Region> getAllRegions() {
		
		List<Region> list = new ArrayList<>();
		list.addAll(this.regionMap.values());
		return list;
	}
	
	public Region getRegionByCode(
        String regionCode)
	throws 
		EntityDoesNotExistException {
		
		Region region = getRegionByRegionCodeNullIfNotFound(regionCode);
		if (region == null) {
			throw new EntityDoesNotExistException("Region with code: [" + regionCode + "] does not exist.");
		}
		return region;
	}
		
	public Region renameRegion(
		String oldRegionCode, 
		String newRegionCode)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException {
		
		Region regionCheck = getRegionByRegionCodeNullIfNotFound(newRegionCode);
		if (regionCheck != null) {
			throw new EntityAlreadyExistsException("Region with code: [" + newRegionCode + "] already exists.");			
		}
		
		Region region = getRegionByRegionCodeNullIfNotFound(oldRegionCode);
		if (region == null) {
			throw new EntityDoesNotExistException("Region with code: [" + oldRegionCode + "] does not exist.");			
		}
		
		region.setRegionCode(newRegionCode);
		
		this.regionMap.put(oldRegionCode, null);
		this.regionMap.put(newRegionCode, region);
		
		return region;
	}

	public AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity) {
		throw new UnsupportedOperationException("Use the type specfic versions of this method for policy set or region");
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		throw new UnsupportedOperationException("Use the type specfic versions of this method for policy set or region");
	}
	
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		throw new UnsupportedOperationException("Use the type specfic versions of this method for policy set or region");
	}
	
	public PolicySet getPolicySetByNameNullIfNotFound(String policySetName) throws ValidationException {
		
		if (policySetName == null || policySetName.trim().isEmpty()) {
			throw new ValidationException("policySetName", "must be specified.");
		}
		
		PolicySet policySet = this.policySetMap.get(policySetName);
		/*
		if (policySet == null) {
			
			// As a last resort, see if we can load this entity from "testdata" on the file system.
			try {
				String json = this.loadTestData(policySetName);
				policySet = new PolicySetJsonConverter().unmarshallFromJsonToEntity(json);
			} catch (Exception e) {
				LOGGER.error("Could not load from testdata area as file does not exist", e);
				policySet = null;
			}
		}
		*/
		return policySet;
	}
	
	public Region getRegionByRegionCodeNullIfNotFound(String regionCode) {
		
		if (regionCode == null || regionCode.trim().isEmpty()) {
			throw new FenixRuntimeException("regionCode must be specified.");
		}
		
		Region region = this.regionMap.get(regionCode);
		return region;
	}
		
	public PolicySet updatePolicySet(PolicySet entity) throws ValidationException {

		this.policySetMap.put(entity.getNaturalIdentity(), entity);
		return entity;
	}
	
	public PolicySet deletePolicySet(PolicySet entity) {

		return this.policySetMap.remove(entity.getNaturalIdentity());
	}
	
	public Region updateRegion(Region entity) throws ValidationException {

		this.regionMap.put(entity.getNaturalIdentity(), entity);
		return entity;
	}
	
	public Region deleteRegion(Region entity) {

		return this.regionMap.remove(entity.getNaturalIdentity());
	}
	
	public String loadTestData(String filename) {
		return super.loadTestData("/orfin/policy/" + filename);
	}
}
