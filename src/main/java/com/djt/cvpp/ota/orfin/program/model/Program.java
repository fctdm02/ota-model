/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.program.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class Program extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	
	private String programCode;
	
	private Program(ProgramBuilder builder) {
		
		this.programCode = builder.programCode;
	}
	
	public String getNaturalIdentity() {

		return this.programCode;
	}

	public void validate(List<String> validationMessages) {

		if (programCode == null || programCode.trim().isEmpty()) {
			validationMessages.add("programCode must be specified.");
		}
	}

	public String getProgramCode() {
		return this.programCode;
	}
	
	public static final class ProgramBuilder {

		private String programCode;

		public ProgramBuilder() {
		}
		
		public ProgramBuilder withProgramCode(String programCode) {
			this.programCode = programCode;
			return this;
		}
		
		public Program build() {
			return new Program(this);
		}
	}
}
