/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.exception;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VadrException extends Exception {
	
	/** */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 
	 * @param message
	 */
	public VadrException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public VadrException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
