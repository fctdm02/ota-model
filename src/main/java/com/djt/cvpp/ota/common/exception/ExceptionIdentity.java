/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.exception;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface ExceptionIdentity {
	
	String DASH = "-";
	String SEMICOLON = ": ";
	
	/**
	 * @return the bounded context name for the containing application/service
	 * e.g. FENIX-FLARE or ORFIN. This information would be used for ConnectedX logging.
	 */
	String getBoundedContextName();
	
	/**
	 * It is the responsibility of the developer to write an interceptor that would
	 * inject the bounded context name at runtime (e.g. FENIX-FLARE or ORFIN)
	 * 
	 * @param boundedContextName
	 */
	void setBoundedContextName(String boundedContextName);
	
	/**
	 * 
	 * @return the service name for the containing application/service
	 * e.g. FENIX-FLARE or ORFIN. This information would be used for ConnectedX logging.
	 */
	String getServiceName();
	
	/**
	 * 
	 * It is the responsibility of the developer to write an interceptor that would
	 * inject the service name at runtime (e.g. Rollout-Initiator)
	 * 
	 * @param serviceName
	 */
	void setServiceName(String serviceName);
	
	/**
	 * 
	 * @return A unique identifier for the exception that would be used for Connected X logging.
	 */
	String getUniqueErrorCode();
	
	/**
	 * 
	 * It is the responsibility of the developer to write an interceptor that would
	 * inject the unique error code at runtime (e.g. Rollout-Initiator)
	 * 
	 * @param uniqueErrorCode
	 */
	void setUniqueErrorCode(String uniqueErrorCode);
	
	/**
	 * This is to allow the caller (at a higher layer) to override the exception message to be more descriptive 
	 * (i.e. give operation name that caused exception)
	 * 
	 * @param messageOverride
	 */
	void setMessageOverride(String messageOverride);
	
	/**
	 * This is to allow the caller (at a higher layer) to override the exception message to be more descriptive 
	 * (i.e. give operation name that caused exception)
	 * 
	 * @return
	 */
	String getMessageOverride();	
}
