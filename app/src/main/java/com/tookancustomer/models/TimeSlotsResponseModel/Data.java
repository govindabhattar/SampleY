
package com.tookancustomer.models.TimeSlotsResponseModel;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("pre_booking_buffer")
    @Expose
    private Integer preBookingBuffer = 0;
    @SerializedName("service_time")
    @Expose
    private int serviceTime = 0;  //in minutes always  /*Here service time is used for laundry to maintain some gap between start and end Date*/

    @SerializedName("slots")
    @Expose
    private List<Slot> slots = new ArrayList<>();

    @SerializedName("delivery_slots")
    @Expose
    private List<Slot> deliverySlots = new ArrayList<>();

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

    public Integer getPreBookingBuffer() {
        return preBookingBuffer != null ? preBookingBuffer : 0;
    }

    public void setPreBookingBuffer(Integer preBookingBuffer) {
        this.preBookingBuffer = preBookingBuffer;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public List<Slot> getDeliverySlots() {
        return deliverySlots;
    }

    public void setDeliverySlots(List<Slot> deliverySlots) {
        this.deliverySlots = deliverySlots;
    }
}
