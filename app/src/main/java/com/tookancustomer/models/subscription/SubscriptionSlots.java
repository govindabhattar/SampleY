package com.tookancustomer.models.subscription;

import com.tookancustomer.utility.DateUtils;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Ashutosh Ojha on 2/15/19.
 */
public class SubscriptionSlots  implements Serializable {

    private int timeOfDay = 0;  //Morning = 0 , Afternoon = 1 , Evening = 2
    private String slotTime;



    public int getTimeOfDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getInstance().getDate(slotTime));
        if (cal.get(Calendar.HOUR_OF_DAY) < 12) return 0;
        else if (cal.get(Calendar.HOUR_OF_DAY) < 17) return 1;
        else if (cal.get(Calendar.HOUR_OF_DAY) < 24) return 2;
        else return timeOfDay;
    }

    public void setTimeOfDay(final int timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(final String slotTime) {
        this.slotTime = slotTime;
    }
}
