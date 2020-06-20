package com.trevorism.kraken

import org.junit.Test


class DefaultKrakenClientTest {

    @Test
    void "server time returns the time"() {
        KrakenClient defaultKrakenClient = new DefaultKrakenClient()
        long value = defaultKrakenClient.serverTime();


        println new Date()

    }

}
