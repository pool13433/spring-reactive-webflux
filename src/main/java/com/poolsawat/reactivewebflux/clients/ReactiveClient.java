package com.poolsawat.reactivewebflux.clients;

import com.poolsawat.reactivewebflux.clients.proxy.ReactiveClientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class ReactiveClient implements ReactiveClientProxy {

    @Value("${clients.endpoints.reactive.path}")
    private String reactivePath;

    @Autowired
    private WebClient webClient;

    @Override
    public Mono<List<String>> getReactiveItems() {
        return webClient.get()
                .uri(reactivePath)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {});
    }
}