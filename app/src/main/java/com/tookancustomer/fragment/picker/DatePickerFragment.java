package com.tookancustomer.fragment.picker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.tookancustomer.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Developer: Rishabh
 * Dated: 21/12/15.
 */
public class DatePickerFragment extends DialogFragment {

    private int year;
    private int month;
    private int day;

    private DatePickerDialog.OnDateSetListener listener;


    private long maximumDate;
    private long minimumDate;

    public DatePickerFragment() {
//        Locale locale = getResources().getConfiguration().locale;
//        Locale.setDefault(locale);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);

    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, year, month, day);

        if (minimumDate > 0) datePickerDialog.getDatePicker().setMinDate(minimumDate);

        if (maximumDate > 0) datePickerDialog.getDatePicker().setMaxDate(maximumDate);

        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok_text), datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_text), datePickerDialog);


        return datePickerDialog;
    }

    public void setMinDate(long minimumDate) {
        this.minimumDate = minimumDate;
    }

    public void setMaxDate(long maximumDate) {
        this.maximumDate = maximumDate;
    }

//add days to current date to set max date
    public void setMaxdays(int maxDays) {
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
//        c.setTime(currentDate);
        c.add(Calendar.DATE, maxDays+1);
        currentDate = c.getTime();
        this.maximumDate = currentDate.getTime();
    }

}
