/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.policy.model.enums;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public enum OtaFunction {

	// From the example OTA spreadsheet in the specs.
	OTA_MANAGER,
	OTA_MANAGER_HMI,
	OTA_STATUS_MANAGER,
	DOWNLOAD_MANAGER,
	IVSU_TRIGGER,
	VEHICLE_OTA_POLICY,

	// From MVP TmcVehicle Status Manager
	OTAM,
	OAEC,
	OAES,
	OAOCC,
	OAOCS,
	OTAH,
	VSM,
	VIL,
	OTATGR,
	DRTCFG,
	IOSF,
	DWM,
	CAVC	
}
