package com.keylent.space.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "minute_stats")
public class MinuteStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant minuteStart;

    private Long uniqueIdCount;

    public Long getId() {
        return id;
    }

    public Instant getMinuteStart() {
        return minuteStart;
    }

    public void setMinuteStart(Instant minuteStart) {
        this.minuteStart = minuteStart;
    }

    public Long getUniqueIdCount() {
        return uniqueIdCount;
    }

    public void setUniqueIdCount(Long uniqueIdCount) {
        this.uniqueIdCount = uniqueIdCount;
    }
}
