package com.example.planner.repository;

import com.example.planner.dto.EventResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface EventRepository {

    @GET("/api/v1/events")
    Call<EventResponse> getEventsByIds(
            @Query("id") Long[] ids,
            @Header("X-Firebase-Auth") String userToken);



}
