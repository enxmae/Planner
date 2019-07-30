package com.example.planner;

import com.example.planner.dao.EventPattern;
import com.example.planner.repository.EventPatternRepository;
import com.example.planner.repository.EventRepository;
import com.example.planner.repository.PermisionRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private final static String BASE_URL = "http://planner.skillmasters.ga/";
    private static NetworkService instance;
    private Retrofit retrofit;

    private static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setLenient()
            .create();

    private NetworkService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static NetworkService getInstance() {
        if(instance == null) {
            instance = new NetworkService();
        }

        return instance;
    }

    public EventRepository getEventRepository() {
        return retrofit.create(EventRepository.class);
    }

    public EventPatternRepository getEventPatternRepository() {
        return retrofit.create(EventPatternRepository.class);
    }

    public PermisionRepository getPermissionRepository() {
        return retrofit.create(PermisionRepository.class);
    }

}
