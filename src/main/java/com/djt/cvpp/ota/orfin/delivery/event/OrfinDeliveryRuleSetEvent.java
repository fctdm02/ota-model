/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.delivery.event;

import com.djt.cvpp.ota.common.event.AbstractEvent;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class OrfinDeliveryRuleSetEvent extends AbstractEvent {
	
	private static final long serialVersionUID = -5077555245017589051L;
	
	public static final String SOFTWARE_UPDATE = "SOFTWARE_UPDATE";
	public static final String APPLICATION_UPDATE = "APPLICATION_UPDATE";
	public static final String ADD_APPLICATION = "ADD_APPLICATION";
	public static final String REMOVE_APPLICATION_UPDATE = "REMOVE_APPLICATION_UPDATE";
	public static final String DIRECT_CONFIGURATION_ONLY_UPDATE = "DIRECT_CONFIGURATION_ONLY_UPDATE";
	public static final String SOFTWARE_WITH_DIRECT_CONFIGURATION_UPDATE = "SOFTWARE_WITH_DIRECT_CONFIGURATION_UPDATE";
	public static final String SOFTWARE_SUSPEND = "SOFTWARE_SUSPEND";
	
	private String updateAction;
	private String deliveryRuleSetName;
	private String nodeAcronym;
	private String nodeAddress;

	public OrfinDeliveryRuleSetEvent(
		String owner, 
		String updateAction,
		String deliveryRuleSetName,
		String nodeAcronym,
		String nodeAddress) {
		super(owner);

		if (!updateAction.equals(SOFTWARE_UPDATE)
			&& !updateAction.equals(APPLICATION_UPDATE)
			&& !updateAction.equals(ADD_APPLICATION)
			&& !updateAction.equals(REMOVE_APPLICATION_UPDATE)
			&& !updateAction.equals(DIRECT_CONFIGURATION_ONLY_UPDATE)
			&& !updateAction.equals(SOFTWARE_WITH_DIRECT_CONFIGURATION_UPDATE)
			&& !updateAction.equals(SOFTWARE_SUSPEND)) {
			
			throw new FenixRuntimeException("updateAction must be one of: [SOFTWARE_UPDATE, APPLICATION_UPDATE, ADD_APPLICATION, REMOVE_APPLICATION_UPDATE, DIRECT_CONFIGURATION_ONLY_UPDATE, SOFTWARE_WITH_DIRECT_CONFIGURATION_UPDATE or SOFTWARE_SUSPEND].");
		}
		
		this.updateAction = updateAction;
		this.deliveryRuleSetName = deliveryRuleSetName;
	}

	public String getUpdateAction() {
		return updateAction;
	}

	public String getDeliveryRuleSetName() {
		return deliveryRuleSetName;
	}
	
	public String getNodeAcronym() {
		return nodeAcronym;
	}

	public String getNodeAddress() {
		return nodeAddress;
	}
	
    public String getPayload() {
    	return getDeliveryRuleSetName();
    }
}
