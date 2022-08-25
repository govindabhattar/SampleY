package com.tookancustomer.modules.recurring.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Developer: Rishabh
 * Dated: 07/08/15.
 */
public class DateInfo implements Parcelable {

    private DateItem dateItem;
//    private DateTasks dateTasks;
    private boolean isExtra;
    private boolean isSelected;
    private boolean isActive;

    public DateInfo() {
    }

    public DateItem getDateItem() {
        return dateItem;
    }

    public void setDateItem(DateItem dateItem) {
        this.dateItem = dateItem;
    }

//    public DateTasks getDateTasks() {
//        return dateTasks;
//    }
//
//    public void setDateTasks(DateTasks dateTasks) {
//        this.dateTasks = dateTasks;
//    }
//
//    @Override
//    public String toString() {
//        return dateItem + ": " + dateTasks;
//    }

    public boolean isExtra() {
        return isExtra;
    }

    public void setIsExtra(boolean isExtra) {
        this.isExtra = isExtra;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean active) {
        isActive = active;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(final boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.dateItem, flags);
//        dest.writeParcelable(this.dateTasks, flags);
        dest.writeByte(this.isExtra ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
    }

    protected DateInfo(Parcel in) {
        this.dateItem = in.readParcelable(DateItem.class.getClassLoader());
//        this.dateTasks = in.readParcelable(DateTasks.class.getClassLoader());
        this.isExtra = in.readByte() != 0;
        this.isSelected = in.readByte() != 0;
        this.isActive = in.readByte() != 0;
    }

    public static final Creator<DateInfo> CREATOR = new Creator<DateInfo>() {
        @Override
        public DateInfo createFromParcel(Parcel source) {
            return new DateInfo(source);
        }

        @Override
        public DateInfo[] newArray(int size) {
            return new DateInfo[size];
        }
    };
}
