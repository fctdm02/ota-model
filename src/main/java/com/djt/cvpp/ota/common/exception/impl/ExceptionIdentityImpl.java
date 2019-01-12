/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.exception.impl;

import com.djt.cvpp.ota.common.exception.ExceptionIdentity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class ExceptionIdentityImpl implements ExceptionIdentity {
	
	private static String BOUNDED_CONTEXT_NAME;
	private static String SERVICE_NAME;

	private String boundedContextName;
	private String serviceName;
	private String uniqueErrorCode;
	private String messageOverride;

	
	public static final void setGlobalBoundedContextName(String boundedContextName) {
		BOUNDED_CONTEXT_NAME = boundedContextName;
	}
	public static final String getGlobalBoundedContextName() {
		return BOUNDED_CONTEXT_NAME;
	}
	
	public static final void setGlobalServiceName(String serviceName) {
		SERVICE_NAME = serviceName;
	}
	public static final String getGlobalServiceName() {
		return SERVICE_NAME;
	}

	
	public ExceptionIdentityImpl() {
	}
	
	public ExceptionIdentityImpl(String uniqueErrorCode) {
		this.uniqueErrorCode = uniqueErrorCode;
	}
	
	public String getBoundedContextName() {
		return this.boundedContextName;
	}
	
	public void setBoundedContextName(String boundedContextName) {
		this.boundedContextName = boundedContextName;
	}
	
	public String getServiceName() {
		return this.serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getUniqueErrorCode() {
		return this.uniqueErrorCode;
	}
	
	public void setUniqueErrorCode(String uniqueErrorCode) {
		this.uniqueErrorCode = uniqueErrorCode;
	}
	
	public void setMessageOverride(String messageOverride) {
		this.messageOverride = messageOverride;
	}
	
	public String getMessageOverride() {
		return this.messageOverride;
	}
}
