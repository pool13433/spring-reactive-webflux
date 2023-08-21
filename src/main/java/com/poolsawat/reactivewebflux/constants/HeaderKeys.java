package com.poolsawat.reactivewebflux.constants;

public enum HeaderKeys {
    X_CORRELATION_ID("X-Correlation-ID"),
    X_CLIENT_IP("clientIp"),
    HEADERS("] headers=["),
    URI("] uri=[");

    protected String key;

    HeaderKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}