package com.tookancustomer.filter.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AllowedDataList implements Parcelable {

    private String value;
    private boolean isSelected;

    public AllowedDataList(String value, boolean isSelected) {
        this.value = value;
        this.isSelected = isSelected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.value);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected AllowedDataList(Parcel in) {
        this.value = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<AllowedDataList> CREATOR = new Parcelable.Creator<AllowedDataList>() {
        @Override
        public AllowedDataList createFromParcel(Parcel source) {
            return new AllowedDataList(source);
        }

        @Override
        public AllowedDataList[] newArray(int size) {
            return new AllowedDataList[size];
        }
    };
}
