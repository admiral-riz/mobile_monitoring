package com.example.monitoring.model;

import java.util.List;

public class KelolaDataResponse {
    private boolean success;
    private List<Object> data;
    private int total;
    private List<String> fields;
    private String primaryKey;

    public boolean isSuccess() { return success; }
    public List<Object> getData() { return data; }
    public int getTotal() { return total; }
    public List<String> getFields() { return fields; }
    public String getPrimaryKey() { return primaryKey; }
}
