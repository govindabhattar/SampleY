package com.tookancustomer.models;

import com.tookancustomer.models.tookanSchedulingModel.Datum;

import java.util.ArrayList;

/**
 * Created by shwetaaggarwal on 23/11/17.
 */

public class TookanScheduleTimeSlotsResponse {
    private String header = "";
    private ArrayList<Datum> timeSlotsArrayList = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<Datum> getTimeSlotsArrayList() {
        return timeSlotsArrayList;
    }

    public void setTimeSlotsArrayList(ArrayList<Datum> timeSlotsArrayList) {
        this.timeSlotsArrayList = timeSlotsArrayList;
    }

    public TookanScheduleTimeSlotsResponse(String header, ArrayList<Datum> timeSlotsArrayList) {
        this.header = header;
        this.timeSlotsArrayList = timeSlotsArrayList;
    }
}
