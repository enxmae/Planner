package com.example.planner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planner.NetworkService;
import com.example.planner.R;
import com.example.planner.dao.EventInstance;
import com.example.planner.dto.EventInstanceResponse;
import com.example.planner.dto.EventResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    HashMap<Integer, List<String>> events = new HashMap<>();
    private com.applandeo.materialcalendarview.CalendarView calendar;
    private NetworkService networkService = NetworkService.getInstance();

    private GregorianCalendar gregorianCalendar = new GregorianCalendar();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.calendarView);

        calendar.setOnDayClickListener(eventDay -> {
            Integer clickedDayCalendar = eventDay.getCalendar().get(Calendar.DAY_OF_MONTH);

            //Toast.makeText(getApplicationContext(),clickedDayCalendar.toString() , Toast.LENGTH_LONG).show();
            createDayAdapter(clickedDayCalendar);
        });

    }


    private void createDayAdapter(final Integer day) {
        List<String> tempList = Collections.EMPTY_LIST;
        events.put(day, tempList);


        gregorianCalendar.set(2019, Calendar.JULY, day, 0, 0);
        Long from = gregorianCalendar.getTimeInMillis();

        gregorianCalendar.set(2019, Calendar.JULY, day, 23, 59);
        Long to = gregorianCalendar.getTimeInMillis();

        Call<EventInstanceResponse> eventInstanceResponseCall = networkService
                .getEventRepository()
                .getInstances(from, to, "serega_mem");

        eventInstanceResponseCall.enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {

                Toast.makeText(getApplicationContext(),
                        response.body().getData()[1].getEventId().toString() +
                        response.body().getData()[1].getPatternId().toString(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<EventInstanceResponse> call, Throwable t) {

            }
        });


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


    public void Connect(View view) {
        Long[] ids = new Long[]{88L};

        Call<EventResponse> eventResponseCall = networkService
                .getEventRepository()
                .getEventsByIds(ids, "serega_mem");

        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                Toast.makeText(getApplicationContext(),response.body().getData()[0].getName() , Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });

    }

    public void callAddEventActivity(View view) {
        Intent intent = new Intent(this, AddEnventActivity.class);
        startActivity(intent);
    }

}
