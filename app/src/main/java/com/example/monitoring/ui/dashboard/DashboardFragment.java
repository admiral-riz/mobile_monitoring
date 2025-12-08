package com.example.monitoring.ui.dashboard;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.ServerAPI;
import com.example.monitoring.databinding.FragmentDashboardBinding;
import com.example.monitoring.model.DashboardResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private PieChart pieChartFixed, pieChartMobile;
    private BarChart barChartAux;
    private Button btnSelectDateKip, btnSelectDateAux;
    private TextView textFixedInfo, textMobileInfo, textAuxInfo;
    private String selectedDateKip, selectedDateAux;

    private int[] colors = {Color.parseColor("#56B7F1"), Color.parseColor("#CDA67F"), Color.parseColor("#8BC34A"), Color.parseColor("#FFC107"), Color.parseColor("#E91E63"), Color.parseColor("#9C27B0")};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pieChartFixed = binding.pieChartFixed;
        pieChartMobile = binding.pieChartMobile;
        barChartAux = binding.barChartAux;

        btnSelectDateKip = binding.btnSelectDateKip;
        btnSelectDateAux = binding.btnSelectDateAux;

        textFixedInfo = binding.textFixedInfo;
        textMobileInfo = binding.textMobileInfo;
        textAuxInfo = binding.textAuxInfo;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDateKip = sdf.format(Calendar.getInstance().getTime());
        selectedDateAux = sdf.format(Calendar.getInstance().getTime());

        loadKipData();
        loadAuxData();

        btnSelectDateKip.setOnClickListener(v -> showDatePickerKip());
        btnSelectDateAux.setOnClickListener(v -> showDatePickerAux());

        return root;
    }

    private void showDatePickerKip() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    selectedDateKip = sdf.format(selectedCal.getTime());
                    loadKipData();
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    private void showDatePickerAux() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    selectedDateAux = sdf.format(selectedCal.getTime());
                    loadAuxData();
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    private void loadKipData() {
        RegisterAPI api = ServerAPI.getClient().create(RegisterAPI.class);
        api.getDashboardData(selectedDateKip).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if(response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    setupPieCharts(response.body().getData().getKipData());
                } else {
                    Toast.makeText(getContext(), "Gagal mengambil data KIP", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAuxData() {
        RegisterAPI api = ServerAPI.getClient().create(RegisterAPI.class);
        api.getDashboardData(selectedDateAux).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if(response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    setupBarChart(response.body().getData().getBarChart());
                } else {
                    Toast.makeText(getContext(), "Gagal mengambil data AUX", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPieCharts(List<DashboardResponse.KipData> kipDataList) {
        List<PieEntry> entriesFixed = new ArrayList<>();
        List<PieEntry> entriesMobile = new ArrayList<>();
        List<Integer> colorsFixed = new ArrayList<>();
        List<Integer> colorsMobile = new ArrayList<>();
        SpannableStringBuilder ssFixed = new SpannableStringBuilder("Hasil KIP Fixed per Channel:\n\n");
        SpannableStringBuilder ssMobile = new SpannableStringBuilder("Hasil KIP Mobile per Channel:\n\n");

        for (int i = 0; i < kipDataList.size(); i++) {
            DashboardResponse.KipData kip = kipDataList.get(i);
            // Fixed
            entriesFixed.add(new PieEntry(kip.getFixed(), kip.getChannel()));
            colorsFixed.add(colors[i % colors.length]);
            SpannableString sFixed = new SpannableString(kip.getChannel() + ": " + kip.getFixed() + "\n");
            sFixed.setSpan(new ForegroundColorSpan(colors[i % colors.length]), 0, sFixed.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssFixed.append(sFixed);

            // Mobile
            entriesMobile.add(new PieEntry(kip.getMobile(), kip.getChannel()));
            colorsMobile.add(colors[i % colors.length]);
            SpannableString sMobile = new SpannableString(kip.getChannel() + ": " + kip.getMobile() + "\n");
            sMobile.setSpan(new ForegroundColorSpan(colors[i % colors.length]), 0, sMobile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssMobile.append(sMobile);
        }

        PieDataSet dataSetFixed = new PieDataSet(entriesFixed, "Fixed");
        dataSetFixed.setColors(colorsFixed);
        PieData dataFixed = new PieData(dataSetFixed);
        pieChartFixed.setData(dataFixed);
        pieChartFixed.getDescription().setText("");
        pieChartFixed.invalidate();

        PieDataSet dataSetMobile = new PieDataSet(entriesMobile, "Mobile");
        dataSetMobile.setColors(colorsMobile);
        PieData dataMobile = new PieData(dataSetMobile);
        pieChartMobile.setData(dataMobile);
        pieChartMobile.getDescription().setText("");
        pieChartMobile.invalidate();

        textFixedInfo.setText(ssFixed);
        textMobileInfo.setText(ssMobile);
    }

    private void setupBarChart(List<DashboardResponse.BarChart> barChartList) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        SpannableStringBuilder ssb = new SpannableStringBuilder("Hasil AUX per Status (menit):\n\n");
        float totalMinutes = 0;
        int[] auxColors = {Color.parseColor("#56B7F1"), Color.parseColor("#FF9800"), Color.parseColor("#4CAF50"), Color.parseColor("#F44336"), Color.parseColor("#9C27B0")};

        for (int i = 0; i < barChartList.size(); i++) {
            entries.add(new BarEntry(i, (float) barChartList.get(i).getValue()));
            labels.add(barChartList.get(i).getLabel());
            totalMinutes += barChartList.get(i).getValue();

            SpannableString s = new SpannableString(barChartList.get(i).getLabel() + ": " + barChartList.get(i).getValue() + " menit\n");
            s.setSpan(new ForegroundColorSpan(auxColors[i % auxColors.length]), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(s);
        }

        BarDataSet dataSet = new BarDataSet(entries, "AUX Minutes");
        dataSet.setColors(auxColors);
        BarData data = new BarData(dataSet);
        barChartAux.setData(data);
        barChartAux.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                return index >= 0 && index < labels.size() ? labels.get(index) : "";
            }
        });
        barChartAux.getXAxis().setGranularity(1f);
        barChartAux.getXAxis().setGranularityEnabled(true);
        barChartAux.getDescription().setText("");
        barChartAux.invalidate();
        ssb.append("\nTotal AUX Minutes: " + totalMinutes);
        textAuxInfo.setText(ssb);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
