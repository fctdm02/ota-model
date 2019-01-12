/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.repository.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.djt.cvpp.ota.common.repository.EntityRepository;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractMockRepository implements EntityRepository {
	
	public String loadTestData(String filename) {
		
		String fullPath = null;
		if (filename.endsWith(".json")) {
			fullPath = "/testdata" + filename;			
		} else {
			fullPath = "/testdata" + filename + ".json";
		}
		
		InputStream inputStream = null;
		File dataDirectory = new File(System.getProperty("java.io.tmpdir"));
		try {

			// Try the class path first (files embedded in src/main/resources or src/test/resources).
			inputStream = AbstractMockRepository.class.getResourceAsStream(fullPath);
			if (inputStream == null) {

				// Failing that, try the temporary directory on the file system.
				File file = new File(dataDirectory, filename);
				if (file.exists()) {
					inputStream = new FileInputStream(file);	
				}
			}
			
			if (inputStream != null) {
				return this.loadTestData(inputStream);
			}
			throw new RuntimeException("Unable to load file: [" + fullPath + "] because it does not exist in the classpath, nor in tempDirectory: " + dataDirectory.getAbsolutePath());
			
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to load file: [" + fullPath + "], error: " + ioe.getMessage(), ioe);
        } finally {
        	if (inputStream != null) {
        		try {
        			inputStream.close();
                } catch (IOException ioe) {
                    throw new RuntimeException("Unable to close input stream: [" + fullPath + "], error: " + ioe.getMessage(), ioe);
                }
        	}
        }
	}
	
	public String loadTestData(InputStream inputStream) throws IOException {

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {
            Iterator<String> iterator = reader.lines().iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
            }
        }
        return sb.toString();
    }	    
}
