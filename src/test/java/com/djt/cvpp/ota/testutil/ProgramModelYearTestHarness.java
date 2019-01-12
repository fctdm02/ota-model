package com.djt.cvpp.ota.testutil;

import com.djt.cvpp.ota.orfin.program.model.ModelYear;
import com.djt.cvpp.ota.orfin.program.model.Program;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

public class ProgramModelYearTestHarness {
	
	public ProgramModelYear buildProgramModelYear() {
		
		Program programEntity = new Program.ProgramBuilder()
			.withProgramCode("C344N")
			.build();  
		
		ModelYear modelYearEntity = new ModelYear
			.ModelYearBuilder()
			.withModelYearValue(Integer.valueOf("2017"))
			.build();
		
		ProgramModelYear programModelYearEntity = new ProgramModelYear
			.ProgramModelYearBuilder()
			.withParentProgram(programEntity)
			.withParentModelYear(modelYearEntity)
			.build();
		
		// TODO: TDM: Uncomment
		//programModelYearEntity.assertValid();

		return programModelYearEntity;		
	}
}
