package com.example.monitoring.model;

public class LoginResponse {

    private boolean success;
    private String message;
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        private int user_id;
        private String username;
        private String role;
        private String token;

        public int getUser_id() {
            return user_id;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }

        public String getToken() {
            return token;
        }
    }
}
