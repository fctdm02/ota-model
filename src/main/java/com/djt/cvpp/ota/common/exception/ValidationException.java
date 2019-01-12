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
public final class ValidationException extends FenixCheckedException {
	
	/** */
	private static final long serialVersionUID = 1L;
	
	
	/** Used for ConnectedX logging */
	public static final String UNIQUE_ERROR_CODE = "CORE-1001";
	private final ExceptionIdentityImpl delegate = new ExceptionIdentityImpl(UNIQUE_ERROR_CODE);

	
	/* */
	private String attributeName;
	
	/* */
	private String reason;

	/**
	 * 
	 * @param message
	 */
	public ValidationException(String message) {
		super(message);
	}

	/**
	 * This signature is for when the natural identity itself is null/empty
	 * 
	 * @param attributeName
	 * @param reason
	 */
	public ValidationException(String attributeName, String reason) {
		super("");
		this.attributeName = attributeName;
		this.reason = reason;
	}

	/**
	 * 
	 * @param attributeName
	 * @param reason
	 * @param boundedContextName
	 * @param serviceName
	 * @param uniqueErrorCode
	 */
	public ValidationException(
		String attributeName, 
		String reason,
		String boundedContextName,
		String serviceName,
		String uniqueErrorCode) {
		super("");
		this.attributeName = attributeName;
		this.reason = reason;
		this.delegate.setBoundedContextName(boundedContextName);
		this.delegate.setServiceName(serviceName);
		this.delegate.setUniqueErrorCode(uniqueErrorCode);
	}
	
	/**
	 * 
	 * @param attributeName
	 * @param reason
	 * @param throwable
	 */
	public ValidationException(String attributeName, String reason, Throwable throwable) {
		super("", throwable);
		this.attributeName = attributeName;
		this.reason = reason;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
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
    	
    	if (this.attributeName != null) {
    		sb.append(this.attributeName).append(": ");
    	}
    	
    	String messageOverride = this.delegate.getMessageOverride();
    	if (messageOverride != null) {
    		sb.append(messageOverride);
    	} else {
    		sb.append(this.reason);	
    	}
    	return sb.toString();
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
