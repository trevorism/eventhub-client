package com.trevorism.event;

/**
 * @author tbrooks
 */
public interface EventProducer<T> {

    String EVENT_BASE_URL = "http://event.trevorism.com";

    void sendEvent(String topic, T event);
    void sendCorrelatedEvent(String topic, T event, String correlationId);
}
