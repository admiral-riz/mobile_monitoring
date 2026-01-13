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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.monitoring.api.RegisterAPI;
import com.example.monitoring.api.ServerAPI;
import com.example.monitoring.databinding.FragmentDashboardBinding;
import com.example.monitoring.model.DashboardResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private String selectedDateKip, selectedDateAux;

    private final int[] chartColors = {
            Color.parseColor("#56B7F1"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#FFC107"),
            Color.parseColor("#F44336"),
            Color.parseColor("#9C27B0")
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());

        selectedDateKip = today;
        selectedDateAux = today;

        binding.btnSelectDateKip.setText("Tanggal KIP : " + today);
        binding.btnSelectDateAux.setText("Tanggal AUX : " + today);

        loadKipData();
        loadAuxData();

        binding.btnSelectDateKip.setOnClickListener(v -> showDatePicker(true));
        binding.btnSelectDateAux.setOnClickListener(v -> showDatePicker(false));

        return binding.getRoot();
    }

    private void showDatePicker(boolean isKip) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), (view, y, m, d) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(y, m, d);
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());

            if (isKip) {
                selectedDateKip = date;
                binding.btnSelectDateKip.setText("Tanggal KIP : " + date);
                loadKipData();
            } else {
                selectedDateAux = date;
                binding.btnSelectDateAux.setText("Tanggal AUX : " + date);
                loadAuxData();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadKipData() {
        ServerAPI.getClient().create(RegisterAPI.class)
                .getDashboardData(selectedDateKip)
                .enqueue(new Callback<DashboardResponse>() {
                    @Override
                    public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                        if (response.isSuccessful() && response.body() != null)
                            setupPieCharts(response.body().getData().getKipData());
                    }
                    @Override public void onFailure(Call<DashboardResponse> call, Throwable t) {}
                });
    }

    private void loadAuxData() {
        ServerAPI.getClient().create(RegisterAPI.class)
                .getDashboardData(selectedDateAux)
                .enqueue(new Callback<DashboardResponse>() {
                    @Override
                    public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                        if (response.isSuccessful() && response.body() != null)
                            setupBarChart(response.body().getData().getBarChart());
                    }
                    @Override public void onFailure(Call<DashboardResponse> call, Throwable t) {}
                });
    }

    private void setupPieCharts(List<DashboardResponse.KipData> list) {

        if (list == null || list.isEmpty()) {
            binding.textFixedInfo.setText("Tidak ada data KIP.");
            binding.textMobileInfo.setText("");
            return;
        }

        List<PieEntry> fixed = new ArrayList<>();
        List<PieEntry> mobile = new ArrayList<>();
        SpannableStringBuilder sf = new SpannableStringBuilder("KIP FIXED\n\n");
        SpannableStringBuilder sm = new SpannableStringBuilder("KIP MOBILE\n\n");

        for (int i = 0; i < list.size(); i++) {
            DashboardResponse.KipData d = list.get(i);
            fixed.add(new PieEntry(d.getFixed(), d.getChannel()));
            mobile.add(new PieEntry(d.getMobile(), d.getChannel()));

            SpannableString s1 = new SpannableString(d.getChannel()+" : "+d.getFixed()+"\n");
            s1.setSpan(new ForegroundColorSpan(chartColors[i%5]),0,s1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sf.append(s1);

            SpannableString s2 = new SpannableString(d.getChannel()+" : "+d.getMobile()+"\n");
            s2.setSpan(new ForegroundColorSpan(chartColors[i%5]),0,s2.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sm.append(s2);
        }

        PieDataSet fSet = new PieDataSet(fixed,"Fixed");
        fSet.setColors(chartColors);
        binding.pieChartFixed.setData(new PieData(fSet));
        binding.pieChartFixed.invalidate();

        PieDataSet mSet = new PieDataSet(mobile,"Mobile");
        mSet.setColors(chartColors);
        binding.pieChartMobile.setData(new PieData(mSet));
        binding.pieChartMobile.invalidate();

        binding.textFixedInfo.setText(sf);
        binding.textMobileInfo.setText(sm);
    }

    private void setupBarChart(List<DashboardResponse.BarChart> list) {

        if (list == null || list.isEmpty()) {
            binding.textAuxInfo.setText("Tidak ada data AUX.");
            return;
        }

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        SpannableStringBuilder info = new SpannableStringBuilder("AUX DETAIL\n\n");

        for (int i = 0; i < list.size(); i++) {
            entries.add(new BarEntry(i, (float) list.get(i).getValue()));
            labels.add(list.get(i).getLabel());
            info.append(list.get(i).getLabel())
                    .append(" : ")
                    .append(convertMinutesToHMS(list.get(i).getValue()))
                    .append("\n");
        }

        BarDataSet set = new BarDataSet(entries,"AUX");
        set.setColors(chartColors);
        BarData data = new BarData(set);

        binding.barChartAux.setData(data);
        binding.barChartAux.getXAxis().setGranularity(1f);
        binding.barChartAux.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override public String getFormattedValue(float value) {
                return value < labels.size() ? labels.get((int)value) : "";
            }
        });
        binding.barChartAux.invalidate();
        binding.textAuxInfo.setText(info);
    }

    private String convertMinutesToHMS(double minutes) {
        int total = (int)(minutes*60);
        int h = total/3600;
        int m = (total%3600)/60;
        int s = total%60;
        return String.format(Locale.getDefault(),
                "%02d jam %02d menit %02d detik",h,m,s);
    }
}
