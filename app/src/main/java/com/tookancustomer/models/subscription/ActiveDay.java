package com.tookancustomer.models.subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashutosh Ojha on 2/15/19.
 */
public class ActiveDay {

    @SerializedName("day_id")
    @Expose
    private int dayId;
    @SerializedName("is_active")
    @Expose
    private int isActive;

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
