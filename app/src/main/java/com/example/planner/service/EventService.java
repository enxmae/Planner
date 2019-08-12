package com.example.planner.service;


import com.example.planner.EventFullInformation;

import java.util.HashMap;
import java.util.List;

public class EventService {
    private static EventService instance;

    public HashMap<Integer,
            HashMap<Integer,
                    HashMap<Integer,
                            List<EventFullInformation>>>> events = new HashMap<>();

    public static EventService getInstance() {
        if(instance == null) {
            instance = new EventService();
        }

        return instance;
    }

}
