package com.example.monitoring.ui.dashboard;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.ServerAPI;
import com.example.monitoring.databinding.FragmentDashboardBinding;
import com.example.monitoring.model.DashboardResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private RegisterAPI api;
    private String selectedDateKip = "2025-12-09";
    private String selectedDateAux = "2025-12-09";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        api = ServerAPI.getClient().create(RegisterAPI.class);

        // Listener tombol pilih tanggal
        binding.btnDateKip.setOnClickListener(v -> showDatePicker(true));
        binding.btnDateAux.setOnClickListener(v -> showDatePicker(false));

        // Load chart default
        loadPieChart();
        loadBarChart();
        loadLineChart();
        loadTop5Kip();

        return binding.getRoot();
    }

    private void showDatePicker(boolean isKip) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, y, m, d) -> {
                    String date = String.format("%04d-%02d-%02d", y, m + 1, d);
                    if (isKip) {
                        selectedDateKip = date;
                        loadPieChart();
                        loadLineChart();
                        loadTop5Kip();
                    } else {
                        selectedDateAux = date;
                        loadBarChart();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Load Pie Chart KIP utama
    private void loadPieChart() {
        api.getDashboardData(selectedDateKip).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setPieChart(response.body().getData().getPieChart());
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Load Bar Chart AUX
    private void loadBarChart() {
        api.getDashboardData(selectedDateAux).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setBarChart(response.body().getData().getBarChart());
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Load Line Chart KIP per jam
    private void loadLineChart() {
        api.getDashboardData(selectedDateKip).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setLineChart(response.body().getData().getLineChart());
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Load Top 5 KIP
    private void loadTop5Kip() {
        api.getDashboardData(selectedDateKip).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayTop5Kip(response.body().getData().getTop5Kip());
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Menampilkan Top 5 KIP (dinamis)
    private void displayTop5Kip(List<DashboardResponse.TopKip> top5) {
        binding.top5Container.removeAllViews();

        for (DashboardResponse.TopKip kip : top5) {
            // Judul Top KIP
            TextView title = new TextView(getContext());
            title.setText("Top KIP: " + kip.getCategory());
            title.setTextSize(18f);
            title.setPadding(0, 16, 0, 8);
            binding.top5Container.addView(title);

            // PieChart Top KIP
            PieChart pieChart = new PieChart(getContext());
            pieChart.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 250));
            List<PieEntry> pieEntries = new ArrayList<>();
            for (DashboardResponse.PieChart p : kip.getPieChart()) {
                pieEntries.add(new PieEntry(p.getValue(), p.getLabel()));
            }
            PieDataSet pieSet = new PieDataSet(pieEntries, kip.getCategory());
            pieSet.setColors(Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN);
            pieChart.setData(new PieData(pieSet));
            pieChart.invalidate();
            binding.top5Container.addView(pieChart);

            // LineChart Top KIP
            LineChart lineChart = new LineChart(getContext());
            lineChart.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 250));
            List<Entry> lineEntries = new ArrayList<>();
            for (DashboardResponse.LineChart l : kip.getLineChart()) {
                lineEntries.add(new Entry(l.getHour(), l.getTotal()));
            }
            LineDataSet lineSet = new LineDataSet(lineEntries, "Jumlah KIP per Jam");
            lineSet.setColor(Color.BLUE);
            lineSet.setCircleColor(Color.RED);
            lineChart.setData(new LineData(lineSet));
            lineChart.invalidate();
            binding.top5Container.addView(lineChart);
        }
    }

    // Set PieChart utama
    private void setPieChart(List<DashboardResponse.PieChart> pieData) {
        PieChart pieChart = binding.pieChart;
        List<PieEntry> entries = new ArrayList<>();
        for (DashboardResponse.PieChart item : pieData) {
            entries.add(new PieEntry(item.getValue(), item.getLabel()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Distribusi KIP");
        dataSet.setColors(Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA, Color.CYAN);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    // Set BarChart utama
    private void setBarChart(List<DashboardResponse.BarChart> barData) {
        BarChart barChart = binding.barChart;
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < barData.size(); i++) {
            entries.add(new BarEntry(i, barData.get(i).getValue()));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Durasi AUX (Menit)");
        dataSet.setColors(Color.BLUE, Color.RED, Color.GREEN);
        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.invalidate();
    }

    // Set LineChart utama
    private void setLineChart(List<DashboardResponse.LineChart> lineData) {
        LineChart lineChart = binding.lineChart;
        List<Entry> entries = new ArrayList<>();
        for (DashboardResponse.LineChart item : lineData) {
            entries.add(new Entry(item.getHour(), item.getTotal()));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Jumlah KIP per Jam");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.RED);
        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
