/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.exception;

import com.djt.cvpp.ota.common.exception.impl.ExceptionIdentityImpl;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public final class EntityDoesNotExistException extends FenixCheckedException {
	
	/** */
	private static final long serialVersionUID = 1L;

	
	/** Used for ConnectedX logging */
	public static final String UNIQUE_ERROR_CODE = "CORE-0003";
	private final ExceptionIdentityImpl delegate = new ExceptionIdentityImpl(UNIQUE_ERROR_CODE);

	
	/**
	 * It is assumed that the message contains the domain object's natural identity.
	 * 
	 * @param message
	 */
	public EntityDoesNotExistException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param boundedContextName
	 * @param serviceName
	 * @param uniqueErrorCode
	 */
	public EntityDoesNotExistException(
		String message,
		String boundedContextName,
		String serviceName,
		String uniqueErrorCode) {
		super(message);
		this.delegate.setBoundedContextName(boundedContextName);
		this.delegate.setServiceName(serviceName);
		this.delegate.setUniqueErrorCode(uniqueErrorCode);
	}
	
	/**
	 * It is assumed that the message contains the domain object's natural identity.
	 * 
	 * @param message
	 * @param throwable
	 */
	public EntityDoesNotExistException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	@Override
	public String getBoundedContextName() {
		return this.delegate.getBoundedContextName();
	}
	
	@Override
	public void setBoundedContextName(String boundedContextName) {
		this.delegate.setBoundedContextName(boundedContextName);
	}
	
	@Override
	public String getServiceName() {
		return this.delegate.getServiceName();
	}
	
	@Override
	public void setServiceName(String serviceName) {
		this.delegate.setServiceName(serviceName);
	}
	
	@Override
	public String getUniqueErrorCode() {
		return this.delegate.getUniqueErrorCode();
	}
	
	@Override
	public void setUniqueErrorCode(String uniqueErrorCode) {
		this.delegate.setUniqueErrorCode(uniqueErrorCode);
	}
	
	@Override
	public void setMessageOverride(String messageOverride) {
		this.delegate.setMessageOverride(messageOverride);
	}
	
	@Override
	public String getMessageOverride() {
		return this.delegate.getMessageOverride();
	}
}
