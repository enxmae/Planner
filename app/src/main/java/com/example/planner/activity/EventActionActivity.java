package com.example.planner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.planner.NetworkService;
import com.example.planner.R;
import com.example.planner.dao.Event;
import com.example.planner.dao.EventPattern;
import com.example.planner.dto.EventPatternResponse;
import com.example.planner.dto.EventResponse;
import com.google.ical.compat.jodatime.LocalDateIterable;
import com.google.ical.compat.jodatime.LocalDateIterator;
import com.google.ical.compat.jodatime.LocalDateIteratorFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
    private TextView eventInterval;
    private TextView eventCount;

    private RadioGroup setFrequencyRB;
    private RadioButton byDayButton;
    private RadioButton byWeekButton;
    private RadioButton byMonthButton;

    private Button updateButton;
    private Button deleteButton;

    private DatePicker eventStartDatePicker;
    private DatePicker eventEndDatePicker;
    private TimePicker eventStartTimePicker;
    private TimePicker eventEndTimePicker;

    private org.joda.time.LocalDate localDate;

    private GregorianCalendar gregorianCalendar = new GregorianCalendar();

    private Long eventId;
    private Long patternId;
    private String RRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_action);

        Intent intent = getIntent();

        eventName = findViewById(R.id.eventName);
        eventDetails = findViewById(R.id.eventDetails);
        eventStatus = findViewById(R.id.eventStatus);
        eventInterval = findViewById(R.id.eventInterval);
        eventCount = findViewById(R.id.eventCount);

        eventStartDatePicker = findViewById(R.id.eventStartDatePicker);
        eventEndDatePicker = findViewById(R.id.eventEndDatePicker);
        eventStartTimePicker = findViewById(R.id.eventStartTimePicker);
        eventEndTimePicker = findViewById(R.id.eventEndTimePicker);

        eventStartTimePicker.setIs24HourView(true);
        eventEndTimePicker.setIs24HourView(true);

        setFrequencyRB = findViewById(R.id.setFrequencyRG);
        byDayButton = findViewById(R.id.byDayButton);
        byWeekButton = findViewById(R.id.byWeekButton);
        byMonthButton = findViewById(R.id.byMonthButton);

        eventId = Long.parseLong(intent.getStringExtra("eventId"));
        patternId = Long.parseLong(intent.getStringExtra("patternId"));
        Long eventDate = Long.parseLong(intent.getStringExtra("date"));

        eventName.setText(intent.getStringExtra("eventName"));
        eventDetails.setText(intent.getStringExtra("eventDetails"));
        eventStatus.setText(intent.getStringExtra("eventStatus"));




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

                eventStartDatePicker.updateDate(
                        gregorianCalendar.get(Calendar.YEAR),
                        gregorianCalendar.get(Calendar.MONTH),
                        gregorianCalendar.get(Calendar.DAY_OF_MONTH));

                eventStartTimePicker.setHour(gregorianCalendar.get(Calendar.HOUR_OF_DAY));
                eventStartTimePicker.setMinute(gregorianCalendar.get(Calendar.MINUTE));

                gregorianCalendar.setTimeInMillis(endTime);

                eventEndDatePicker.updateDate(
                        gregorianCalendar.get(Calendar.YEAR),
                        gregorianCalendar.get(Calendar.MONTH),
                        gregorianCalendar.get(Calendar.DAY_OF_MONTH));


                eventEndTimePicker.setHour(gregorianCalendar.get(Calendar.HOUR_OF_DAY));
                eventEndTimePicker.setMinute(gregorianCalendar.get(Calendar.MINUTE));

            }

            @Override
            public void onFailure(Call<EventPatternResponse> call, Throwable t) {

            }
        });


    }

    public void updateEvent(View view) throws ParseException {

        Long startAt;
        Long endAt;
        Long duration;

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

        Event event = new Event();
        event.setName(eventName.getText().toString());
        event.setDetails(eventDetails.getText().toString());

        duration = endAt - startAt;

        if(RRule != null) {
            endAt = 253402300799000L;

            if(!eventCount.getText().toString().equals("")) {
                endAt = calculateEndAt(RRule);
            }

        }

        EventPattern eventPattern = new EventPattern();
        eventPattern.setDuration(duration);
        eventPattern.setStartedAt(startAt);
        eventPattern.setEndedAt(endAt);
        eventPattern.setRrule(RRule);
        eventPattern.setExrule(null);

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
        gregorianCalendar.set(gregorianCalendar.get(Calendar.YEAR),
                gregorianCalendar.get(Calendar.MONTH),
                gregorianCalendar.get(Calendar.DAY_OF_MONTH),
                eventEndTimePicker.getHour(),
                eventEndTimePicker.getMinute());

        return gregorianCalendar.getTimeInMillis();
    }
}
