/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.exception;

import java.util.ArrayList;
import java.util.List;

import com.djt.cvpp.ota.common.exception.impl.ExceptionIdentityImpl;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public final class ValidationListException extends FenixCheckedException {

    private static final long serialVersionUID = 1L;

    
	/** Used for ConnectedX logging */
    public static final String UNIQUE_ERROR_CODE = "CORE-1004";
	private final ExceptionIdentityImpl delegate = new ExceptionIdentityImpl(UNIQUE_ERROR_CODE);
    
    
    private List<String> validationMessages = new ArrayList<>();

    public ValidationListException(String message, String validationMessage) {
        super(message);
        this.validationMessages.add(validationMessage);
    }
    
    public ValidationListException(String message, List<String> validationMessages) {
        super(message);
        this.validationMessages = validationMessages;
    }
    
    public List<String> getValidationMessages() {
    	return this.validationMessages;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
    	
    	StringBuilder sb = new StringBuilder();
    	String boundedContextName = this.getBoundedContextName();
    	if (boundedContextName != null) {
        	sb.append(boundedContextName);
        	sb.append(DASH);
    	}
    	
    	String serviceName = this.getServiceName();
    	if (serviceName != null) {
        	sb.append(serviceName);
        	sb.append(DASH);
    	}
    	sb.append(this.getUniqueErrorCode());
    	sb.append(SEMICOLON);
    	
    	String messageOverride = this.getMessageOverride();
    	if (messageOverride != null) {
    		sb.append(messageOverride);
    	} else {
    		sb.append(super.getMessage());	
    	}
    	
    	sb.append(", validationMessages: ");
    	sb.append(this.validationMessages);
    	
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
