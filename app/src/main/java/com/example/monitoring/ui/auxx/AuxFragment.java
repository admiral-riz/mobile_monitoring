package com.example.monitoring.ui.auxx;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoring.R;
import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.RetrofitClientInstance;
import com.example.monitoring.model.AuxResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuxFragment extends Fragment {

    private RecyclerView recyclerAux;
    private AuxAdapter adapter;
    private String selectedDate;
    private android.widget.TextView tvSelectedDate;
    private android.widget.Button btnPickDate, btnFilter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_aux, container, false);

        recyclerAux = root.findViewById(R.id.recyclerAux);
        tvSelectedDate = root.findViewById(R.id.tvSelectedDate);
        btnPickDate = root.findViewById(R.id.btnPilihTanggal);
        btnFilter = root.findViewById(R.id.btnFilter);

        recyclerAux.setLayoutManager(new LinearLayoutManager(getContext()));

        // default tanggal: hari ini
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        tvSelectedDate.setText(selectedDate);

        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnFilter.setOnClickListener(v -> loadData(selectedDate));

        // load awal
        loadData(selectedDate);

        return root;
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    tvSelectedDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void loadData(String date) {
        RegisterAPI api = RetrofitClientInstance.getRetrofitInstance().create(RegisterAPI.class);
        api.getTopAux(date).enqueue(new Callback<AuxResponse>() {
            @Override
            public void onResponse(Call<AuxResponse> call, Response<AuxResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuxResponse body = response.body();
                    if (body.getTop10() != null && !body.getTop10().isEmpty()) {
                        List<AuxResponse.AuxItem> list = body.getTop10();
                        adapter = new AuxAdapter(list, date); // kirim date ke adapter
                        recyclerAux.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "Tidak ada data untuk tanggal " + date, Toast.LENGTH_SHORT).show();
                        adapter = new AuxAdapter(java.util.Collections.emptyList(), date);
                        recyclerAux.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getContext(), "Gagal: response code " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuxResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error koneksi: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
