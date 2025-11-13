package com.example.monitoring.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class KipResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("selected_date")
    private String selectedDate;

    @SerializedName("data")
    private Map<String, KipChannelData> data; // gunakan map karena adapter memanggilnya seperti itu

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public Map<String, KipChannelData> getData() {
        return data;
    }

    public void setData(Map<String, KipChannelData> data) {
        this.data = data;
    }

    // ======================================================
    // 👇 Tambahkan inner class ini agar sesuai dengan adapter
    // ======================================================

    // Kelas untuk channel data (misal Email, IG, TW, VA)
    public static class KipChannelData {
        @SerializedName("fixed")
        private List<IssueItem> fixed;

        @SerializedName("mobile")
        private List<IssueItem> mobile;

        public List<IssueItem> getFixed() {
            return fixed;
        }

        public List<IssueItem> getMobile() {
            return mobile;
        }
    }

    // Kelas untuk item issue di setiap kategori
    public static class IssueItem {
        @SerializedName("issue")
        private String issue;

        @SerializedName("total")
        private int total;

        public String getIssue() {
            return issue;
        }

        public int getTotal() {
            return total;
        }
    }
}
