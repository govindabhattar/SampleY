package com.tookancustomer.models.recurringvacation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ashutosh Ojha on 2/25/19.
 */
public class RecurringVacation {

    @SerializedName("result")
    @Expose
    private List<SkippedData> skippedData = null;

    public List<SkippedData> getSkippedData() {
        return skippedData;
    }
}
