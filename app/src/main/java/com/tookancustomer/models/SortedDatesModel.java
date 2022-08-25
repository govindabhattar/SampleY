package com.tookancustomer.models;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by shwetaaggarwal on 22/11/17.
 */

public class SortedDatesModel implements Comparable<SortedDatesModel> {
    private Date dateTime;
    private int timeOfDay = 0; //Morning = 0 , Afternoon = 1 , Evening = 2
    private boolean isBooked = false;


    public Date getDateTime() {
        return dateTime;
    }

    public int getTimeOfDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        if (cal.get(Calendar.HOUR_OF_DAY) < 12) return 0;
        else if (cal.get(Calendar.HOUR_OF_DAY) < 17) return 1;
        else if (cal.get(Calendar.HOUR_OF_DAY) < 24) return 2;
        else return timeOfDay;
    }

    public SortedDatesModel(Date dateTime) {
        this.dateTime = dateTime;
    }

    public SortedDatesModel(Date dateTime, boolean isBooked) {
        this.dateTime = dateTime;
        this.isBooked = isBooked;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public int compareTo(SortedDatesModel o) {
        return getDateTime().compareTo(o.getDateTime());
    }
}
