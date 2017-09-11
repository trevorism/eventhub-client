package com.trevorism.event;

/**
 * @author tbrooks
 */
public class DefaultEventProducer<T> extends EventhubProducer<T> {

    @Override
    protected String buildUrl(String topic) {
        return EVENT_BASE_URL + "/api/" + topic;
    }

    @Override
    protected int getPingWaitMillis() {
        return 15000;
    }
}
