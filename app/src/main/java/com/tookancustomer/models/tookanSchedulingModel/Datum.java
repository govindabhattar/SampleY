package com.tookancustomer.models.tookanSchedulingModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class Datum {
    @SerializedName("slot_timming")
    @Expose
    private String slotTimming = "";
    @SerializedName("available_status")
    @Expose
    private Integer availableStatus = 0;  // 0 --> tookan agent available, 1--> booked, 2--> unavailable
    @SerializedName("slot_start_time")
    @Expose
    private String slotStartTime = "";
    @SerializedName("slot_end_time")
    @Expose
    private String slotEndTime = "";

    private int timeOfDay = 0;  //Morning = 0 , Afternoon = 1 , Evening = 2

    public int getTimeOfDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getInstance().getDate(slotStartTime));
        if (cal.get(Calendar.HOUR_OF_DAY) < 12) return 0;
        else if (cal.get(Calendar.HOUR_OF_DAY) < 17) return 1;
        else if (cal.get(Calendar.HOUR_OF_DAY) < 24) return 2;
        else return timeOfDay;
    }

    public Date getStartTimeDate() {
        return DateUtils.getInstance().getDate(slotStartTime);
    }

    public Date getEndTimeDate() {
        return DateUtils.getInstance().getDate(slotEndTime);
    }

    public String getSlotTimming() {
        return slotTimming;
    }

    public void setSlotTimming(String slotTimming) {
        this.slotTimming = slotTimming;
    }

    public Integer getAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(Integer availableStatus) {
        this.availableStatus = availableStatus;
    }

    public String getSlotStartTime() {
        return slotStartTime;
    }

    public void setSlotStartTime(String slotStartTime) {
        this.slotStartTime = slotStartTime;
    }

    public String getSlotEndTime() {
        return slotEndTime;
    }

    public void setSlotEndTime(String slotEndTime) {
        this.slotEndTime = slotEndTime;
    }

}
