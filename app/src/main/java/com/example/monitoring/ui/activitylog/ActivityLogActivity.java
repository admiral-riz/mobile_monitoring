package com.example.monitoring.ui.activitylog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoring.R;
import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.ServerAPI;
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

    private TextView tvUserId, tvUsername;
    private Button btnBack;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_log);

        sharedPreferences = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerActivityLog);
        tvUserId = findViewById(R.id.tvUserId);
        tvUsername = findViewById(R.id.tvUsername);
        btnBack = findViewById(R.id.btnBack);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ActivityLogAdapter(logList);
        recyclerView.setAdapter(adapter);

        // Ambil User Data
        String userId = sharedPreferences.getString("id", "Unknown");
        String username = sharedPreferences.getString("username", "Unknown");

        tvUserId.setText("User ID: " + userId);
        tvUsername.setText("Username: " + username);

        // Load Log
        loadActivityLog(userId);

        // Tombol kembali
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadActivityLog(String userId) {
        RegisterAPI api = ServerAPI.getClient().create(RegisterAPI.class);
        Call<ActivityLogResponse> call = api.getActivityLogs(1, userId);

        call.enqueue(new Callback<ActivityLogResponse>() {
            @Override
            public void onResponse(Call<ActivityLogResponse> call, Response<ActivityLogResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    logList.clear();
                    logList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(ActivityLogActivity.this, "Gagal memuat data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ActivityLogResponse> call, Throwable t) {
                Toast.makeText(ActivityLogActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
