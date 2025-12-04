package com.example.monitoring.model;

import java.util.List;

public class ActivityLogResponse {
    private boolean success;
    private List<ActivityLog> data;

    public boolean isSuccess() { return success; }
    public List<ActivityLog> getData() { return data; }
}
