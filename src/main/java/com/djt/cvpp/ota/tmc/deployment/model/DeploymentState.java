/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.deployment.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class DeploymentState extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private String value;
	private Integer total;
	private Integer totalSuccess;
	
	public DeploymentState(
		String value,
		Integer total,
		Integer totalSuccess) {
		this.value = value;
		this.total = total;
		this.totalSuccess = totalSuccess;
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			value,
			total,
			totalSuccess
		);
	}

	public void validate(List<String> validationMessages) {

		if (value == null || value.trim().isEmpty()) {
			validationMessages.add("value must be specified.");
		}

		if (total == null || total.intValue() < 0) {
			validationMessages.add("total must be a positive integer");
		}
		
		if (totalSuccess == null || totalSuccess.intValue() < 0) {
			validationMessages.add("totalSuccess must be a positive integer");
		}
	}

	public String getValue() {
		return value;
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getTotalSuccess() {
		return totalSuccess;
	}	
}
