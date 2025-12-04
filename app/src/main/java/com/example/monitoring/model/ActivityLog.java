package com.example.monitoring.model;

public class ActivityLog {
    private int id;
    private int user_id;
    private String username;
    private String activity;
    private String description;
    private String status;
    private String action;
    private String ip_address;
    private String user_agent;
    private String created_at;

    public int getId() { return id; }
    public int getUser_id() { return user_id; }
    public String getUsername() { return username; }
    public String getActivity() { return activity; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getAction() { return action; }
    public String getIp_address() { return ip_address; }
    public String getUser_agent() { return user_agent; }
    public String getCreated_at() { return created_at; }
}
