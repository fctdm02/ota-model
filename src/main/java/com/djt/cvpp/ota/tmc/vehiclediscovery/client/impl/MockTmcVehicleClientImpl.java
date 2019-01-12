/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.client.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.repository.impl.AbstractMockRepository;
import com.djt.cvpp.ota.flare.rollout.model.CampaignBatch;
import com.djt.cvpp.ota.tmc.vehiclediscovery.client.TmcVehicleClient;
import com.djt.cvpp.ota.tmc.vehiclediscovery.exception.TmcResourceException;
import com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.VehicleDtoMapper;
import com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.VehicleJsonConverter;
import com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle;
import com.djt.cvpp.ota.vadr.model.DomainInstance;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockTmcVehicleClientImpl extends AbstractMockRepository implements TmcVehicleClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockTmcVehicleClientImpl.class);

	private VehicleJsonConverter vehicleJsonConverter = new VehicleJsonConverter();
	private VehicleDtoMapper vehicleDtoMapper = new VehicleDtoMapper();

	public void reset() {
		throw new UnsupportedOperationException("Not supported");
	}
	
	// 1FA6P8TH4J5000000
	public static final String STARTING_VIN_SEQUENCE_NUMBER = "000000"; // starting at 0 and ending at 999,999, there can be 1 million vehicles in the sequence (without changing anything else)
	
	public static final String MUSTANG_2018_VIN = "1FA6P8TH4J5000000";
	public static final String MUSTANG_2018_UUID = "264b362c-f185-4984-8fae-46627a4430e1";
	
	public static final String FUSION_2010_VIN = "3FAHP0HA0AR000000";
	public static final String FUSION_2010_UUID = "83ee69d0-2bd0-461c-a7eb-695aea9e8c7b";
	
	private static final List<String> ALL_VINS = new ArrayList<>();
	static {
		ALL_VINS.add(MUSTANG_2018_VIN);
		ALL_VINS.add(FUSION_2010_VIN);
	}
	
	
	private Map<String, TmcVehicle> tmcVehicleMap = new TreeMap<>();
	private VehicleDiscoveryHelper vehicleDiscoveryHelper = new VehicleDiscoveryHelper();
	
	public MockTmcVehicleClientImpl() {

		Iterator<String> iterator = ALL_VINS.iterator();
		while (iterator.hasNext()) {
			
			String vin = iterator.next();
			String filename = "/tmc/vehiclediscovery/VehicleObject__" + vin + ".json";
			
			String json = this.loadTestData(filename);
			TmcVehicle tmcVehicle = this.vehicleJsonConverter.unmarshallFromJsonToEntity(json);
			tmcVehicleMap.put(tmcVehicle.getNaturalIdentity(), tmcVehicle);
		}
	}
	
	public void provisionVirtualVehicle(TmcVehicle tmcVehicle) throws EntityAlreadyExistsException, TmcResourceException {
		
		if (this.tmcVehicleMap.get(tmcVehicle.getNaturalIdentity()) != null) {
			throw new EntityAlreadyExistsException("TmcVehicle with tenantId and vin: [" + tmcVehicle.getNaturalIdentity() + "] already exists.");
		}
		
		tmcVehicleMap.put(tmcVehicle.getNaturalIdentity(), tmcVehicle);
	}
	
	public void provisionVirtualVehiclesFromTemplateVehicle(TmcVehicle templateVehicle, int numberToProvision) throws EntityAlreadyExistsException, TmcResourceException {
		
		if (numberToProvision < 0 || numberToProvision > 999998) {
			throw new IllegalArgumentException("numberToProvision must be between 0 and 999998, inclusive.");
		}
		
		if (numberToProvision > 0) {

			String templateVehicleUuid = templateVehicle.getUuid().toString();
			String templateVehicleVin = templateVehicle.getVin();
			String templateVehicleJson = this.vehicleJsonConverter.marshallFromEntityToJson(templateVehicle);
			
			for (int i=1; i <= numberToProvision; i++) {
				
				String sequenceNumber = Integer.toString(i);
				
				// TODO: TDM: See if there is a string padding utility method somewhere.
				while (sequenceNumber.length() < 6) {
					sequenceNumber = "0" + sequenceNumber; 
				}
			
				String virtualVehicleUuid = UUID.randomUUID().toString();
				String virtualVehicleVin = templateVehicleVin.replaceAll(STARTING_VIN_SEQUENCE_NUMBER, sequenceNumber);
				String virtualVehicleJson = templateVehicleJson.replaceAll(templateVehicleVin, virtualVehicleVin);
				virtualVehicleJson = virtualVehicleJson.replaceAll(templateVehicleUuid, virtualVehicleUuid);
				
				TmcVehicle virtualVehicle = this.vehicleJsonConverter.unmarshallFromJsonToEntity(virtualVehicleJson);
				this.provisionVirtualVehicle(virtualVehicle);
			}
		}
	}
	
	public TmcVehicle getVehicleByVin(String vin) throws EntityDoesNotExistException, TmcResourceException {

		Iterator<TmcVehicle> iterator = this.tmcVehicleMap.values().iterator();
		while (iterator.hasNext()) {
			
			TmcVehicle tmcVehicle = iterator.next();
			if (tmcVehicle.getVin().equals(vin)) {
				return tmcVehicle;
			}
		}
		throw new EntityDoesNotExistException("TmcVehicle with vin: [" + vin + "] does not exist.");
	}
	
	public List<TmcVehicle> getAllVehicles() throws TmcResourceException {
		
		List<TmcVehicle> list = new ArrayList<>();
		list.addAll(this.tmcVehicleMap.values());
		return list;
	}

	public List<TmcVehicle> performVehicleDiscovery(
		Set<String> programCodeModelYearInclusions,
		DomainInstance domainInstance) 
	throws 
		TmcResourceException {

		List<TmcVehicle> list = new ArrayList<>();
		Iterator<TmcVehicle> iterator = this.tmcVehicleMap.values().iterator();
		while (iterator.hasNext()) {
			
			TmcVehicle tmcVehicle = iterator.next();
			
			boolean isVehicleQualified = this.vehicleDiscoveryHelper.isVehicleQualified(
				programCodeModelYearInclusions,
				domainInstance, 
				tmcVehicle);
			
			LOGGER.debug("VehicleDiscovery: isVehicleQualified: [{}] VIN: [{}].", isVehicleQualified, tmcVehicle.getVin());
			if (isVehicleQualified) {
				list.add(tmcVehicle);
			}
		}
		return list;
	}			
	
	public void createTmcDeployment(CampaignBatch campaignBatch) {
		
		// TODO: TDM: Implement
		throw new RuntimeException("Not implemented yet.");
	}
	
	
	
	public AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity) {
		throw new UnsupportedOperationException("This method is not supported by TmcVehicleClient");
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		throw new UnsupportedOperationException("This method is not supported by TmcVehicleClient");
	}
	
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		throw new UnsupportedOperationException("This method is not supported by TmcVehicleClient");
	}
	
	public VehicleJsonConverter getVehicleJsonConverter() {
		return this.vehicleJsonConverter;
	}

	public VehicleDtoMapper getVehicleDtoMapper() {
		return this.vehicleDtoMapper;
	}
}
