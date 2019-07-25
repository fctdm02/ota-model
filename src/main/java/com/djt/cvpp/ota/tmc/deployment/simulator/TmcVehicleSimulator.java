/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.deployment.simulator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.flare.rollout.model.Campaign;
import com.djt.cvpp.ota.flare.rollout.model.CampaignBatch;
import com.djt.cvpp.ota.tmc.vehiclediscovery.model.TmcVehicle;
import com.djt.cvpp.ota.tmc.vehiclestatus.event.TmcVehicleStatusMessageEvent;
import com.djt.cvpp.ota.tmc.vehiclestatus.event.impl.MockTmcVehicleStatusMessageEventPublisher;
import com.djt.cvpp.ota.tmc.vehiclestatus.model.TmcVehicleStatusMessage;

/**
 *
 * <pre>
		1. Any status message with [Any string]_ 'S'[any integers](_[variableinformation])n would be mapped to 'Deploying' TMC state
		2. Status message which is specifically OTAM_S1010(_[variable information])* will be mapped to 'Deployment succeeded' TMC state
		3. Any status message with [Any string]_'E'[any integers](_[variableinformation])n will be mapped to an TMC error state

 		OTAM_1001 (Trigger received)
		OTAM_1006 (Download start)
		OTAM_1007 (Ready to activate)
		OTAM_1008 (Activation complete)
		OTAM_1010 (Campaign finished)
		OTAM_1011 (Campaign status change)
		OTAM_1015 (Master reset)
		
		
		OTAM,S,1001,num_num_num,triggerType_authorizationLevel_expirationTimeinHours,OTAM_S1001_1_2_720
		OTAM,S,1002,num_bool,consentResourceCode_result,OTAM_S1002_2_true
		OTAM,S,1003,num_num,responseType_responsetimeInMilliseconds,OTAM_S1003_2_40
		OTAM,S,1004,(str_str_str)+,(ecu!N!_filename!N!_softwarePartNumberOrSoftwareVersion!N!)+,OTAM_S1004_SYNC_fmel.tar.gz_14G43-23495-AA_ABS_efgh.vbf_22222-22222-BB
		OTAM,S,1005,num,combinationOfAdditionalFiles,OTAM_S1005_4
		OTAM,S,1006,(str_num)+,(filename!N!_fileLength!N!InBytes)+,OTAM_S1006_abcd.vbf_2032_fmel.tar.gz_23498239522
		OTAM,S,1007,num_num_num,numberOfSuccessFiles_numberOfSkipFiles_totalNumberOfFiles,OTAM_S1007_4_4_8
		OTAM,S,1008,num_num_num,numberOfSuccessFiles_numberOfSkipFiles_totalNumberOfFiles,OTAM_S1008_7_0_8
		OTAM,S,1009,num_num_num,numberOfSuccessFiles_numberOfSkipFiles_totalNumberOfFiles,OTAM_S1009_1_0_1
		OTAM,S,1010,num_num_num_num_num,timeForTriggerCampaignInSecs_timeforPostVilInSecs_timeForInstallationInSecs_timeForActivationRollbackInSecs_otaTotalWakeupTimeInSecs,OTAM_S1010_100_234_456_234_2969
		OTAM,S,1011,num_num,type_reason,OTAM_S1011_1_34
		OTAM,S,1012,str,scheduleTime,OTAM_S1012_1521761482142
		OTAM,S,1013,num,notificationSettings,OTAM_S1013_2
		OTAM,S,1014,num,consentSetting,OTAM_S1014_3
		OTAM,S,1015
		
		
		OTAM,E,1001,num,reasonCode,OTAM_E1001_1
		
 * </pre> 
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TmcVehicleSimulator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Campaign.class);

	// The following are on a scale from 0.0 to 1.0, with 0.0 meaning 0%, 1.0 meaning 100% and 0.5 meaning 50%
	private static double DEFAULT_EXPECTED_PERCENTAGE_SUCCESS = 1.0;
	private static double DEFAULT_EXPECTED_PERCENTAGE_FAILURE = 0.0;
	private static double DEFAULT_EXPECTED_PERCENTAGE_OFFLINE = 0.0;
	
	private static double EXPECTED_PERCENTAGE_SUCCESS = DEFAULT_EXPECTED_PERCENTAGE_SUCCESS;
	private static double EXPECTED_PERCENTAGE_FAILURE = DEFAULT_EXPECTED_PERCENTAGE_FAILURE;
	private static double EXPECTED_PERCENTAGE_OFFLINE = DEFAULT_EXPECTED_PERCENTAGE_OFFLINE;
	
	public static void setVehicleBehaviorExpectations(
		double expectedPercentageSuccess,
		double expectedPercentageFailure,
		double expectedPercentageOffline) {
		
		assertValidPercentage(expectedPercentageSuccess);
		assertValidPercentage(expectedPercentageFailure);
		assertValidPercentage(expectedPercentageOffline);
		
		if ((expectedPercentageSuccess + expectedPercentageFailure + expectedPercentageOffline) != 1.0) {
			throw new RuntimeException("expectedPercentageSuccess: " 
				+ expectedPercentageSuccess 
				+ ", expectedPercentageFailure: " 
				+ expectedPercentageFailure 
				+ ", expectedPercentageOffline: " 
				+ expectedPercentageOffline 
				+ " must add up to 1.0, but was: " 
				+ (expectedPercentageSuccess + expectedPercentageFailure + expectedPercentageOffline));	
		}
		
		EXPECTED_PERCENTAGE_SUCCESS = expectedPercentageSuccess;
		EXPECTED_PERCENTAGE_FAILURE = expectedPercentageFailure;
		EXPECTED_PERCENTAGE_OFFLINE = expectedPercentageOffline;
	}
	
	public static void assertValidPercentage(double percentage) {
		if (percentage < 0.0 || percentage > 1.0) {
			throw new RuntimeException("Number representing a percentage must be between 0.0 and 1.0, but was: " + percentage);
		}
	}

	public static void resetVehicleBehaviorExpectations() {
			
		EXPECTED_PERCENTAGE_SUCCESS = DEFAULT_EXPECTED_PERCENTAGE_SUCCESS;
		EXPECTED_PERCENTAGE_FAILURE = DEFAULT_EXPECTED_PERCENTAGE_FAILURE;
		EXPECTED_PERCENTAGE_OFFLINE = DEFAULT_EXPECTED_PERCENTAGE_OFFLINE;
	}

	
	public static double getExpectedPercentageSuccess() {
		return EXPECTED_PERCENTAGE_SUCCESS;
	}
	public static double getExpectedPercentageFailure() {
		return EXPECTED_PERCENTAGE_FAILURE;
	}
	public static double getExpectedPercentageOffline() {
		return EXPECTED_PERCENTAGE_OFFLINE;
	}

	
	public static int getExpectedNumberSuccess(int numberOfVehicles) {
		return (int)(EXPECTED_PERCENTAGE_SUCCESS * numberOfVehicles);
	}
	public static int getExpectedNumberFailure(int numberOfVehicles) {
		return (int)(EXPECTED_PERCENTAGE_FAILURE * numberOfVehicles);
	}
	public static int getExpectedNumberOffline(int numberOfVehicles) {
		return (int)(EXPECTED_PERCENTAGE_OFFLINE * numberOfVehicles);
	}
	
	private static Boolean SEND_FAILURE_MESSAGES = null; 
	private static Boolean IS_OFFLINE = null;

	public static void setSendFailureMessages(Boolean b) {
		SEND_FAILURE_MESSAGES = b;	
	}
	public static void setIsOffline(Boolean b) {
		IS_OFFLINE = b;
	}
	public static Boolean getSendFailureMessages() {
		return SEND_FAILURE_MESSAGES;
	}
	public static Boolean getIsOffline() {
		return IS_OFFLINE;
	}
	
	
	private static final String OTAM_S1001 = "OTAM_S1001_1_2_720";
	private static final String OTAM_S1006 = "OTAM_S1006_abcd.vbf_2032_fmel.tar.gz_23498239522";
	private static final String OTAM_S1007 = "OTAM_S1007_4_4_8";
	private static final String OTAM_S1008 = "OTAM_S1008_7_0_8";
	private static final String OTAM_S1010 = "OTAM_S1010_100_234_456_234_2969"; 
	private static final List<String> SUCCESSFUL_STATUS_MESSAGES = new ArrayList<>();
	static {
		SUCCESSFUL_STATUS_MESSAGES.add(OTAM_S1001);
		SUCCESSFUL_STATUS_MESSAGES.add(OTAM_S1006);
		SUCCESSFUL_STATUS_MESSAGES.add(OTAM_S1007);
		SUCCESSFUL_STATUS_MESSAGES.add(OTAM_S1008);
		SUCCESSFUL_STATUS_MESSAGES.add(OTAM_S1010);
	}

	private static final String OTAM_E1001 = "OTAM_E1001_12345";	
	private static final List<String> FAILURE_STATUS_MESSAGES = new ArrayList<>();
	static {
		FAILURE_STATUS_MESSAGES.add(OTAM_E1001);
	}
	
	public static void processUpdate(
		CampaignBatch campaignBatch, 
		TmcVehicle tmcVehicle, 
		Boolean sendFailureMessages, 
		Boolean isOffline) throws EntityDoesNotExistException, ValidationException {
		
		if (isOffline.equals(Boolean.FALSE)) {
			
			List<String> statusMessagesToSend = null; 
			if (sendFailureMessages.equals(Boolean.TRUE)) {
				statusMessagesToSend = FAILURE_STATUS_MESSAGES;
			} else {
				statusMessagesToSend = SUCCESSFUL_STATUS_MESSAGES;
			}
			
			LOGGER.debug("VehicleSimulator::processMessages(): starting processing vehicle status messages for vehicle: [{}], sendFailureMessages: [{}], isOffline: [{}]", tmcVehicle, sendFailureMessages, isOffline);
			for (int i=0; i < statusMessagesToSend.size(); i++) {
				
				String deploymentId = campaignBatch.getTmcDeploymentId();
				String vehicleId = tmcVehicle.getUuid().toString();
				Timestamp timestamp = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
				String vehicleStatusMessageExpression = statusMessagesToSend.get(i);
				TmcVehicleStatusMessage tmcVehicleStatusMessage = new TmcVehicleStatusMessage(
					deploymentId,
					vehicleId, 
					timestamp,
					vehicleStatusMessageExpression);
				
				String owner = "tmc";
				TmcVehicleStatusMessageEvent tmcVehicleStatusMessageEvent = new TmcVehicleStatusMessageEvent(owner, tmcVehicleStatusMessage); 
				
				MockTmcVehicleStatusMessageEventPublisher.getInstance().publishTmcVehicleStatusMessageEvent(tmcVehicleStatusMessageEvent);
			}
		}
		LOGGER.debug("VehicleSimulator::processMessages(): finished processing vehicle status messages for vehicle: [{}].", tmcVehicle);
	}
}
