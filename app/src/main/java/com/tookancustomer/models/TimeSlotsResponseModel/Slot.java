
package com.tookancustomer.models.TimeSlotsResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class Slot {

    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("is_booked")
    @Expose
    public int isBooked;

    private int timeOfDay = 0;  //Morning = 0 , Afternoon = 1 , Evening = 2

    public int getTimeOfDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getInstance().getDate(startTime));
        if (cal.get(Calendar.HOUR_OF_DAY) < 12) return 0;
        else if (cal.get(Calendar.HOUR_OF_DAY) < 17) return 1;
        else if (cal.get(Calendar.HOUR_OF_DAY) < 24) return 2;
        else return timeOfDay;
    }

    public Date getStartTimeDate() {
        return DateUtils.getInstance().getDate(startTime);
    }

    public Date getEndTimeDate() {
        return DateUtils.getInstance().getDate(endTime);
    }

    public Slot(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(int isBooked) {
        this.isBooked = isBooked;
    }
}
