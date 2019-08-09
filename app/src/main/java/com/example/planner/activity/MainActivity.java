package com.example.planner.activity;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.EventDay;
import com.example.planner.EventFullInformation;
import com.example.planner.NetworkService;
import com.example.planner.R;
import com.example.planner.dao.Event;
import com.example.planner.dao.EventPattern;
import com.example.planner.dto.EventInstanceResponse;
import com.example.planner.dto.EventPatternResponse;
import com.example.planner.dto.EventResponse;
import com.example.planner.service.EventService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private Long MILLS_IN_DAY = 86400000L;
    private EventService eventService = new EventService();

    private HashMap<Integer,
            HashMap<Integer,
                    HashMap<Integer,
                            List<EventFullInformation> >>> events = new HashMap<>();

    private HashMap<Long, List<String>> eventsName = new HashMap<>();
    private Long eventDateInMills;
    List<EventDay> mEventDays = new ArrayList<>();

    private com.applandeo.materialcalendarview.CalendarView calendar;
    private NetworkService networkService = NetworkService.getInstance();

    private GregorianCalendar gregorianCalendar = new GregorianCalendar();
    private String userToken = "serega_mem";

    private TextView currentDayTextView;
    List<EventDay> drawableEvents = new ArrayList<>();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentDayTextView = findViewById(R.id.currentDateTextView);
        calendar = findViewById(R.id.calendarView);

        //showMonthEvents();

        dispayCurrentDay();


        calendar.setOnDayClickListener(eventDay -> {
            Long mills = eventDay.getCalendar().getTimeInMillis();
            gregorianCalendar.setTimeInMillis(mills);

            dispayCurrentDay();
            createDayAdapter(mills);
        });

        eventDateInMills = gregorianCalendar.getTimeInMillis();

        eventService.setCurrentDate(eventDateInMills);

        //events = eventService.getEvents();


       // while(events.isEmpty()) {
            //events = eventService.getEvents();
       // }

       // events.clear();
    }


    public void createDayAdapter(final Long dateMills)  {

    eventDateInMills = dateMills;
    gregorianCalendar.setTimeInMillis(dateMills);

    Integer year = gregorianCalendar.get(Calendar.YEAR);
    Integer month = gregorianCalendar.get(Calendar.MONTH);
    Integer day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

    final ListView eventList = (ListView) findViewById(R.id.events);


    List<EventFullInformation> eventInfoTempList = new ArrayList<>();


        Long from = calculateFrom();
        Long to = calculateTo();

        Call<EventInstanceResponse> eventInstanceResponseCall = networkService
                .getEventRepository()
                .getInstances(from, to, userToken);

        eventInstanceResponseCall.enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {

                Long[] ids = new Long[response.body().getCount()];
                Long[] patternIds = new Long[response.body().getCount()];
                Long[] startedAt = new Long[response.body().getCount()];

                for (int i = 0; i < ids.length; i++) {
                    ids[i] = response.body().getData()[i].getEventId();
                    patternIds[i] = response.body().getData()[i].getPatternId();
                    startedAt[i] = response.body().getData()[i].getStartedAt();
                }


                networkService.getEventRepository()
                        .getEventsByIds(ids, userToken)
                        .enqueue(new Callback<EventResponse>() {

                            @Override
                            public void onResponse(Call<EventResponse> call,
                                                   Response<EventResponse> response) {



                                Event[] tempEvents = response.body().getData();
                                for(int i = 0; i < ids.length; i++) {
                                    for(int j = 0; j < tempEvents.length; j++) {
                                        if(ids[i].equals(tempEvents[j].getId()))
                                            eventInfoTempList.add(new EventFullInformation(tempEvents[j]));
                                    }
                                    eventInfoTempList.get(i).setStartedAt(startedAt[i]);
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
                                                            if(patternIds[i].equals(eventPatterns[j].getId()))
                                                                eventInfoTempList.get(i).setEventPattern(eventPatterns[j]);
                                                        }
                                                    }
                                                    calculateDateToEvents(eventInfoTempList);
                                                    List<EventFullInformation> tempEventInfoList = Collections.EMPTY_LIST;



                                                    if(events.containsKey(year)) {
                                                        if(events.get(year).containsKey(month)) {
                                                            if(events.get(year).get(month).containsKey(day)) {
                                                                tempEventInfoList = events.get(year).get(month).get(day);
                                                            }
                                                        }
                                                    }

                                                    if(events.containsKey(year)) {
                                                        if(events.get(year).containsKey(month)) {
                                                            Set<Integer> days = events.get(year).get(month).keySet();
                                                            for(Integer i : days) {

                                                                Calendar calendar1 = Calendar.getInstance();
                                                                calendar1.set(year, month, i);

                                                                drawableEvents.add(new EventDay(calendar1, R.drawable.ic_star));
                                                                calendar.setEvents(drawableEvents);
                                                            }
                                                        }
                                                    }

                                                    List<String> tempList = new ArrayList<>();


                                                    for(int i = 0; i < tempEventInfoList.size(); i++) {
                                                        tempList.add(tempEventInfoList.get(i).toString());
                                                    }

                                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                                            getBaseContext(),
                                                            android.R.layout.simple_list_item_1,
                                                            tempList);

                                                    eventList.setAdapter(adapter);

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




    eventList.setOnItemClickListener((parent, view, position, id) -> {

        Intent intent = new Intent(getApplicationContext(), EventActionActivity.class);
        Event event = events.get(year).get(month).get(day).get(position).getEvent();
        EventPattern eventPattern = events.get(year).get(month).get(day).get(position).getEventPattern();


        intent.putExtra("eventId", event.getId().toString());
        intent.putExtra("patternId", eventPattern.getId().toString());
        intent.putExtra("eventName", event.getName());
        intent.putExtra("eventDetails", event.getDetails());
        intent.putExtra("eventStatus", event.getStatus());
        intent.putExtra("date", eventDateInMills.toString());

        startActivity(intent);


    });

    }


    public void callAddEventActivity(View view) {
        Intent intent = new Intent(this, AddEnventActivity.class);

        intent.putExtra("date", eventDateInMills.toString());

        startActivity(intent);
    }

    private void showMonthEvents() {
        Long mills1 = 1564581600000L;


        for(int i = 1; i <= 31; i++) {

            createDayAdapter(mills1);
            mills1 +=  86400000L;

        }


    }

    private void giveMonthEvents() {



    }

    private void dispayCurrentDay() {
        currentDayTextView.setText(  gregorianCalendar.get(Calendar.DAY_OF_MONTH) + " " +
                (gregorianCalendar.get(Calendar.MONTH) + 1) + " " +
                gregorianCalendar.get(Calendar.YEAR));
    }

    @Override
    protected void onResume() {
        super.onResume();
       //createDayAdapter(eventDateInMills);

    }

    private int calculateDaysInMonth() {
        int daysInMonth;
        gregorianCalendar.setTimeInMillis(eventDateInMills);

        daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return daysInMonth;
    }


    private Long calculateFrom() {
        gregorianCalendar.setTimeInMillis(eventDateInMills);
        int year = gregorianCalendar.get(Calendar.YEAR);
        int month = gregorianCalendar.get(Calendar.MONTH);

        gregorianCalendar.set(year, month, 1);
        Long from = gregorianCalendar.getTimeInMillis();

        return from;
    }

    private Long calculateTo() {
        Long to = calculateFrom() + calculateDaysInMonth() * MILLS_IN_DAY - 1;
        return to;
    }

    private void calculateDateToEvents(List<EventFullInformation> eventFullInformations) {
        Integer year;
        Integer month;
        Integer day;

        events.clear();

        for(int i = 0; i < eventFullInformations.size(); i++) {
            Long date = eventFullInformations.get(i).getStartedAt();
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

}