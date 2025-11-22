package com.example.monitoring.model;

public class ProfileResponse {

    private boolean success;
    private ProfileData data;

    public boolean isSuccess() { return success; }
    public ProfileData getData() { return data; }

    public class ProfileData {
        private String name;
        private String role;
        private String created_at;
        private String updated_at;

        public String getName() { return name; }
        public String getRole() { return role; }
        public String getCreated_at() { return created_at; }
        public String getUpdated_at() { return updated_at; }
    }
}

