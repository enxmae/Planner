package com.example.planner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planner.EventFullInformation;
import com.example.planner.NetworkService;
import com.example.planner.R;
import com.example.planner.dao.Event;
import com.example.planner.dao.EventPattern;
import com.example.planner.dto.EventInstanceResponse;
import com.example.planner.dto.EventPatternResponse;
import com.example.planner.dto.EventResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    HashMap<Integer, List<EventFullInformation>> events = new HashMap<>();
    HashMap<Integer, List<String>> eventsName = new HashMap<>();

    private com.applandeo.materialcalendarview.CalendarView calendar;
    private NetworkService networkService = NetworkService.getInstance();

    private GregorianCalendar gregorianCalendar = new GregorianCalendar();
    private String userToken = "serega_mem";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.calendarView);

        calendar.setOnDayClickListener(eventDay -> {
            Long mills = eventDay.getCalendar().getTimeInMillis();

            createDayAdapter(mills);
        });

    }


    private void createDayAdapter(final Long dateMills) {

        gregorianCalendar.setTimeInMillis(dateMills);

        Integer year = gregorianCalendar.get(Calendar.YEAR);
        Integer month = gregorianCalendar.get(Calendar.MONTH);
        Integer day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

        final ListView eventList = (ListView)findViewById(R.id.events);

        gregorianCalendar.set(year, month, day, 0, 0);
        Long from = gregorianCalendar.getTimeInMillis();

        gregorianCalendar.set(year, month, day, 23, 59);
        Long to = gregorianCalendar.getTimeInMillis();

        Call<EventInstanceResponse> eventInstanceResponseCall = networkService
                .getEventRepository()
                .getInstances(from, to, userToken);

        eventInstanceResponseCall.enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {

                Long[] ids = new Long[response.body().getCount()];
                List<EventFullInformation> eventInfoTempList = new ArrayList<>();
                List<String> tempList = new ArrayList<>();

                for(int i = 0; i < ids.length; i++) {
                    ids[i] = response.body().getData()[i].getEventId();
                }

                networkService.getEventRepository()
                        .getEventsByIds(ids, userToken)
                        .enqueue(new Callback<EventResponse>() {

                            @Override
                            public void onResponse(Call<EventResponse> call,
                                                   Response<EventResponse> response) {

                                List<String> tempList = new ArrayList<String>();
                                for(int i = 0; i < ids.length; i++) {
                                    Event event = response.body().getData()[i];
                                    eventInfoTempList.add(new EventFullInformation(event));
                                }


                            }

                            @Override
                            public void onFailure(Call<EventResponse> call, Throwable t) {

                            }
                        });

                networkService.getEventPatternRepository()
                        .getEventPatterns(ids, userToken)
                        .enqueue(new Callback<EventPatternResponse>() {

                            @Override
                            public void onResponse(Call<EventPatternResponse> call,
                                                   Response<EventPatternResponse> response) {

                                for(int i = 0; i < ids.length; i++) {
                                    EventPattern eventPattern = response.body().getData()[i];
                                    eventInfoTempList.get(i).setEventPattern(eventPattern);
                                }

                                for(int i = 0; i < eventInfoTempList.size(); i++) {
                                    tempList.add(eventInfoTempList.get(i).toString());
                                }
                                events.put(day, eventInfoTempList);




                                eventsName.put(day, tempList);

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                        getBaseContext(),
                                        android.R.layout.simple_list_item_1,
                                        tempList);

                                eventList.setAdapter(adapter);

                            }

                            @Override
                            public void onFailure(Call<EventPatternResponse> call, Throwable t) {

                            }
                        });





            }

            @Override
            public void onFailure(Call<EventInstanceResponse> call, Throwable t) {

            }
        });


        //todo edit
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List tempList = eventsName.get(day);
                tempList.set(position, "1");
                eventsName.put(day,tempList);

                TextView textView = (TextView)view;

                textView.setText("1");

            }
        });

    }

    
    public void callAddEventActivity(View view) {
        Intent intent = new Intent(this, AddEnventActivity.class);
        startActivity(intent);
    }

}