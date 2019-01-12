/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.common.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public class Tag extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	public final static String CAMPAIGN_ID = "campaignID";
    public final static String DEPLOYMENT_PROTOCOL = "deploymentProtocol";
    public final static String DEPLOYMENT_URL = "deploymentURL";
    public final static String DOMAIN = "domain";
    public final static String DOMAIN_INSTANCE = "domainInstance";
    public final static String DOMAIN_INSTANCE_VERSION = "domainInstanceVersion";
    public final static String PROGRAM_CODE = "programCode";
    public final static String MODEL_YEAR = "modelYear";
	
	
	private String name;
	private String value;
	
	protected Tag() {
	}
	
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			name,
			value
		);
	}

	public void validate(List<String> validationMessages) {

		if (name == null || name.trim().isEmpty()) {
			validationMessages.add("name must be specified.");
		}
		
		if (value == null || value.trim().isEmpty()) {
			validationMessages.add("value must be specified.");
		}
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
