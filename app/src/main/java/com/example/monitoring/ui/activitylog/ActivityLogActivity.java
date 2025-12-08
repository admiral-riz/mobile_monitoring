package com.example.monitoring.ui.activitylog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.monitoring.model.DeleteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogActivity extends AppCompatActivity {

    Spinner spinnerActivity;
    Button btnFilter, btnPrev, btnNext, btnDeleteOldLogs;
    ImageView btnBackToProfile;
    TextView txtPage;
    RecyclerView recycler;
    ActivityLogAdapter adapter;

    int currentPage = 1;
    String selectedAction = ""; // nilai yang dikirim ke API: login, upload, bucket, dsb
    int perPageFromResponse = 10; // fallback

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
        btnBackToProfile = findViewById(R.id.btnBackToProfile);
        btnDeleteOldLogs = findViewById(R.id.btnDeleteOldLogs);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        setupSpinner();
        loadData();

        btnFilter.setOnClickListener(v -> {
            String selected = spinnerActivity.getSelectedItem().toString();

            if (selected.equals("Semua Aktivitas")) {
                selectedAction = "";
            } else if (selected.equals("Login")) {
                selectedAction = "login";
            } else if (selected.equals("Logout")) {
                selectedAction = "logout";
            } else if (selected.equals("Upload File")) {
                selectedAction = "upload";
            } else if (selected.equals("Hitung Bucket")) {
                selectedAction = "bucket";
            } else {
                selectedAction = "";
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

        // Tombol Kembali -> finish activity (kembali ke profile/activity sebelumnya)
        btnBackToProfile.setOnClickListener(v -> {
            finish(); // jika ingin buka ProfileActivity: buat Intent ke ProfileActivity
        });

        // Tombol Hapus Log Lama
        btnDeleteOldLogs.setOnClickListener(v -> showDeleteOldLogsDialog());
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
                selectedAction,
                currentPage,
                "true"
        );

        call.enqueue(new Callback<ActivityLogResponse>() {
            @Override
            public void onResponse(Call<ActivityLogResponse> call, Response<ActivityLogResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(ActivityLogActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<ActivityLogResponse.LogData> list = response.body().getData().getLogs();

                if (list == null || list.isEmpty()) {
                    // jika kosong, tetap tampilkan kosong tapi jangan return agar prev/next dapat di-handle
                    Toast.makeText(ActivityLogActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                }

                adapter = new ActivityLogAdapter(list);
                recycler.setAdapter(adapter);

                txtPage.setText(" " + currentPage+ " ");

                // perPage jika dikembalikan dari response
                try {
                    perPageFromResponse = response.body().getData().getPerPage();
                } catch (Exception e) {
                    perPageFromResponse = 10;
                }

                // Control tombol prev/next
                btnPrev.setEnabled(currentPage > 1);
                // Jika jumlah item < perPage -> disable next
                btnNext.setEnabled(list != null && list.size() >= perPageFromResponse);
            }

            @Override
            public void onFailure(Call<ActivityLogResponse> call, Throwable t) {
                Toast.makeText(ActivityLogActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteOldLogsDialog() {
        // Dialog input jumlah hari (default 60)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Log Lama");
        builder.setMessage("Masukkan jumlah hari. Log lebih lama dari hari ini akan dihapus.");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("60");
        builder.setView(input);

        builder.setPositiveButton("Hapus", (dialog, which) -> {
            String val = input.getText().toString().trim();
            int days = 60;
            if (!val.isEmpty()) {
                try {
                    days = Integer.parseInt(val);
                    if (days <= 0) days = 60;
                } catch (NumberFormatException e) {
                    days = 60;
                }
            }
            confirmAndDeleteOldLogs(days);
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void confirmAndDeleteOldLogs(int days) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Yakin hapus log yang lebih lama dari " + days + " hari?")
                .setPositiveButton("Ya", (dialog, which) -> doDeleteOldLogs(days))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void doDeleteOldLogs(int days) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Menghapus log...");
        pd.setCancelable(false);
        pd.show();

        RegisterAPI api = RetrofitClientInstance.getRetrofitInstance().create(RegisterAPI.class);

        Call<DeleteResponse> call = api.cleanOldLogs(days);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                pd.dismiss();
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(ActivityLogActivity.this, "Gagal menghapus log", Toast.LENGTH_SHORT).show();
                    return;
                }
                DeleteResponse res = response.body();
                if (res.isSuccess()) {
                    Toast.makeText(ActivityLogActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    // reload data
                    currentPage = 1;
                    loadData();
                } else {
                    Toast.makeText(ActivityLogActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(ActivityLogActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
