/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.orfin.vadrevent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VadrReleaseEventPublisherTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(VadrReleaseEventPublisherTest.class);

	
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
	
	private File dataDirectory = new File(System.getProperty("java.io.tmpdir") + "/testdata/orfin/vadrevent/");
	
	@Before
	public void before() {
		dataDirectory.mkdir();
		dataDirectory.mkdirs();
	}
	
	@Test
	public void publishVadrEvent() throws IOException {

		String domainName = "MayHackathon-ECG";
		String domainInstanceName = "ECG_Hack_02";
		String domainInstanceDescription = "This is the second round of testing with the ECG Image file received on 6-18";
		String domainInstanceVersion = "01.01.00";
		String appId = "";
		String appVersion = "";
		String productionState = "PRODUCTION";
		String releaseDate = "10-28-2018";
		String softwarePriorityLevel = "CRITICAL_PRIORITY";
		String hasBeenProcessed = "false";
		
		String filename = VADR_RELEASE_EVENT_PREFIX + "_" + new Random().nextLong() + VADR_RELEASE_EVENT_SUFFIX;
		File eventFile = new File(dataDirectory, filename);
				
		Properties properties = new Properties();
		OutputStream outputStream = new FileOutputStream(eventFile);
		
		properties.setProperty(DOMAIN_NAME, domainName);
		properties.setProperty(DOMAIN_INSTANCE_NAME, domainInstanceName);
		properties.setProperty(DOMAIN_INSTANCE_DESCRIPTION, domainInstanceDescription);
		properties.setProperty(DOMAIN_INSTANCE_VERSION, domainInstanceVersion);
		properties.setProperty(APP_ID, appId);
		properties.setProperty(APP_VERSION, appVersion);
		properties.setProperty(PRODUCTION_STATE, productionState);
		properties.setProperty(RELEASE_DATE, releaseDate);
		properties.setProperty(SOFTWARE_PRIORITY_LEVEL, softwarePriorityLevel);
		properties.setProperty(HAS_BEEN_PROCESSED, hasBeenProcessed);
		
		properties.store(outputStream, null);
		
		LOGGER.warn(properties.toString());
		LOGGER.warn("Writing VADR Release Event file to: " + eventFile.getAbsolutePath());
		
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
