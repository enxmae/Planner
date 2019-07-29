package com.example.planner.activity;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.planner.NetworkService;
import com.example.planner.R;
import com.example.planner.dao.Event;
import com.example.planner.dao.EventPattern;
import com.example.planner.dto.EventPatternResponse;
import com.example.planner.dto.EventResponse;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.ical.compat.jodatime.LocalDateIterable;
import com.google.ical.compat.jodatime.LocalDateIterator;
import com.google.ical.compat.jodatime.LocalDateIteratorFactory;

import com.google.ical.values.RRule;
import com.google.ical.values.Frequency;



public class AddEnventActivity extends AppCompatActivity {

    private NetworkService networkService = NetworkService.getInstance();
    private String userToken = "serega_mem";

    private TextView eventName;
    private TextView eventDetails;
    private TextView eventStatus;
    private TextView eventInterval;
    private TextView eventCount;

    private DatePicker eventStartDatePicker;
    private DatePicker eventEndDatePicker;
    private TimePicker eventStartTimePicker;
    private TimePicker eventEndTimePicker;

    private RadioGroup setFrequencyRB;
    private RadioButton byDayButton;
    private RadioButton byWeekButton;
    private RadioButton byMonthButton;

    private GregorianCalendar gregorianCalendar;
    private org.joda.time.LocalDate localDate;

    private Long startAt;
    private Long endAt;
    private Long duration;
    private String RRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_envent);

        eventName = findViewById(R.id.eventName);
        eventDetails = findViewById(R.id.eventDetails);
        eventStatus = findViewById(R.id.eventStatus);
        eventInterval = findViewById(R.id.eventInterval);
        eventCount = findViewById(R.id.eventCount);

        eventStartDatePicker = findViewById(R.id.eventStartDatePicker);
        eventEndDatePicker = findViewById(R.id.eventEndDatePicker);
        eventStartTimePicker = findViewById(R.id.eventStartTimePicker);
        eventEndTimePicker = findViewById(R.id.eventEndTimePicker);

        setFrequencyRB = findViewById(R.id.setFrequencyRG);
        byDayButton = findViewById(R.id.byDayButton);
        byWeekButton = findViewById(R.id.byWeekButton);
        byMonthButton = findViewById(R.id.byMonthButton);

        eventStartTimePicker.setIs24HourView(true);
        eventEndTimePicker.setIs24HourView(true);

    }

    public void addEvent(View view) throws ParseException {

        gregorianCalendar = new GregorianCalendar();
        RRule = createRRule();

        gregorianCalendar.set(
                eventStartDatePicker.getYear(),
                eventStartDatePicker.getMonth(),
                eventStartDatePicker.getDayOfMonth(),
                eventStartTimePicker.getHour(),
                eventStartTimePicker.getMinute());

        startAt = gregorianCalendar.getTimeInMillis();
        localDate = org.joda.time.LocalDate.fromCalendarFields(gregorianCalendar);

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

        if(RRule != null) {
            endAt = Long.MAX_VALUE - 1;

            if(!eventCount.getText().toString().equals("")) {
                endAt = calculateEndAt(RRule);
            }

        }

        EventPattern eventPattern = new EventPattern(duration,
                endAt,
                null,
                RRule,
                startAt,
                TimeZone.getDefault().getID());


        Call<EventResponse> eventResponseCall = networkService
                .getEventRepository()
                .saveEvent(event, userToken);

        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                networkService
                        .getEventPatternRepository()
                        .savePattern(response.body()
                                .getData()[0].getId(), eventPattern, userToken)
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

    private String createRRule() {
        String RRule = "";
        String interval = "INTERVAL=";
        String count = "COUNT=";
        String intervalCount = "1";
        String countAmount = "";


        if(!eventInterval.getText().toString().equals(""))
            intervalCount = eventInterval.getText().toString();

        if(!eventCount.getText().toString().equals("")) {
            countAmount = eventCount.getText().toString();
            count = count + countAmount + ";";
        } else count = "";

        interval += intervalCount;

        if(byDayButton.isChecked()) {
            RRule += "FREQ=DAILY;";
        } else if(byWeekButton.isChecked()) {
            RRule += "FREQ=WEEKLY;";
        } else if(byMonthButton.isChecked()) {
            RRule += "FREQ=MONTHLY;";
        } else RRule = null;

        if(RRule != null) {
            RRule = RRule + count  + interval;
        }

        return RRule;
    }


    private Long calculateEndAt(String RRule) throws ParseException {
        String SRule = "RRULE:" + RRule;

        LocalDateIterable localDateIterable = LocalDateIteratorFactory
                .createLocalDateIterable(SRule, localDate, true);

        LocalDateIterator iterator = localDateIterable.iterator();
        List<Date> dates = new ArrayList<>();
        while(iterator.hasNext())
            dates.add(iterator.next().toDate());

        gregorianCalendar.setTime(dates.get(dates.size()-1));

        return gregorianCalendar.getTimeInMillis();
    }
}
