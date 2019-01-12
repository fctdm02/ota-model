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
public enum ProductionState {
    DEVELOPMENT {
		public String toString() {
			return "DEVELOPMENT";
		}
	},
    
    PROTOTYPE {
		public String toString() {
			return "PROTOTYPE";
		}
	},
    
    EXPERIMENTAL {
		public String toString() {
			return "EXPERIMENTAL";
		}
	},
    
    PRODUCTION {
		public String toString() {
			return "PRODUCTION";
		}
	}
}
