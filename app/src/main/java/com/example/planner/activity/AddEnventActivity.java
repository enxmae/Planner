package com.example.planner.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.planner.NetworkService;
import com.example.planner.R;
import com.example.planner.dao.Event;
import com.example.planner.dao.EventPattern;
import com.example.planner.dto.EventPatternResponse;
import com.example.planner.dto.EventResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEnventActivity extends AppCompatActivity {

    private NetworkService networkService = NetworkService.getInstance();
    private String userToken = "serega_mem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_envent);
    }

    public void addEvent(View view) {
        TextView eventName = findViewById(R.id.eventName);
        TextView eventDetails = findViewById(R.id.eventDetails);
        TextView eventLocation = findViewById(R.id.eventLocation);
        TextView eventStatus = findViewById(R.id.eventStatus);

        Event event = new Event(
                eventDetails.getText().toString(),
                "Asia/Vladivostok",
                eventName.getText().toString(),
                eventStatus.getText().toString());

        EventPattern eventPattern = new EventPattern(7200000L,
                1563960257406L + 7200000L,
                null,
                null,
                1563960257406L,
                "GMT");


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

}
