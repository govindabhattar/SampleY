package com.tookancustomer.utility;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.tookancustomer.appdata.Constants;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.interfaces.DateListners;
import com.tookancustomer.modules.recurring.model.DateItem;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Developer: Rishabh
 * Dated: 17/07/15.
 */
public class DateUtils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm aa";

    private static DateUtils dateUtils;

    private final int DEFAULT_DATE = 1;
    private final int DEFAULT_YEAR = 2017;

    /**
     * Method to get instance of this class
     *
     * @return
     */
    public static DateUtils getInstance() {

        if (dateUtils == null)
            dateUtils = new DateUtils();

        return dateUtils;
    }

    /**
     * Method to get Todays date in
     * yyyy-MM-dd HH:mmZ format
     *
     * @return
     */
    public String getTodaysDate() {

        return getTodaysDate(DEFAULT_DATE_FORMAT);
    }

    /**
     * Method to get Todays date in a specified format
     *
     * @return
     */
    public String getTodaysDate(String format) {
        return getFormattedDate(new Date(), format);
    }

    /**
     * Method to get Todays date in a specified format
     *
     * @return
     */
    public String getFormattedDate(Date date, String format) {
        return new SimpleDateFormat(format, getDateTimeLocale()).format(date);
    }

    public Date getDateFromString(String formattedDate, String currentFormat) {
        Date date;

        try {

            date = new SimpleDateFormat(currentFormat, getDateTimeLocale()).parse(formattedDate);
        } catch (Exception exTz) {
            Utils.printStackTrace(exTz);
            date = new Date();
        }

        return date;

    }

    /**
     * Returns the desired time from a standard formatted time
     *
     * @param formattedDate 2015-06-01 13:25:00 or 2015-06-01'T'13:25:00'Z'
     * @param currentFormat
     * @param desiredFormat
     * @return
     */
    public String parseDateAs(String formattedDate, String currentFormat, String desiredFormat) {

        Date date;

        try {

            date = new SimpleDateFormat(currentFormat, getDateTimeLocale()).parse(formattedDate);
        } catch (Exception exTz) {
            Utils.printStackTrace(exTz);
            date = new Date();
        }

        return new SimpleDateFormat(desiredFormat, getDateTimeLocale()).format(date);
    }

    public String parseDatetaskDetalAs(String formattedDate, String currentFormat, String desiredFormat) {

        Date date;

        try {

            date = new SimpleDateFormat(currentFormat, Locale.ENGLISH).parse(formattedDate);
        } catch (Exception exTz) {
            Utils.printStackTrace(exTz);
            date = new Date();
        }

        return new SimpleDateFormat(desiredFormat, getDateTimeLocale()).format(date);
    }


    public String convertTimeTodesiredFormet(String date, String desiredFormat, String currentFormat) {

        String dateString = "";


        //Displaying given time in 12 hour format with AM/PM
        //old format
        SimpleDateFormat sdf = new SimpleDateFormat(currentFormat);
        try {
            Date date3 = sdf.parse(date);
            //new format
            SimpleDateFormat sdf2 = new SimpleDateFormat(desiredFormat);
            //formatting the given time to new format with AM/PM
            dateString = sdf2.format(date3);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return dateString;
    }

    /**
     * Returns the desired time from a standard formatted time
     *
     * @param formattedDate 2015-06-01 13:25:00 or 2015-06-01'T'13:25:00'Z'
     * @param desiredFormat
     * @return
     */
    public String parseDateAs(String formattedDate, String desiredFormat) {

        return new SimpleDateFormat(desiredFormat, getDateTimeLocale()).format(getDate(formattedDate));
    }

    /**
     * @param formattedDate
     * @return
     */
    public Date getDate(String formattedDate) {

        Date date;

        try {

            date = new SimpleDateFormat(UIManager.getStandardDateTimeFormat(), getDateTimeLocale()).parse(formattedDate);

        } catch (Exception e1) {
            try {
                date = new SimpleDateFormat(Constants.DateFormat.STANDARD_DATE_FORMAT_NEW, getDateTimeLocale()).parse(formattedDate);

            } catch (Exception expnewFormat) {

                try {

                    date = new SimpleDateFormat(Constants.DateFormat.STANDARD_DATE_FORMAT, getDateTimeLocale()).parse(formattedDate);
                } catch (ParseException e) {

                    try {

                        date = new SimpleDateFormat(Constants.DateFormat.STANDARD_DATE_FORMAT_TZ, getDateTimeLocale()).parse(formattedDate);
                    } catch (Exception exTz) {

                        try {
                            date = new SimpleDateFormat(Constants.DateFormat.STANDARD_DATE_FORMAT_NEW, getDateTimeLocale()).parse(formattedDate);
                        } catch (Exception exNewFormat) {
                            try {
                                date = new SimpleDateFormat(Constants.DateFormat.ONLY_DATE, getDateTimeLocale()).parse(formattedDate);
                            } catch (Exception exDate) {

                                try {
                                    date = new SimpleDateFormat(Constants.DateFormat.TIME_FORMAT_24, getDateTimeLocale()).parse(formattedDate);


                                } catch (Exception exTime) {
                                    try {
                                        date = new SimpleDateFormat(Constants.DateFormat.TIME_FORMAT_12_without_ampm, getDateTimeLocale()).parse(formattedDate);
                                    } catch (Exception timeFormatException) {
                                        try {
                                            date = new SimpleDateFormat(Constants.DateFormat.BACKEND_PICKUP_TIME, getDateTimeLocale()).parse(formattedDate);
                                        } catch (Exception exD) {
                                            try {
                                                date = new SimpleDateFormat(UIManager.getDateTimeFormat(), getDateTimeLocale()).parse(formattedDate);
                                            } catch (Exception exEndUserFormat) {

                                                Utils.printStackTrace(exEndUserFormat);
                                                Utils.printStackTrace(exD);
//                                        exTim
                                                Utils.printStackTrace(e);
//                                        exDat
                                                Utils.printStackTrace(e);
//
                                                Utils.printStackTrace(e);
                                                Utils.printStackTrace(exTz);
                                                Utils.printStackTrace(timeFormatException);
                                                date = new Date();
                                            }
                                        }
                                    }
                                }


                            }
                        }


                    }
                }
            }
        }


        return date;
    }

    /**
     * Method to get Todays date in a specified format
     *
     * @return
     */
    public int getCurrentDay() {
        return getCurrent(Calendar.DAY_OF_MONTH);
    }

    /**
     * Method to get current month
     *
     * @return
     */
    public int getCurrentMonth() {
        return getCurrent(Calendar.MONTH);
    }

    /**
     * Method to get current month
     *
     * @return
     */
    public int getCurrentYear() {
        return getCurrent(Calendar.YEAR);
    }

    /**
     * Method to get value of field
     *
     * @param field
     * @return
     */
    private int getCurrent(int field) {
        return get(Calendar.getInstance(), field);
    }

    /**
     * Method to get value of field
     *
     * @param calendar
     * @param field
     * @return
     */
    public int get(Calendar calendar, int field) {
        return calendar.get(field);
    }

    /**
     * Method to return the name of the day
     *
     * @param day
     * @param month
     * @param year
     * @return
     */
    public String getDayName(int day, int month, int year) {

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE", getDateTimeLocale());
        Date intendedDate = new GregorianCalendar(year, month, day).getTime();
        return formatter.format(intendedDate);
    }

    /**
     * Method to get current month
     *
     * @return
     */
    public String getMonthName(int month) {

        SimpleDateFormat formatter = new SimpleDateFormat("MMMM", getDateTimeLocale());
        Date intendedDate = new GregorianCalendar(DEFAULT_YEAR, month, DEFAULT_DATE).getTime();
        return formatter.format(intendedDate);
    }

    /**
     * Calculates the maximum number of
     * days in the given month of year
     *
     * @return
     */
    public int getDaysCount(int month, int year) {

        Calendar calendar = new GregorianCalendar(year, month, DEFAULT_DATE);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * Method to convert the UTC date time to Local
     *
     * @return
     */
    public String convertToLocal(String dateTime) {

        return convertToLocal(dateTime, Constants.DateFormat.STANDARD_DATE_FORMAT_TZ);
    }

    public String convertToLocal(String dateTime, String format) {

        Log.e("UTC Date", dateTime + "");

        DateFormat utcFormat = new SimpleDateFormat(format, getDateTimeLocale());
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date;
        try {
            date = utcFormat.parse(dateTime);
        } catch (ParseException e) {
            date = new Date();
            Utils.printStackTrace(e);
        }


        DateFormat pstFormat = new SimpleDateFormat(UIManager.getDateTimeFormat(), getDateTimeLocale());
        pstFormat.setTimeZone(TimeZone.getDefault());

        String result = pstFormat.format(date);

        System.out.println(result);

        Log.e("Local Date", result + "");
        return result;
    }

    public String convertToLocal(String dateTime, String inputFormat, String outputFormat) {

        Log.e("UTC Date", dateTime + "");

        DateFormat utcFormat = new SimpleDateFormat(inputFormat, getDateTimeLocale());
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date;
        try {
            date = utcFormat.parse(dateTime);
        } catch (ParseException e) {
            date = new Date();
            Utils.printStackTrace(e);
        }

        DateFormat pstFormat = new SimpleDateFormat(outputFormat, getDateTimeLocale());
        pstFormat.setTimeZone(TimeZone.getDefault());

        String result = pstFormat.format(date);

        System.out.println(result);

        Log.e("Local Date", result + "");
        return result;
    }


    /**
     * Method to convert the UTC date time to Local
     *
     * @return
     */
    public String convertToUTC(String dateTime) {

        Log.e("Local Date", dateTime + "");

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateFormat.STANDARD_DATE_FORMAT_TZ, getDateTimeLocale());
        Date localTime = null;
        try {
            localTime = sdf.parse(dateTime);
        } catch (ParseException e) {
            localTime = new Date();
            Utils.printStackTrace(e);
        }

        // Convert Local Time to UTC (Works Fine)
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String result = sdf.format(localTime);

        Log.e("Local Date", result + "");
        return result;
    }


    public String getTimeIn12Hours(int hourOfDay, int minute) {
        int hours = hourOfDay;
        int minutes = minute;
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String hr = "";
        if (hours < 10)
            hr = "0" + hours;
        else
            hr = String.valueOf(hours);

        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hr).append(':')
                .append(min).append(" ").append(timeSet).toString();
        return aTime;
    }

    public String getTimeIn24Hours(int hourOfDay, int minute) {


        String hr = "";
        if (hourOfDay < 10)
            hr = "0" + hourOfDay;
        else
            hr = String.valueOf(hourOfDay);

        String min = "";
        if (minute < 10)
            min = "0" + minute;
        else
            min = String.valueOf(minute);

        // Append in a StringBuilder
        return hr + ':' + min;
    }


    public String getRelativeTimeWithCurrentTime(Date time) {
        Date currentDate = Calendar.getInstance().getTime();
        long relativeDifference = (currentDate.getTime() - time.getTime()) / 1000;


        if (relativeDifference > 0) {
            if (relativeDifference < 60) {
                return relativeDifference + " seconds ago";
            } else if (relativeDifference < (60 * 60)) {
                return (relativeDifference / 60) + " minutes ago";
            } else if (relativeDifference < (60 * 60 * 24)) {
                return (relativeDifference / (60 * 60)) + " hours ago";
            } else {
                return (relativeDifference / (60 * 60 * 24)) + " days ago";
            }

        } else {
            return "moments ago";
        }

    }

    /**
     * @param date date to be converted
     * @return
     */
    public Calendar convertStringToCalender(String date) {
//        String mDate = parseDateAs(date, Constants.DateFormat.ONLY_DATE, Constants.DateFormat.STANDARD_DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateFormat.ONLY_DATE, getDateTimeLocale());
        try {
            cal.setTime(sdf.parse(date));// all done
        } catch (ParseException e) {
            Utils.printStackTrace(e);
        }
        Log.e("calValue<><><>", cal.toString());
        return cal;
    }

    /**
     * @param date date to be formatted
     * @return
     */
    public String dateFormatter(int date) {
        String format;
        if (date < 10) {
            format = "0" + String.valueOf(date);
        } else {
            format = String.valueOf(date);
        }
        return format;
    }

    public String convertDateObjectToUtc(Date inputDate) throws ParseException {
        java.text.DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", getDateTimeLocale());
        pstFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return pstFormat.format(inputDate);

    }

    public String getCurrentDateTimeUtc() {
        Calendar calendar = Calendar.getInstance();
        Date dateTime = calendar.getTime();
        java.text.DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", getDateTimeLocale());
        pstFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return pstFormat.format(dateTime);

    }


    public void openDatePicker(AppCompatActivity activity,
                               final Date date,
                               final DateListners.OnDateSelectedListener dateSelectedListener) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
//                calendar.set(Calendar.HOUR_OF_DAY, 0);
//                calendar.set(Calendar.MINUTE, 0);
//                calendar.set(Calendar.SECOND, 0);
                Date date = calendar.getTime();

                Log.e("DAY_OF_MONTH", day + "");
                Log.e("date", date.toString());

                if (dateSelectedListener != null)
                    dateSelectedListener.onDateSelected(date);
            }
        });
        datePickerFragment.setMinDate(date.getTime());
        datePickerFragment.show(activity.getSupportFragmentManager(), "date picker");
    }

    public void openTimePicker(AppCompatActivity activity,
                               final Date date, final DateListners.OnTimeSelectedListener timeSelectedListener) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (sdf.format(new Date()).equals(sdf.format(date))) {
            timePickerFragment.setHour(cal.get(Calendar.HOUR_OF_DAY));
            timePickerFragment.setMinute(cal.get(Calendar.MINUTE));
        }
        timePickerFragment.setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                Log.e("hour", hour + "");
                calendar.set(Calendar.MINUTE, min);
                Log.e("MINUTE", min + "");

                if (timeSelectedListener != null)
                    timeSelectedListener.onTimeSelected(calendar.getTime());
            }
        });

        timePickerFragment.show(activity.getSupportFragmentManager(), "time picker");
    }

    public String displayDateInCustomFormat(Date inputDate, String outputFormat) throws ParseException {
        java.text.DateFormat pstFormat = new SimpleDateFormat(outputFormat, getDateTimeLocale());
        pstFormat.setTimeZone(TimeZone.getDefault());
        return pstFormat.format(inputDate);

    }

    public DateFormatSymbols getDateFormatSymbols() {
        return new DateFormatSymbols(getDateTimeLocale());
    }

    private Locale getDateTimeLocale() {
        if (Locale.getDefault().getLanguage().equals("ar")
                || Locale.getDefault().getLanguage().equals("bn")
                || Locale.getDefault().getLanguage().equals("es")
                || Locale.getDefault().getLanguage().equals("mr")) {
            return Locale.ENGLISH;
        } else {
            return Locale.getDefault();
        }
    }

    /**
     * Method to calculate all the dates in month
     * of given year
     *
     * @param month
     * @param year
     * @return
     */
    public ArrayList<DateItem> getAllDatesInMonth(int month, int year) {

        ArrayList<DateItem> datesInMonth = new ArrayList<>();

        int numberOfDaysInMonth = getDaysCount(month, year);

        for (int day = 1; day <= numberOfDaysInMonth; day++)
            datesInMonth.add(DateItem.create(day, month, year));

        return datesInMonth;
    }
}
