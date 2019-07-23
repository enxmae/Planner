package com.example.planner.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventInstance implements Serializable {

    @Expose
    @SerializedName("started_at")
    private Long startedAt;

    @Expose
    @SerializedName("ended_at")
    private Long endedAt;

    @Expose
    @SerializedName("event_id")
    private Long eventId;

    @Expose
    @SerializedName("pattern_id")
    private Long patternId;


    public Long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Long endedAt) {
        this.endedAt = endedAt;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getPatternId() {
        return patternId;
    }

    public void setPatternId(Long patternId) {
        this.patternId = patternId;
    }

}
