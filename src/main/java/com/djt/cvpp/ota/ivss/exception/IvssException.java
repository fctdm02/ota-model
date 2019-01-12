/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.ivss.exception;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class IvssException extends Exception {
	
	/** */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 
	 * @param message
	 */
	public IvssException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public IvssException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
