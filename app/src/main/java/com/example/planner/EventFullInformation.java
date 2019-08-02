package com.example.planner;

import com.example.planner.dao.Event;
import com.example.planner.dao.EventInstance;
import com.example.planner.dao.EventPattern;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class EventFullInformation {
    private Event event;
    private EventPattern eventPattern;
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
                " started at " + startAtHour + ":" + startAtMinute +
                " ended at " + endAtHour + ":" + endAtMinute;
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

    private void calculateDate() {
        gregorianCalendar.setTimeInMillis(eventPattern.getStartedAt());

        startAtHour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
        startAtMinute = gregorianCalendar.get(Calendar.MINUTE);

        gregorianCalendar.setTimeInMillis(gregorianCalendar.getTimeInMillis() + eventPattern.getDuration());

        endAtHour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);
        endAtMinute = gregorianCalendar.get(Calendar.MINUTE);


    }
}
