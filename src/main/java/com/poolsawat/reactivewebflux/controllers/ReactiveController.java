package com.poolsawat.reactivewebflux.controllers;

import com.poolsawat.reactivewebflux.services.ReactiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ReactiveController {

    @Autowired
    private ReactiveService reactiveService;

    @GetMapping(value = "/welcome")
    public Mono<ResponseEntity<List<String>>> welcome() {
        return reactiveService.getReactiveListItems().map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}