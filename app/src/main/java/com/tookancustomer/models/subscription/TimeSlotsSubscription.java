package com.tookancustomer.models.subscription;

import java.util.ArrayList;

/**
 * Created by Ashutosh Ojha on 2/15/19.
 */
public class TimeSlotsSubscription {
    private String header = "";
    private ArrayList<SubscriptionSlots> sortedDatesArrayList = new ArrayList<>();

    public TimeSlotsSubscription(String header, ArrayList<SubscriptionSlots> sortedDatesArrayList) {
        this.header = header;
        this.sortedDatesArrayList = sortedDatesArrayList;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<SubscriptionSlots> getSortedDatesArrayList() {
        return sortedDatesArrayList;
    }

    public void setSortedDatesArrayList(ArrayList<SubscriptionSlots> sortedDatesArrayList) {
        this.sortedDatesArrayList = sortedDatesArrayList;
    }

    public void AvailableTimeSlotsModelResponse(String header, ArrayList<SubscriptionSlots> sortedDatesArrayList) {
        this.header = header;
        this.sortedDatesArrayList = sortedDatesArrayList;
    }
}
