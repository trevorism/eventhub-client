package com.trevorism.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    protected HeadersHttpClient headersClient = new HeadersJsonHttpClient();
    private PasswordProvider passwordProvider = new PasswordProvider();
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

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

        if(!"true".equals(result))
            throw new EventNotSentException("Unable to successfully send the event");
    }

    @Override
    public void validateTopic(String topic) {
        if(topic == null){
            throw new EventNotSentException("Topic not specified");
        }
        if(topic.length() < 3){
            throw new EventNotSentException("Topic must be at least 3 characters");
        }
        if(topic.length() > 255){
            throw new EventNotSentException("Topic must be less than 256 characters");
        }
        if(topic.startsWith("goog")){
            throw new EventNotSentException("Topic cannot start with 'goog'");
        }
    }

    private String buildUrl(String topic) {
        return EVENT_BASE_URL + "/api/" + topic;
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


}
