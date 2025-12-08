package com.example.monitoring.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DashboardResponse {
    private boolean success;
    private String message;
    private Data data;

    public Data getData() { return data; }

    public static class Data {
        private List<PieChart> pieChart;
        private List<BarChart> barChart;
        private List<LineChart> lineChart;
        private List<TopKip> top5Kip;

        public List<PieChart> getPieChart() { return pieChart; }
        public List<BarChart> getBarChart() { return barChart; }
        public List<LineChart> getLineChart() { return lineChart; }
        public List<TopKip> getTop5Kip() { return top5Kip; }
    }

    public static class PieChart { private String label; private float value; public String getLabel() { return label; } public float getValue() { return value; } }
    public static class BarChart { private String label; private float value; public String getLabel() { return label; } public float getValue() { return value; } }
    public static class LineChart { private int hour; private int total; public int getHour() { return hour; } public int getTotal() { return total; } }

    public static class TopKip {
        private String category;
        private List<PieChart> pieChart;
        private List<LineChart> lineChart;

        public String getCategory() { return category; }
        public List<PieChart> getPieChart() { return pieChart; }
        public List<LineChart> getLineChart() { return lineChart; }
    }
}

