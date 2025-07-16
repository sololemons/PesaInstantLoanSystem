package com.applicationservice.retrofit;

import com.applicationservice.dtos.UserIdDto;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiClient {

    @POST("/users/verify/user")
    Call <UserIdDto> getUserId(@Header("Authorization") String authHeader);
}