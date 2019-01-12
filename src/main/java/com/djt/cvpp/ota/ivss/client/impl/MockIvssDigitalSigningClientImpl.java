/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.ivss.client.impl;

import com.djt.cvpp.ota.ivss.client.IvssDigitalSigningClient;
import com.djt.cvpp.ota.ivss.exception.IvssException;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public class MockIvssDigitalSigningClientImpl implements IvssDigitalSigningClient {
	
	// TODO: Need to keep a map that we load up from the file system or classpath that contains the keys for the passed in fesn values.

	public byte[] signBytes(
		String fesn,
		byte[] payload)
	throws 
		IvssException {
		
		throw new RuntimeException("Not implemented yet.");
	}
}
