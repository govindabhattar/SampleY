package com.tookancustomer.models.MarketplaceStorefrontModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.UIManager;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.tookancustomer.appdata.Constants.DateFormat.END_USER_DATE_FORMAT_24;
import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;
import static com.tookancustomer.appdata.Constants.DateFormat.TIME_FORMAT_24_WITHOUT_SECOND;

public class StoreTimingsArr implements Serializable {
    @SerializedName("start_time")
    @Expose
    private String startTime = "";
    @SerializedName("end_time")
    @Expose
    private String endTime = "";

    public static String displayTime(String utcDateAndTime) throws ParseException {
        String format = "hh:mm a";
        if (!UIManager.is12HourFormat()) {
            format = TIME_FORMAT_24_WITHOUT_SECOND;
        }

        java.text.DateFormat utcFormat = new SimpleDateFormat(STANDARD_DATE_FORMAT_TZ);
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = utcFormat.parse(utcDateAndTime);

        java.text.DateFormat pstFormat = new SimpleDateFormat(format);
        pstFormat.setTimeZone(TimeZone.getDefault());
        return pstFormat.format(date);

    }

    public String getStartTime() {
        try {
            return displayTime(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return startTime;

        }
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        try {
            return displayTime(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return endTime;

        }
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}
