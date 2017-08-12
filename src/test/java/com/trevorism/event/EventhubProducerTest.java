package com.trevorism.event;

import org.junit.Test;

import java.util.Date;

/**
 * @author tbrooks
 */
public class EventhubProducerTest {

    @Test
    public void sendEvent() throws Exception {
        SomeThing thing = new SomeThing("test",5, new Date());
        EventProducer<SomeThing> producer = new DefaultEventProducer<>();
        producer.sendEvent("testTopic", thing);
    }

    @Test(expected = EventNotSentException.class)
    public void sendEventWithInvalidUrl() throws Exception {
        SomeThing thing = new SomeThing("test",5, new Date());
        EventProducer<SomeThing> producer = new EventhubProducer<SomeThing>() {
            @Override
            protected String buildUrl(String topic) {
                return "http://trevorism.com";
            }
        };
        producer.sendEvent("testTopic", thing);
    }
}