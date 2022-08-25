
package com.tookancustomer.filter.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("result")
    @Expose
    private ArrayList<Result> result = null;
    @SerializedName("is_filters_enabled")
    @Expose
    private int isFiltersEnabled;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
        this.result = result;
    }

    public int getIsFiltersEnabled() {
        return isFiltersEnabled;
    }

    public void setIsFiltersEnabled(int isFiltersEnabled) {
        this.isFiltersEnabled = isFiltersEnabled;
    }
}
