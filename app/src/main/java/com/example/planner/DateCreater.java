package com.example.planner;

import java.util.GregorianCalendar;

public class DateCreater {

    private Long mills;
    private GregorianCalendar date = new GregorianCalendar();

    DateCreater(Long mills) {
        this.mills = mills;
        this.date.setTimeInMillis(this.mills);
    }

    public Integer getDay() {
        return date.get(GregorianCalendar.DAY_OF_MONTH);
    }

    public Integer getMonth() {
        return date.get(GregorianCalendar.MONTH);
    }

    public Integer getYear() {
        return date.get(GregorianCalendar.YEAR);
    }

}
