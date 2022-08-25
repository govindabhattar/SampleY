package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecurringSurgeListData implements Serializable {

    @SerializedName("schedule_time")
    @Expose
    private String scheduleTime;
    @SerializedName("day_id")
    @Expose
    private int dayId;
    @SerializedName("occurances")
    @Expose
    private int occurances;
    @SerializedName("delivery_charges")
    @Expose
    private double deliveryCharges;


    @SerializedName("initial_delivery_charges")
    @Expose
    private double initialDeliveryCharges;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("total_amount")
    @Expose
    private double totalAmount;

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }


    public int getOccurances() {
        return occurances;
    }

    public void setOccurances(int occurances) {
        this.occurances = occurances;
    }

    public double getInitialDeliveryCharges() {
        return initialDeliveryCharges;
    }

    public void setInitialDeliveryCharges(double initialDeliveryCharges) {
        this.initialDeliveryCharges = initialDeliveryCharges;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


}