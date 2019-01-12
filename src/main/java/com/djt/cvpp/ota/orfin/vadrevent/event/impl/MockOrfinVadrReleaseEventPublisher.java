/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.vadrevent.event.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.vadrevent.event.OrfinVadrReleaseEvent;
import com.djt.cvpp.ota.orfin.vadrevent.event.OrfinVadrReleaseEventSubscriber;

/**
  *
  * @author tmyers1@yahoo.com (Tom Myers)
  *
  */
public class MockOrfinVadrReleaseEventPublisher extends AbstractOrfinVadrReleaseEventPublisher {
	
	public static final String VADR_RELEASE_EVENT_PREFIX = "VADR_RELEASE_EVENT_";
	public static final String VADR_RELEASE_EVENT_SUFFIX = ".txt";
	
	public static final String DOMAIN_NAME = "domainName";
	public static final String DOMAIN_INSTANCE_NAME = "domainInstanceName";
	public static final String DOMAIN_INSTANCE_DESCRIPTION = "domainInstanceDescription";
	public static final String DOMAIN_INSTANCE_VERSION = "domainInstanceVersion";
	public static final String APP_ID = "appId";
	public static final String APP_VERSION = "appVersion";
	public static final String PRODUCTION_STATE = "productionState";
	public static final String RELEASE_DATE = "releaseDate";
	public static final String SOFTWARE_PRIORITY_LEVEL = "softwarePriorityLevel";
	public static final String HAS_BEEN_PROCESSED = "hasBeenProcessed";
	
	private static MockOrfinVadrReleaseEventPublisher INSTANCE = new MockOrfinVadrReleaseEventPublisher();
	public static MockOrfinVadrReleaseEventPublisher getInstance() {
		return INSTANCE;
	}
	private MockOrfinVadrReleaseEventPublisher() {
		
		File dataDirectory = new File(System.getProperty("java.io.tmpdir") + "/testdata/orfin/vadrevent/");
		Set<File> ignoredFiles = new HashSet<>();
		FilenameFilter filter = new FilenameFilter() {
			@Override
		    public boolean accept(File dir, String name) {
		    	if (name.startsWith(VADR_RELEASE_EVENT_PREFIX) && name.endsWith(VADR_RELEASE_EVENT_SUFFIX)) {
		    		return true;	
		    	}
		    	return false;
		    }
		};
		
		Thread t = new Thread() {
			public void run() {
				
				while (true) {
					
					File[] fileArray = dataDirectory.listFiles(filter);
					if (fileArray != null && fileArray.length > 0) {
						for (int i=0; i < fileArray.length; i++) {
							
							File file = fileArray[i];
							if (!ignoredFiles.contains(file)) {

								OrfinVadrReleaseEvent vadrReleaseEvent = loadVadrReleaseEvent(file);
								if (vadrReleaseEvent != null) {
									Iterator<OrfinVadrReleaseEventSubscriber> iterator = subscribers.iterator();
									while (iterator.hasNext()) {
										
										OrfinVadrReleaseEventSubscriber subscriber = iterator.next();
										try {
											subscriber.handleOrfinVadrReleaseEvent(vadrReleaseEvent);
										} catch (ValidationException ve) {
											throw new FenixRuntimeException("Unable to handle VADR Release Event from file: [" + file.getAbsolutePath() + "], error: [" + ve.getMessage() + "].", ve);
										}
									}
									file.delete();
									ignoredFiles.add(file);
								}
							}
						}
					}
					
					// Sleep for 30 seconds
					try {
						Thread.sleep(30000);
					} catch (InterruptedException ie) {
						throw new FenixRuntimeException("Unable to sleep for 30 seconds, error: [" + ie.getMessage() + "].", ie);
					}
				}				
			}
		};
		t.start();
	}
	
	public void clearSubscribers() {
		this.subscribers.clear();
	}
	
	public static OrfinVadrReleaseEvent loadVadrReleaseEvent(File file) {
		
		OrfinVadrReleaseEvent orfinVadrReleaseEvent = null;
		Properties properties = new Properties();
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		try {
			
			inputStream = new FileInputStream(file);
			properties.load(inputStream);
			
			String owner = "tmyers28";
			String domainName = properties.getProperty(DOMAIN_NAME);
			String domainInstanceName = properties.getProperty(DOMAIN_INSTANCE_NAME);
			String domainInstanceDescription = properties.getProperty(DOMAIN_INSTANCE_DESCRIPTION);
			String domainInstanceVersion = properties.getProperty(DOMAIN_INSTANCE_VERSION);
			String appId = properties.getProperty(APP_ID);
			String appVersion = properties.getProperty(APP_VERSION);
			String productionState = properties.getProperty(PRODUCTION_STATE);
			String releaseDate = properties.getProperty(RELEASE_DATE);
			String softwarePriorityLevel = properties.getProperty(SOFTWARE_PRIORITY_LEVEL);
			String hasBeenProcessed = properties.getProperty(HAS_BEEN_PROCESSED);
			
			if (hasBeenProcessed.trim().equalsIgnoreCase("false")) {

				orfinVadrReleaseEvent = new OrfinVadrReleaseEvent(
					owner,
					domainName,
					domainInstanceName,
					domainInstanceDescription,
					domainInstanceVersion,
					appId,
					appVersion,
					productionState,
					releaseDate,
					softwarePriorityLevel);
			} else {
				properties.setProperty(HAS_BEEN_PROCESSED, "true");
				
				outputStream = new FileOutputStream(file);
				properties.store(outputStream, null);
			}
			
			return orfinVadrReleaseEvent;
			
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to load VADR Release Event from file: [" + file.getAbsolutePath() + "], error: [" + ioe.getMessage() + "].", ioe);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ioe) {
					throw new FenixRuntimeException("Unable to close VADR Release Event input stream: [" + file.getAbsolutePath() + "], error: [" + ioe.getMessage() + "].", ioe);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ioe) {
					throw new FenixRuntimeException("Unable to close Delivery Rule Set Event output stream: [" + file.getAbsolutePath() + "], error: [" + ioe.getMessage() + "].", ioe);
				}
			}
		}
	}	
}
