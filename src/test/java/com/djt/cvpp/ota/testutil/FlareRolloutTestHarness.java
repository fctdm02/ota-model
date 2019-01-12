package com.djt.cvpp.ota.testutil;

import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.flare.rollout.model.AbstractRollout;
import com.djt.cvpp.ota.flare.rollout.model.SoftwareUpdateRollout;

public class FlareRolloutTestHarness {
	
	public AbstractRollout buildRollout() throws ValidationException, EntityDoesNotExistException {

		String rolloutName = "rollout1";
		String deliveryRuleSetName = "deliveryRuleSet_MayHackathon-ECG";
		SoftwareUpdateRollout softwareUpdateRollout = new SoftwareUpdateRollout(rolloutName, deliveryRuleSetName);
		
		return softwareUpdateRollout;
	}
}
