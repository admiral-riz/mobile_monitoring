package com.example.monitoring.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DashboardResponse {

    private boolean success;
    private String message;
    private Data data;

    public boolean isSuccess() { return success; }
    public Data getData() { return data; }

    public static class Data {
        @SerializedName("kipData")
        private List<KipData> kipData;
        @SerializedName("barChart")
        private List<BarChart> barChart;

        public List<KipData> getKipData() { return kipData; }
        public List<BarChart> getBarChart() { return barChart; }
    }

    public static class KipData {
        private String channel;
        private int fixed;
        private int mobile;

        public String getChannel() { return channel; }
        public int getFixed() { return fixed; }
        public int getMobile() { return mobile; }
    }

    public static class BarChart {
        private String label;
        private double value;

        public String getLabel() { return label; }
        public double getValue() { return value; }
    }
}
