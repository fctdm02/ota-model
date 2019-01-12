/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.common.exception;

import com.djt.cvpp.ota.common.exception.FenixCheckedException;
import com.djt.cvpp.ota.tmc.common.model.TmcEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class TmcException extends FenixCheckedException {
	
	/** */
	private static final long serialVersionUID = 1L;

	/** This is specified when known */ 
	private TmcEntity tmcEntity;

	/**
	 * 
	 * @param message
	 * @param tmcEntity
	 */
	public TmcException(String message, TmcEntity tmcEntity) {
		super(message);
		this.tmcEntity = tmcEntity;
	}

	/**
	 * 
	 * @param message
	 * @param tmcEntity
	 * @param throwable
	 */
	public TmcException(String message, TmcEntity tmcEntity, Throwable throwable) {
		super(message, throwable);
		this.tmcEntity = tmcEntity;
	}
	
	/**
	 * 
	 * @param message
	 */
	public TmcException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public TmcException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
	
		if (this.tmcEntity != null) {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(super.getMessage());
	    	sb.append(" TMC Entity: [");
	    	sb.append(this.tmcEntity);
	    	sb.append("].");
	    	return sb.toString();
		} else {
			return super.getMessage();
		}
	}	
}
