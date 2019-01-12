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
public enum SoftwareClassification {
    PRODUCTION {
		public String toString() {
			return "PRODUCTION";
		}
	},
    
    PROTOTYPE {
		public String toString() {
			return "PROTOTYPE";
		}
	},
    
    DEVELOPMENT {
		public String toString() {
			return "DEVELOPMENT";
		}
	},
    
    EXPERIMENTAL {
		public String toString() {
			return "EXPERIMENTAL";
		}
	}
}
