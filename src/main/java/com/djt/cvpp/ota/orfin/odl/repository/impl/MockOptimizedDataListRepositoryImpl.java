/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.repository.impl;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.djt.cvpp.ota.orfin.odl.mapper.OdlJsonConverter;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.odl.repository.OptimizedDataListRepository;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class MockOptimizedDataListRepositoryImpl extends AbstractMockRepository implements OptimizedDataListRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MockOptimizedDataListRepositoryImpl.class);
	

	private Map<String, Odl> odlMap = new TreeMap<>();

	private static OptimizedDataListRepository INSTANCE = new MockOptimizedDataListRepositoryImpl();
	public static OptimizedDataListRepository getInstance() {
		return INSTANCE;
	}

	private MockOptimizedDataListRepositoryImpl() {
	}
	
	public void reset() {
		this.odlMap.clear();
	}
	
	public Odl createOdl(
		String odlName)
	throws 
		EntityAlreadyExistsException,
		ValidationException {
		
		Odl odlCheck = (Odl)getEntityByNaturalIdentityNullIfNotFound(odlName);
		if (odlCheck != null) {
			
			throw new EntityAlreadyExistsException("Cannot create odl with name: [" + odlName + "] because it already exists");
		}
		
		Odl odl = new Odl
			.OdlBuilder()
			.withOdlName(odlName)
			.build();
		
		this.odlMap.put(odl.getNaturalIdentity(), odl);
		
		return odl;
	}
		
	public List<Odl> getAllOdls() {
		
		List<Odl> list = new ArrayList<>();
		list.addAll(this.odlMap.values());
		return list;
	}
	
	public Odl getOdlByName(
        String odlName)
	throws 
		EntityDoesNotExistException {
		
		Odl odl = (Odl)getEntityByNaturalIdentityNullIfNotFound(odlName);
		if (odl == null) {
			
			// As a last resort, see if we can load this entity from "testdata" on the file system.
			try {
				String json = this.loadTestData(odlName);
				odl = new OdlJsonConverter().unmarshallFromJsonToEntity(json);
			} catch (Exception e) {
				LOGGER.error("Could not load from testdata area as file does not exist", e);
				odl = null;
			}
		}
		
		if (odl == null) {
			throw new EntityDoesNotExistException("Odl with name: [" + odlName + "] does not exist.");
		}
		return odl;
	}
	
	public Odl getOdlByProgramCodeAndModelYear(
        String programCode,
        Integer modelYear)
	throws 
		EntityDoesNotExistException {
		
		Iterator<Odl> odlIterator = this.odlMap.values().iterator();
		while (odlIterator.hasNext()) {
			
			Odl odl = odlIterator.next();
			Iterator<ProgramModelYear> programModelYearIterator = odl.getProgramModelYears().iterator();
			while (programModelYearIterator.hasNext()) {
				
				ProgramModelYear programModelYear = programModelYearIterator.next();
				if (programModelYear.getParentProgram().getProgramCode().equals(programCode) && programModelYear.getParentModelYear().getModelYear().equals(modelYear)) {
					return odl;
				}
			}
		}
		throw new EntityDoesNotExistException("Odl with programCodeModelYear: [" + programCode + modelYear + "] does not exist.");
	}	
		
	public Odl renameOdl(
		String oldOdlName, 
		String newOdlName)
	throws 
		EntityDoesNotExistException,
		EntityAlreadyExistsException {
		
		Odl odlCheck = (Odl)getEntityByNaturalIdentityNullIfNotFound(newOdlName);
		if (odlCheck != null) {
			throw new EntityAlreadyExistsException("Odl with name: [" + newOdlName + "] already exists.");			
		}
		
		Odl odl = (Odl)getEntityByNaturalIdentityNullIfNotFound(oldOdlName);
		if (odl == null) {
			throw new EntityDoesNotExistException("Odl with name: [" + oldOdlName + "] does not exist.");			
		}
		
		odl.setOdlName(newOdlName);
		
		this.odlMap.put(oldOdlName, null);
		this.odlMap.put(newOdlName, odl);
		
		return odl;
	}

	public AbstractEntity getEntityByNaturalIdentityNullIfNotFound(String naturalIdentity) {
		
		if (naturalIdentity == null || naturalIdentity.trim().isEmpty()) {
			throw new FenixRuntimeException("naturalIdentity must be specified.");
		}
		
		Odl odl = this.odlMap.get(naturalIdentity);
		return odl;
	}
	
	public AbstractEntity updateEntity(AbstractEntity entity) throws ValidationException {
		
		if (entity instanceof Odl == false) {
			throw new RuntimeException("Expected an instance of Odl, but instead was: " + entity.getClassAndIdentity());
		}
		
		this.odlMap.put(entity.getNaturalIdentity(), (Odl)entity);
		return entity;
	}
	
	public AbstractEntity deleteEntity(AbstractEntity entity) {
		
		if (entity instanceof Odl == false) {
			throw new RuntimeException("Expected an instance of Odl, but instead was: " + entity.getClassAndIdentity());
		}
		
		return this.odlMap.remove(entity.getNaturalIdentity());
	}
	
	public String loadTestData(String filename) {
		return super.loadTestData("/orfin/odl/" + filename);
	}
}
