package com.trevorism.event;

import com.google.gson.Gson;
import com.trevorism.http.HttpClient;
import com.trevorism.http.JsonHttpClient;

/**
 * @author tbrooks
 */
public abstract class EventhubProducer<T> implements EventProducer<T> {

    @Override
    public void sendEvent(String topic, T event) {
        String url = buildUrl(topic);
        String json = convertObjectToJson(event);
        String result = emitEvent(url, json);

        if(!"true".equals(result))
            throw new EventNotSentException("Unable to successfully send the event");
    }

    private String emitEvent(String url, String json) {
        HttpClient client = new JsonHttpClient();
        return client.post(url, json);
    }

    private String convertObjectToJson(T event) {
        Gson gson = new Gson();
        return gson.toJson(event);
    }

    protected abstract String buildUrl(String topic);
}
