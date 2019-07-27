package com.example.planner;

import com.example.planner.dao.Event;
import com.example.planner.dao.EventInstance;
import com.example.planner.dao.EventPattern;

public class EventFullInformation {
    private Event event;
    private EventPattern eventPattern;
    private EventInstance eventInstance;

    public EventFullInformation() {

    }

    public EventFullInformation(Event event
                         ) {

        this.event = event;

    }

    @Override
    public String toString() {
        return  "Name " + event.getName() + " started at " + eventPattern.getStartedAt();
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
}
