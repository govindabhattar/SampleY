package com.tookancustomer.interfaces;

import java.util.Date;

public class DateListners {

    public interface OnDateSelectedListener{
        void onDateSelected(Date date);
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(Date date);
    }
}
