/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.deployment.exception;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TmcDeploymentException extends Exception {
	
	/** */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 
	 * @param message
	 */
	public TmcDeploymentException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public TmcDeploymentException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
