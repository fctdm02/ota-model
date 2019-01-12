/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.event;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface EventSubscriber<T> {

	
    void handleEvent(final T event);
}
