package com.example.planner.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventPattern implements Serializable {

    @Expose
    private Long id;
    private Long createdAt;
    private Long updatedAt;

    @Expose
    private Long duration;

    @Expose
    @SerializedName("ended_at")
    private Long endedAt;

    @Expose
    private String exrule;

    @Expose
    private String rrule;

    @Expose
    @SerializedName("started_at")
    private Long startedAt;

    @Expose
    private String timezone;

    public EventPattern() {
    }

    public EventPattern(Long duration,
                        Long endedAt,
                        String exrule,
                        String rrule,
                        Long startedAt,
                        String timezone) {

        this.duration = duration;
        this.endedAt = endedAt;
        this.exrule = exrule;
        this.rrule = rrule;
        this.startedAt = startedAt;
        this.timezone = timezone;
    }

    public Long getId() {
        return id;
    }



    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Long endedAt) {
        this.endedAt = endedAt;
    }

    public String getExrule() {
        return exrule;
    }

    public void setExrule(String exrule) {
        this.exrule = exrule;
    }

    public String getRrule() {
        return rrule;
    }

    public void setRrule(String rrule) {
        this.rrule = rrule;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
