package com.example.monitoring.model;

import java.util.List;

public class KelolaDataResponse {
    private boolean success;
    private List<Object> data;
    private int total;
    private int currentPage;
    private int totalPage;
    private String primaryKey;

    public boolean isSuccess() { return success; }
    public List<Object> getData() { return data; }
    public int getTotal() { return total; }
    public int getCurrentPage() { return currentPage; }
    public int getTotalPage() { return totalPage; }
    public String getPrimaryKey() { return primaryKey; }
}

