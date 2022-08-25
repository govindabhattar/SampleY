
package com.tookancustomer.models.scheduleTimeSlots.merchantTimeSlots;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slot {

    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("is_booked")
    @Expose
    public int isBooked;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(int isBooked) {
        this.isBooked = isBooked;
    }
}
