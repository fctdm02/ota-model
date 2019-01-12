/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.model;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AbstractEntityBuilder {

    private Long persistentIdentity;

    public AbstractEntityBuilder withPersistentIdentity(Long persistentIdentity) {
        this.persistentIdentity = persistentIdentity;
        return this;
    }

    public Long getPersistentIdentity() {
        return this.persistentIdentity;
    }
}



