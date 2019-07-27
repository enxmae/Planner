package com.example.planner.repository;

import com.example.planner.dao.EventPattern;
import com.example.planner.dto.EventPatternResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventPatternRepository {

    @GET("/api/v1/patterns")
    Call<EventPatternResponse> getEventPatterns(
            @Query("events") Long[] ids,
            @Header("X-Firebase-Auth") String userToken
    );


    @POST("/api/v1/patterns")
    Call<EventPatternResponse> savePattern(
            @Query("event_id") Long eventId,
            @Body EventPattern eventPattern,
            @Header("X-Firebase-Auth") String userToken
    );

}
