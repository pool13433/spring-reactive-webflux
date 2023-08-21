package com.poolsawat.reactivewebflux.clients.proxy;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ReactiveClientProxy {
    Mono<List<String>> getReactiveItems();
}