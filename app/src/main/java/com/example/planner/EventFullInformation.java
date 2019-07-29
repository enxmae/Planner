package com.example.planner;

import com.example.planner.dao.Event;
import com.example.planner.dao.EventInstance;
import com.example.planner.dao.EventPattern;

import java.util.Date;
import java.util.GregorianCalendar;


public class EventFullInformation {
    private Event event;
    private EventPattern eventPattern;
    private EventInstance eventInstance;
    private GregorianCalendar gregorianCalendar = new GregorianCalendar();
    private Date date;

    public EventFullInformation(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        this.date = calculateDate();
        return  "Name " + event.getName() + " started at " + date;
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

    public void setEventPattern(EventPattern eventPattern) {
        this.eventPattern = eventPattern;
    }

    private Date calculateDate() {
        gregorianCalendar.setTimeInMillis(eventPattern.getStartedAt());
        return gregorianCalendar.getTime();
    }
}
