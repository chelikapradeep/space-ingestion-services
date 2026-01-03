package com.keylent.space.store;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class InMemoryMinuteStore {

    private final Map<Instant, Set<String>> idsPerMinute = new ConcurrentHashMap<>();
    private final Map<Instant, Set<String>> endpointsPerMinute = new ConcurrentHashMap<>();

    public void record(String id, String endpoint) {

        Instant minute = Instant.now().truncatedTo(ChronoUnit.MINUTES);

        idsPerMinute
                .computeIfAbsent(minute, k -> ConcurrentHashMap.newKeySet())
                .add(id);

        if (endpoint != null && !endpoint.isBlank()) {
            endpointsPerMinute
                    .computeIfAbsent(minute, k -> ConcurrentHashMap.newKeySet())
                    .add(endpoint);
        }
    }

    public Set<String> getAndClearIds(Instant minute) {
        return idsPerMinute.remove(minute);
    }

    public Set<String> getAndClearEndpoints(Instant minute) {
        return endpointsPerMinute.remove(minute);
    }
}
