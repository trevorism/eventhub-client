package com.trevorism.event;

import com.google.gson.Gson;
import com.trevorism.http.headers.HeadersHttpClient;
import com.trevorism.http.headers.HeadersJsonHttpClient;
import com.trevorism.http.util.ResponseUtils;
import com.trevorism.secure.PasswordProvider;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tbrooks
 */
public abstract class EventhubProducer<T> implements EventProducer<T> {

    private HeadersHttpClient headersClient = new HeadersJsonHttpClient();
    private PasswordProvider passwordProvider = new PasswordProvider();

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
            sendPingRequest();
        }catch (Exception e){
            try {
                Thread.sleep(getPingWaitMillis());
                sendPingRequest();
            } catch (Exception ie) {
                throw new RuntimeException("Interrupted failure", ie);
            }
        }
    }

    private void throwPingExceptionIfResponseNotPong(String pong, String errorMessage) {
        if(!"pong".equals(pong))
            throw new RuntimeException(errorMessage);
    }

    private void sendPingRequest() {
        CloseableHttpResponse response = headersClient.get(EVENT_BASE_URL + "/ping", null);
        String pong = ResponseUtils.getEntity(response);
        ResponseUtils.closeSilently(response);
        throwPingExceptionIfResponseNotPong(pong, "Unable to ping events");
    }

    private String emitEvent(String url, String json) {
        Map<String, String> headersMap = createHeaderMap(null);
        CloseableHttpResponse response = headersClient.post(url, json, headersMap);
        String result = ResponseUtils.getEntity(response);
        ResponseUtils.closeSilently(response);
        return result;
    }

    private String emitCorrelatedEvent(String url, String json, String correlationId) {
        CloseableHttpResponse response = null;
        try {
            Map<String, String> headersMap = createHeaderMap(correlationId);
            response = headersClient.post(url, json, headersMap);
            return ResponseUtils.getEntity(response);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally{
            ResponseUtils.closeSilently(response);
        }
    }

    private Map<String, String> createHeaderMap(String correlationId) {
        Map<String, String> headersMap = new HashMap<>();
        if(correlationId != null)
            headersMap.put(HeadersHttpClient.CORRELATION_ID_HEADER_KEY, correlationId);
        headersMap.put(PasswordProvider.AUTHORIZATION_HEADER, passwordProvider.getPassword());
        return headersMap;
    }

    private String convertObjectToJson(T event) {
        Gson gson = new Gson();
        return gson.toJson(event);
    }

    protected abstract String buildUrl(String topic);

    protected abstract int getPingWaitMillis();
}
