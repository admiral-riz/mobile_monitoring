package com.example.monitoring.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class KipData {
    @SerializedName("channel")
    private String channel;

    @SerializedName("fixed")
    private List<KipDetail> fixed;

    @SerializedName("mobile")
    private List<KipDetail> mobile;

    public String getChannel() {
        return channel;
    }

    public List<KipDetail> getFixed() {
        return fixed;
    }

    public List<KipDetail> getMobile() {
        return mobile;
    }
}
