package com.example.planner.service;


import android.widget.ArrayAdapter;

import com.applandeo.materialcalendarview.EventDay;
import com.example.planner.EventFullInformation;
import com.example.planner.NetworkService;
import com.example.planner.R;
import com.example.planner.dao.Event;
import com.example.planner.dao.EventPattern;
import com.example.planner.dto.EventInstanceResponse;
import com.example.planner.dto.EventPatternResponse;
import com.example.planner.dto.EventResponse;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventService {
    public int count = 1;
    private com.applandeo.materialcalendarview.CalendarView calendar;
    private Long MILLS_IN_DAY = 86400000L;

    private NetworkService networkService = NetworkService.getInstance();
    private String userToken = "serega_mem";

    private Long currentDate;
    private GregorianCalendar gregorianCalendar;

    private HashMap<Integer,
            HashMap<Integer,
                    HashMap<Integer,
                            List<EventFullInformation> >>> events = new HashMap<>();

    private HashMap<Long, List<String>> eventsName = new HashMap<>();

    private Long from;
    private Long to;


    public EventService() {
        gregorianCalendar = new GregorianCalendar();
    }

    public void calculateInstancesToCurrentMonth(Long currentDate) {

    }

    public HashMap<Integer, HashMap<Integer, HashMap<Integer, List<EventFullInformation>>>> getEvents() {

        return events;
    }

    private int calculateDaysInMonth() {
        int daysInMonth;
        gregorianCalendar.setTimeInMillis(currentDate);

        daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return daysInMonth;
    }

    private Long calculateFrom() {
        gregorianCalendar.setTimeInMillis(currentDate);
        int year = gregorianCalendar.get(Calendar.YEAR);
        int month = gregorianCalendar.get(Calendar.MONTH);

        gregorianCalendar.set(year, month, 1);
        from = gregorianCalendar.getTimeInMillis();

        return from;
    }

    private Long calculateTo() {
        to = from + calculateDaysInMonth() * MILLS_IN_DAY - 1;
        return to;
    }

    private void calculateDateToEvents(List<EventFullInformation> eventFullInformations) {
        Integer year;
        Integer month;
        Integer day;

        for(int i = 0; i < eventFullInformations.size(); i++) {
            Long date = eventFullInformations.get(i).getEventPattern().getStartedAt();
            gregorianCalendar.setTimeInMillis(date);

            year = gregorianCalendar.get(Calendar.YEAR);
            month = gregorianCalendar.get(Calendar.MONTH);
            day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

            if(!events.containsKey(year)) {
                events.put(year, new HashMap<>());
            }

            if(!events.get(year).containsKey(month)) {
                events.get(year).put(month, new HashMap<>());
            }

            if(!events.get(year).get(month).containsKey(day)) {
                events.get(year).get(month).put(day, new ArrayList<>());
            }



            events.get(year).get(month).get(day).add(eventFullInformations.get(i));

        }

    }

    public HashMap<Integer, HashMap<Integer, HashMap<Integer, List<EventFullInformation>>>>
    giveInstancesFromTo() {
        List<EventFullInformation> eventInfoTempList = new ArrayList<>();
        List<String> tempList = new ArrayList<>();

        from = calculateFrom();
        to = calculateTo();

        Call<EventInstanceResponse> eventInstanceResponseCall = networkService
                .getEventRepository()
                .getInstances(from, to, userToken);

        eventInstanceResponseCall.enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {

                Long[] ids = new Long[response.body().getCount()];
                Long[] patternIds = new Long[response.body().getCount()];

                for (int i = 0; i < ids.length; i++) {
                    ids[i] = response.body().getData()[i].getEventId();
                    patternIds[i] = response.body().getData()[i].getPatternId();
                }


                    networkService.getEventRepository()
                            .getEventsByIds(ids, userToken)
                            .enqueue(new Callback<EventResponse>() {

                                @Override
                                public void onResponse(Call<EventResponse> call,
                                                       Response<EventResponse> response) {



                                    Event[] events = response.body().getData();
                                    for(int i = 0; i < ids.length; i++) {
                                        for(int j = 0; j < events.length; j++) {
                                            if(ids[i].equals(events[j].getId()))
                                                eventInfoTempList.add(new EventFullInformation(events[j]));
                                        }
                                    }



                                    networkService.getEventPatternRepository()
                                            .getEventPatterns(ids, userToken)
                                            .enqueue(new Callback<EventPatternResponse>() {

                                                @Override
                                                public void onResponse(Call<EventPatternResponse> call,
                                                                       Response<EventPatternResponse> response) {
                                                    if (response.isSuccessful()) {

                                                        EventPattern[] eventPatterns = response.body().getData();
                                                        for(int i = 0; i < patternIds.length; i++) {
                                                            for(int j = 0; j < eventPatterns.length; j++) {
                                                                if(patternIds[i].equals(eventPatterns[j].getId()));
                                                                eventInfoTempList.get(i).setEventPattern(eventPatterns[j]);
                                                            }
                                                        }
                                                        calculateDateToEvents(eventInfoTempList);

                                                        /*for (int i = 0; i < eventInfoTempList.size(); i++) {
                                                            tempList.add(eventInfoTempList.get(i).toString());
                                                        }
*/
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<EventPatternResponse> call, Throwable t) {

                                                }
                                            });

                                }

                                @Override
                                public void onFailure(Call<EventResponse> call, Throwable t) {

                                }
                            });




            }

            @Override
            public void onFailure(Call<EventInstanceResponse> call, Throwable t) {

            }
        });


        return events;

    }


    private int getYear() {

        return gregorianCalendar.get(Calendar.YEAR);
    }

    private int getMonth() {
        return gregorianCalendar.get(Calendar.MONTH);
    }

    private int getDay() {
        return  gregorianCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setCurrentDate(Long currentDate) {
        this.currentDate = currentDate;
    }


}
