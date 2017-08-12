package com.trevorism.event;

import com.google.gson.Gson;
import com.trevorism.http.HttpClient;
import com.trevorism.http.JsonHttpClient;

/**
 * @author tbrooks
 */
public abstract class EventhubProducer<T> implements EventProducer<T> {

    private HttpClient client = new JsonHttpClient();

    @Override
    public void sendEvent(String topic, T event) {
        ping();

        String url = buildUrl(topic);
        String json = convertObjectToJson(event);
        String result = emitEvent(url, json);

        if(!"true".equals(result))
            throw new EventNotSentException("Unable to successfully send the event");
    }

    private void ping() {
        //ping the API to wake it up since it is not always on
        client.get(EVENT_BASE_URL + "/ping");
    }

    private String emitEvent(String url, String json) {
        return client.post(url, json);
    }

    private String convertObjectToJson(T event) {
        Gson gson = new Gson();
        return gson.toJson(event);
    }

    protected abstract String buildUrl(String topic);
}
