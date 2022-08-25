package com.tookancustomer.modules.recurring.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Class to hold information about the Date
 * <p/>
 * Developer: Rishabh
 * Dated: 17/07/15.
 */
public class DateItem implements Parcelable {

    private int day;
    private int month;
    private int year;

    private String dayName;
    private String monthName;
    private Date date;


    /**
     * Default constructor for new creation
     */
    public DateItem() {
        // does nothing
    }

    /**
     * Method to create a DateItem
     *
     * @param day
     * @param month
     * @param year
     * @return
     */
    public static DateItem create(int day, int month, int year) {

        DateItem item = new DateItem();

        item.day = day;
        item.month = month;
        item.year = year;


        SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMMM");
        Date date = new GregorianCalendar(year, month, day).getTime();

        String[] names = formatter.format(date).split(" ");

        item.dayName = names[0];    //  DateUtils.getInstance().getDayName(day, month, year);
        item.monthName = names[1];  //  DateUtils.getInstance().getMonthName(month);

        item.date = date;

        return item;
    }

    /**
     * Method to create a DateItem
     *
     * @param day
     * @return
     */
    public static DateItem create(int day) {

        DateItem item = new DateItem();
        item.day = day;
        return item;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {

        return day + "(" + dayName + ")" + ", " + (month + 1) + "(" + monthName + ")" + ", " + year;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.day);
        dest.writeInt(this.month);
        dest.writeInt(this.year);
        dest.writeString(this.dayName);
        dest.writeString(this.monthName);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }

    protected DateItem(Parcel in) {
        this.day = in.readInt();
        this.month = in.readInt();
        this.year = in.readInt();
        this.dayName = in.readString();
        this.monthName = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<DateItem> CREATOR = new Creator<DateItem>() {
        @Override
        public DateItem createFromParcel(Parcel source) {
            return new DateItem(source);
        }

        @Override
        public DateItem[] newArray(int size) {
            return new DateItem[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }
}
