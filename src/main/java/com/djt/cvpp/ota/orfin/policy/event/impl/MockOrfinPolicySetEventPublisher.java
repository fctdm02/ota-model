/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.policy.event.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEvent;
import com.djt.cvpp.ota.orfin.policy.event.OrfinPolicySetEventSubscriber;

/**
  *
  * @author tmyers1@yahoo.com (Tom Myers)
  *
  */
public class MockOrfinPolicySetEventPublisher extends AbstractOrfinPolicySetEventPublisher {

	public static final String POLICY_SET_EVENT_PREFIX = "POLICY_SET_EVENT_";
	public static final String POLICY_SET_EVENT_SUFFIX = ".txt";

	public static final String PROGRAM_CODE = "programCode";
	public static final String MODEL_YEAR = "modelYear";
	public static final String REGION_CODE = "regionCode";
	public static final String POLICY_SET_NAME = "policySetName";
	
	private static MockOrfinPolicySetEventPublisher INSTANCE = new MockOrfinPolicySetEventPublisher();
	public static MockOrfinPolicySetEventPublisher getInstance() {
		return INSTANCE;
	}
	private MockOrfinPolicySetEventPublisher() {
		
		File dataDirectory = new File(System.getProperty("java.io.tmpdir") + "/testdata/orfin/policy/");
		Set<File> ignoredFiles = new HashSet<>();
		FilenameFilter filter = new FilenameFilter() {
			@Override
		    public boolean accept(File dir, String name) {
		    	if (name.startsWith(POLICY_SET_EVENT_PREFIX) && name.endsWith(POLICY_SET_EVENT_SUFFIX)) {
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

								OrfinPolicySetEvent odlEventEvent = loadPolicySetEvent(file);
								Iterator<OrfinPolicySetEventSubscriber> iterator = subscribers.iterator();
								while (iterator.hasNext()) {
									
									OrfinPolicySetEventSubscriber subscriber = iterator.next();
									try {
										subscriber.handleOrfinPolicySetEvent(odlEventEvent);
									} catch (ValidationException ve) {
										throw new FenixRuntimeException("Unable to handle Policy Set Event from file: [" + file.getAbsolutePath() + "], error: [" + ve.getMessage() + "].", ve);
									}
								}
								file.delete();
								ignoredFiles.add(file);
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
	
	public static OrfinPolicySetEvent loadPolicySetEvent(File file) {
		
		Properties properties = new Properties();
		InputStream inputStream = null;
		
		try {
			
			inputStream = new FileInputStream(file);
			properties.load(inputStream);
			
			String owner = "tmyers28";
			String programCode = properties.getProperty(PROGRAM_CODE);
			Integer modelYear = Integer.parseInt(properties.getProperty(MODEL_YEAR));
			String regionCode = properties.getProperty(REGION_CODE);
			String policySetName = properties.getProperty(POLICY_SET_NAME);
			
			return new OrfinPolicySetEvent(
				owner,
				programCode,
				modelYear,
				regionCode,
				policySetName);
			
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to load Policy Set Event from file: [" + file.getAbsolutePath() + "], error: [" + ioe.getMessage() + "].", ioe);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ioe) {
					throw new FenixRuntimeException("Unable to close Policy Set Event file: [" + file.getAbsolutePath() + "], error: [" + ioe.getMessage() + "].", ioe);
				}
			}
		}
	}	
}
