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
public enum ApplicationNecessity {
	
    MANDATORY {
		public String toString() {
			return "MANDATORY";
		}
	},
    
    OPTIONAL {
		public String toString() {
			return "OPTIONAL";
		}
	}
}
