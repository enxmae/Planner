package com.example.planner;

import com.example.planner.dao.Event;
import com.example.planner.dao.EventInstance;
import com.example.planner.dao.EventPattern;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class EventFullInformation implements Serializable {
    private Event event;
    private EventPattern eventPattern;
    private Long startedAt;
    private EventInstance eventInstance;
    private GregorianCalendar gregorianCalendar = new GregorianCalendar();

    private Integer startAtHour;
    private Integer startAtMinute;
    private Integer endAtHour;
    private Integer endAtMinute;

    public EventFullInformation(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        calculateDate();

        return  "Name " + event.getName() +
                " started at " + IntToStr(startAtHour) + ":" + IntToStr(startAtMinute) +
                " ended at " + IntToStr(endAtHour) + ":" + IntToStr(endAtMinute);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public EventPattern getEventPattern() {
        return eventPattern;
    }

    public void setEventInstance(EventInstance eventInstance) {
        this.eventInstance = eventInstance;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setEventPattern(EventPattern eventPattern) {
        this.eventPattern = eventPattern;
    }

    private void calculateDate() {
        gregorianCalendar.setTimeInMillis(eventPattern.getStartedAt());

        startAtHour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
        startAtMinute = gregorianCalendar.get(Calendar.MINUTE);

        gregorianCalendar.setTimeInMillis(gregorianCalendar.getTimeInMillis() + eventPattern.getDuration());

        endAtHour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
        endAtMinute = gregorianCalendar.get(Calendar.MINUTE);


    }


    private String IntToStr(Integer value) {
        String returnValue = value.toString();
        if(value < 10) returnValue = "0" + returnValue;
        return returnValue;
    }
}
