/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.program.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.common.repository.impl.AbstractMockRepository;
import com.djt.cvpp.ota.orfin.program.mapper.ProgramModelYearJsonConverter;
import com.djt.cvpp.ota.orfin.program.model.ModelYear;
import com.djt.cvpp.ota.orfin.program.model.Program;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;
import com.djt.cvpp.ota.orfin.program.repository.ProgramModelYearRepository;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockProgramModelYearRepositoryImpl extends AbstractMockRepository implements ProgramModelYearRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MockProgramModelYearRepositoryImpl.class);
	

	private Map<String, Program> programMap = new TreeMap<>();
	private Map<Integer, ModelYear> modelYearMap = new TreeMap<>();
	private Map<String, ProgramModelYear> programModelYearMap = new TreeMap<>();

	private static ProgramModelYearRepository INSTANCE = new MockProgramModelYearRepositoryImpl();
	public static ProgramModelYearRepository getInstance() {
		return INSTANCE;
	}

	private MockProgramModelYearRepositoryImpl() {
	}
	
	public void reset() {
		this.programMap.clear();
		this.modelYearMap.clear();
		this.programModelYearMap.clear();
	}
	
	
	public ProgramModelYear createProgramModelYear(
		String programCode,
		Integer modelYear)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		String naturalIdentity = AbstractEntity.buildNaturalIdentity(
			programCode, 
			modelYear);
		
		ProgramModelYear check = (ProgramModelYear)getEntityByNaturalIdentityNullIfNotFound(naturalIdentity);
		if (check != null) {
			throw new EntityAlreadyExistsException("ProgramModelYear: [" + naturalIdentity + "] already exists.");
		}
		
		Program programEntity = new Program.ProgramBuilder()
			.withProgramCode(programCode)
			.build();
		this.programMap.put(programCode, programEntity);
		
		ModelYear modelYearEntity = new ModelYear
			.ModelYearBuilder()
			.withModelYearValue(modelYear)
			.build();
		this.modelYearMap.put(modelYear, modelYearEntity);
		
		ProgramModelYear programModelYearEntity = new ProgramModelYear
			.ProgramModelYearBuilder()
			.withParentProgram(programEntity)
			.withParentModelYear(modelYearEntity)
			.build();
		
		this.programModelYearMap.put(naturalIdentity, programModelYearEntity);
		
		return programModelYearEntity;
	}
	
	public List<ProgramModelYear> getAllProgramModelYears() {
		
		List<ProgramModelYear> list = new ArrayList<>();
		list.addAll(this.programModelYearMap.values());
		return list;
	}
	
	public ProgramModelYear getProgramModelYearByNaturalIdentity(
		String programCode,
		Integer modelYear)
	throws 
		EntityDoesNotExistException,
		ValidationException {
		
		if (programCode == null || programCode.isEmpty()) {
			throw new ValidationException("programCode", "must be specified");
		}

		if (modelYear == null || modelYear.intValue() < 2000 || modelYear.intValue() > 2050) {
			throw new ValidationException("modelYear", "must be non-null and between 2000 and 2050");
		}
		
		String naturalIdentity = AbstractEntity.buildNaturalIdentity(
			programCode, 
			modelYear);
		
		ProgramModelYear programModelYear = (ProgramModelYear)getEntityByNaturalIdentityNullIfNotFound(naturalIdentity);
		if (programModelYear == null) {
			
			// As a last resort, see if we can load this entity from "testdata" on the file system.
			try {
				String json = this.loadTestData(programCode + "_" + modelYear);
				programModelYear = new ProgramModelYearJsonConverter().unmarshallFromJsonToEntity(json);
			} catch (Exception e) {
				LOGGER.error("Could not load from testdata area as file does not exist", e);
				programModelYear = null;
			}
		}
		
		if (programModelYear == null) {
			throw new EntityDoesNotExistException("ProgramModelYear: [" + naturalIdentity + "] does not exist.");
		}
		
		return programModelYear;
	}

	public AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity) {

		if (naturalIdentity == null || naturalIdentity.trim().isEmpty()) {
			throw new FenixRuntimeException("naturalIdentity must be specified.");
		}
		
		ProgramModelYear programModelYear = this.programModelYearMap.get(naturalIdentity);
		return programModelYear;
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		
		if (entity instanceof ProgramModelYear == false) {
			throw new FenixRuntimeException("entity must be an instance of ProgramModelYear, but was instead: [" + entity.getClassAndIdentity() + "]."); 
		}
		this.programModelYearMap.put(entity.getNaturalIdentity(), (ProgramModelYear)entity);
		return entity;
	}
	
	public AbstractEntity deleteEntity(AbstractEntity entity) {

		if (entity instanceof ProgramModelYear == false) {
			throw new FenixRuntimeException("entity must be an instance of ProgramModelYear, but was instead: [" + entity.getClassAndIdentity() + "]."); 
		}
		return this.programModelYearMap.remove(entity.getNaturalIdentity());
	}
	
	public String loadTestData(String filename) {
		return super.loadTestData("/orfin/program/" + filename);
	}
}
