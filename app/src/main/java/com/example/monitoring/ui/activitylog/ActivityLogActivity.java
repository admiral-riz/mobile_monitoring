package com.example.monitoring.ui.activitylog;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoring.R;
import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.RetrofitClientInstance;
import com.example.monitoring.model.ActivityLogResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogActivity extends AppCompatActivity {

    Spinner spinnerActivity;
    Button btnFilter, btnPrev, btnNext;
    TextView txtPage;
    RecyclerView recycler;
    ActivityLogAdapter adapter;

    int currentPage = 1;
    String selectedAction = ""; // FIX: gunakan action, bukan activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_log);

        spinnerActivity = findViewById(R.id.spinnerActivity);
        btnFilter = findViewById(R.id.btnFilter);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        txtPage = findViewById(R.id.txtPage);

        recycler = findViewById(R.id.recyclerActivityLog);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        setupSpinner();
        loadData();

        btnFilter.setOnClickListener(v -> {
            String selected = spinnerActivity.getSelectedItem().toString();

            switch (selected) {
                case "Login":
                    selectedAction = "Login";
                    break;
                case "Logout":
                    selectedAction = "Logout";
                    break;
                case "Upload File":
                    selectedAction = "Upload File";
                    break;
                case "Hitung Bucket":
                    selectedAction = "Hitung Bucket";
                    break;
                default:
                    selectedAction = ""; // Semua aktivitas
            }

            currentPage = 1;
            loadData();
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadData();
            }
        });

        btnNext.setOnClickListener(v -> {
            currentPage++;
            loadData();
        });
    }

    void setupSpinner() {
        String[] items = {"Semua Aktivitas", "Login", "Logout", "Upload File", "Hitung Bucket"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                items
        );
        spinnerActivity.setAdapter(adapter);
    }

    void loadData() {

        RegisterAPI api = RetrofitClientInstance.getRetrofitInstance()
                .create(RegisterAPI.class);

        Call<ActivityLogResponse> call = api.getActivityLog(
                "true",
                selectedAction,    // FIXED: kirim action, bukan nama spinner langsung
                currentPage
        );

        call.enqueue(new Callback<ActivityLogResponse>() {
            @Override
            public void onResponse(Call<ActivityLogResponse> call, Response<ActivityLogResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(ActivityLogActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<ActivityLogResponse.LogData> list = response.body().getData().getLogs();

                adapter = new ActivityLogAdapter(list);
                recycler.setAdapter(adapter);

                txtPage.setText("Halaman: " + currentPage);
            }

            @Override
            public void onFailure(Call<ActivityLogResponse> call, Throwable t) {
                Toast.makeText(ActivityLogActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
