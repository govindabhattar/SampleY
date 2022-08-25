package com.tookancustomer.models;

import com.tookancustomer.models.TimeSlotsResponseModel.Slot;
import com.tookancustomer.models.tookanSchedulingModel.Datum;

import java.util.ArrayList;

/**
 * Created by shwetaaggarwal on 23/11/17.
 */

public class LaundryScheduleTimeSlotsResponse {
    private String header = "";
    private ArrayList<Slot> timeSlotsArrayList = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<Slot> getTimeSlotsArrayList() {
        return timeSlotsArrayList;
    }

    public void setTimeSlotsArrayList(ArrayList<Slot> timeSlotsArrayList) {
        this.timeSlotsArrayList = timeSlotsArrayList;
    }

    public LaundryScheduleTimeSlotsResponse(String header, ArrayList<Slot> timeSlotsArrayList) {
        this.header = header;
        this.timeSlotsArrayList = timeSlotsArrayList;
    }
}
