/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.vadrevent.event;

import com.djt.cvpp.ota.common.event.AbstractEvent;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class OrfinVadrReleaseEvent extends AbstractEvent {
	
	private static final long serialVersionUID = -5077555245017589051L;

	
	private String domainName;
	private String domainInstanceName;
	private String domainInstanceDescription;
	private String domainInstanceVersion;
	private String appId;
	private String appVersion;
	private String productionState;
	private String releaseDate;
	private String softwarePriorityLevel;

	public OrfinVadrReleaseEvent(
		String owner,
		String domainName,
		String domainInstanceName,
		String domainInstanceDescription,
		String domainInstanceVersion,
		String appId,
		String appVersion,
		String productionState,
		String releaseDate,
		String softwarePriorityLevel) {
		super(owner);
		this.domainName = domainName;
		this.domainInstanceName = domainInstanceName;
		this.domainInstanceDescription = domainInstanceDescription;
		this.domainInstanceVersion = domainInstanceVersion;
		this.appId = appId;		
		this.appVersion = appVersion;
		this.productionState = productionState;		
		this.releaseDate = releaseDate;
		this.softwarePriorityLevel = softwarePriorityLevel;
	}

	public String getDomainName() {
		return domainName;
	}

	public String getDomainInstanceName() {
		return domainInstanceName;
	}

	public String getDomainInstanceDescription() {
		return domainInstanceDescription;
	}
	
	public String getDomainInstanceVersion() {
		return domainInstanceVersion;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public String getProductionState() {
		return productionState;
	}

	public String getReleaseDate() {
		return releaseDate;
	}
	
	public String getSoftwarePriorityLevel() {
		return softwarePriorityLevel;
	}
	
	public Object getPayload() {
		throw new RuntimeException("Not supported.");
	}
}
