package com.example.planner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    HashMap<Integer, List<String>> events = new HashMap<>();
    private com.applandeo.materialcalendarview.CalendarView calendar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.calendarView);

        for(int j = 1; j < 31; j++) {
            List<String> tempList = new ArrayList<>();
            for (Integer i = 0; i < j; i++)
                tempList.add(i.toString());
            events.put(j, tempList);
        }

        calendar.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Integer clickedDayCalendar = eventDay.getCalendar().get(Calendar.DAY_OF_MONTH);
                createDayAdapter(clickedDayCalendar);
            }
        });

    }


    private void createDayAdapter(final Integer day) {
        final ListView eventList = (ListView)findViewById(R.id.events);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, events.get(day));

        eventList.setAdapter(adapter);

        //todo edit
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List tempList = events.get(day);
                tempList.set(position, "1");
                events.put(day,tempList);

                TextView textView = (TextView)view;
                textView.setText("1");

            }
        });

    }

}
