package com.example.monitoring.api;

import com.example.monitoring.model.ActivityLogResponse;
import com.example.monitoring.model.AuxDetailResponse;
import com.example.monitoring.model.AuxResponse;
import com.example.monitoring.model.DeleteResponse;
import com.example.monitoring.model.KelolaDataResponse;
import com.example.monitoring.model.KipResponse;
import com.example.monitoring.model.LoginResponse;
import com.example.monitoring.model.ProfileResponse;

import java.util.List;

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

    @GET("api/profile")
    Call<ProfileResponse> getProfile(@Query("id") String userId);

    @GET("api/kelola-data")
    Call<KelolaDataResponse> getKelolaData(
            @Query("table") String table,
            @Query("search") String search,
            @Query("page") int page
    );

    @FormUrlEncoded
    @POST("api/kelola-data/delete")
    Call<DeleteResponse> deleteData(
            @Field("table") String table,
            @Field("ids[]") List<Integer> ids
    );
    @GET("api/activity-log")
    Call<ActivityLogResponse> getActivityLog(
            @Query("is_mobile") String isMobile,
            @Query("action") String action,
            @Query("page") int page
    );






}
