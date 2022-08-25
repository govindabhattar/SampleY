package com.tookancustomer.models.recurringFilteredVacation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ashutosh Ojha on 4/10/19.
 */
public class FilteredVacationData {

    @SerializedName("result")
    @Expose
    private List<FilteredDates> result = null;

    public List<FilteredDates> getResult() {
        return result;
    }

    public void setResult(List<FilteredDates> result) {
        this.result = result;
    }
}
