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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

    private GregorianCalendar gregorianCalendar = new GregorianCalendar();

    private Long eventId;
    private Long patternId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_action);

        Intent intent = getIntent();

        eventName = findViewById(R.id.eventName);
        eventDetails = findViewById(R.id.eventDetails);
        eventStatus = findViewById(R.id.eventStatus);

        eventStartDatePicker = findViewById(R.id.eventStartDatePicker);
        eventEndDatePicker = findViewById(R.id.eventEndDatePicker);
        eventStartTimePicker = findViewById(R.id.eventStartTimePicker);
        eventEndTimePicker = findViewById(R.id.eventEndTimePicker);

        eventStartTimePicker.setIs24HourView(true);
        eventEndTimePicker.setIs24HourView(true);

        eventId = Long.parseLong(intent.getStringExtra("eventId"));
        patternId = Long.parseLong(intent.getStringExtra("patternId"));
        Long eventDate = Long.parseLong(intent.getStringExtra("date"));

        eventName.setText(intent.getStringExtra("eventName"));
        eventDetails.setText(intent.getStringExtra("eventDetails"));
        eventStatus.setText(intent.getStringExtra("eventStatus"));

        gregorianCalendar.setTimeInMillis(eventDate);
        eventStartDatePicker.updateDate(
                gregorianCalendar.get(Calendar.YEAR),
                gregorianCalendar.get(Calendar.MONTH),
                gregorianCalendar.get(Calendar.DAY_OF_MONTH));

        eventEndDatePicker.updateDate(
                gregorianCalendar.get(Calendar.YEAR),
                gregorianCalendar.get(Calendar.MONTH),
                gregorianCalendar.get(Calendar.DAY_OF_MONTH));



        networkService
                .getEventPatternRepository()
                .getPatternById(patternId, userToken)
                .enqueue(new Callback<EventPatternResponse>() {

            @Override
            public void onResponse(Call<EventPatternResponse> call,
                                   Response<EventPatternResponse> response) {

                Long startTime = response.body().getData()[0].getStartedAt();
                Long endTime = response.body().getData()[0].getEndedAt();

                gregorianCalendar.setTimeInMillis(startTime);
                eventStartTimePicker.setHour(gregorianCalendar.get(Calendar.HOUR_OF_DAY));
                eventStartTimePicker.setMinute(gregorianCalendar.get(Calendar.MINUTE));

                gregorianCalendar.setTimeInMillis(endTime);
                eventEndTimePicker.setHour(gregorianCalendar.get(Calendar.HOUR_OF_DAY));
                eventEndTimePicker.setMinute(gregorianCalendar.get(Calendar.MINUTE));

            }

            @Override
            public void onFailure(Call<EventPatternResponse> call, Throwable t) {

            }
        });


    }

    public void updateEvent(View view) {

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
                TimeZone.getDefault().getID());

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

    public void deleteEvent(View view) {
        Call<Void> eventResponseCall = networkService
                .getEventRepository()
                .delete(eventId, userToken);

        eventResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void startShareActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ShareActivity.class);

        intent.putExtra("eventId", eventId.toString());
        intent.putExtra("patternId", patternId.toString());

        startActivity(intent);
    }
}
