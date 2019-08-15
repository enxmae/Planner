package com.example.planner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class DayActivity extends AppCompatActivity {


    private List<HashMap<Integer, List<EventFullInformation>>> sortedEvents = new ArrayList<>();
    private List<List<ListView>> eventsListViews = new ArrayList<>();
    private List<LinearLayout> linearLayout = new ArrayList<>();
    private List<EventFullInformation> tempEventInfoList;

    private LinearLayout weekLinearLayout;
    private ListView listView;
    private TextView currentDayTextView;
    private GregorianCalendar gregorianCalendar = new GregorianCalendar();
    private Long eventDateInMills;

    private HashMap<Integer,
            HashMap<Integer,
                    HashMap<Integer,
                            List<EventFullInformation> >>> events = new HashMap<>();

    Integer year;
    Integer month;
    Integer day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        currentDayTextView = findViewById(R.id.currentDateTextView);
        weekLinearLayout = findViewById(R.id.weekLinearLayout);
      //  eventList = findViewById(R.id.events);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        eventDateInMills = Long.parseLong(intent.getStringExtra("eventDateInMills"));

        events = (HashMap<Integer,
                HashMap<Integer,
                        HashMap<Integer,
                                List<EventFullInformation> >>>) bundle.getSerializable("tempEventInfoList");


        gregorianCalendar.setTimeInMillis(eventDateInMills);
        year = gregorianCalendar.get(Calendar.YEAR);
        month = gregorianCalendar.get(Calendar.MONTH);
        day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

        dispayCurrentDay();



        for(int i = 0; i < 1; i++) {
            sortedEvents.add(new HashMap<>());

            linearLayout.add(new LinearLayout(this));
            linearLayout.get(i).setOrientation(LinearLayout.VERTICAL);
            weekLinearLayout.addView(linearLayout.get(i));

            eventsListViews.add(new ArrayList<>());

            createDayAdapter(i);
            day += 1;
        }

    }

    private void createDayAdapter(int dayOfWeek) {
        for(int i = 0; i < 24; i++) {
            sortedEvents.get(dayOfWeek).put(i, new ArrayList<>());
        }

        sortEventsByHours(dayOfWeek);

        for(int i = 0; i < eventsListViews.get(dayOfWeek).size(); i++) {
            ArrayList<String> tempList = new ArrayList<>();

            for(int j = 0; j < sortedEvents.get(dayOfWeek).get(i).size(); j++) {

                tempList.add(sortedEvents.get(dayOfWeek).get(i).get(j).toString());

                int finalI = i;
                eventsListViews.get(dayOfWeek).get(i).setOnItemClickListener((parent, view, position, id) -> {

                    Intent intent1 = new Intent(this, EventActionActivity.class);
                    Event event = sortedEvents.get(dayOfWeek).get(finalI).get(position).getEvent();
                    EventPattern eventPattern = sortedEvents.get(dayOfWeek).get(finalI).get(position).getEventPattern();


                    intent1.putExtra("eventId", event.getId().toString());
                    intent1.putExtra("patternId", eventPattern.getId().toString());
                    intent1.putExtra("eventName", event.getName());
                    intent1.putExtra("eventDetails", event.getDetails());
                    intent1.putExtra("eventStatus", event.getStatus());

                    startActivity(intent1);


                });
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getBaseContext(),
                    android.R.layout.simple_list_item_1,
                    tempList);

            eventsListViews.get(dayOfWeek).get(i).setAdapter(adapter);



        }



    }

    private void sortEventsByHours(int dayOfWeek) {


        tempEventInfoList = events.get(year).get(month).get(day);
        if(tempEventInfoList == null) {
            tempEventInfoList = new ArrayList<>();
        }

        for(int i = 0; i < tempEventInfoList.size(); i++) {
            gregorianCalendar.setTimeInMillis(tempEventInfoList.get(i).getStartedAt());
            sortedEvents.get(dayOfWeek).get(gregorianCalendar.get(Calendar.HOUR_OF_DAY)).add(tempEventInfoList.get(i));
        }

        for(Integer i = 0; i < 24; i++) {
            TextView textView = new TextView(this);
            textView.setText(i.toString() + ":00");
            textView.setTextSize(20);


            eventsListViews.get(dayOfWeek).add(new ListView(this));

            eventsListViews.get(dayOfWeek).get(i).setNestedScrollingEnabled(true);

            linearLayout.get(dayOfWeek).addView(textView);
            linearLayout.get(dayOfWeek).addView(eventsListViews.get(dayOfWeek).get(i));

        }


    }

    public void callAddEventActivity(View view) {
        Intent intent = new Intent(this, AddEnventActivity.class);

        intent.putExtra("date", eventDateInMills.toString());

        startActivity(intent);
    }

    private void dispayCurrentDay() {
        gregorianCalendar.setTimeInMillis(eventDateInMills);

        currentDayTextView.setText(  gregorianCalendar.get(Calendar.DAY_OF_MONTH) + " " +
                (gregorianCalendar.get(Calendar.MONTH) + 1) + " " +
                gregorianCalendar.get(Calendar.YEAR));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //linearLayout.removeAllViews();
       // createDayAdapter();

    }

}
