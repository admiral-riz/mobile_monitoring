package com.example.monitoring.model;

import com.google.gson.annotations.SerializedName;

public class KipDetail {
    @SerializedName("rank")
    private int rank;

    @SerializedName("name")
    private String name;

    @SerializedName("count")
    private int count;

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
