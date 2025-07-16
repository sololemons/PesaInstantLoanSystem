package com.userservice.user.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AfricaTalkingApi {

    @FormUrlEncoded
    @POST("version1/messaging")
    Call<ResponseBody> sendSms(
            @Header("apiKey") String apiKey,
            @Field("username") String username,
            @Field("to") String to,
            @Field("message") String message
    );
}
