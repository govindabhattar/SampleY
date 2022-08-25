package com.tookancustomer.models.scheduleTimeSlots.merchantTimeSlots;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantTimeSlotData {

    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("new_flow")
    @Expose
    public int newFlow;
    @SerializedName("slots")
    @Expose
    public List<Slot> slots = new ArrayList<>();

    @SerializedName("delivery_slots")
    @Expose
    public List<Slot> deliverySlots = new ArrayList<>();
    @SerializedName("pre_booking_buffer")
    @Expose
    public int preBookingBuffer;

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

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public int getPreBookingBuffer() {
        return preBookingBuffer;
    }

    public void setPreBookingBuffer(int preBookingBuffer) {
        this.preBookingBuffer = preBookingBuffer;
    }

    public List<Slot> getDeliverySlots() {
        return deliverySlots;
    }

    public void setDeliverySlots(List<Slot> deliverySlots) {
        this.deliverySlots = deliverySlots;
    }
    public int getNewFlow() {
        return newFlow;
    }

    public void setNewFlow(int newFlow) {
        this.newFlow = newFlow;
    }
}