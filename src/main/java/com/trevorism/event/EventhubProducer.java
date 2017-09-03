package com.trevorism.event;

import com.google.gson.Gson;
import com.trevorism.http.HttpClient;
import com.trevorism.http.JsonHttpClient;
import com.trevorism.http.headers.HeadersHttpClient;
import com.trevorism.http.headers.HeadersJsonHttpClient;
import com.trevorism.http.util.ResponseUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tbrooks
 */
public abstract class EventhubProducer<T> implements EventProducer<T> {

    private static final int WAIT_MILLIS = 10000;
    private HttpClient client = new JsonHttpClient();
    private HeadersHttpClient headersClient = new HeadersJsonHttpClient();

    @Override
    public void sendEvent(String topic, T event) {
        ping();

        String url = buildUrl(topic);
        String json = convertObjectToJson(event);
        String result = emitEvent(url, json);

        if(!"true".equals(result))
            throw new EventNotSentException("Unable to successfully send the event");
    }

    @Override
    public void sendCorrelatedEvent(String topic, T event, String correlationId) {
        ping();

        String url = buildUrl(topic);
        String json = convertObjectToJson(event);
        String result = emitCorrelatedEvent(url, json, correlationId);

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
                Thread.sleep(WAIT_MILLIS);
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

    private String emitCorrelatedEvent(String url, String json, String correlationId) {
        CloseableHttpResponse response = null;
        try {
            Map<String, String> headersMap = createCorrelationHeader(correlationId);
            response = headersClient.post(url, json, headersMap);
            return ResponseUtils.getEntity(response);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally{
            ResponseUtils.closeSilently(response);
        }
    }

    private Map<String, String> createCorrelationHeader(String correlationId) {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(HeadersHttpClient.CORRELATION_ID_HEADER_KEY, correlationId);
        return headersMap;
    }

    private String convertObjectToJson(T event) {
        Gson gson = new Gson();
        return gson.toJson(event);
    }

    protected abstract String buildUrl(String topic);
}
