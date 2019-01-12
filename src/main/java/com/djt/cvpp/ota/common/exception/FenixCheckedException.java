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
public abstract class FenixCheckedException extends Exception implements ExceptionIdentity {
	
	/** */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 
	 * @param message
	 */
	public FenixCheckedException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public FenixCheckedException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		
    	StringBuilder sb = new StringBuilder();
    	String boundedContextName = this.getBoundedContextName();
    	if (boundedContextName != null) {
        	sb.append(boundedContextName).append(DASH);
    	}
    	
    	String serviceName = this.getServiceName();
    	if (serviceName != null) {
        	sb.append(serviceName).append(DASH);
    	}
    	sb.append(this.getUniqueErrorCode()).append(SEMICOLON);
    	
    	String messageOverride = this.getMessageOverride();
    	if (messageOverride != null) {
    		sb.append(messageOverride);
    	} else {
    		sb.append(super.getMessage());	
    	}
    	
    	return sb.toString();
	}
}
