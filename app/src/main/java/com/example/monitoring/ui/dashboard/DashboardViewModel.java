package com.example.monitoring.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.ServerAPI;
import com.example.monitoring.model.DashboardResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<DashboardResponse> dashboardData = new MutableLiveData<>();

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Memuat data dashboard...");
        loadDashboardData(); // panggil saat ViewModel dibuat
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<DashboardResponse> getDashboardDataLive() {
        return dashboardData;
    }

    private void loadDashboardData() {
        RegisterAPI api = ServerAPI.getClient().create(RegisterAPI.class);
        Call<DashboardResponse> call = api.getDashboardData("2025-12-09"); // bisa diganti date dinamis

        call.enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dashboardData.postValue(response.body());
                } else {
                    mText.postValue("Gagal memuat data dashboard");
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                mText.postValue("Error: " + t.getMessage());
            }
        });
    }
}
