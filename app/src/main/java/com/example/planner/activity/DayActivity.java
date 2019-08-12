package com.example.planner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.planner.EventFullInformation;
import com.example.planner.R;
import com.example.planner.dao.Event;
import com.example.planner.dao.EventPattern;

import java.util.ArrayList;
import java.util.List;

public class DayActivity extends AppCompatActivity {

    private ListView eventList;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        eventList = findViewById(R.id.events);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        List<EventFullInformation> tempEventInfoList = (List<EventFullInformation>) bundle.getSerializable("tempEventInfoList");

        ArrayList<String> tempList = new ArrayList<>();

        for(int i = 0; i < tempEventInfoList.size(); i++) {
            tempList.add(tempEventInfoList.get(i).toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                android.R.layout.simple_list_item_1,
                tempList);

        eventList.setAdapter(adapter);

        eventList.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent1 = new Intent(this, EventActionActivity.class);
            Event event = tempEventInfoList.get(position).getEvent();
            EventPattern eventPattern = tempEventInfoList.get(position).getEventPattern();


            intent1.putExtra("eventId", event.getId().toString());
            intent1.putExtra("patternId", eventPattern.getId().toString());
            intent1.putExtra("eventName", event.getName());
            intent1.putExtra("eventDetails", event.getDetails());
            intent1.putExtra("eventStatus", event.getStatus());
           // intent.putExtra("date", eventDateInMills.toString());

            startActivity(intent1);


        });

    }
}
