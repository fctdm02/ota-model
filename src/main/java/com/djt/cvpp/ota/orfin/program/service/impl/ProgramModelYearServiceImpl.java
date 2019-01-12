/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.program.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.service.impl.AbstractService;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.delivery.service.DeliveryRuleSetService;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.odl.service.OptimizedDataListService;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;
import com.djt.cvpp.ota.orfin.policy.service.PolicySetService;
import com.djt.cvpp.ota.orfin.program.mapper.ProgramModelYearDtoMapper;
import com.djt.cvpp.ota.orfin.program.mapper.ProgramModelYearJsonConverter;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;
import com.djt.cvpp.ota.orfin.program.repository.ProgramModelYearRepository;
import com.djt.cvpp.ota.orfin.program.service.ProgramModelYearService;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class ProgramModelYearServiceImpl extends AbstractService implements ProgramModelYearService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgramModelYearServiceImpl.class);
	
	private ProgramModelYearJsonConverter jsonConverter = new ProgramModelYearJsonConverter();
	private ProgramModelYearDtoMapper dtoMapper = new ProgramModelYearDtoMapper();
	
	private ProgramModelYearRepository programModelYearRepository;
	
	private DeliveryRuleSetService deliveryRuleSetService;
	private OptimizedDataListService optimizedDataListService;
	private PolicySetService policySetService;
	
	public ProgramModelYearServiceImpl(
		ProgramModelYearRepository programModelYearRepository,
		DeliveryRuleSetService deliveryRuleSetService,
		OptimizedDataListService optimizedDataListService,
		PolicySetService policySetService) {
		this.programModelYearRepository = programModelYearRepository;
		this.deliveryRuleSetService = deliveryRuleSetService;
		this.optimizedDataListService = optimizedDataListService;
		this.policySetService = policySetService;
	}
	
	public void setProgramModelYearRepository(ProgramModelYearRepository programModelYearRepository) {
		this.programModelYearRepository = programModelYearRepository;
	}

	public void setDeliveryRuleSetService(DeliveryRuleSetService deliveryRuleSetService) {
		this.deliveryRuleSetService = deliveryRuleSetService;
	}

	public void setOdlService(OptimizedDataListService optimizedDataListService) {
		this.optimizedDataListService = optimizedDataListService;
	}

	public void setPolicySetService(PolicySetService policySetService) {
		this.policySetService = policySetService;
	}
	
	public ProgramModelYearJsonConverter getJsonConverter() {
		return this.jsonConverter;
	}
	
	public ProgramModelYearDtoMapper getDtoMapper() {
		return this.dtoMapper;
	}
	
	public ProgramModelYear createProgramModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		LOGGER.debug("ProgramModelYearServiceImpl::createRollout(): createProgramModelYear: [{}{}].", programCode, modelYear);
		return this.programModelYearRepository.createProgramModelYear(
			programCode, 
			modelYear);
	}

	public List<ProgramModelYear> getAllProgramModelYears() {
		
		return this.programModelYearRepository.getAllProgramModelYears();
	}
	
	public ProgramModelYear getProgramModelYearByProgramCodeAndModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		return this.programModelYearRepository.getProgramModelYearByNaturalIdentity(
			programCode, 
			modelYear);
	}
	
	public Odl getOdlByProgramCodeAndModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		ProgramModelYear programModelYear = this.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		return programModelYear.getOdl();
	}
	
	public PolicySet getPolicySetByProgramCodeAndModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		ProgramModelYear programModelYear = this.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		return programModelYear.getPolicySet();
	}

	public DeliveryRuleSet getDeliveryRuleSetByProgramCodeAndModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		throw new RuntimeException("Not implemented yet.");
	}

	public ProgramModelYear associateOdlToProgramModelYear (
		String odlName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException,
		EntityAlreadyExistsException {
		
		Odl odl = this.optimizedDataListService.getOdlByName(odlName);
		
		ProgramModelYear programModelYear = this.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		programModelYear.setOdl(odl);
		odl.addProgramModelYear(programModelYear);
		
		this.optimizedDataListService.updateEntity(odl);
		this.updateEntity(programModelYear);
		
		return programModelYear;
	}

	public ProgramModelYear disassociateOdlFromProgramModelYear(
		String odlName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		Odl odl = this.optimizedDataListService.getOdlByName(odlName);
		
		ProgramModelYear programModelYear = this.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		programModelYear.setOdl(null);
		odl.removeProgramModelYear(programModelYear);
		
		this.optimizedDataListService.updateEntity(odl);
		this.updateEntity(programModelYear);
		
		return programModelYear;
	}
	
	public ProgramModelYear associatePolicySetToProgramModelYear(
		String policySetName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException, 
		EntityAlreadyExistsException {
		
		PolicySet policySet = this.policySetService.getPolicySetByName(policySetName);
		
		ProgramModelYear programModelYear = this.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		programModelYear.setPolicySet(policySet);
		policySet.addProgramModelYear(programModelYear);
		
		this.policySetService.updateEntity(policySet);
		this.updateEntity(programModelYear);
		
		return programModelYear;
	}
	
	public ProgramModelYear disassociatePolicySetFromProgramModelYear(
		String policySetName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		PolicySet policySet = this.policySetService.getPolicySetByName(policySetName);
		
		ProgramModelYear programModelYear = this.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		programModelYear.setPolicySet(null);
		policySet.removeProgramModelYear(programModelYear);
		
		this.policySetService.updateEntity(policySet);
		this.updateEntity(programModelYear);
		
		return programModelYear;
	}
	
	public ProgramModelYear addDeliveryRuleSetToProgramModelYear(
		String deliveryRuleSetName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		DeliveryRuleSet deliveryRuleSet = this.deliveryRuleSetService.getDeliveryRuleSetByName(deliveryRuleSetName);
		
		ProgramModelYear programModelYear = this.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		programModelYear.addDeliveryRuleSet(deliveryRuleSet);
		deliveryRuleSet.addProgramModelYear(programModelYear);
		
		this.deliveryRuleSetService.updateEntity(deliveryRuleSet);
		this.updateEntity(programModelYear);
		
		return programModelYear;
	}
		
	public ProgramModelYear removeDeliveryRuleSetFromProgramModelYear(
		String deliveryRuleSetName,	
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		DeliveryRuleSet deliveryRuleSet = this.deliveryRuleSetService.getDeliveryRuleSetByName(deliveryRuleSetName);
		
		ProgramModelYear programModelYear = this.getProgramModelYearByProgramCodeAndModelYear(programCode, modelYear);
		programModelYear.removeDeliveryRuleSet(deliveryRuleSet);
		deliveryRuleSet.removeProgramModelYear(programModelYear);
		
		this.deliveryRuleSetService.updateEntity(deliveryRuleSet);
		this.updateEntity(programModelYear);
		
		return programModelYear;
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		return this.programModelYearRepository.updateEntity(entity);
	}
		
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		return this.programModelYearRepository.deleteEntity(entity);
	}
}
