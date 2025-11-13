package com.example.monitoring.ui.kip;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.monitoring.databinding.FragmentKipBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class KipFragment extends Fragment {

    private FragmentKipBinding binding;
    private KipAdapter adapter;
    private KipViewModel kipViewModel;
    private String selectedDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentKipBinding.inflate(inflater, container, false);
        kipViewModel = new ViewModelProvider(this).get(KipViewModel.class);

        adapter = new KipAdapter();
        binding.recyclerKip.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.recyclerKip.setAdapter(adapter);

        // Set tanggal hari ini
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        binding.textTanggalValue.setText(selectedDate);

        // Muat data awal
        loadKip(selectedDate);

        // Event tombol filter
        binding.btnFilterTanggal.setOnClickListener(v -> showDatePicker());

        // Observe data
        kipViewModel.getKipData().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.isSuccess()) {
                if (response.getData() != null && !response.getData().isEmpty()) {
                    adapter.setData(response.getData());
                    binding.textNoData.setVisibility(View.GONE);
                } else {
                    binding.textNoData.setVisibility(View.VISIBLE);
                    binding.textNoData.setText("Tidak ada data untuk tanggal " + response.getSelectedDate());
                }
            }
        });

        kipViewModel.getErrorMessage().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());

        return binding.getRoot();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    selectedDate = sdf.format(selectedCal.getTime());
                    binding.textTanggalValue.setText(selectedDate);
                    loadKip(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void loadKip(String date) {
        binding.textNoData.setVisibility(View.VISIBLE);
        binding.textNoData.setText("Memuat data...");
        kipViewModel.loadKipData(date);
    }
}
