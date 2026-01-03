package com.keylent.space.service;

import com.keylent.space.store.InMemoryMinuteStore;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class IngestionServiceImpl implements IngestionService {

    private final InMemoryMinuteStore store;
    private final WebClient webClient;

    public IngestionServiceImpl(InMemoryMinuteStore store, WebClient webClient) {
        this.store = store;
        this.webClient = webClient;
    }

    @Override
    public void process(String id, String endpoint) {

        // Record ID for current minute
        store.record(id, endpoint);

        // Fire GET asynchronously (non-blocking)
        if (endpoint != null && !endpoint.isBlank()) {
            webClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .onStatus(
                            s -> s.is4xxClientError() || s.is5xxServerError(),
                            r -> Mono.empty()
                    )
                    .toBodilessEntity()
                    .onErrorResume(ex -> Mono.empty())
                    .subscribe();
        }
    }
}
