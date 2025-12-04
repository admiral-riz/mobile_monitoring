package com.example.monitoring.ui.activitylog;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoring.R;
import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.RetrofitClientInstance;
import com.example.monitoring.model.ActivityLog;
import com.example.monitoring.model.ActivityLogResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityLogAdapter adapter;
    private List<ActivityLog> logList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_log);

        recyclerView = findViewById(R.id.recyclerActivityLog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ActivityLogAdapter(logList);
        recyclerView.setAdapter(adapter);

        loadActivityLogs();
    }

    private void loadActivityLogs() {
        RegisterAPI api = RetrofitClientInstance.getRetrofitInstance().create(RegisterAPI.class);

        Call<ActivityLogResponse> call = api.getActivityLogs(1, null);

        call.enqueue(new Callback<ActivityLogResponse>() {
            @Override
            public void onResponse(Call<ActivityLogResponse> call, Response<ActivityLogResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    logList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ActivityLogResponse> call, Throwable t) {
                Log.e("ActivityLog", "Error: " + t.getMessage());
            }
        });
    }
}
