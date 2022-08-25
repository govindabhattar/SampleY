
package com.tookancustomer.models.TimeSlotsResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewData {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("slots")
    @Expose
    private List<String> slots = new ArrayList<>();
    @SerializedName("pre_booking_buffer")
    @Expose
    private Integer preBookingBuffer = 0;

    @SerializedName("is_google_calendar_active")
    @Expose
    private int isGoogleCalendarActive;


    public int getIsGoogleCalendarActive() {
        return isGoogleCalendarActive;
    }

    public void setIsGoogleCalendarActive(int isGoogleCalendarActive) {
        this.isGoogleCalendarActive = isGoogleCalendarActive;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }

    public Integer getPreBookingBuffer() {
        return preBookingBuffer != null ? preBookingBuffer:0;
    }

    public void setPreBookingBuffer(Integer preBookingBuffer) {
        this.preBookingBuffer = preBookingBuffer;
    }
}
