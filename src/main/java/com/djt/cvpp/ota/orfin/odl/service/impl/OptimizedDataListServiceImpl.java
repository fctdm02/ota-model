/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.service.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ExceptionIdentity;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.service.impl.AbstractService;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEvent;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEventPublisher;
import com.djt.cvpp.ota.orfin.odl.event.OrfinOdlEventSubscriber;
import com.djt.cvpp.ota.orfin.odl.mapper.OdlDtoMapper;
import com.djt.cvpp.ota.orfin.odl.mapper.OdlJsonConverter;
import com.djt.cvpp.ota.orfin.odl.model.CustomOdl;
import com.djt.cvpp.ota.orfin.odl.model.Did;
import com.djt.cvpp.ota.orfin.odl.model.EcgSignal;
import com.djt.cvpp.ota.orfin.odl.model.Network;
import com.djt.cvpp.ota.orfin.odl.model.Node;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.odl.model.enums.SpecificationCategoryType;
import com.djt.cvpp.ota.orfin.odl.repository.OptimizedDataListRepository;
import com.djt.cvpp.ota.orfin.odl.service.OptimizedDataListService;
import com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class OptimizedDataListServiceImpl extends AbstractService implements OptimizedDataListService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OptimizedDataListServiceImpl.class);

	
	// For domain services and repositories relating to ORFIN-DELIVERY	
	protected OdlJsonConverter odlJsonConverter = new OdlJsonConverter();
	protected OdlDtoMapper odlDtoMapper = new OdlDtoMapper();
	
	protected OptimizedDataListRepository optimizedDataListRepository;
	
	// For dealing with publishing odl events
	protected OrfinOdlEventPublisher delegate;

	
	public OptimizedDataListServiceImpl() {
	}

	public OptimizedDataListServiceImpl(
		OptimizedDataListRepository optimizedDataListRepository,
		OrfinOdlEventPublisher orfinOdlEventPublisher) {
		
		this.optimizedDataListRepository = optimizedDataListRepository;
		this.delegate = orfinOdlEventPublisher;
	}

	public OdlJsonConverter getJsonConverter() {
		return this.odlJsonConverter;
	}

	public OdlDtoMapper getDtoMapper() {
		return this.odlDtoMapper;
	}
	
	public void setOdlRepository(OptimizedDataListRepository optimizedDataListRepository) {
		this.optimizedDataListRepository = optimizedDataListRepository;
	}
	
	public void setOrfinOdlEventPublisher(OrfinOdlEventPublisher orfinOdlEventPublisher) {
		this.delegate = orfinOdlEventPublisher;
	}
	
	// EVENT BASED BEHAVIORS
	public void subscribe(OrfinOdlEventSubscriber orfinOdlEventSubscriber) {
		this.delegate.subscribe(orfinOdlEventSubscriber);
	}
	
	public void unsubscribe(OrfinOdlEventSubscriber orfinOdlEventSubscriber) {
		this.delegate.unsubscribe(orfinOdlEventSubscriber);
	}
	
	public OrfinOdlEvent publishOrfinOdlEvent(
		String owner,
		String programCode,
		Integer modelYear,
		String odlName)
	throws 
		ValidationException {
		
		return this.delegate.publishOrfinOdlEvent(
			owner,
			programCode,
			modelYear,
			odlName);
	}
	
	// INHERITED BUSINESS BEHAVIORS
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		return this.optimizedDataListRepository.updateEntity((Odl)entity);
	}
		
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		return this.optimizedDataListRepository.deleteEntity((Odl)entity);
	}
			

	// BUSINESS BEHAVIORS
	public Odl createOdl(
		String odlName)
	throws 
		EntityAlreadyExistsException, 
		ValidationException {
		
		try {
			
			LOGGER.debug("OptimizedDataListService::createOdl(): odlName: [{}].", odlName);
			
			return this.optimizedDataListRepository.createOdl(
				odlName);
			
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1001", "Could not create odl: [" + odlName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1002", "Could not create odl: [" + odlName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public Odl createNetwork(
		String odlName,
		String networkName,
		String protocol,
		String dataRate,
		String dclName,
		String networkPins)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("OptimizedDataListService::createNetwork(): networkName: [{}], odlName: [{}].", networkName, odlName);
			
			Odl odl = this.optimizedDataListRepository.getOdlByName(odlName);
			Network network = new Network
				.NetworkBuilder()
				.withDataRate(dataRate)
				.withDclName(dclName)
				.withNetworkName(networkName)
				.withNetworkPins(networkPins)
				.withProtocol(protocol)
				.build();
			
			odl.addNetwork(network);
			
			this.optimizedDataListRepository.updateEntity(odl);
			
			return odl;

		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "10XX", "Could not create network: [" + networkName + "] because [" + odlName +"] does not exist");
			throw ednee;
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "10XX", "Could not create network: [" + networkName + "] for odl: [" + odlName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "10XX", "Could not create network: [" + networkName + "] for odl: [" + odlName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public Odl createNode(
		String odlName,
		String networkName,
		String nodeAcronym,
		String nodeAddress,
		String gatewayNodeId,
		String gatewayType,
		Boolean hasConditionBasedOnDtc,
		Boolean isOvtp,
		String ovtpDestinationAddress,
		String specificationCategoryType,
		Integer diagnosticSpecificationResponse,
		Integer activationTime)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("OptimizedDataListService::createNode(): nodeAcronym: [{}], nodeAddress: [{}], networkName: [{}], odlName: [{}].", nodeAcronym, nodeAddress, networkName, odlName);
			
			Odl odl = this.optimizedDataListRepository.getOdlByName(odlName);
			
			Node node = new Node
				.NodeBuilder()
				.withAcronym(nodeAcronym)
				.withAddress(nodeAddress)
				.withDiagnosticSpecificationResponse(diagnosticSpecificationResponse)
				.withActivationTime(activationTime)
				.withGatewayType(gatewayType)
				.withHasConditionBasedOnDtc(hasConditionBasedOnDtc)
				.withIsOvtp(isOvtp)
				.withOvtpDestinationAddress(ovtpDestinationAddress)
				.withSpecificationCategoryType(SpecificationCategoryType.valueOf(specificationCategoryType))
				.build();
			
			odl.addNode(networkName, node);
			
			this.optimizedDataListRepository.updateEntity(odl);
			
			return odl;

		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "10XX", "Could not create node: [" + nodeAcronym + nodeAddress + "] because network: [" + networkName + "] or odl: [" + odlName +"] does not exist");
			throw ednee;
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "10XX", "Could not create node: [" + nodeAcronym + nodeAddress + "] for network: ["+ networkName + "] for odl: [" + odlName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "10XX", "Could not create node: [" + nodeAcronym + nodeAddress + "] for network: [" + networkName + "] for odl: [" + odlName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public Odl addIgnoredDidsToNode(
		String odlName,
		String networkName,
		String nodeAcronym,
		String nodeAddress,
		List<String> ignoredDids)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("OptimizedDataListService::addIgnoredDidsToNode(): nodeAcronym: [{}], nodeAddress: [{}], networkName: [{}], odlName: [{}].", nodeAcronym, nodeAddress, networkName, odlName);
			
			Odl odl = this.optimizedDataListRepository.getOdlByName(odlName);
						
			odl.addIgnoredDidsToNode(networkName, nodeAcronym, nodeAddress, ignoredDids);
			
			this.optimizedDataListRepository.updateEntity(odl);
			
			return odl;

		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "10XX", "Could not add ignored dids to node: [" + nodeAcronym + nodeAddress + "] because network: [" + networkName + "] or odl: [" + odlName +"] does not exist");
			throw ednee;
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "10XX", "Could not add ignored dids to node: [" + nodeAcronym + nodeAddress + "] for network: ["+ networkName + "] for odl: [" + odlName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "10XX", "Could not add ignored dids to node: [" + nodeAcronym + nodeAddress + "] for network: [" + networkName + "] for odl: [" + odlName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public Odl createDid(
		String odlName,
		String networkName,
		String nodeAcronym,
		String nodeAddress,
		String didName,
		String description,
		Boolean vinSpecificDidFlag,
		Boolean directConfigurationDidFlag,
		Boolean privateNetworkDidFlag)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("OptimizedDataListService::createDid(): nodeAcronym: [{}], nodeAddress: [{}], networkName: [{}], odlName: [{}].", nodeAcronym, nodeAddress, networkName, odlName);
			
			Odl odl = this.optimizedDataListRepository.getOdlByName(odlName);
						
			Network network = odl.getNetworkByName(networkName);
			
			Node node = network.getNodeByAcronymAndAddress(nodeAcronym, nodeAddress);
			
			Did did = new Did
				.DidBuilder()
				.withDidName(didName)
				.withDescription(description)
				.withDirectConfigurationDidFlag(directConfigurationDidFlag)
				.withPrivateNetworkDidFlag(privateNetworkDidFlag)
				.withVinSpecificDidFlag(vinSpecificDidFlag)
				.build();
			
			node.addDid(did);
						
			this.optimizedDataListRepository.updateEntity(odl);
			
			return odl;

		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "10XX", "Could not create did: [" + didName + "] because node: [" + nodeAcronym + nodeAddress + "] or network: [" + networkName + "] or odl: [" + odlName +"] does not exist");
			throw ednee;
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "10XX", "Could not create did: [" + didName + "] for node: [" + nodeAcronym + nodeAddress + "] for network: ["+ networkName + "] for odl: [" + odlName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "10XX", "Could not create did: [" + didName + "] for node node: [" + nodeAcronym + nodeAddress + "] for network: [" + networkName + "] for odl: [" + odlName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public Odl addEcgSignalToOdl(
		String odlName,
		String ecgSignalName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
		
		try {
			
			LOGGER.debug("OptimizedDataListService::addEcgSignalToOdl(): ecgSignalName: [{}], odlName: [{}].", ecgSignalName, odlName);
			
			Odl odl = this.optimizedDataListRepository.getOdlByName(odlName);
			
			new EcgSignal.EcgSignalBuilder().withEcgSignalName(ecgSignalName).withParentOdl(odl).build();
						
			this.optimizedDataListRepository.updateEntity(odl);
			
			return odl;

		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "10XX", "Could not add ecg signal: [" + ecgSignalName + "] because odl: [" + odlName +"] does not exist");
			throw ednee;
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "10XX", "Could not create ecg signal: [" + ecgSignalName + "] for odl: [" + odlName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "10XX", "Could not create ecg signal: [" + ecgSignalName + "] for odl: [" + odlName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public Odl addCustomOdlToOdl(
		String odlName,
		String customOdlName,
		List<String> customOdlNodeList)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException,
		ValidationException {
				
		try {
			
			LOGGER.debug("OptimizedDataListService::addCustomOdlToOdl(): customOdlName: [{}], odlName: [{}], customOdlNodeList: [{}].", customOdlName, odlName, customOdlNodeList);
			
			Odl odl = this.optimizedDataListRepository.getOdlByName(odlName);
			
			Set<Node> nodes = odl.getNodeSubset(customOdlNodeList);
			
			new CustomOdl.CustomOdlBuilder().withCustomOdlName(customOdlName).withNodes(nodes).withParentOdl(odl).build();
						
			this.optimizedDataListRepository.updateEntity(odl);
			
			return odl;

		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "10XX", "Could not add custom odl: [" + customOdlName + "] because odl: [" + odlName +"] does not exist");
			throw ednee;
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "10XX", "Could not add custom odl: [" + customOdlName + "] for odl: [" + odlName +"] because it already exists");
			throw eaee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "10XX", "Could not add custom odl: [" + customOdlName + "] for odl: [" + odlName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public List<Odl> getAllOdls() {
		
		return this.optimizedDataListRepository.getAllOdls();
	}

	public Odl getOdlByName(String odlName) throws EntityDoesNotExistException, ValidationException {
		
		try {
			return this.optimizedDataListRepository.getOdlByName(odlName);	
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1003", "Could not retrieve odl: [" + odlName + "] because it does not exist");
			throw ednee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1004", "Could not retrieve odl: [" + odlName + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public Odl getOdlByProgramCodeAndModelYear(
        String programCode,
        Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
	
		try {
			return this.optimizedDataListRepository.getOdlByProgramCodeAndModelYear(programCode, modelYear);	
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1003", "Could not retrieve odl by programCodeModelYear: [" + programCode + modelYear + "] because it does not exist");
			throw ednee;
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1004", "Could not retrieve odl by programCodeModelYear: [" + programCode + modelYear + "] because attribute: [" + ve.getAttributeName() + "] was invalid for reason: [" + ve.getReason() + "]");
			throw ve;
		}
	}
	
	public Odl renameOdl(
		String oldOdlName, 
		String newOdlName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException {
		
		try {

			return this.optimizedDataListRepository.renameOdl(oldOdlName, newOdlName);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1010", "Could not rename odl: [" + oldOdlName + "] because it doesn't exist");
			throw ednee;
	
		} catch (EntityAlreadyExistsException eaee) {
			this.setExceptionIdentity(eaee, "1011", "Could not rename odl: [" + oldOdlName + "] because there already exists a odl with name: [" + newOdlName + "]");
			throw eaee;
		}
	}
	
	public Odl updateOdl(
		String odlName)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		try {

			Odl odl = this.getOdlByName(odlName);

			// TODO: TDM: Add whatever we can modify as direct attributes (when they are added) here.

			LOGGER.debug("OptimizedDataListService::updateOdl(): odlName: [{}].", odlName);
			this.optimizedDataListRepository.updateEntity(odl);
			
			return odl;
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1012", "Could not update odl: [" + odlName + "] because it doesn't exist");
			throw ednee;
	
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1013", "Could not update odl: [" + odlName + "] because attribute: [" + ve.getAttributeName() + "] was invalid because of: [" + ve.getReason() + "]");
			throw ve;
		}
	}

	public void deleteOdl(
		String odlName)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		try {

			LOGGER.debug("OptimizedDataListService::deleteOdl(): odlName: [{}].", odlName);
			Odl odl = this.getOdlByName(odlName);
			this.optimizedDataListRepository.deleteEntity(odl);
			
		} catch (EntityDoesNotExistException ednee) {
			this.setExceptionIdentity(ednee, "1014", "Could not delete odl: [" + odlName + "] because it does not exist");
			throw ednee;
			
		} catch (ValidationException ve) {
			this.setExceptionIdentity(ve, "1015", "Could not delete odl: [" + odlName + "] because the specified odl name is invalid");
			throw ve;
		}
	}
	
	public String renderFullOdlWithEcgSignalsForProgram(
		ProgramModelYear programModelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
	
		return this.renderOdlForProgram(programModelYear, null, Boolean.TRUE);
	}
	
	public String renderOdlForProgram(
		ProgramModelYear programModelYear,
		String customOdlName,
		Boolean includeEcgSignals)
	throws 
		EntityDoesNotExistException,
		ValidationException {

		if (programModelYear == null) {
			
			throw new ValidationException("programModelYear", "must be specified");
		}

		if (includeEcgSignals == null) {
			
			throw new ValidationException("includeEcgSignals", "must be specified");
		}
		
		Odl odlEntity = this.getOdlByProgramCodeAndModelYear(
			programModelYear.getParentProgram().getProgramCode(), 
			programModelYear.getParentModelYear().getModelYearValue());

		String errorMessage = null;
		Set<Node> nodesToInclude = null;
		if (customOdlName != null) {
			
			nodesToInclude = odlEntity.getCustomOdlByName(customOdlName).getNodes();
			
			errorMessage = "Unable to render ODL for programModelYear: [" + programModelYear + "], customOdlName: [" + customOdlName + "] and includeEcgSignals: [" + includeEcgSignals + "], reason: ";
			
		} else {
			
			nodesToInclude = odlEntity.getAllNodes(); 
			
			errorMessage = "Unable to render ODL for programModelYear: [" + programModelYear + "], reason: ";
		}
		
		com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg.Odl vehicleOdlDto = this.getDtoMapper().mapEntityToVehicleDto(
			odlEntity, 
			nodesToInclude, 
			includeEcgSignals);
		
		String odlJson = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			odlJson = objectMapper.writeValueAsString(vehicleOdlDto);
		} catch (JsonProcessingException jpe) {
			throw new FenixRuntimeException(errorMessage + jpe.getMessage(), jpe);
		}
		return odlJson;
	}
	
	private void setExceptionIdentity(ExceptionIdentity exceptionIdentity, String uniqueErrorCode, String messageOverride) {
		
		exceptionIdentity.setBoundedContextName(BOUNDED_CONTEXT_NAME);
		exceptionIdentity.setServiceName(SERVICE_NAME);
		exceptionIdentity.setUniqueErrorCode(uniqueErrorCode);
		exceptionIdentity.setMessageOverride(messageOverride);
	}
}
