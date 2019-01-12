/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.event;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	
    private final UUID eventUuid;
    private final Timestamp occurredOnDate;
    private final String owner;

    public AbstractEvent(String owner) {

    	this.eventUuid = UUID.randomUUID();
        this.occurredOnDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
        this.owner = owner;
    }

    public UUID getEventUuid() {
        return this.eventUuid;
    }

    public Timestamp occurredOn() {
        return new Timestamp(this.occurredOnDate.getTime());
    }

    public String getOwner() {
    	return this.owner; 
    }
    
    public abstract Object getPayload();
}
