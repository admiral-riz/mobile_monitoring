package com.example.monitoring.model;

import java.util.List;

public class ActivityLogResponse {

    private boolean success;
    private Data data;

    public boolean isSuccess() { return success; }
    public Data getData() { return data; }

    public class Data {
        private List<LogData> logs;
        private int currentPage;
        private int perPage;
        private int total;

        public List<LogData> getLogs() { return logs; }
        public int getCurrentPage() { return currentPage; }
        public int getPerPage() { return perPage; }
        public int getTotal() { return total; }
    }


    public class LogData {
        private int id;
        private String username;
        private String activity;
        private String status;
        private String description;
        private String ip_address;
        private String user_agent;
        private String created_at;

        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getActivity() { return activity; }
        public String getStatus() { return status; }
        public String getDescription() { return description; }
        public String getIp_address() { return ip_address; }
        public String getUser_agent() { return user_agent; }
        public String getCreated_at() { return created_at; }
    }
}
