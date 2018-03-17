package com.trevorism.event

import org.junit.Test

/**
 * @author tbrooks
 */
class DefaultEventProducerTest {

    @Test
    void testBuildUrl() {
        DefaultEventProducer defaultEventProducer = new DefaultEventProducer()
        assert "https://event.trevorism.com/api/testTopic" == defaultEventProducer.buildUrl("testTopic")
    }

    @Test
    void testGetPingWaitMillis() {
        DefaultEventProducer defaultEventProducer = new DefaultEventProducer()
        assert defaultEventProducer.getPingWaitMillis() == 15000
    }
}
