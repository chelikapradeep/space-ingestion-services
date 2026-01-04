package com.keylent.space.scheduler;

import com.keylent.space.entity.MinuteStat;
import com.keylent.space.repository.MinuteStatRepository;
import com.keylent.space.store.InMemoryMinuteStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

@Component
public class MinuteAggregationScheduler {

    private final InMemoryMinuteStore store;
    private final MinuteStatRepository repository;
    private final WebClient webClient;

    @Value("${aggregation.post.endpoint}")
    private String aggregationPostEndpoint;

    public MinuteAggregationScheduler(
            InMemoryMinuteStore store,
            MinuteStatRepository repository,
            WebClient webClient) {
        this.store = store;
        this.repository = repository;
        this.webClient = webClient;
    }

    @Scheduled(cron = "0 * * * * *")
    public void aggregate() {

        // Aggregate previous completed minute
        Instant minute = Instant.now()
                .minus(1, ChronoUnit.MINUTES)
                .truncatedTo(ChronoUnit.MINUTES);

        Set<String> ids = store.getAndClearIds(minute);
        Set<String> endpoints = store.getAndClearEndpoints(minute);

       
        if (ids == null || ids.isEmpty()) return;

       
        if (endpoints == null || endpoints.isEmpty()) return;

        long uniqueCount = ids.size();

       
        MinuteStat stat = new MinuteStat();
        stat.setMinuteStart(minute);
        stat.setUniqueIdCount(uniqueCount);
        repository.save(stat);

        System.out.println(minute + " -> " + uniqueCount + " unique ids");

        
        Map<String, Object> payload = Map.of(
                "minuteStart", minute.toString(),
                "uniqueIdCount", uniqueCount
        );

        
        webClient.post()
                .uri(aggregationPostEndpoint)
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(res ->
                        System.out.println("POST success to " + aggregationPostEndpoint))
                .doOnError(err ->
                        System.err.println("POST failed to " + aggregationPostEndpoint))
                .onErrorResume(ex -> Mono.empty())
                .subscribe();
    }
}
