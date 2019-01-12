/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.client.impl;

import java.util.Iterator;
import java.util.Set;

import com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle;
import com.djt.cvpp.ota.vadr.model.DomainInstance;
import com.djt.cvpp.ota.vadr.model.EcuNode;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VehicleDiscoveryHelper {

	/**
	 * @param programCodeModelYearInclusions Inclusions take the form of: XXXX_YYYY where XXXX is the programCode and modelYear is the 4 digit model year
	 * @param domainInstance
	 * @param tmcVehicle
	 * @return
	 */
	public boolean isVehicleQualified(
		Set<String> programCodeModelYearInclusions,
		DomainInstance domainInstance, 
		TmcVehicle tmcVehicle) {
		
		// programCode/modelYear inclusions/exclusions are optional, but if specified, reject right away if the TMC vehicle object does not match the inclusions or it matches the exclusions
		String programCodeModelYear = tmcVehicle.getProgramCodeModelYear();
		if (programCodeModelYearInclusions != null && !programCodeModelYearInclusions.isEmpty() && !programCodeModelYearInclusions.contains(programCodeModelYear)) {
			return false;
		}
		
		Iterator<EcuNode> ecuNodeIterator = domainInstance.getEcuNodes().iterator();
		while (ecuNodeIterator.hasNext()) {
			
			EcuNode ecuNode = ecuNodeIterator.next();
			
			String nodeName = ecuNode.getEcuAcronymName();
			String nodeAddress = ecuNode.getEcuLogicalAddress();
			
			boolean hardwareMatchesForEcuNode = false;
			
			Iterator<String> ecuHardwarePartNumberIterator = domainInstance.getEcuHardwarePartNumbersForEcuNode(ecuNode).iterator();
			while (ecuHardwarePartNumberIterator.hasNext()) {
				
				String ecuHardwarePartNumber = ecuHardwarePartNumberIterator.next();
				if (tmcVehicle.hasDidValue(nodeName, nodeAddress, DomainInstance.HW_DID_ADDRESS, ecuHardwarePartNumber)) {
					
					hardwareMatchesForEcuNode = true;
					break;
				}
			}
			
			if (!hardwareMatchesForEcuNode) {
				return false;
			}
		}
		
		return true;
	}
}
