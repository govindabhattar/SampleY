package com.tookancustomer.models.staticAddressData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaticAddressData implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("flat_no")
    @Expose
    private String flatNo;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("is_active")
    @Expose
    private int isActive;
    @SerializedName("is_delete")
    @Expose
    private int isDelete;
    @SerializedName("updation_datetime")
    @Expose
    private String updationDatetime;
    @SerializedName("creatiion_datetime")
    @Expose
    private String creatiionDatetime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getUpdationDatetime() {
        return updationDatetime;
    }

    public void setUpdationDatetime(String updationDatetime) {
        this.updationDatetime = updationDatetime;
    }

    public String getCreatiionDatetime() {
        return creatiionDatetime;
    }

    public void setCreatiionDatetime(String creatiionDatetime) {
        this.creatiionDatetime = creatiionDatetime;
    }

    public String getFullAddress() {
        String fullAddress = "";
        if (flatNo != null && !flatNo.isEmpty()) {
            fullAddress += flatNo + ",";
        }
        fullAddress += address;

        if (landmark != null && !landmark.isEmpty()) {
            fullAddress += "," + landmark;
        }
        return fullAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeString(this.label);
        dest.writeString(this.address);
        dest.writeString(this.flatNo);
        dest.writeString(this.landmark);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.isActive);
        dest.writeInt(this.isDelete);
        dest.writeString(this.updationDatetime);
        dest.writeString(this.creatiionDatetime);
    }

    public StaticAddressData() {
    }

    protected StaticAddressData(Parcel in) {
        this.id = in.readInt();
        this.userId = in.readInt();
        this.label = in.readString();
        this.address = in.readString();
        this.flatNo = in.readString();
        this.landmark = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.isActive = in.readInt();
        this.isDelete = in.readInt();
        this.updationDatetime = in.readString();
        this.creatiionDatetime = in.readString();
    }

    public static final Parcelable.Creator<StaticAddressData> CREATOR = new Parcelable.Creator<StaticAddressData>() {
        @Override
        public StaticAddressData createFromParcel(Parcel source) {
            return new StaticAddressData(source);
        }

        @Override
        public StaticAddressData[] newArray(int size) {
            return new StaticAddressData[size];
        }
    };
}
