/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.model;

import java.util.List;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class EcgSignal extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	// TODO: TDM: Make the relationship between Odl and EcgSignal be many-to-many, with EcgSignal being immutable (all it has is just a signal name for identity)
	private Odl parentOdl;
	
	private String ecgSignalName;
	
	private EcgSignal(EcgSignalBuilder builder) throws EntityAlreadyExistsException {
		
		this.parentOdl = builder.parentOdl;
		this.parentOdl.addEcgSignal(this);
		this.ecgSignalName = builder.ecgSignalName;
	}
	
	public String getNaturalIdentity() {

		return AbstractEntity.buildNaturalIdentity(this.parentOdl, this.ecgSignalName);
	}

	public void validate(List<String> validationMessages) {

		if (parentOdl == null) {
			validationMessages.add("parentOdl must be specified.");
		}
		
		if (ecgSignalName == null) {
			validationMessages.add("ecgSignalName must be specified.");
		}
	}

	public Odl getParentOdl() {
		return parentOdl;
	}

	public void setParentOdl(Odl parentOdl) {
		this.parentOdl = parentOdl;
	}

	public String getEcgSignalName() {
		return ecgSignalName;
	}

	public void setEcgSignalName(String ecgSignalName) {
		this.ecgSignalName = ecgSignalName;
	}

	public static final class EcgSignalBuilder {

		private Odl parentOdl;
		private String ecgSignalName;

		public EcgSignalBuilder() {
		}

		public EcgSignalBuilder withParentOdl(Odl parentOdl) {
			this.parentOdl = parentOdl;
			return this;
		}
		
		public EcgSignalBuilder withEcgSignalName(String ecgSignalName) {
			this.ecgSignalName = ecgSignalName;
			return this;
		}

		public EcgSignal build() throws EntityAlreadyExistsException {
			return new EcgSignal(this);
		}
	}	
}
