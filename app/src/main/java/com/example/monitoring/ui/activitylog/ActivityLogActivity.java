package com.example.monitoring.ui.activitylog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
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
    int totalPage = 1;
    String selectedAction = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_log);

        spinnerActivity  = findViewById(R.id.spinnerActivity);
        btnFilter        = findViewById(R.id.btnFilter);
        btnPrev          = findViewById(R.id.btnPrev);
        btnNext          = findViewById(R.id.btnNext);
        btnDeleteOldLogs = findViewById(R.id.btnDeleteOldLogs);
        txtPage          = findViewById(R.id.txtPage);
        recycler         = findViewById(R.id.recyclerActivityLog);
        btnBackToProfile = findViewById(R.id.btnBackToProfile);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        setupSpinner();
        loadData();

        btnFilter.setOnClickListener(v -> {
            String selected = spinnerActivity.getSelectedItem().toString();
            if (selected.equals("Semua Aktivitas")) selectedAction = "";
            else if (selected.equals("Login")) selectedAction = "login";
            else if (selected.equals("Logout")) selectedAction = "logout";
            else if (selected.equals("Upload File")) selectedAction = "upload";
            else if (selected.equals("Hitung Bucket")) selectedAction = "bucket";

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
            if (currentPage < totalPage) {
                currentPage++;
                loadData();
            }
        });

        btnBackToProfile.setOnClickListener(v -> finish());
        btnDeleteOldLogs.setOnClickListener(v -> showDeleteOldLogsDialog());
    }

    private void setupSpinner() {
        String[] items = {"Semua Aktivitas", "Login", "Logout", "Upload File", "Hitung Bucket"};
        spinnerActivity.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items));
    }

    private void loadData() {
        RegisterAPI api = RetrofitClientInstance.getRetrofitInstance().create(RegisterAPI.class);

        api.getActivityLog(selectedAction, currentPage, "true")
                .enqueue(new Callback<ActivityLogResponse>() {
                    @Override
                    public void onResponse(Call<ActivityLogResponse> call, Response<ActivityLogResponse> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(ActivityLogActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<ActivityLogResponse.LogData> list = response.body().getData().getLogs();
                        adapter = new ActivityLogAdapter(list);
                        recycler.setAdapter(adapter);

                        int totalData = response.body().getData().getTotal();
                        int perPage   = response.body().getData().getPerPage();
                        totalPage = (int) Math.ceil((double) totalData / perPage);

                        txtPage.setText(currentPage + " / " + totalPage);
                        btnPrev.setEnabled(currentPage > 1);
                        btnNext.setEnabled(currentPage < totalPage);
                    }

                    @Override
                    public void onFailure(Call<ActivityLogResponse> call, Throwable t) {
                        Toast.makeText(ActivityLogActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDeleteOldLogsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Log Lama");
        builder.setMessage("Masukkan jumlah hari. Log lebih lama dari hari ini akan dihapus.");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("60");
        builder.setView(input);

        builder.setPositiveButton("Hapus", (dialog, which) -> {
            int days = input.getText().toString().isEmpty()
                    ? 60
                    : Integer.parseInt(input.getText().toString());
            confirmAndDeleteOldLogs(days);
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void confirmAndDeleteOldLogs(int days) {
        String filterName = selectedAction.equals("")
                ? "SEMUA AKTIVITAS"
                : selectedAction.toUpperCase();

        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Yakin hapus log \"" + filterName + "\" yang lebih lama dari " + days + " hari?")
                .setPositiveButton("Ya", (d, w) -> doDeleteOldLogs(days))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void doDeleteOldLogs(int days) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Menghapus log sesuai filter...");
        pd.setCancelable(false);
        pd.show();

        RegisterAPI api = RetrofitClientInstance.getRetrofitInstance().create(RegisterAPI.class);

        api.cleanOldLogs(days, selectedAction)
                .enqueue(new Callback<DeleteResponse>() {
                    @Override
                    public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                        pd.dismiss();
                        Toast.makeText(ActivityLogActivity.this,
                                response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        currentPage = 1;
                        loadData();
                    }

                    @Override
                    public void onFailure(Call<DeleteResponse> call, Throwable t) {
                        pd.dismiss();
                        Toast.makeText(ActivityLogActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
