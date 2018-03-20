package com.trevorism.event

import com.trevorism.event.model.WorkComplete
import com.trevorism.http.headers.HeadersHttpClient
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.entity.StringEntity
import org.junit.Test

/**
 * @author tbrooks
 */
class EventhubProducerTest {

    private final pingResponse = [getEntity:{new StringEntity("pong")}] as CloseableHttpResponse
    private final postResponse = [getEntity:{new StringEntity("true")}] as CloseableHttpResponse


    @Test
    void testSendEvent() {
        EventProducer<WorkComplete> eventhubProducer = new PingingEventProducer<>()
        eventhubProducer.headersClient = [get: {url, map -> return pingResponse}, post: {url,json,map ->
            assert url == "https://event.trevorism.com/api/testTopic"
            return postResponse
        }] as HeadersHttpClient
        eventhubProducer.sendEvent("testTopic", new WorkComplete(), null)
    }

    @Test
    void testSendCorrelatedEvent() {
        EventProducer<WorkComplete> eventhubProducer = new PingingEventProducer<>()
        eventhubProducer.headersClient = [get: {url, map -> return pingResponse}, post: {url,json,map ->
            assert url == "https://event.trevorism.com/api/testTopic"
            assert map["X-Correlation-ID"] == "12345"
            return postResponse
        }] as HeadersHttpClient
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
