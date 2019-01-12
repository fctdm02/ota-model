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
public interface EventPublisher {
	
	
	void subscribe(EventSubscriber<AbstractEvent> eventSubscriber);
	
	void unsubscribe(EventSubscriber<AbstractEvent> eventSubscriber);
	
	void publishEvent(AbstractEvent event);
}
