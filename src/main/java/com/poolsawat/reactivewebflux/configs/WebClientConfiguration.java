package com.poolsawat.reactivewebflux.configs;

import com.poolsawat.reactivewebflux.loggers.ClientCallLogger;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
@EnableWebFlux
public class WebClientConfiguration implements WebFluxConfigurer {

    @Value("${clients.endpoints.reactive.host}")
    private String reactiveHost;

    @Bean
    public WebClient createWebClient(HttpClient httpClient){
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(reactiveHost)
                .build();
    }

    @Bean
    public HttpClient createHttpClient(ClientCallLogger clientLogger){
        val connectionProvider = ConnectionProvider.builder("reactive-tcp-connection-pool")
                .maxConnections(10)
                .pendingAcquireTimeout(Duration.ofMillis(15000)) // 15 sec
                .maxIdleTime(Duration.ofMillis(30000)) // 30 sec
                .build();

        return HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000) // 30 sec
                .doOnConnected( it -> {
                    it.addHandlerLast(new ReadTimeoutHandler(30000)) // 30 sec
                            .addHandlerLast(new WriteTimeoutHandler(30000)); // 30 sec
                })
                .doOnRequest( (x,conn) -> conn.addHandlerFirst(clientLogger))
                .doOnResponse( (x, conn) -> conn.addHandlerFirst(clientLogger));
    }
}