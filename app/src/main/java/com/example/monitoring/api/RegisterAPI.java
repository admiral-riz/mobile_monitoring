package com.example.monitoring.api;

import com.example.monitoring.model.AuxDetailResponse;
import com.example.monitoring.model.AuxResponse;
import com.example.monitoring.model.KipResponse;
import com.example.monitoring.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterAPI {

    @FormUrlEncoded
    @POST("api/login")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("api/kip/top")
    Call<KipResponse> getTopKip(@Query("date") String date);

    @GET("api/aux/top")
    Call<AuxResponse> getTopAux(@Query("date") String date);

    @GET("api/aux/detail")
    Call<AuxDetailResponse> getAuxDetail(
            @Query("date") String date,
            @Query("agent") String agent
    );

}
