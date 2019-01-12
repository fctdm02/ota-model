/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.delivery.event.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEvent;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEventPublisher;
import com.djt.cvpp.ota.orfin.delivery.event.OrfinDeliveryRuleSetEventSubscriber;

/**
  *
  * @author tmyers1@yahoo.com (Tom Myers)
  *
  */
public class MockOrfinDeliveryRuleSetEventPublisher implements OrfinDeliveryRuleSetEventPublisher {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MockOrfinDeliveryRuleSetEventPublisher.class);
	

	public static final String DELIVERY_RULE_SET_EVENT_PREFIX = "DELIVERY_RULE_SET_EVENT_";
	public static final String DELIVERY_RULE_SET_EVENT_SUFFIX = ".txt";
	
	public static final String OWNER = "owner";
	public static final String UPDATE_ACTION = "updateAction";
	public static final String DELIVERY_RULE_SET_NAME = "deliveryRuleSetName";
	public static final String NODE_ACRONYM = "nodeAcronym";
	public static final String NODE_ADDRESS = "nodeAddress";
	public static final String HAS_BEEN_PROCESSED = "hasBeenProcessed";
	
	private static MockOrfinDeliveryRuleSetEventPublisher INSTANCE = new MockOrfinDeliveryRuleSetEventPublisher();
	public static MockOrfinDeliveryRuleSetEventPublisher getInstance() {
		return INSTANCE;
	}
	
	private File dataDirectory = new File(System.getProperty("java.io.tmpdir") + "/testdata/orfin/delivery/");
	
	protected List<OrfinDeliveryRuleSetEventSubscriber> subscribers = new ArrayList<>();
	
	public void subscribe(OrfinDeliveryRuleSetEventSubscriber orfinDeliveryRuleSetEventSubscriber) {
		
		this.subscribers.add(orfinDeliveryRuleSetEventSubscriber);
	}
	
	public void unsubscribe(OrfinDeliveryRuleSetEventSubscriber orfinDeliveryRuleSetEventSubscriber) {
		
		this.subscribers.remove(orfinDeliveryRuleSetEventSubscriber);
	}

	public OrfinDeliveryRuleSetEvent publishOrfinDeliveryRuleSetEvent(
		String owner, 
		String updateAction,
		String deliveryRuleSetName,
		String nodeAcronym,
		String nodeAddress)
	throws 
		ValidationException {

		OrfinDeliveryRuleSetEvent orfinDeliveryRuleSetEvent = new OrfinDeliveryRuleSetEvent(
			owner,
			updateAction,
			deliveryRuleSetName,
			nodeAcronym,
			nodeAddress);
		
		try {
			this.writeDeliveryRuleSetEvent(orfinDeliveryRuleSetEvent);
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to write Delivery Rule Set Event to file, error: [" + ioe.getMessage() + "].", ioe);
		}		
		
		Iterator<OrfinDeliveryRuleSetEventSubscriber> iterator = this.subscribers.iterator();
		while (iterator.hasNext()) {
			
			OrfinDeliveryRuleSetEventSubscriber orfinDeliveryRuleSetEventSubscriber = iterator.next();
			orfinDeliveryRuleSetEventSubscriber.handleDeliveryRuleSetEvent(orfinDeliveryRuleSetEvent);
		}
		
		return orfinDeliveryRuleSetEvent;
	}
	
	private MockOrfinDeliveryRuleSetEventPublisher() {
		
		Set<File> ignoredFiles = new HashSet<>();
		FilenameFilter filter = new FilenameFilter() {
			@Override
		    public boolean accept(File dir, String name) {
		    	if (name.startsWith(DELIVERY_RULE_SET_EVENT_PREFIX) && name.endsWith(DELIVERY_RULE_SET_EVENT_SUFFIX)) {
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

								OrfinDeliveryRuleSetEvent deliveryRuleSetEvent = loadDeliveryRuleSetEvent(file);
								if (deliveryRuleSetEvent != null) {

									Iterator<OrfinDeliveryRuleSetEventSubscriber> iterator = subscribers.iterator();
									while (iterator.hasNext()) {
										
										OrfinDeliveryRuleSetEventSubscriber subscriber = iterator.next();
										try {
											subscriber.handleDeliveryRuleSetEvent(deliveryRuleSetEvent);
										} catch (ValidationException ve) {
											throw new FenixRuntimeException("Unable to handle Delivery Rule Set Event from file: [" + file.getAbsolutePath() + "], error: [" + ve.getMessage() + "].", ve);
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
	
	public OrfinDeliveryRuleSetEvent loadDeliveryRuleSetEvent(File file) {
		
		OrfinDeliveryRuleSetEvent orfinDeliveryRuleSetEvent = null;
		Properties properties = new Properties();
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		try {
			
			inputStream = new FileInputStream(file);
			properties.load(inputStream);
			
			String owner = "tmyers28";
			String updateAction = properties.getProperty(UPDATE_ACTION);
			String deliveryRuleSetName = properties.getProperty(DELIVERY_RULE_SET_NAME);
			String nodeAcronym = properties.getProperty(NODE_ACRONYM);
			String nodeAddress = properties.getProperty(NODE_ADDRESS);
			String hasBeenProcessed = properties.getProperty(HAS_BEEN_PROCESSED);
			
			if (hasBeenProcessed.trim().equalsIgnoreCase("false")) {
				orfinDeliveryRuleSetEvent = new OrfinDeliveryRuleSetEvent(
					owner,
					updateAction,
					deliveryRuleSetName,
					nodeAcronym,
					nodeAddress);
			} else {
				properties.setProperty(HAS_BEEN_PROCESSED, "true");
				
				outputStream = new FileOutputStream(file);
				properties.store(outputStream, null);
			}
			
			return orfinDeliveryRuleSetEvent;
			
		} catch (IOException ioe) {
			throw new FenixRuntimeException("Unable to load Delivery Rule Set Event from file: [" + file.getAbsolutePath() + "], error: [" + ioe.getMessage() + "].", ioe);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ioe) {
					throw new FenixRuntimeException("Unable to close Delivery Rule Set Event input stream: [" + file.getAbsolutePath() + "], error: [" + ioe.getMessage() + "].", ioe);
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
	
	public void writeDeliveryRuleSetEvent(OrfinDeliveryRuleSetEvent orfinDeliveryRuleSetEvent) throws IOException {

		String filename = DELIVERY_RULE_SET_EVENT_PREFIX + "_" + new Random().nextLong() + DELIVERY_RULE_SET_EVENT_SUFFIX;
		File eventFile = new File(dataDirectory, filename);
				
		Properties properties = new Properties();
		OutputStream outputStream = new FileOutputStream(eventFile);
		
		properties.setProperty(OWNER, orfinDeliveryRuleSetEvent.getOwner());
		properties.setProperty(UPDATE_ACTION, orfinDeliveryRuleSetEvent.getUpdateAction());
		properties.setProperty(DELIVERY_RULE_SET_NAME, orfinDeliveryRuleSetEvent.getDeliveryRuleSetName());
		
		String nodeAcronym = orfinDeliveryRuleSetEvent.getNodeAcronym();
		if (nodeAcronym != null) {
			properties.setProperty(NODE_ACRONYM, nodeAcronym);	
		}

		String nodeAddress = orfinDeliveryRuleSetEvent.getNodeAddress();
		if (nodeAddress != null) {
			properties.setProperty(NODE_ADDRESS, nodeAddress);	
		}
		
		properties.setProperty(HAS_BEEN_PROCESSED, "false");
		
		properties.store(outputStream, null);
		
		LOGGER.warn(properties.toString());
		LOGGER.warn("Writing Delivery Rule Set Event file to: " + eventFile.getAbsolutePath());
		
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
