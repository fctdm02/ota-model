/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.odl.event;

import com.djt.cvpp.ota.common.event.AbstractEvent;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class OrfinOdlEvent extends AbstractEvent {
	
	private static final long serialVersionUID = -5077555245017589051L;

	private String programCode;
	private Integer modelYear;
	private String odlName;

	public OrfinOdlEvent(
		String owner,
		String programCode,
		Integer modelYear,
		String odlName) {
		super(owner);
		this.programCode = programCode;
		this.modelYear = modelYear;
		this.odlName = odlName;
	}

	public String getProgramCode() {
		return programCode;
	}

	public Integer getModelYear() {
		return modelYear;
	}
	
    public String getOdlName() {
    	return this.odlName;
    }
    
	public String getPayload() {
		return odlName;
	}
}
