/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.tmc.vehiclediscovery.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
*
* @author tmyers1@yahoo.com (Tom Myers)
*
*/
public class Node extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private TmcVehicle parentVehicle;
	private String name;
	private String address;
	private Set<Did> dids = new TreeSet<>();
	
	public Node(
		String name,
		String address) {
		this.name = name;
		this.address = address;
	}

	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentVehicle,
			name,
			address
		);
	}

	public void validate(List<String> validationMessages) {

		if (parentVehicle == null) {
			validationMessages.add(getClassAndIdentity() + "'parentVehicle' must be specified.");
		}

		if (name == null || name.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'name' must be specified.");
		}
		
		if (address == null || address.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'address' must be specified.");
		}
	}
	
	public void setParentVehicle(TmcVehicle parentVehicle) {
		this.parentVehicle = parentVehicle;
	}
	
	public TmcVehicle getParentVehicle() {
		return parentVehicle;
	}
	
	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public List<Did> getDids() {
		List<Did> list = new ArrayList<>();
		list.addAll(dids);
		return list;
	}
	
	public void setDids(Set<Did> dids) {
		this.dids.clear();
		this.dids.addAll(dids);
		Iterator<Did> iterator = this.dids.iterator();
		while (iterator.hasNext()) {
			
			Did did = iterator.next();
			did.setParentNode(this);
		}
	}
	
	public Did getDid(String didAddress) {
		
		Iterator<Did> iterator = this.dids.iterator();
		while (iterator.hasNext()) {
			
			Did did = iterator.next();
			if (did.getDidAddress().equals(didAddress)) {
				return did;
			}
		}
		return null;
	}
}
