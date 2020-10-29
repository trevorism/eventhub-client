package com.trevorism.event;

import com.trevorism.https.token.ObtainTokenStrategy;

/**
 * @author tbrooks
 */
public class PingingEventProducer<T> extends EventhubProducer<T> {

    private static final int PING_WAIT_MILLIS = 15000;

    public PingingEventProducer() {
        super();
    }

    public PingingEventProducer(ObtainTokenStrategy strategy) {
        super(strategy);
    }

    @Override
    public void sendEvent(String topic, T event, String correlationId) {
        ping();
        super.sendEvent(topic, event, correlationId);
    }

    private void ping() {
        try {
            //ping the API to wake it up since it is not always on
            sendPingRequest();
        }catch (Exception e){
            try {
                Thread.sleep(PING_WAIT_MILLIS);
                sendPingRequest();
            } catch (Exception ie) {
                throw new RuntimeException("Interrupted failure", ie);
            }
        }
    }

    private void sendPingRequest() {
        String pong = httpClient.get(EVENT_BASE_URL + "/ping");
        throwPingExceptionIfResponseNotPong(pong);
    }

    private void throwPingExceptionIfResponseNotPong(String pong) {
        if(!"pong".equals(pong))
            throw new RuntimeException("Unable to ping " + EVENT_BASE_URL);
    }

}
