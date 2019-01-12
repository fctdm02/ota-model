/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.flare.rollout.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ExceptionIdentity;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.flare.rollout.mapper.RolloutDtoMapper;
import com.djt.cvpp.ota.flare.rollout.mapper.RolloutJsonConverter;
import com.djt.cvpp.ota.flare.rollout.mapper.dto.RolloutProgress;
import com.djt.cvpp.ota.flare.rollout.model.AbstractRollout;
import com.djt.cvpp.ota.flare.rollout.model.AddApplicationRollout;
import com.djt.cvpp.ota.flare.rollout.model.ApplicationUpdateRollout;
import com.djt.cvpp.ota.flare.rollout.model.Campaign;
import com.djt.cvpp.ota.flare.rollout.model.CampaignBatch;
import com.djt.cvpp.ota.flare.rollout.model.DirectConfigurationOnlyRollout;
import com.djt.cvpp.ota.flare.rollout.model.OdlUpdateRollout;
import com.djt.cvpp.ota.flare.rollout.model.PolicyTableUpdateRollout;
import com.djt.cvpp.ota.flare.rollout.model.RemoveApplicationRollout;
import com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout;
import com.djt.cvpp.ota.flare.rollout.model.SoftwareWithDirectConfigurationRollout;
import com.djt.cvpp.ota.flare.rollout.model.SuspendSoftwareRollout;
import com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch;
import com.djt.cvpp.ota.flare.rollout.model.enums.CampaignBatchState;
import com.djt.cvpp.ota.flare.rollout.model.enums.CampaignState;
import com.djt.cvpp.ota.flare.rollout.model.enums.RolloutState;
import com.djt.cvpp.ota.flare.rollout.model.vehiclestatus.VehicleStatusMessage;
import com.djt.cvpp.ota.flare.rollout.repository.RolloutRepository;
import com.djt.cvpp.ota.flare.rollout.service.RolloutService;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.model.VehicleStatusMessageTemplate;
import com.djt.cvpp.ota.flare.vehiclestatustemplate.service.VehicleStatusMessageTemplateService;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEvent;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEvent;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEvent;
import com.djt.cvpp.ota.orfin.vadrevent.mapper.dto.VadrRelease;
import com.djt.cvpp.ota.tmc.bytestream.client.TmcByteStreamClient;
import com.djt.cvpp.ota.tmc.bytestream.exception.TmcByteStreamException;
import com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary;
import com.djt.cvpp.ota.tmc.deployment.client.TmcDeploymentClient;
import com.djt.cvpp.ota.tmc.deployment.exception.TmcDeploymentException;
import com.djt.cvpp.ota.tmc.vehiclediscovery.client.TmcVehicleClient;
import com.djt.cvpp.ota.tmc.vehiclediscovery.exception.TmcResourceException;
import com.djt.cvpp.ota.tmc.vehiclediscovery.mapper.VehicleJsonConverter;
import com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle;
import com.djt.cvpp.ota.tmc.vehiclestatus.event.TmcVehicleStatusMessageEvent;
import com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage;
import com.djt.cvpp.ota.vadr.client.VadrClient;
import com.djt.cvpp.ota.vadr.exception.VadrException;
import com.djt.cvpp.ota.vadr.model.Domain;
import com.djt.cvpp.ota.vadr.model.DomainInstance;
import com.djt.cvpp.ota.vadr.model.enums.ProductionState;
import com.djt.cvpp.ota.vadr.model.enums.ReleaseState;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class RolloutServiceImpl implements RolloutService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RolloutServiceImpl.class);
	
	
	// Mapper related
	private RolloutJsonConverter rolloutJsonConverter = new RolloutJsonConverter();
	private RolloutDtoMapper dtoMapper = new RolloutDtoMapper();
	private VehicleJsonConverter tmcVehicleJsonConverter = new VehicleJsonConverter();
	
	// Repository layer
	private RolloutRepository rolloutRepository;
	
	// Other FENIX-FLARE services	
	private VehicleStatusMessageTemplateService vehicleStatusMessageTemplateService;

	// External Services
	private TmcVehicleClient tmcVehicleClient;
	private TmcByteStreamClient tmcByteStreamClient;
	private TmcDeploymentClient tmcDeploymentClient;
	private VadrClient vadrClient;
		
	public RolloutServiceImpl() {
	}

	public RolloutServiceImpl(
		RolloutRepository rolloutRepository,
		VehicleStatusMessageTemplateService vehicleStatusMessageTemplateService,
		VadrClient vadrClient,
		TmcVehicleClient tmcVehicleClient,
		TmcByteStreamClient tmcByteStreamClient,
		TmcDeploymentClient tmcDeploymentClient) {
		
		this.rolloutRepository = rolloutRepository;
		this.vehicleStatusMessageTemplateService = vehicleStatusMessageTemplateService;
		this.vadrClient = vadrClient;
		this.tmcVehicleClient = tmcVehicleClient;
		this.tmcByteStreamClient = tmcByteStreamClient;
		this.tmcDeploymentClient = tmcDeploymentClient;
	}

	public RolloutJsonConverter getJsonConverter() {
		return this.rolloutJsonConverter;
	}
	
	public RolloutDtoMapper getDtoMapper() {
		return this.dtoMapper;
	}
	
	public void setRolloutRepository(RolloutRepository rolloutRepository) {
		this.rolloutRepository = rolloutRepository;
	}
	
	public void setVehicleStatusMessageTemplateService(VehicleStatusMessageTemplateService vehicleStatusMessageTemplateService) {
		this.vehicleStatusMessageTemplateService = vehicleStatusMessageTemplateService;
	}	

	public SoftwareUpdateRollout createSoftwareUpdateRollout(
		String rolloutName,
        String deliveryRuleSetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
	
		try {
			
			LOGGER.debug("RolloutService::createSoftwareUpdateRollout(): rolloutName: [{}], deliveryRuleSetName: {}", rolloutName, deliveryRuleSetName);
			
			SoftwareUpdateRollout rollout = new SoftwareUpdateRollout(rolloutName, deliveryRuleSetName);
			this.rolloutRepository.saveRollout(rollout);
			return rollout;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create rollout: [" + rolloutName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1003", "Could not create rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public ApplicationUpdateRollout createApplicationUpdateRollout(
		String rolloutName,
		String deliveryRuleSetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("RolloutService::createApplicationUpdateRollout(): rolloutName: [{}], deliveryRuleSetName: {}", rolloutName, deliveryRuleSetName);
			
			ApplicationUpdateRollout rollout = new ApplicationUpdateRollout(rolloutName, deliveryRuleSetName);
			this.rolloutRepository.saveRollout(rollout);
			return rollout;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create rollout: [" + rolloutName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1003", "Could not create rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public AddApplicationRollout createAddApplicationRollout(
		String rolloutName,
		String deliveryRuleSetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("RolloutService::createAddApplicationRollout(): rolloutName: [{}], deliveryRuleSetName: {}", rolloutName, deliveryRuleSetName);
			
			AddApplicationRollout rollout = new AddApplicationRollout(rolloutName, deliveryRuleSetName);
			this.rolloutRepository.saveRollout(rollout);
			return rollout;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create rollout: [" + rolloutName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1003", "Could not create rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public RemoveApplicationRollout createRemoveApplicationRollout(
		String rolloutName,
		String deliveryRuleSetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("RolloutService::createRemoveApplicationRollout(): rolloutName: [{}], deliveryRuleSetName: {}", rolloutName, deliveryRuleSetName);
			
			RemoveApplicationRollout rollout = new RemoveApplicationRollout(rolloutName, deliveryRuleSetName);
			this.rolloutRepository.saveRollout(rollout);
			return rollout;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create rollout: [" + rolloutName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1003", "Could not create rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public SuspendSoftwareRollout createSuspendSoftwareRollout(
		String rolloutName,
		String deliveryRuleSetName)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("RolloutService::createSuspendSoftwareRollout(): rolloutName: [{}], deliveryRuleSetName: {}", rolloutName, deliveryRuleSetName);
			
			SuspendSoftwareRollout rollout = new SuspendSoftwareRollout(rolloutName, deliveryRuleSetName);
			this.rolloutRepository.saveRollout(rollout);
			return rollout;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create rollout: [" + rolloutName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1003", "Could not create rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public SoftwareWithDirectConfigurationRollout createSoftwareWithDirectConfigurationRollout(
		String rolloutName,
		String deliveryRuleSetName,
		String nodeAcronym,
		String nodeAddress)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("RolloutService::createSoftwareWithDirectConfigurationRollout(): rolloutName: [{}], deliveryRuleSetName: {}", rolloutName, deliveryRuleSetName);
			
			SoftwareWithDirectConfigurationRollout rollout = new SoftwareWithDirectConfigurationRollout(
				rolloutName, 
				deliveryRuleSetName,
				nodeAcronym,
				nodeAddress);				
			this.rolloutRepository.saveRollout(rollout);
			return rollout;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create rollout: [" + rolloutName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1003", "Could not create rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public DirectConfigurationOnlyRollout createDirectConfigurationOnlyRollout(
		String rolloutName,
		String nodeAcronym,
		String nodeAddress)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("RolloutService::createDirectConfigurationOnlyRollout(): rolloutName: [{}], deliveryRuleSetName: {}", rolloutName);
			
			DirectConfigurationOnlyRollout rollout = new DirectConfigurationOnlyRollout(
				rolloutName, 
				nodeAcronym,
				nodeAddress);				
			this.rolloutRepository.saveRollout(rollout);
			return rollout;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create rollout: [" + rolloutName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1003", "Could not create rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public OdlUpdateRollout createOdlUpdateRollout(
		String rolloutName,
		String programCode,
		Integer modelYear,
		String regionCode)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("RolloutService::createOdlUpdateRollout(): rolloutName: [{}], deliveryRuleSetName: {}", rolloutName);
			
			OdlUpdateRollout rollout = new OdlUpdateRollout(
				rolloutName, 
				programCode,
				modelYear,
				regionCode);				
			this.rolloutRepository.saveRollout(rollout);
			return rollout;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create rollout: [" + rolloutName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1003", "Could not create rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public PolicyTableUpdateRollout createPolicyTableUpdateRollout(
		String rolloutName,
		String programCode,
		Integer modelYear,
		String regionCode)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("RolloutService::createPolicyTableUpdateRollout(): rolloutName: [{}], deliveryRuleSetName: {}", rolloutName);
			
			PolicyTableUpdateRollout rollout = new PolicyTableUpdateRollout(
				rolloutName, 
				programCode,
				modelYear,
				regionCode);				
			this.rolloutRepository.saveRollout(rollout);
			return rollout;
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create rollout: [" + rolloutName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1003", "Could not create rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public List<AbstractRollout> getAllRollouts() {
		return this.rolloutRepository.getAllRollouts();
	}
	
	public AbstractRollout getRolloutByName(
        String rolloutName)
	throws 
		EntityDoesNotExistException {

		try {
			return this.rolloutRepository.getRolloutByName(rolloutName);
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1004", "Could not retrieve rollout: [" + rolloutName +"] because it does not exist");
			throw ednee;
		}
	}
	
	public RolloutProgress getRolloutProgress(
        String rolloutName)
	throws 
		EntityDoesNotExistException {

		try {
			AbstractRollout rollout = this.rolloutRepository.getRolloutByName(rolloutName);
			
			int totalNumberOfCampaigns = rollout.getTotalNumberOfCampaigns();
			int totalNumberofBatches = rollout.getTotalNumberOfBatches();
			int totalNumberOfVehicles = rollout.getTotalNumberOfVehicles();
			int totalNumberOfVehicleMessages = rollout.getTotalNumberOfVehicleMessages();
			
			RolloutState rolloutState = rollout.getRolloutState();
			int numberComplete = rollout.getNumberComplete();
			int numberSuccessful = rollout.getNumberSuccessful();
			int numberFailure = rollout.getNumberFailure();
			int numberIncomplete = rollout.getNumberIncomplete();
			
			double percentageComplete = rollout.getPercentageComplete();
			double percentageSuccessful = rollout.getPercentageSuccessful();
			double percentageFailure = rollout.getPercentageFailure();
			
			RolloutProgress rolloutProgress = new RolloutProgress();
			rolloutProgress.setTotalNumberOfCampaigns(totalNumberOfCampaigns);
			rolloutProgress.setTotalNumberofBatches(totalNumberofBatches);
			rolloutProgress.setTotalNumberOfVehicles(totalNumberOfVehicles);
			rolloutProgress.setTotalNumberOfVehicleMessages(totalNumberOfVehicleMessages);
			rolloutProgress.setRolloutState(rolloutState);
			rolloutProgress.setNumberComplete(numberComplete);
			rolloutProgress.setNumberSuccessful(numberSuccessful);
			rolloutProgress.setNumberFailure(numberFailure);
			rolloutProgress.setNumberIncomplete(numberIncomplete);
			rolloutProgress.setPercentageComplete(percentageComplete);
			rolloutProgress.setPercentageFailure(percentageFailure);
			rolloutProgress.setPercentageSuccessful(percentageSuccessful);
			return rolloutProgress;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1004", "Could not retrieve rollout progress because rollout: [" + rolloutName +"] does not exist");
			throw ednee;
		}
	}
	
	public void performPreProcessing(
		String rolloutName, 
		com.djt.cvpp.ota.orfin.delivery.mapper.dto.DeliveryRuleSet deliveryRuleSet) 
	throws 
		EntityDoesNotExistException, 
		ValidationException, 
		VadrException, 
		TmcResourceException {
		
		try {
			
			// Get the rollout entity given the name 
			LOGGER.debug("RolloutService::performPreProcessing(): rollout: [{}].", rolloutName);
			AbstractRollout abstractRollout = this.rolloutRepository.getRolloutByName(rolloutName);
			
			// Make sure that we only perform this step once and when we are in the right "pre" state
			RolloutState rolloutState = abstractRollout.getRolloutState();
			if (!rolloutState.equals(RolloutState.PRE_ROLLOUT_INVALID)) {
				throw new IllegalStateException("AbstractRollout pre-processing  can only be performed when in the PRE_ROLLOUT_INVALID state, yet rollout: [" + rolloutName + "] was in state: [" + rolloutState + "].");
			}
			
			// TODO: TDM: Move the type specific logic here to an appropriate strategy class.
			if (abstractRollout instanceof SoftwareUpdateRollout) {
				
				// This is the "target" or "destination" state for the software.  If, for whatever reason, a vehicle is already at the target state, then it can be ignored, as it is "up-to-date"...
				// All other vehicles will be in some back-level, or prior version of the software (assuming everthing was installed atomically).
				Iterator<VadrRelease> vadrReleaseIterator = deliveryRuleSet.getVadrReleases().iterator();
				while (vadrReleaseIterator.hasNext()) {
					
					VadrRelease vadrRelease = vadrReleaseIterator.next();
					
					String domainName = vadrRelease.getDomainName();
					String domainInstanceName = vadrRelease.getDomainInstanceName();
					String targetDomainInstanceVersion = vadrRelease.getDomainInstanceVersion();
					
					// FENIX-FLARE will only be updating PRODUCTION vehicles
					Domain domain = this.vadrClient.retrieveDomainInstanceLineage(
						domainName, 
						domainInstanceName, 
						targetDomainInstanceVersion, 
						abstractRollout.getNumberOfPriorDomainInstancesToUpdate(), 
						ProductionState.PRODUCTION.toString(), 
						ReleaseState.RELEASED.toString());
										
					DomainInstance domainInstance = domain.getDomainInstance(
						domainInstanceName, 
						targetDomainInstanceVersion);
					
					// Inclusions take the form of: XXXX_YYYY where XXXX is the programCode and modelYear is the 4 digit model year
					Set<String> programCodeModelYearInclusions = deliveryRuleSet.getProgramCodeModelYearInclusions();
					
					// Build the TMC vehicle discovery query and also save it to the rollout (for diagnostic/troubleshooting purposes)
					String tmcVehicleDiscoveryQuery = domainInstance.buildTmcVehicleDiscoveryQuery();
					abstractRollout.setTmcVehicleDiscoveryQuery(tmcVehicleDiscoveryQuery);
					
					// Get all qualified vehicles, that is, all vehicles that have matching hardware part numbers *and* match the program inclusions (if specified)
					LOGGER.debug("RolloutService::performPreProcessing(): rollout: [{}], TMC TmcVehicle Discovery Query: [{}].", abstractRollout, tmcVehicleDiscoveryQuery);
					List<TmcVehicle> vehicleList = this.tmcVehicleClient.performVehicleDiscovery(
						programCodeModelYearInclusions, 
						domainInstance);

					// Iterate through all the vehicles, bucketing them into the right campaign, which is a combination of the programCode/modelYear and the "source" domainInstanceVersion 
					// (i.e. out-of-date version, which assumes that all software for the domain instance is installed atomically)
					Map<String, Campaign> campaignMap = new HashMap<>();
					if (!vehicleList.isEmpty()) {
						
						Iterator<TmcVehicle> vehicleIterator = vehicleList.iterator();
						while (vehicleIterator.hasNext()) {
							
							// Get the next vehicle to process.  
							TmcVehicle tmcVehicle = vehicleIterator.next();
							String programCode = tmcVehicle.getProgramCode();
							Integer modelYear = tmcVehicle.getModelYear();

							// Get the version of the software that the vehicle is at. We call this the "source" version.
							DomainInstance sourceDomainInstance = domain.getSourceDomainInstanceForVehicle(tmcVehicle);
							
							// If the vehicle has had a module replaced, then it may not be at an "atomic" version of the domain instance.
							// In this scenario, we have to skip the vehicle.  TODO: TDM: Do we need to "report" this oddball vehicle?
							if (sourceDomainInstance != null) {

								String sourceDomainInstanceVersion = sourceDomainInstance.getDomainInstanceVersion();
								
								String key = AbstractEntity.buildNaturalIdentity(
									programCode,
									modelYear,
									domainName,
									domainInstanceName,
									sourceDomainInstanceVersion);
								
								// See if we need to instantiate a new campaign given this combination of programCode/modelYear and "source" domain instance version.
								Campaign campaign = campaignMap.get(key);
								if (campaign == null) {
									
									/*
									// Each campaign is associated with a programCode/modelYear, so retrieve the ODL, so that we know how to generate the manifest
							        Odl odl = this.programModelYearService.getOdlByProgramCodeAndModelYear(programCode, modelYear); 
							        String odlPayload = this.optimizedDataListService.getJsonConverter().marshallFromEntityToJson(odl); 
							        */
							        // TODO: TDM: Since FENIX-FIRE will be generating the manifest, there is no need to store it in the campaign anymore.
							        String odlPayload = "";
									
									campaign = new Campaign(
										abstractRollout,
										domainName,
										domainInstanceName,
										sourceDomainInstanceVersion,
										programCode,
										modelYear,
										odlPayload);
									
									campaign.generateManifest(domainInstance, odlPayload);
									
									abstractRollout.addCampaign(campaign);
									campaignMap.put(key, campaign);
								}
								this.addVehicleToCampaign(campaign, tmcVehicle);
							} else {
								LOGGER.warn("Vehicle: [" 
							        + tmcVehicle 
							        + "] is not eligible because it has software that does not match any domain instance version for domain instance: " 
							        + domainInstance 
							        + "] and target domain instance version: [" 
							        + targetDomainInstanceVersion 
							        + "].");
							}
						}
					}
				}				
			}
			
			this.updateEntity(abstractRollout);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1005", "Could not perform pre-processing on rollout: [" + rolloutName +"] because it does not exist");
			throw ednee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1006", "Could not perform pre-processing on rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	private void addVehicleToCampaign(Campaign campaign, TmcVehicle tmcVehicle) throws ValidationException {
		
		LOGGER.debug("RolloutService::addVehicleToCampaign(): campaign: [{}], vehicle: [{}].", campaign, tmcVehicle);
		
		CampaignBatch campaignBatch = campaign.getFirstNonFullCampaignBatch();
		int totalNumberOfVehicles = 0;
		if (campaignBatch != null) {
			totalNumberOfVehicles = campaignBatch.getTotalNumberOfVehicles();
		}
		int maxNumberOfVehiclesPerBatch = campaign.getParentRollout().getMaxNumberVehiclesPerCampaignBatch().intValue();
		if (campaignBatch == null || (totalNumberOfVehicles+1) > maxNumberOfVehiclesPerBatch) {
			
			campaignBatch = new CampaignBatch(campaign, Integer.toString(campaign.getCampaignBatches().size() + 1));
			campaign.addCampaignBatch(campaignBatch);
		}
		
		String tmcVehicleId = tmcVehicle.getUuid().toString();
		String tmcVehicleObjectPayload = this.tmcVehicleJsonConverter.marshallFromEntityToJson(tmcVehicle);
		
		VehicleCampaignBatch vehicleCampaignBatch = new VehicleCampaignBatch(
			campaignBatch,
			tmcVehicle.getVin(),
			tmcVehicleId,
			tmcVehicleObjectPayload);
		
		vehicleCampaignBatch.generateSignedCommands();
		
		campaignBatch.addVehicleCampaignBatch(vehicleCampaignBatch);
	}
	
	public void uploadGeneratedContentToTmcByteStream(
		String rolloutName) 
	throws 
		EntityDoesNotExistException, 
		ValidationException, 
		TmcByteStreamException {
		
		try {
			AbstractRollout abstractRollout = this.rolloutRepository.getRolloutByName(rolloutName);

			LOGGER.debug("RolloutService::uploadGeneratedContentToTmcByteStream(): rollout: [{}].", abstractRollout);
			Iterator<Campaign> campaignIterator = abstractRollout.getCampaigns().iterator();
			while (campaignIterator.hasNext()) {
				
				Campaign campaign = campaignIterator.next();
				this.uploadGenericManifestToTmcByteStream(campaign);
				
				Iterator<CampaignBatch> campaignBatchIterator = campaign.getCampaignBatches().iterator();
				while (campaignBatchIterator.hasNext()) {
					
					CampaignBatch campaignBatch = campaignBatchIterator.next();
					
					campaignBatch.setStateToReleaseArtifactUploadPending();
					
					Iterator<VehicleCampaignBatch> vehicleCampaignBatchIterator = campaignBatch.getVehicleCampaignBatches().iterator();
					while (vehicleCampaignBatchIterator.hasNext()) {

						VehicleCampaignBatch vehicleCampaignBatch = vehicleCampaignBatchIterator.next();
						
						this.uploadSignedCommandsToTmcByteStream(vehicleCampaignBatch);					
					}
				}
			}
			abstractRollout.setStateToReleaseArtifactUploadPending();
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1007", "Could not upload generated content for rollout: [" + rolloutName +"] because it does not exist");
			throw ednee;
		} catch (TmcByteStreamException tbse) {
			this.setExceptionIdentity(tbse, "1008", "Could not perform upload generated content for rollout: [" + rolloutName + "] because of TMC Byte Stream error: [" + tbse.getMessage() + "]");
			throw tbse;
		}
	}

	private TmcBinary uploadGenericManifestToTmcByteStream(Campaign campaign) throws TmcByteStreamException {
		
		campaign.setStateToReleaseArtifactUploadPending();
		
		LOGGER.debug("RolloutService::uploadGenericManifestToTmcByteStream(): campaign: [{}].", campaign);
		return this.tmcByteStreamClient.uploadGenericManifestToTmcByteStream(campaign);
	}
	
	private TmcBinary uploadSignedCommandsToTmcByteStream(VehicleCampaignBatch vehicleCampaignBatch) throws TmcByteStreamException {

		vehicleCampaignBatch.setStateToReleaseArtifactUploadPending();
		
		LOGGER.debug("RolloutService::uploadSignedCommandsToTmcByteStream(): vehicleCampaignBatch: [{}].", vehicleCampaignBatch);
		return this.tmcByteStreamClient.uploadVehicleCampaignBatchSignedCommandToTmcByteStream(vehicleCampaignBatch);
	}
	
	public void initiateRollout(
		String rolloutName,
		String initiatedBy) 
	throws 
		EntityDoesNotExistException, 
		ValidationException {

		try {
			AbstractRollout abstractRollout = this.rolloutRepository.getRolloutByName(rolloutName);
			LOGGER.debug("RolloutService::initiateRollout(): rollout: [{}], initiatedBy: [{}]", abstractRollout, initiatedBy);
			abstractRollout.initiateRollout(initiatedBy);
			this.updateEntity(abstractRollout);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1009", "Could not initiate rollout: [" + rolloutName +"] because it does not exist");
			throw ednee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1010", "Could not initiate rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public void evaluateRolloutState(
		String rolloutName) 
	throws 
		EntityDoesNotExistException, 
		ValidationException,
		TmcDeploymentException {
		
		try {
			AbstractRollout abstractRollout = this.rolloutRepository.getRolloutByName(rolloutName);
			LOGGER.debug("RolloutService::evaluateRolloutState(): rollout: [{}].", abstractRollout);
			
			// TODO: TDM: deal with rollout "phase" processing once we can get complex behavior going with the "validation phase" (which can be as many vehicles as we want)
	
			// We are only considered to be "active" if the current state is DEPLOYING.  All other states are static in nature (either waiting to be deployed, paused, complete or cancelled)
			int numberOfCampaigns = abstractRollout.getCampaigns().size();
			int numberOfCompleteCampaigns = 0;
			if (abstractRollout.getRolloutState() == RolloutState.DEPLOYING) {
				
				// See if we can trigger any new deployments, which are at the campaign batch level.  Here, we simply evaluate each child campaign.
				Iterator<Campaign> campaignIterator = abstractRollout.getCampaigns().iterator();
				while (campaignIterator.hasNext()) {
					
					Campaign campaign = campaignIterator.next();
					
					this.evaluateCampaignState(campaign);
					
					CampaignState campaignState = campaign.getCampaignState();
					if (campaignState == CampaignState.FINISHED
						|| campaignState == CampaignState.CANCELLED) {
						
						numberOfCompleteCampaigns++;
					}
				}
							
				// See if the rollout time limit has been reached.
				if (!AbstractEntity.getTimeKeeper().getCurrentTimestamp().before(abstractRollout.getEndDate())) {
					
					LOGGER.debug("RolloutService::evaluateRolloutState(): END DATE: [{}] REACHED, CANCELLING PENDING ACTIVITY for rollout: [{}].", abstractRollout.getEndDate(), abstractRollout);
					
					// This will cancel any pending activity.  It will not alter anything already finished.
					abstractRollout.cancel();
				}
				
			} else {
				
				// If we are not actively deploying, then we might be in a precursor state.  If so, validate the entity graph.
				List<String> validationMessages = new ArrayList<String>();
				abstractRollout.validate(validationMessages);
			}
			
			// TODO: TDM: There may be a percentage threshold, by which a campaign is considered complete.
			if (numberOfCompleteCampaigns == numberOfCampaigns) {
				abstractRollout.setRolloutStateToFinished();
			}
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1011", "Could not evaluate state for rollout: [" + rolloutName +"] because it does not exist");
			throw ednee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1012", "Could not evaluate state for rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	private void evaluateCampaignState(Campaign campaign) throws EntityDoesNotExistException, ValidationException, TmcDeploymentException {
		
		CampaignState campaignState = campaign.getCampaignState();

		LOGGER.debug("RolloutService::evaluateCampaignState(): campaign [{}].", campaign);
		
		int numberOfCampaignBatches = campaign.getCampaignBatches().size();
		int numberOfCompleteCampaignBatches = 0;
		
		int maxNumberActiveCampaignBatches = campaign.getParentRollout().getMaxNumberActiveCampaignBatches();
		int numberActiveCampaignBatches = campaign.getParentRollout().getNumberActiveCampaignBatches();
		
		Iterator<CampaignBatch> campaignBatchIterator = campaign.getCampaignBatches().iterator();
		while (campaignBatchIterator.hasNext()) {
			
			CampaignBatch campaignBatch = campaignBatchIterator.next();
			
			CampaignBatchState campaignBatchState = campaignBatch.getCampaignBatchState();
			
			if (campaignBatchState.equals(CampaignBatchState.PENDING) && (numberActiveCampaignBatches < maxNumberActiveCampaignBatches)) {
				
				if (campaignState.equals(CampaignState.PENDING)) {
					campaign.setStateToDeploying();	
				}

				campaignBatch.setStateToActive();
				this.tmcDeploymentClient.pushDeploymentToTmc(campaignBatch);
				numberActiveCampaignBatches++;
			}				
			
			campaignBatch.evaluateCampaignBatchState();
			
			campaignBatchState = campaignBatch.getCampaignBatchState();
			if (campaignBatchState == CampaignBatchState.FINISHED || campaignBatchState == CampaignBatchState.CANCELLED) {
				
				numberOfCompleteCampaignBatches++;
			}
		}
		
		// TODO: TDM: There may be a percentage threshold, by which a campaign is considered complete.
		if (numberOfCompleteCampaignBatches == numberOfCampaignBatches) {
			campaign.setStateToFinished();
		}
	}
	
	private VehicleCampaignBatch getVehicleCampaignBatchByTmcVehicleId(String tmcDeploymentId, String tmcVehicleId) {
		
		LOGGER.debug("RolloutService::getVehicleCampaignBatchByTmcVehicleId(): tmcDeploymentId [{}], tmcVehicleId: [{}].", tmcDeploymentId, tmcVehicleId);
		
		Iterator<AbstractRollout> iterator = this.getAllRollouts().iterator();
		while (iterator.hasNext()) {
			
			AbstractRollout abstractRollout = iterator.next();
			
			VehicleCampaignBatch vehicleCampaignBatch = abstractRollout.getVehicleCampaignBatchByTmcVehicleId(tmcDeploymentId, tmcVehicleId);
			if (vehicleCampaignBatch != null) {
				return vehicleCampaignBatch;
			}
		}
		return null;
	}
			
	public void pauseRollout(
		String rolloutName) 
	throws 
		EntityDoesNotExistException, 
		ValidationException {

		try {
			
			AbstractRollout abstractRollout = this.rolloutRepository.getRolloutByName(rolloutName);
			abstractRollout.pause();
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1013", "Could not pause rollout: [" + rolloutName +"] because it does not exist");
			throw ednee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1014", "Could not pause rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public void resumeRollout(
		String rolloutName) 
	throws 
		EntityDoesNotExistException, 
		ValidationException {
		
		try {
			
			AbstractRollout abstractRollout = this.rolloutRepository.getRolloutByName(rolloutName);
			abstractRollout.resume();
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1015", "Could not resume rollout: [" + rolloutName +"] because it does not exist");
			throw ednee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1016", "Could not resume rollout: [" + rolloutName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public void cancelRollout(
		String rolloutName) 
	throws 
		EntityDoesNotExistException {
		
		try {
			
			AbstractRollout abstractRollout = this.rolloutRepository.getRolloutByName(rolloutName);
			abstractRollout.cancel();
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1017", "Could not cancel rollout: [" + rolloutName +"] because it does not exist");
			throw ednee;
		}
	}
	
	
	
	// INHERITED BUSINESS BEHAVIORS
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		return this.rolloutRepository.updateEntity((AbstractRollout)entity);
	}
		
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		return this.rolloutRepository.deleteEntity((AbstractRollout)entity);
	}
	
	
	// EVENT BASED BEHAVIORS
	public void handleTmcVehicleStatusMessageEvent(TmcVehicleStatusMessageEvent tmcVehicleStatusMessageEvent) throws ValidationException, EntityDoesNotExistException {
		
		LOGGER.debug("RolloutService::handleTmcVehicleStatusMessageEvent(): tmcVehicleStatusMessageEvent [{}].", tmcVehicleStatusMessageEvent);
		
		TmcVehicleStatusMessage tmcVehicleStatusMessage = tmcVehicleStatusMessageEvent.getPayload();
		
		String otaFunction = tmcVehicleStatusMessage.extractOtaFunction();
		String prefix = tmcVehicleStatusMessage.extractPrefix();
		String lookupCode = tmcVehicleStatusMessage.extractLookupCode();
		String tmcDeploymentId = tmcVehicleStatusMessage.getDeploymentId();
		String tmcVehicleId = tmcVehicleStatusMessage.getVehicleId();
		String vehicleStatusMessageExpression = tmcVehicleStatusMessage.getVehicleStatusMessageExpression();
		
		VehicleStatusMessageTemplate vehicleStatusMessageTemplate = this.vehicleStatusMessageTemplateService.getVehicleStatusMessageTemplate(
			otaFunction, 
			prefix, 
			lookupCode);
		
		VehicleCampaignBatch vehicleCampaignBatch = this.getVehicleCampaignBatchByTmcVehicleId(tmcDeploymentId, tmcVehicleId);
		if (vehicleCampaignBatch != null) {
		
			VehicleStatusMessage vehicleStatusMessage = vehicleStatusMessageTemplate.buildVehicleStatusMessage(vehicleCampaignBatch, vehicleStatusMessageExpression);
			vehicleCampaignBatch.addVehicleStatusMessage(vehicleStatusMessage);
			
		} else {

			String errorMessage = "Could not find any rollout containing campaignBatch with tmcDeploymentID: [" 
				+ tmcDeploymentId 
				+ "] and vehicleCampaignBatch with tmcVehicleId: [" 
				+ tmcVehicleId 
				+ "].";
			System.err.println(errorMessage);
			throw new FenixRuntimeException(errorMessage);
		}
	}
		
	private void setExceptionIdentity(ExceptionIdentity exceptionIdentity, String uniqueErrorCode, String messageOverride) {
		
		exceptionIdentity.setBoundedContextName(BOUNDED_CONTEXT_NAME);
		exceptionIdentity.setServiceName(SERVICE_NAME);
		exceptionIdentity.setUniqueErrorCode(uniqueErrorCode);
		exceptionIdentity.setMessageOverride(messageOverride);
	}

	public void handleDeliveryRuleSetEvent(OrfinDeliveryRuleSetEvent orfinDeliveryRuleSetEvent) throws ValidationException {
		
		String updateAction = orfinDeliveryRuleSetEvent.getUpdateAction();
		String rolloutName = updateAction + AbstractEntity.getTimeKeeper().getCurrentInstant();
		String deliveryRuleSetName = orfinDeliveryRuleSetEvent.getDeliveryRuleSetName();
		String nodeAcronym = orfinDeliveryRuleSetEvent.getNodeAcronym();
		String nodeAddress = orfinDeliveryRuleSetEvent.getNodeAddress();
		
		try {

			if (updateAction.equals(OrfinDeliveryRuleSetEvent.SOFTWARE_UPDATE)) {
				
				this.createSoftwareUpdateRollout(rolloutName, deliveryRuleSetName);
				
			} else if (updateAction.equals(OrfinDeliveryRuleSetEvent.APPLICATION_UPDATE)) {
				
				this.createApplicationUpdateRollout(rolloutName, deliveryRuleSetName);
				
			} else if (updateAction.equals(OrfinDeliveryRuleSetEvent.ADD_APPLICATION)) {
				
				this.createAddApplicationRollout(rolloutName, deliveryRuleSetName);
							
			} else if (updateAction.equals(OrfinDeliveryRuleSetEvent.REMOVE_APPLICATION_UPDATE)) {
				
				this.createRemoveApplicationRollout(rolloutName, deliveryRuleSetName);
				
			} else if (updateAction.equals(OrfinDeliveryRuleSetEvent.DIRECT_CONFIGURATION_ONLY_UPDATE)) {
				
				this.createDirectConfigurationOnlyRollout(rolloutName, nodeAcronym, nodeAddress);
				
			} else if (updateAction.equals(OrfinDeliveryRuleSetEvent.SOFTWARE_WITH_DIRECT_CONFIGURATION_UPDATE)) {
				
				this.createSoftwareWithDirectConfigurationRollout(rolloutName, deliveryRuleSetName, nodeAcronym, nodeAddress);
				
			} else if (updateAction.equals(OrfinDeliveryRuleSetEvent.SOFTWARE_SUSPEND)) {
				
				this.createSuspendSoftwareRollout(rolloutName, deliveryRuleSetName);
				
			} else {
				throw new FenixRuntimeException("Unsupported delivery rule set update action: " + updateAction);
			}
			
		} catch (EntityAlreadyExistsException eaee) {
			throw new FenixRuntimeException("Cannot create rollout: [" + rolloutName + "], because one already exists with the same name, error: " + eaee.getMessage(), eaee);
		}
	}
	
	public void handleOrfinPolicySetEvent(OrfinPolicySetEvent event) throws ValidationException {

		String rolloutName = "PolicyTableUpdateRollout " + AbstractEntity.getTimeKeeper().getCurrentInstant();
		try {
			
			String programCode = event.getProgramCode();
			Integer modelYear = event.getModelYear();
			String regionCode = event.getRegionCode();
			
			this.createPolicyTableUpdateRollout(rolloutName, programCode, modelYear, regionCode);
			
		} catch (EntityAlreadyExistsException | ValidationException e) {
			throw new FenixRuntimeException("Could not create rollout for PolicySet event, error: " + e.getMessage(), e);
		}
	}
	
	public void handleOrfinOdlEvent(OrfinOdlEvent event) throws ValidationException {

		String rolloutName = "OdlUpdateRollout " + AbstractEntity.getTimeKeeper().getCurrentInstant();
		try {
			
			String programCode = event.getProgramCode();
			Integer modelYear = event.getModelYear();
			String regionCode = null;
			
			this.createOdlUpdateRollout(rolloutName, programCode, modelYear, regionCode);
			
		} catch (EntityAlreadyExistsException | ValidationException e) {
			throw new FenixRuntimeException("Could not create rollout for ODL event, error: " + e.getMessage(), e);
		}
	}
}
