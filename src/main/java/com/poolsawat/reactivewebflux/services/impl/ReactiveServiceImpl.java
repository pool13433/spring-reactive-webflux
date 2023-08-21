package com.poolsawat.reactivewebflux.services.impl;

import com.poolsawat.reactivewebflux.clients.ReactiveClient;
import com.poolsawat.reactivewebflux.services.ReactiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ReactiveServiceImpl implements ReactiveService {

    @Autowired
    private ReactiveClient reactiveClient;

    @Override
    public Mono<List<String>> getReactiveListItems() {
        return reactiveClient.getReactiveItems();
    }
}