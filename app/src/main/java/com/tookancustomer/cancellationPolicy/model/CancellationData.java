package com.tookancustomer.cancellationPolicy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CancellationData {
    @SerializedName("cancellationRules")
    @Expose
    private ArrayList<CancellationRules> cancellationRules;

    public ArrayList<CancellationRules> getCancellationRules() {
        return cancellationRules;
    }
}
