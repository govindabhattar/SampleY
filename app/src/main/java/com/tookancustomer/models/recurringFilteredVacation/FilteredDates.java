package com.tookancustomer.models.recurringFilteredVacation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashutosh Ojha on 4/10/19.
 */
public class FilteredDates {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("is_skipped")
    @Expose
    private int isSkipped;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsSkipped() {
        return isSkipped;
    }

    public void setIsSkipped(int isSkipped) {
        this.isSkipped = isSkipped;
    }

}
