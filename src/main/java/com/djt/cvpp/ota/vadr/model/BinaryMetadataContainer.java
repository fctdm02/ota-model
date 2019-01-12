/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.model;

import java.util.List;
import java.util.Set;


/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface BinaryMetadataContainer {
	
	/**
	 * 
	 * @return
	 */
	List<BinaryMetadata> getBinaryMetadatas();
	
	/**
	 * 
	 * @param binaryMetadata
	 */
	void addBinaryMetadata(BinaryMetadata binaryMetadata);
	
	/**
	 * 
	 * @param binaryMetadatas
	 */
	void addBinaryMetadatas(Set<BinaryMetadata> binaryMetadatas);
}
