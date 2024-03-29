package com.example.planner.repository;

import com.example.planner.dao.Event;
import com.example.planner.dto.EventInstanceResponse;
import com.example.planner.dto.EventResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventRepository {

    @GET("/api/v1/events")
    Call<EventResponse> getEventsByIds(
            @Query("id") Long[] ids,
            @Header("X-Firebase-Auth") String userToken);

    @GET("/api/v1/events/{id}")
    Call<EventResponse> getEventById(
            @Path("id") Long id,
            @Header("X-Firebase-Auth") String userToken);

    @GET("/api/v1/events/instances")
    Call<EventInstanceResponse> getInstances(
            @Query("from") Long from,
            @Query("to") Long to,
            @Header("X-Firebase-Auth") String userToken);


    @POST("/api/v1/events")
    Call<EventResponse> saveEvent(
            @Body Event event,
            @Header("X-Firebase-Auth") String userToken);

    @PATCH("/api/v1/events/{id}")
    Call<EventResponse> update(
            @Path("id") Long id,
            @Body Event event,
            @Header("X-Firebase-Auth") String userToken
    );

    @DELETE("/api/v1/events/{id}")
    Call<Void> delete(@Path("id") Long id, @Header("X-Firebase-Auth") String userToken);

}
