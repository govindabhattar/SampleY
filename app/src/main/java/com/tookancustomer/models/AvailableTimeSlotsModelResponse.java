package com.tookancustomer.models;

import java.util.ArrayList;

/**
 * Created by shwetaaggarwal on 23/11/17.
 */

public class AvailableTimeSlotsModelResponse {
    private String header = "";
    private ArrayList<SortedDatesModel> sortedDatesArrayList = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<SortedDatesModel> getSortedDatesArrayList() {
        return sortedDatesArrayList;
    }

    public void setSortedDatesArrayList(ArrayList<SortedDatesModel> sortedDatesArrayList) {
        this.sortedDatesArrayList = sortedDatesArrayList;
    }

    public AvailableTimeSlotsModelResponse(String header, ArrayList<SortedDatesModel> sortedDatesArrayList) {
        this.header = header;
        this.sortedDatesArrayList = sortedDatesArrayList;
    }

}
