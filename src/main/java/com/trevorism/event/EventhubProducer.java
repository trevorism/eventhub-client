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
        try {
            //ping the API to wake it up since it is not always on
            String pong = client.get(EVENT_BASE_URL + "/ping");
            if(!"pong".equals(pong))
                throw new Exception("Unable to ping events");
        }catch (Exception e){
            try {
                Thread.sleep(10000);
                String pong = client.get(EVENT_BASE_URL + "/ping");
                if(!"pong".equals(pong))
                    throw new RuntimeException("Unable to ping events after 10 second retry");
            } catch (InterruptedException ie) {
                throw new RuntimeException("Interrupted failure", ie);
            }
        }
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
