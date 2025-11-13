package com.example.monitoring.ui.auxx;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoring.R;
import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.RetrofitClientInstance;
import com.example.monitoring.model.AuxDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuxDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDate, tvTotalDurasi, tvTotalMenit, tvStatus;
    private RecyclerView recyclerDetail;
    private CardView cardStatus;
    private Button btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aux_detail);

        // --- Inisialisasi Komponen UI ---
        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);
        tvTotalDurasi = findViewById(R.id.tvTotalDurasi);
        tvTotalMenit = findViewById(R.id.tvTotalMenit);
        tvStatus = findViewById(R.id.tvStatus);
        recyclerDetail = findViewById(R.id.recyclerDetail);
        cardStatus = findViewById(R.id.cardStatus);
        btnKembali = findViewById(R.id.btnKembali);

        recyclerDetail.setLayoutManager(new LinearLayoutManager(this));

        // --- Tombol kembali ke halaman sebelumnya ---
        btnKembali.setOnClickListener(v -> finish());

        // --- Ambil data dari Intent ---
        String date = getIntent().getStringExtra("date");
        String agent = getIntent().getStringExtra("agent");

        // --- Load Data Detail AUX ---
        loadDetail(date, agent);
    }

    private void loadDetail(String date, String agent) {
        RegisterAPI api = RetrofitClientInstance.getRetrofitInstance().create(RegisterAPI.class);
        api.getAuxDetail(date, agent).enqueue(new Callback<AuxDetailResponse>() {
            @Override
            public void onResponse(Call<AuxDetailResponse> call, Response<AuxDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuxDetailResponse data = response.body();

                    // --- Isi data ke UI ---
                    tvTitle.setText("Detail AUX (" + agent + ")");
                    tvDate.setText("Tanggal: " + data.getDate());
                    tvTotalDurasi.setText(data.getRingkasan().getTotal_durasi());
                    tvTotalMenit.setText(data.getRingkasan().getTotal_menit());
                    tvStatus.setText(data.getRingkasan().getStatus());

                    // --- Ubah warna card status berdasarkan status ---
                    String status = data.getRingkasan().getStatus();
                    int color;
                    switch (status.toUpperCase()) {
                        case "DANGER":
                            color = Color.parseColor("#F44336"); // merah
                            break;
                        case "WARNING":
                            color = Color.parseColor("#FFA726"); // oranye
                            break;
                        default:
                            color = Color.parseColor("#66BB6A"); // hijau
                            break;
                    }
                    cardStatus.setCardBackgroundColor(color);

                    // --- Tampilkan data detail di RecyclerView ---
                    recyclerDetail.setAdapter(new AuxDetailAdapter(data.getDetail_aktivitas()));
                } else {
                    Toast.makeText(AuxDetailActivity.this, "Data kosong atau tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuxDetailResponse> call, Throwable t) {
                Toast.makeText(AuxDetailActivity.this, "Gagal koneksi: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
