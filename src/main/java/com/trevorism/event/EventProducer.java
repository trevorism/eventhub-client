package com.trevorism.event;

/**
 * @author tbrooks
 */
public interface EventProducer<T> {

    String EVENT_BASE_URL = "https://event.trevorism.com";

    void sendEvent(String topic, T event);
    void sendEvent(String topic, T event, String correlationId);

    void validateTopic(String topic);

}
