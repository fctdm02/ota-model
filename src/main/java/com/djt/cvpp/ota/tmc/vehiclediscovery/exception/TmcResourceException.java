/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.exception;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TmcResourceException extends Exception {
	
	/** */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 
	 * @param message
	 */
	public TmcResourceException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public TmcResourceException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
