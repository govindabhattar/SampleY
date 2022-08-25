package com.tookancustomer.fragment.picker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Developer: Rishabh
 * Dated: 21/12/15.
 */
public class TimePickerFragment extends DialogFragment {


    private int hour;
    private int minute;

    private TimePickerDialog.OnTimeSetListener listener;

    public TimePickerFragment() {

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), listener, hour, minute, false);

        return timePickerDialog;
    }

}
