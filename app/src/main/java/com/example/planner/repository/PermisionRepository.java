package com.example.planner.repository;

import com.example.planner.dao.PermissionRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PermisionRepository {

    @POST("/api/v1/share")
    Call<ResponseBody> generateLinkForSharing(
            @Body PermissionRequest[] permissionRequests,
            @Header("X-Firebase-Auth") String userToken
    );



}
