package com.example.monitoring.model;

import java.util.List;

public class AuxDetailResponse {
    private boolean success;
    private String message;
    private String title;
    private String date;
    private List<Detail> detail_aktivitas;
    private Ringkasan ringkasan;

    public static class Detail {
        private String waktu_mulai;
        private String waktu_selesai;
        private String durasi;
        private String status;
        private String keterangan;

        // Getter
        public String getWaktu_mulai() { return waktu_mulai; }
        public String getWaktu_selesai() { return waktu_selesai; }
        public String getDurasi() { return durasi; }
        public String getStatus() { return status; }
        public String getKeterangan() { return keterangan; }
    }

    public static class Ringkasan {
        private String total_durasi;
        private String total_menit;
        private String status;

        public String getTotal_durasi() { return total_durasi; }
        public String getTotal_menit() { return total_menit; }
        public String getStatus() { return status; }
    }

    // Getter utama
    public boolean isSuccess() { return success; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public List<Detail> getDetail_aktivitas() { return detail_aktivitas; }
    public Ringkasan getRingkasan() { return ringkasan; }
}
