package com.example.monitoring.ui.kelola;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.ServerAPI;
import com.example.monitoring.databinding.ActivityKelolaDataBinding;
import com.example.monitoring.model.DeleteResponse;
import com.example.monitoring.model.KelolaDataResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelolaDataActivity extends AppCompatActivity {

    ActivityKelolaDataBinding binding;

    String selectedTable = "sdm";
    int currentPage = 1;

    KelolaDataAdapter adapter;
    List<JSONObject> dataList = new ArrayList<>();
    String primaryKey = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKelolaDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tombol kembali
        binding.btnBack.setOnClickListener(v -> finish());

        setupSpinner();
        binding.recyclerKelola.setLayoutManager(new LinearLayoutManager(this));

        binding.btnSearch.setOnClickListener(v -> loadData());
        binding.btnDelete.setOnClickListener(v -> deleteSelected());

        loadData();
    }

    private void setupSpinner() {
        List<String> tables = List.of(
                "sdm", "presensi", "queue_onx",
                "report_agent_log", "tickets", "agent_bucket"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, tables);

        binding.spinnerTable.setAdapter(adapter);

        // FIX: Tidak bisa pakai lambda → harus implement interface lengkap
        binding.spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTable = tables.get(position);
                loadData(); // Auto reload saat table diganti
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadData() {
        String search = binding.etSearch.getText().toString().trim();

        RegisterAPI api = ServerAPI.getClient().create(RegisterAPI.class);

        api.getKelolaData(selectedTable, search, currentPage)
                .enqueue(new Callback<KelolaDataResponse>() {

                    @Override
                    public void onResponse(Call<KelolaDataResponse> call, Response<KelolaDataResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                            primaryKey = response.body().getPrimaryKey();

                            dataList.clear();
                            for (Object obj : response.body().getData()) {
                                dataList.add(new JSONObject((java.util.Map) obj));
                            }

                            adapter = new KelolaDataAdapter(dataList, primaryKey);
                            binding.recyclerKelola.setAdapter(adapter);

                        } else {
                            Toast.makeText(KelolaDataActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<KelolaDataResponse> call, Throwable t) {
                        Toast.makeText(KelolaDataActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteSelected() {
        List<String> selectedIds = adapter.getSelectedIds();

        if (selectedIds.isEmpty()) {
            Toast.makeText(this, "Tidak ada data terpilih!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Konversi String → Integer (API PHP butuh ID angka)
        List<Integer> idIntList = new ArrayList<>();
        for (String s : selectedIds) {
            try {
                idIntList.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "ID tidak valid: " + s, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        RegisterAPI api = ServerAPI.getClient().create(RegisterAPI.class);

        api.deleteData(selectedTable, idIntList)
                .enqueue(new Callback<DeleteResponse>() {

                    @Override
                    public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                        if (response.isSuccessful() && response.body().isSuccess()) {
                            Toast.makeText(KelolaDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            loadData(); // Refresh ulang
                        } else {
                            Toast.makeText(KelolaDataActivity.this, "Gagal menghapus!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteResponse> call, Throwable t) {
                        Toast.makeText(KelolaDataActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
