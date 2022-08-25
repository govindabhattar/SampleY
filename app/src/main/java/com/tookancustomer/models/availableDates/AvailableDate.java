package com.tookancustomer.models.availableDates;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableDate {
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("not_available_dates")
    @Expose
    private List<String> notAvailableDates = null;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getNotAvailableDates() {
        return notAvailableDates;
    }

    public void setNotAvailableDates(List<String> notAvailableDates) {
        this.notAvailableDates = notAvailableDates;
    }

}