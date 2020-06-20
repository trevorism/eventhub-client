package com.trevorism.kraken;

import com.trevorism.kraken.model.Asset;

import java.util.List;

public interface KrakenClient {

    long serverTime();
    List<Asset> getAssets();

}
