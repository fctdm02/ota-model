/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.model.enums;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public enum ProgrammingMethodType {

	/** Programming is done by the ECU itself */
	LOCAL {
		public String toString() {
			return "Local";
		}
	}, 

	/** Programming is done via SFTP */
	ETHERNET_SFTP {
		public String toString() {
			return "Ethernet-SFTP";
		}
	},  
	
	/** Programming is done via the ECG over Ethernet */
	ETHERNET_OVTP {
		public String toString() {
			return "Ethernet-OVTP";
		}
	},  

	/** Programming is done via ECG over CAN */
	CAN_OVTP {
		public String toString() {
			return "CAN-OVTP";
		}
	},  
	
	/** Programming is done via ECG over CAN using UDS. Used for and data that needs to be transferred via diagnostic protocol */
	CAN_UDS {
		public String toString() {
			return "CAN-UDS";
		}
	},  
	
	/** Programming is done via ECG over CANFD */
	CANFD_OVTP {
		public String toString() {
			return "CANFD-OVTP";
		}
	},  
	
	/** Programming is done via ECG over CANFD using UDS. Used for and data that needs to be transferred via diagnostic protocol */
	CANFD_UDS {
		public String toString() {
			return "CANFD-UDS";
		}
	}
}
