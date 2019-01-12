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
public enum SoftwareCategory {
    BINARY_APPLICATION {
		public String toString() {
			return "BINARY_APPLICATION";
		}
	},
    
    CALIBRATION {
		public String toString() {
			return "CALIBRATION";
		}
	},
    
    ECU_CONFIG {
		public String toString() {
			return "ECU_CONFIG";
		}
	},
    
    IMAGE {
		public String toString() {
			return "IMAGE";
		}
	},
    
    LICENSE {
		public String toString() {
			return "LICENSE";
		}
	},
    
    OTHER {
		public String toString() {
			return "OTHER";
		}
	},
    
    PRIMARY_BOOTLOADER {
		public String toString() {
			return "PRIMARY_BOOTLOADER";
		}
	},
    
    SCRIPTING_APPLICATION {
		public String toString() {
			return "SCRIPTING_APPLICATION";
		}
	},
    
    SECONDARY_BOOTLOADER {
		public String toString() {
			return "SECONDARY_BOOTLOADER";
		}
	},
    
    SIGNAL_CONFIGURATION {
		public String toString() {
			return "SIGNAL_CONFIGURATION";
		}
	},
    
    STRATEGY {
		public String toString() {
			return "STRATEGY";
		}
	},
    
    UNINSTALLER {
		public String toString() {
			return "UNINSTALLER";
		}
	},
    
    UNLOCKER {
		public String toString() {
			return "UNLOCKER";
		}
	}
}
