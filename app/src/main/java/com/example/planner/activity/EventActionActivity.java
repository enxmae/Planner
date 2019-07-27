package com.example.planner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.planner.NetworkService;
import com.example.planner.R;
import com.example.planner.dao.Event;
import com.example.planner.dao.EventPattern;
import com.example.planner.dto.EventPatternResponse;
import com.example.planner.dto.EventResponse;

import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActionActivity extends AppCompatActivity {

    private NetworkService networkService = NetworkService.getInstance();
    private String userToken = "serega_mem";

    private TextView eventName;
    private TextView eventDetails;
    private TextView eventStatus;

    private Button updateButton;
    private Button deleteButton;

    private DatePicker eventStartDatePicker;
    private DatePicker eventEndDatePicker;
    private TimePicker eventStartTimePicker;
    private TimePicker eventEndTimePicker;

    private GregorianCalendar gregorianCalendar;

    private Long eventId;
    private Long patternId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_action);

        Intent intent = getIntent();

        eventId = Long.parseLong(intent.getStringExtra("eventId"));
        patternId = Long.parseLong(intent.getStringExtra("patternId"));

    }

    public void updateEvent(View view) {
        eventName = findViewById(R.id.eventName);
        eventDetails = findViewById(R.id.eventDetails);
        eventStatus = findViewById(R.id.eventStatus);

        eventStartDatePicker = findViewById(R.id.eventStartDatePicker);
        eventEndDatePicker = findViewById(R.id.eventEndDatePicker);
        eventStartTimePicker = findViewById(R.id.eventStartTimePicker);
        eventEndTimePicker = findViewById(R.id.eventEndTimePicker);

        gregorianCalendar = new GregorianCalendar();

        Long startAt;
        Long endAt;
        Long duration;

        gregorianCalendar.set(
                eventStartDatePicker.getYear(),
                eventStartDatePicker.getMonth(),
                eventStartDatePicker.getDayOfMonth(),
                eventStartTimePicker.getHour(),
                eventStartTimePicker.getMinute());

        startAt = gregorianCalendar.getTimeInMillis();

        gregorianCalendar.set(
                eventEndDatePicker.getYear(),
                eventEndDatePicker.getMonth(),
                eventEndDatePicker.getDayOfMonth(),
                eventEndTimePicker.getHour(),
                eventEndTimePicker.getMinute());

        endAt = gregorianCalendar.getTimeInMillis();

        Event event = new Event(
                eventDetails.getText().toString(),
                "Asia/Vladivostok",
                eventName.getText().toString(),
                eventStatus.getText().toString());

        duration = endAt - startAt;

        EventPattern eventPattern = new EventPattern(duration,
                endAt,
                null,
                null,
                startAt,
                "GMT");

        Call<EventResponse> eventResponseCall = networkService
                .getEventRepository()
                .update(eventId, event, userToken);

        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                networkService
                        .getEventPatternRepository()
                        .update(patternId, eventPattern, userToken)
                        .enqueue(new Callback<EventPatternResponse>() {
                            @Override
                            public void onResponse(Call<EventPatternResponse> call,
                                                   Response<EventPatternResponse> response) {

                                finish();
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

}
