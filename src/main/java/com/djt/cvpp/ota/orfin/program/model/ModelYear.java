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
public class ModelYear extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	
	private Integer modelYearValue;
	
	private ModelYear(ModelYearBuilder builder) {
		
		this.modelYearValue = builder.modelYearValue;
	}
	
	public String getNaturalIdentity() {

		return modelYearValue.toString();
	}

	public void validate(List<String> validationMessages) {

		if (modelYearValue == null || modelYearValue.intValue() < 1950 || modelYearValue.intValue() > 2050) {
			validationMessages.add("modelYearValue must be specified and be between 1950-2050.");
		}
	}
	
	public Integer getModelYearValue() {
		return modelYearValue;
	}

	public Integer getModelYear() {
		return this.modelYearValue;
	}
	
	public static final class ModelYearBuilder {

		private Integer modelYearValue;

		public ModelYearBuilder() {
		}
		
		public ModelYearBuilder withModelYearValue(Integer modelYearValue) {
			this.modelYearValue = modelYearValue;
			return this;
		}
		
		public ModelYear build() {
			return new ModelYear(this);
		}
	}
}
