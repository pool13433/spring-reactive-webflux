package com.poolsawat.reactivewebflux.services;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ReactiveService {
    Mono<List<String>> getReactiveListItems();
}