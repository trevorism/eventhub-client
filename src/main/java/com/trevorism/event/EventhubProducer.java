package com.trevorism.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trevorism.https.DefaultSecureHttpClient;
import com.trevorism.https.SecureHttpClient;

/**
 * @author tbrooks
 */
public class EventhubProducer<T> implements EventProducer<T> {

    protected SecureHttpClient httpClient;
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

    public EventhubProducer() {
        httpClient = new DefaultSecureHttpClient();
    }

    public EventhubProducer(SecureHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void sendEvent(String topic, T event) {
        sendEvent(topic, event, null);
    }

    @Override
    public void sendEvent(String topic, T event, String correlationId) {
        validateTopic(topic);
        String url = buildUrl(topic);
        String json = gson.toJson(event);
        String result = emitCorrelatedEvent(url, json, correlationId);

        if (!"true".equals(result))
            throw new EventNotSentException("Unable to successfully send the event");
    }

    @Override
    public void validateTopic(String topic) {
        if (topic == null) {
            throw new EventNotSentException("Topic not specified");
        }
        if (topic.length() < 3) {
            throw new EventNotSentException("Topic must be at least 3 characters");
        }
        if (topic.length() > 255) {
            throw new EventNotSentException("Topic must be less than 256 characters");
        }
        if (topic.startsWith("goog")) {
            throw new EventNotSentException("Topic cannot start with 'goog'");
        }
    }

    private String buildUrl(String topic) {
        return EVENT_BASE_URL + "/api/" + topic;
    }

    private String emitCorrelatedEvent(String url, String json, String correlationId) {
        return httpClient.post(url, json, correlationId);
    }
}
