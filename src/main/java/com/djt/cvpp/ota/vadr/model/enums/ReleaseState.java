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
public enum ReleaseState {
    PRE_RELEASED {
		public String toString() {
			return "PRE_RELEASED";
		}
	},
    
    READY_FOR_RELEASE {
		public String toString() {
			return "READY_FOR_RELEASE";
		}
	},
    
    RELEASED_PENDING_UPLOAD {
		public String toString() {
			return "RELEASED_PENDING_UPLOAD";
		}
	},
    
    RELEASED {
		public String toString() {
			return "RELEASED";
		}
	}
}
