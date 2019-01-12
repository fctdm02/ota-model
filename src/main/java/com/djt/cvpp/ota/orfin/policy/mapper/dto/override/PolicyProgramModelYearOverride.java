/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper.dto.override;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.djt.cvpp.ota.orfin.program.mapper.dto.ProgramModelYear;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class PolicyProgramModelYearOverride extends AbstractPolicyOverride {

	private ProgramModelYear programModelYear;
}
