package com.example.monitoring.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AuxResponse {
    private boolean success;
    private String message;
    private String date;
    private int total_found;

    // Gunakan nama field persis dari API: top_10
    @SerializedName("top_10")
    private List<AuxItem> top10;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getDate() { return date; }
    public int getTotal_found() { return total_found; }
    public List<AuxItem> getTop10() { return top10; }

    public static class AuxItem {
        // Coba beberapa nama key yang mungkin dipakai API
        @SerializedName("agent")
        private String agent;

        @SerializedName("fullname")
        private String fullname;

        @SerializedName("fullname_norm")
        private String fullname_norm;

        @SerializedName("site")
        private String site;

        // beberapa API mungkin gunakan nama berbeda untuk kolom menit
        @SerializedName("istirahat1")
        private Double istirahat1;

        @SerializedName("istirahat_1")
        private Double istirahat_1;

        @SerializedName("istirahat_2")
        private Double istirahat_2;

        @SerializedName("istirahat_3")
        private Double istirahat_3;

        @SerializedName("toilet")
        private Double toilet;

        @SerializedName("ibadah")
        private Double ibadah;

        @SerializedName("total")
        private Double total;

        @SerializedName("total_aux_minutes")
        private Double total_aux_minutes;

        @SerializedName("status")
        private String status;

        // Getter dengan fallback untuk nama agent
        public String getAgentName() {
            if (agent != null && !agent.trim().isEmpty()) return agent;
            if (fullname != null && !fullname.trim().isEmpty()) return fullname;
            if (fullname_norm != null && !fullname_norm.trim().isEmpty()) return fullname_norm;
            return "-";
        }

        public String getSite() { return site == null ? "-" : site; }

        public double getIstirahat1() {
            if (istirahat1 != null) return istirahat1;
            if (istirahat_1 != null) return istirahat_1;
            return 0.0;
        }

        public double getIstirahat2() { return istirahat_2 == null ? 0.0 : istirahat_2; }
        public double getIstirahat3() { return istirahat_3 == null ? 0.0 : istirahat_3; }
        public double getToilet() { return toilet == null ? 0.0 : toilet; }
        public double getIbadah() { return ibadah == null ? 0.0 : ibadah; }

        public double getTotal() {
            if (total != null) return total;
            if (total_aux_minutes != null) return total_aux_minutes;
            return 0.0;
        }

        public String getStatus() { return status == null ? "-" : status; }
    }
}
