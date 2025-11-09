package com.example.monitoring.api;

import com.example.monitoring.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterAPI {

    @FormUrlEncoded
    @POST("api/login")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );
}
