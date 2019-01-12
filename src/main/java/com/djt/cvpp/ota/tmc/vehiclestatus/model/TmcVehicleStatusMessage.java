/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.vehiclestatus.model;

import java.sql.Timestamp;
import java.util.List;

import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public class TmcVehicleStatusMessage extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

	static final String UNDERSCORE = "_";
	
	
	private String deploymentId;
	private String vehicleId;
	private Timestamp timestamp;
	private String vehicleStatusMessageExpression;
	
	
	// The following are used for extracting the components and are not saved anywhere.
	private transient String otaFunction;
	private transient String prefix;
	private transient String lookupCode;
	
	
	public TmcVehicleStatusMessage(
		String deploymentId,	
		String vehicleId,
		Timestamp timestamp,
		String vehicleStatusMessageExpression) {
		
		if (deploymentId == null) {
			throw new IllegalStateException("deploymentId must be specified");
		}
		this.deploymentId = deploymentId;
		
		if (vehicleId == null) {
			throw new IllegalStateException("vehicleId must be specified");
		}
		this.vehicleId = vehicleId;
		
		if (timestamp == null) {
			throw new IllegalStateException("timestamp must be specified");
		}
		this.timestamp = new Timestamp(timestamp.getTime());
		
		if (vehicleStatusMessageExpression == null) {
			throw new IllegalStateException("vehicleStatusMessageExpression must be specified");
		}
		this.vehicleStatusMessageExpression = vehicleStatusMessageExpression;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			deploymentId,	
			vehicleId,
			timestamp
		);
	}

	public void validate(List<String> validationMessages) {

		this.validateNotNull(validationMessages, "deploymentId", this.deploymentId);
		this.validateNotNull(validationMessages, "vehicleId", this.vehicleId);
		this.validateNotNull(validationMessages, "timestamp", this.timestamp);
		this.validateNotNull(validationMessages, "vehicleStatusMessageExpression", this.vehicleStatusMessageExpression);
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public Timestamp getTimestamp() {
		return new Timestamp(timestamp.getTime());
	}

	public String getVehicleStatusMessageExpression() {
		return vehicleStatusMessageExpression;
	}
	
	
	// BUSINESS BEHAVIORS
	public String extractOtaFunction() {
		if (this.otaFunction == null) {
			parseTmcMessage();
		}
		return this.otaFunction;
	}
	
	public String extractPrefix() {
		if (this.prefix == null) {
			parseTmcMessage();
		}
		return this.prefix;
	}
	
	public String extractLookupCode() {
		if (this.lookupCode == null) {
			parseTmcMessage();
		}
		return this.lookupCode;
	}
		
	/*
	 * OTAM_S1010_100_234_456_234_2969
	 * 
	 * parses to:
	 * 
	 */
	private void parseTmcMessage() {
		
		String[] stringArray = this.vehicleStatusMessageExpression.split(UNDERSCORE);
		if (stringArray.length > 2) {
			
			this.otaFunction = stringArray[0];
			this.prefix = stringArray[1].substring(0, 1);
			this.lookupCode = stringArray[1].substring(1);
			
		} else {
			throw new FenixRuntimeException("vehicleStatusMessageExpression: [" + vehicleStatusMessageExpression + "] is malformed. It must be of the form: OTAM_S1010_x1_x2_xn, where x are status message specific additional parameters.");
		}
	}
}
