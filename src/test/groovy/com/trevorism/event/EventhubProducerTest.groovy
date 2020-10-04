package com.trevorism.event

import com.trevorism.event.model.WorkComplete
import com.trevorism.http.headers.HeadersHttpClient
import com.trevorism.https.SecureHttpClient
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.StringEntity
import org.junit.Test

/**
 * @author tbrooks
 */
class EventhubProducerTest {

    @Test
    void testSendEvent() {
        EventProducer<WorkComplete> eventhubProducer = new PingingEventProducer<>()
        eventhubProducer.httpClient = [get: { url -> return "pong" }, post: { url, json, map ->
            assert url == "https://event.trevorism.com/api/testTopic"
            return "true"
        }] as SecureHttpClient
        eventhubProducer.sendEvent("testTopic", new WorkComplete(), null)
    }

    @Test
    void testSendCorrelatedEvent() {
        EventProducer<WorkComplete> eventhubProducer = new PingingEventProducer<>()
        eventhubProducer.httpClient = [get: { url -> return "pong" }, post: { url, json, map ->
            assert url == "https://event.trevorism.com/api/testTopic"
            return "true"
        }] as SecureHttpClient
        eventhubProducer.sendEvent("testTopic", new WorkComplete(), "12345")
    }

    @Test(expected = EventNotSentException)
    void testInvalidTopic() {
        WorkCompleteEventProducer producer = new WorkCompleteEventProducer()
        producer.validateTopic("vm")
    }

    @Test(expected = EventNotSentException)
    void testInvalidTopicGoogle() {
        WorkCompleteEventProducer producer = new WorkCompleteEventProducer()
        producer.validateTopic("google")
    }


}
