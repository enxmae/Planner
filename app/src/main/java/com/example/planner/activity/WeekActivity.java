package com.example.planner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.planner.EventFullInformation;
import com.example.planner.R;
import com.example.planner.dao.Event;
import com.example.planner.dao.EventPattern;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class WeekActivity extends AppCompatActivity {

    private LinearLayout weekLinearLayout;

    private HashMap<Integer,
            HashMap<Integer,
                    HashMap<Integer,
                            List<EventFullInformation> >>> events = new HashMap<>();

    private HashMap<Integer, HashMap<Integer, List<EventFullInformation>>> sortedEvents = new HashMap<>();
    private List<List<EventFullInformation>> tempEventInfoList = new ArrayList<>();
    private List<List<ListView>> eventsListViews = new ArrayList<>();
    private List<Integer> weekDays = new ArrayList<>();
    private List<LinearLayout> linearLayouts = new ArrayList<>();
    private Long eventDateInMills;
    private GregorianCalendar gregorianCalendar = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        weekLinearLayout = findViewById(R.id.weekLinearLayout);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        events = (HashMap<Integer,
                HashMap<Integer,
                        HashMap<Integer,
                                List<EventFullInformation> >>>) bundle.getSerializable("tempEventInfoList");

        eventDateInMills = Long.parseLong(intent.getStringExtra("eventDateInMills"));

        gregorianCalendar.setTimeInMillis(eventDateInMills);
        int day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

        for(int i = 0; i < 7; i++) {
            weekDays.add(day);
            day += 1;
        }

        createWeekAdapter();

    }


    private void createWeekAdapter() {
        for(int j = 0; j < 7; j++) {
            sortedEvents.put(weekDays.get(j), new HashMap<>());
            for (int i = 0; i < 24; i++) {
                sortedEvents.get(weekDays.get(j)).put(i, new ArrayList<>());
            }
        }

        gregorianCalendar.setTimeInMillis(eventDateInMills);
        Integer year = gregorianCalendar.get(Calendar.YEAR);
        Integer month = gregorianCalendar.get(Calendar.MONTH);
        Integer day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

        for(int i = 0; i < 7; i++) {
            tempEventInfoList.add(weekDays.get(i), events.get(year).get(month).get(day));
        }

        sortEventsByHours();

        for(int i = 0; i < eventsListViews.size(); i++) {
            ArrayList<String> tempList = new ArrayList<>();

            for(int j = 0; j < sortedEvents.get(i).size(); j++) {

                tempList.add(sortedEvents.get(i).get(j).toString());

                /*int finalI = i;
                eventsListViews.get(i).setOnItemClickListener((parent, view, position, id) -> {

                    Intent intent1 = new Intent(this, EventActionActivity.class);
                    Event event = sortedEvents.get(finalI).get(position).getEvent();
                    EventPattern eventPattern = sortedEvents.get(finalI).get(position).getEventPattern();


                    intent1.putExtra("eventId", event.getId().toString());
                    intent1.putExtra("patternId", eventPattern.getId().toString());
                    intent1.putExtra("eventName", event.getName());
                    intent1.putExtra("eventDetails", event.getDetails());
                    intent1.putExtra("eventStatus", event.getStatus());

                    startActivity(intent1);


                });*/
            }





        }

    }

    private void calculateFirstWeekDay() {

    }

    private void sortEventsByHours() {
        if(tempEventInfoList == null) {
            tempEventInfoList = new ArrayList<>();
        }

        for(int day = 0; day < 7; day++) {
            for (int i = 0; i < tempEventInfoList.get(weekDays.get(day)).size(); i++) {
                gregorianCalendar.setTimeInMillis(tempEventInfoList.get(weekDays.get(day)).get(i).getStartedAt());
                sortedEvents.get(weekDays.get(day)).get(gregorianCalendar.get(Calendar.HOUR_OF_DAY)).add(tempEventInfoList.get(weekDays.get(day)).get(i));
            }
        }



        for(Integer i = 0; i < sortedEvents.size(); i++) {
           linearLayouts.add(new LinearLayout(this));
           linearLayouts.get(i).setOrientation(LinearLayout.VERTICAL);
           eventsListViews.add(new ArrayList<>());

            for(Integer j = 0; j < sortedEvents.get(i).size(); j++) {
                TextView textView = new TextView(this);
                textView.setText(i.toString() + ":00");
                textView.setTextSize(20);


                eventsListViews.get(i).add(new ListView(this));

                eventsListViews.get(i).get(j).setNestedScrollingEnabled(true);

                linearLayouts.get(i).addView(textView);
                linearLayouts.get(i).addView(eventsListViews.get(i).get(j));

                //linearLayout.setMinimumHeight(1000);
            }
            weekLinearLayout.addView(linearLayouts.get(i));
        }


    }

}
