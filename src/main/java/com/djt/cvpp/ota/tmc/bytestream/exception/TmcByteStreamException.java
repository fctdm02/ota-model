/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.bytestream.exception;

import com.djt.cvpp.ota.tmc.common.exception.TmcException;
import com.djt.cvpp.ota.tmc.common.model.TmcEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class TmcByteStreamException extends TmcException {
	
	/** */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 
	 * @param message
	 * @param tmcEntity
	 */
	public TmcByteStreamException(String message, TmcEntity tmcEntity) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param tmcEntity
	 * @param throwable
	 */
	public TmcByteStreamException(String message, TmcEntity tmcEntity, Throwable throwable) {
		super(message, throwable);
	}
	
	/**
	 * 
	 * @param message
	 */
	public TmcByteStreamException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public TmcByteStreamException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
