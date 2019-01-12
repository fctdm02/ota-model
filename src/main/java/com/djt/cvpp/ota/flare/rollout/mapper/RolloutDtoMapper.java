/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.rollout.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.mapper.DtoMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class RolloutDtoMapper implements DtoMapper<com.djt.cvpp.ota.flare.rollout.model.AbstractRollout, com.djt.cvpp.ota.flare.rollout.mapper.dto.AbstractRollout> {
	
	public com.djt.cvpp.ota.flare.rollout.mapper.dto.AbstractRollout mapEntityToDto(com.djt.cvpp.ota.flare.rollout.model.AbstractRollout rolloutEntity) {
		
		com.djt.cvpp.ota.flare.rollout.mapper.dto.AbstractRollout rolloutDto = null;
		
		if (rolloutEntity instanceof com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout) {
			
			rolloutDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.SoftwareUpdateRollout();
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.SoftwareUpdateRollout)rolloutDto).setDeliveryRuleSetName(((com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout)rolloutEntity).getDeliveryRuleSetName());

		} else if (rolloutEntity instanceof com.djt.cvpp.ota.flare.rollout.model.ApplicationUpdateRollout) {

			rolloutDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.ApplicationUpdateRollout();
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.SoftwareUpdateRollout)rolloutDto).setDeliveryRuleSetName(((com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout)rolloutEntity).getDeliveryRuleSetName());
			
		} else if (rolloutEntity instanceof com.djt.cvpp.ota.flare.rollout.model.AddApplicationRollout) {
			
			rolloutDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.AddApplicationRollout();
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.SoftwareUpdateRollout)rolloutDto).setDeliveryRuleSetName(((com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout)rolloutEntity).getDeliveryRuleSetName());
			
		} else if (rolloutEntity instanceof com.djt.cvpp.ota.flare.rollout.model.RemoveApplicationRollout) {

			rolloutDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.RemoveApplicationRollout();
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.SoftwareUpdateRollout)rolloutDto).setDeliveryRuleSetName(((com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout)rolloutEntity).getDeliveryRuleSetName());
			
		} else if (rolloutEntity instanceof com.djt.cvpp.ota.flare.rollout.model.SuspendSoftwareRollout) {

			rolloutDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.SuspendSoftwareRollout();
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.SoftwareUpdateRollout)rolloutDto).setDeliveryRuleSetName(((com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout)rolloutEntity).getDeliveryRuleSetName());
						
		} else if (rolloutEntity instanceof com.djt.cvpp.ota.flare.rollout.model.OdlUpdateRollout) {

			rolloutDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.OdlUpdateRollout();
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.AbstractProgramBasedRollout)rolloutDto).setProgramCode((((com.djt.cvpp.ota.flare.rollout.model.AbstractProgramBasedRollout)rolloutEntity).getProgramCode()));
			
		} else if (rolloutEntity instanceof com.djt.cvpp.ota.flare.rollout.model.PolicyTableUpdateRollout) {

			rolloutDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.PolicyTableUpdateRollout();
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.AbstractProgramBasedRollout)rolloutDto).setProgramCode((((com.djt.cvpp.ota.flare.rollout.model.AbstractProgramBasedRollout)rolloutEntity).getProgramCode()));
			
		} else if (rolloutEntity instanceof com.djt.cvpp.ota.flare.rollout.model.SoftwareWithDirectConfigurationRollout) {

			rolloutDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.SoftwareWithDirectConfigurationRollout();
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.SoftwareUpdateRollout)rolloutDto).setDeliveryRuleSetName(((com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout)rolloutEntity).getDeliveryRuleSetName());
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.SoftwareWithDirectConfigurationRollout)rolloutDto).setNodeAcronym(((com.djt.cvpp.ota.flare.rollout.model.DirectConfigurationBasedRollout)rolloutEntity).getNodeAcronym());
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.SoftwareWithDirectConfigurationRollout)rolloutDto).setNodeAddress(((com.djt.cvpp.ota.flare.rollout.model.DirectConfigurationBasedRollout)rolloutEntity).getNodeAddress());
			
		} else if (rolloutEntity instanceof com.djt.cvpp.ota.flare.rollout.model.DirectConfigurationOnlyRollout) {

			rolloutDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.DirectConfigurationOnlyRollout();
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.DirectConfigurationOnlyRollout)rolloutDto).setNodeAcronym(((com.djt.cvpp.ota.flare.rollout.model.DirectConfigurationBasedRollout)rolloutEntity).getNodeAcronym());
			((com.djt.cvpp.ota.flare.rollout.mapper.dto.DirectConfigurationOnlyRollout)rolloutDto).setNodeAddress(((com.djt.cvpp.ota.flare.rollout.model.DirectConfigurationBasedRollout)rolloutEntity).getNodeAddress());
			
		}
		
		if (rolloutDto != null) {
			
			rolloutDto.setRolloutName(rolloutEntity.getRolloutName());
			rolloutDto.setInitiatedBy(rolloutEntity.getInitiatedBy());
			rolloutDto.setCreationDate(rolloutEntity.getCreationDate().getTime());
			rolloutDto.setStartDate(rolloutEntity.getStartDate().getTime());
			rolloutDto.setExpirationDays(rolloutEntity.getExpirationDays());
			rolloutDto.setEndDate(rolloutEntity.getEndDateAsMillis());
			rolloutDto.setRolloutState(rolloutEntity.getRolloutState().toString());
			rolloutDto.setRolloutPhase(rolloutEntity.getRolloutPhase().toString());
			rolloutDto.setMaxNumberActiveCampaignBatches(rolloutEntity.getMaxNumberActiveCampaignBatches());
			rolloutDto.setMaxNumberVehiclesPerCampaignBatch(rolloutEntity.getMaxNumberVehiclesPerCampaignBatch());
			rolloutDto.setPriorityLevel(rolloutEntity.getPriorityLevel());
			rolloutDto.setCampaigns(mapCampaigns(rolloutEntity.getCampaigns()));
			
		} else {
			throw new FenixRuntimeException("Unknown type of rollout enountered: [" + rolloutEntity.getClassAndIdentity() + "]");
		}
				
		
		return rolloutDto;
	}
	
	private List<com.djt.cvpp.ota.flare.rollout.mapper.dto.Campaign> mapCampaigns(List<com.djt.cvpp.ota.flare.rollout.model.Campaign> campaignEntities) {
		
		List<com.djt.cvpp.ota.flare.rollout.mapper.dto.Campaign> list = new ArrayList<>();
		
		Iterator<com.djt.cvpp.ota.flare.rollout.model.Campaign> iterator = campaignEntities.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.flare.rollout.model.Campaign campaignEntity = iterator.next();
			
			com.djt.cvpp.ota.flare.rollout.mapper.dto.Campaign campaignDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.Campaign();
			campaignDto.setBeginDate(campaignEntity.getBeginDate().getTime());
			campaignDto.setEndDate(campaignEntity.getEndDateAsMillis());
			campaignDto.setDomainName(campaignEntity.getDomainName());
			campaignDto.setDomainInstanceName(campaignEntity.getDomainInstanceName());
			campaignDto.setDomainInstanceVersion(campaignEntity.getDomainInstanceVersion());
			campaignDto.setApplicationName(campaignEntity.getApplicationName());
			campaignDto.setApplicationVersion(campaignEntity.getApplicationVersion());
			campaignDto.setProgramCode(campaignEntity.getProgramCode());
			campaignDto.setModelYear(campaignEntity.getModelYear());
			campaignDto.setOdlPayload(campaignEntity.getOdlPayload());
			campaignDto.setCampaignState(campaignEntity.getCampaignState().toString());
		    campaignDto.setCampaignBatches(mapCampaignBatches(campaignEntity.getCampaignBatches()));
			
			list.add(campaignDto);
		}
		
		return list;
	}
	
	private List<com.djt.cvpp.ota.flare.rollout.mapper.dto.CampaignBatch> mapCampaignBatches(List<com.djt.cvpp.ota.flare.rollout.model.CampaignBatch> campaignBatchEntities) {
		
		List<com.djt.cvpp.ota.flare.rollout.mapper.dto.CampaignBatch> list = new ArrayList<>();
		
		Iterator<com.djt.cvpp.ota.flare.rollout.model.CampaignBatch> iterator = campaignBatchEntities.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.flare.rollout.model.CampaignBatch campaignBatchEntity = iterator.next();
			
			com.djt.cvpp.ota.flare.rollout.mapper.dto.CampaignBatch campaignBatchDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.CampaignBatch();
			campaignBatchDto.setBeginDate(campaignBatchEntity.getBeginDate().getTime());
			campaignBatchDto.setEndDate(campaignBatchEntity.getEndDateAsMillis());
			campaignBatchDto.setCampaignBatchName(campaignBatchEntity.getCampaignBatchName());
			campaignBatchDto.setTmcDeploymentId(campaignBatchEntity.getTmcDeploymentId());
			campaignBatchDto.setCampaignBatchState(campaignBatchEntity.getCampaignBatchState().toString());
			campaignBatchDto.setVehicleCampaignBatches(mapVehicleCampaignBatches(campaignBatchEntity.getVehicleCampaignBatches()));
			
			list.add(campaignBatchDto);
		}
		
		return list;
	}

	private List<com.djt.cvpp.ota.flare.rollout.mapper.dto.VehicleCampaignBatch> mapVehicleCampaignBatches(List<com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch> vehicleCampaignBatchEntities) {
		
		List<com.djt.cvpp.ota.flare.rollout.mapper.dto.VehicleCampaignBatch> list = new ArrayList<>();
		
		Iterator<com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch> iterator = vehicleCampaignBatchEntities.iterator();
		while (iterator.hasNext()) {
			list.add(this.mapVehicleCampaignBatch(iterator.next()));
		}
		
		return list;
	}
	
	public com.djt.cvpp.ota.flare.rollout.mapper.dto.VehicleCampaignBatch mapVehicleCampaignBatch(com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch vehicleCampaignBatchEntity) {
		
		com.djt.cvpp.ota.flare.rollout.mapper.dto.VehicleCampaignBatch vehicleCampaignBatchDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.VehicleCampaignBatch();
		vehicleCampaignBatchDto.setBeginDate(vehicleCampaignBatchEntity.getBeginDate().getTime());
		vehicleCampaignBatchDto.setEndDate(vehicleCampaignBatchEntity.getEndDateAsMillis());
		vehicleCampaignBatchDto.setVin(vehicleCampaignBatchEntity.getVin());
		vehicleCampaignBatchDto.setTmcVehicleId(vehicleCampaignBatchEntity.getTmcVehicleId());
		vehicleCampaignBatchDto.setTmcVehicleObjectPayload(vehicleCampaignBatchEntity.getTmcVehicleObjectPayload());	
		vehicleCampaignBatchDto.setNumRetries(vehicleCampaignBatchEntity.getNumRetries());
		vehicleCampaignBatchDto.setLastRetryDate(vehicleCampaignBatchEntity.getLastRetryDateAsMillis());
		vehicleCampaignBatchDto.setIsValidationVehicle(vehicleCampaignBatchEntity.getIsValidationVehicle());
		vehicleCampaignBatchDto.setVehicleCampaignBatchState(vehicleCampaignBatchEntity.getVehicleCampaignBatchState().toString());
		vehicleCampaignBatchDto.setVehicleStatusMessages(mapVehicleStatusMessageEntityToDto(vehicleCampaignBatchEntity.getVehicleStatusMessages()));
		
		return vehicleCampaignBatchDto;
	}
	
	public List<com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.VehicleStatusMessage> mapVehicleStatusMessageEntityToDto(List<com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessage> vehicleStatusMessageEntities) {
		
		List<com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.VehicleStatusMessage> list = new ArrayList<>();
		
		Iterator<com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessage> iterator = vehicleStatusMessageEntities.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessage vehicleStatusMessageEntity = iterator.next();
			
			com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.VehicleStatusMessage vehicleStatusMessageDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.VehicleStatusMessage();
			vehicleStatusMessageDto.setTimestamp(vehicleStatusMessageEntity.getTimestamp().getTime());
			vehicleStatusMessageDto.setFullLookupCodeValue(vehicleStatusMessageEntity.getFullLookupCodeValue());
			vehicleStatusMessageDto.setVehicleStatusMessageAdditionalParameters(mapVehicleStatusMessageAdditionalParameters(vehicleStatusMessageEntity.getVehicleStatusMessageAdditionalParameters()));
			
			list.add(vehicleStatusMessageDto);
		}
		
		return list;
	}
	
	private List<com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.VehicleStatusMessageAdditionalParameter> mapVehicleStatusMessageAdditionalParameters(List<com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessageAdditionalParameter> vehicleStatusMessageAdditionalParameterEntities) {
		
		List<com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.VehicleStatusMessageAdditionalParameter> list = new ArrayList<>();
		
		Iterator<com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessageAdditionalParameter> iterator = vehicleStatusMessageAdditionalParameterEntities.iterator();
		while (iterator.hasNext()) {
			
			com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessageAdditionalParameter vehicleStatusMessageAdditionalParameterEntity = iterator.next();
			
			if (vehicleStatusMessageAdditionalParameterEntity instanceof com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.BooleanAdditionalParameter) {
				
				com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.BooleanAdditionalParameter vehicleStatusMessageAdditionalParameterDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.BooleanAdditionalParameter();
				vehicleStatusMessageAdditionalParameterDto.setSequenceNumber(vehicleStatusMessageAdditionalParameterEntity.getSequenceNumber());
				vehicleStatusMessageAdditionalParameterDto.setAdditionalParameterLabelName(vehicleStatusMessageAdditionalParameterEntity.getAdditionalParameterLabelName());
				vehicleStatusMessageAdditionalParameterDto.setBooleanValue((Boolean)vehicleStatusMessageAdditionalParameterEntity.getAdditionalParameterValue());
				list.add(vehicleStatusMessageAdditionalParameterDto);
				
			} else if (vehicleStatusMessageAdditionalParameterEntity instanceof com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.NumericAdditionalParameter) {
				
				com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.NumericAdditionalParameter vehicleStatusMessageAdditionalParameterDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.NumericAdditionalParameter();
				vehicleStatusMessageAdditionalParameterDto.setSequenceNumber(vehicleStatusMessageAdditionalParameterEntity.getSequenceNumber());
				vehicleStatusMessageAdditionalParameterDto.setAdditionalParameterLabelName(vehicleStatusMessageAdditionalParameterEntity.getAdditionalParameterLabelName());
				vehicleStatusMessageAdditionalParameterDto.setNumericValue((Number)vehicleStatusMessageAdditionalParameterEntity.getAdditionalParameterValue());
				list.add(vehicleStatusMessageAdditionalParameterDto);
				
			} else {

				com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.StringAdditionalParameter vehicleStatusMessageAdditionalParameterDto = new com.djt.cvpp.ota.flare.rollout.mapper.dto.vehiclestatus.StringAdditionalParameter();
				vehicleStatusMessageAdditionalParameterDto.setSequenceNumber(vehicleStatusMessageAdditionalParameterEntity.getSequenceNumber());
				vehicleStatusMessageAdditionalParameterDto.setAdditionalParameterLabelName(vehicleStatusMessageAdditionalParameterEntity.getAdditionalParameterLabelName());
				vehicleStatusMessageAdditionalParameterDto.setStringValue(vehicleStatusMessageAdditionalParameterEntity.getAdditionalParameterValue().toString());
				list.add(vehicleStatusMessageAdditionalParameterDto);
				
			}
		}
		
		return list;
	}
	
	public com.djt.cvpp.ota.flare.rollout.model.AbstractRollout mapDtoToEntity(com.djt.cvpp.ota.flare.rollout.mapper.dto.AbstractRollout dto) {
		
		throw new UnsupportedOperationException("Mapping AbstractRollout DTOs to entities are not supported.");
	}
}
