package com.example.planner.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.planner.NetworkService;
import com.example.planner.R;
import com.example.planner.dao.Event;
import com.example.planner.dto.EventResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEnventActivity extends AppCompatActivity {

    private NetworkService networkService = NetworkService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_envent);
    }

    public void addEvent(View view) {
        TextView eventName = findViewById(R.id.eventName);
        
        Event event = new Event(
                "...", "unknown", eventName.getText().toString(), "TENTATIVE");

        Call<EventResponse> eventResponseCall = networkService.getEventRepository().saveEvent(event, "serega_mem");
        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });
    }

}
