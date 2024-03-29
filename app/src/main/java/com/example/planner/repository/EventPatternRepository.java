package com.example.planner.repository;

import com.example.planner.dao.EventPattern;
import com.example.planner.dto.EventPatternResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventPatternRepository {

    @GET("/api/v1/patterns")
    Call<EventPatternResponse> getEventPatterns(
            @Query("events") Long[] ids,
            @Header("X-Firebase-Auth") String userToken
    );

    @GET("/api/v1/patterns/{id}")
    Call<EventPatternResponse> getPatternById(
            @Path("id") Long id,
            @Header("X-Firebase-Auth") String userToken
    );

    @POST("/api/v1/patterns")
    Call<EventPatternResponse> savePattern(
            @Query("event_id") Long eventId,
            @Body EventPattern eventPattern,
            @Header("X-Firebase-Auth") String userToken
    );

    @PATCH("/api/v1/patterns/{id}")
    Call<EventPatternResponse> update(
            @Path("id") Long id,
            @Body EventPattern eventPattern,
            @Header("X-Firebase-Auth") String userToken
    );

    @DELETE("/api/v1/patterns/{id}")
    Call<Void> delete(@Path("id") Long id, @Header("X-Firebase-Auth") String userToken);

}
