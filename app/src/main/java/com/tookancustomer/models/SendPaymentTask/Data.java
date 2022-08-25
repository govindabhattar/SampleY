
package com.tookancustomer.models.SendPaymentTask;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    @SerializedName("per_task_cost")
    @Expose
    private String perTaskCost="0";
    @SerializedName("fare_upper_limit")
    @Expose
    private String fareUpperLimit;
    @SerializedName("fare_lower_limit")
    @Expose
    private String fareLowerLimit;
    @SerializedName("currency")
    @Expose
    private Currency currency;


    @SerializedName("unit")
    @Expose
    private Number unit = 1;
    @SerializedName("unit_count")
    @Expose
    private Number unitCount = 1;
    @SerializedName("unit_type")
    @Expose
    private Integer unitType = 1;
    @SerializedName("business_type")
    @Expose
    private Integer businessType = 1;


    public Number getUnit() {
        return unit;
    }

    public void setUnit(Number unit) {
        this.unit = unit;
    }

    public Number getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(Number unitCount) {
        this.unitCount = unitCount;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getFareUpperLimit() {
        return fareUpperLimit;
    }

    public void setFareUpperLimit(String fareUpperLimit) {
        this.fareUpperLimit = fareUpperLimit;
    }

    public String getFareLowerLimit() {
        return fareLowerLimit;
    }

    public void setFareLowerLimit(String fareLowerLimit) {
        this.fareLowerLimit = fareLowerLimit;
    }

    public static Creator<Data> getCREATOR() {
        return CREATOR;
    }

    public String getPerTaskCost() {
        return perTaskCost;
    }

    public void setPerTaskCost(String perTaskCost) {
        this.perTaskCost = perTaskCost;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Data() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.perTaskCost);
        dest.writeString(this.fareUpperLimit);
        dest.writeString(this.fareLowerLimit);
        dest.writeParcelable(this.currency, flags);
        dest.writeSerializable(this.unit);
        dest.writeSerializable(this.unitCount);
        dest.writeValue(this.unitType);
        dest.writeValue(this.businessType);
    }

    protected Data(Parcel in) {
        this.perTaskCost = in.readString();
        this.fareUpperLimit = in.readString();
        this.fareLowerLimit = in.readString();
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.unit = (Number) in.readSerializable();
        this.unitCount = (Number) in.readSerializable();
        this.unitType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.businessType = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}
