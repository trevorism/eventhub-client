package com.trevorism.kraken

import com.google.gson.Gson;
import com.trevorism.http.headers.HeadersBlankHttpClient;
import com.trevorism.http.headers.HeadersHttpClient
import com.trevorism.http.util.ResponseUtils
import com.trevorism.kraken.model.Asset;
import org.apache.http.client.methods.CloseableHttpResponse;


class DefaultKrakenClient implements KrakenClient {

    private Gson gson = new Gson()

    @Override
    long serverTime() {
        HeadersHttpClient httpClient = new HeadersBlankHttpClient()
        def headersMap = ["User-Agent": "Mozilla/5.0"]
        CloseableHttpResponse response = httpClient.get("https://api.kraken.com/0/public/Time", headersMap)
        String json = ResponseUtils.getEntity(response)
        def map = gson.fromJson(json, Map)
        return map.result.unixtime * 1000

    }

    @Override
    List<Asset> getAssets() {
        return null;
    }
}
