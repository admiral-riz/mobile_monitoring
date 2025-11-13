package com.example.monitoring.ui.kip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.RetrofitClientInstance;
import com.example.monitoring.model.KipResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KipViewModel extends ViewModel {

    private final MutableLiveData<KipResponse> kipData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<KipResponse> getKipData() {
        return kipData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadKipData(String date) {
        RegisterAPI api = RetrofitClientInstance.getRetrofitInstance().create(RegisterAPI.class);
        Call<KipResponse> call = api.getTopKip(date);

        call.enqueue(new Callback<KipResponse>() {
            @Override
            public void onResponse(Call<KipResponse> call, Response<KipResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    kipData.setValue(response.body());
                } else {
                    errorMessage.setValue("Gagal memuat data dari server");
                }
            }

            @Override
            public void onFailure(Call<KipResponse> call, Throwable t) {
                errorMessage.setValue("Kesalahan koneksi: " + t.getMessage());
            }
        });
    }
}
