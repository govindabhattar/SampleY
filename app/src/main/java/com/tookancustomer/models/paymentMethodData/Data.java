package com.tookancustomer.models.paymentMethodData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cl-macmini-125 on 07/12/17.
 */

public class Data implements Parcelable {

    @SerializedName("paytm")
    @Expose
    private PaytmData paytm;
    @SerializedName("add_card_link")
    @Expose
    private String add_card_link = "";
    @SerializedName("cards")
    @Expose
    private List<Datum> data = new ArrayList<>();

    public String getAdd_card_link() {
        return add_card_link;
    }

    public void setAdd_card_link(String add_card_link) {
        this.add_card_link = add_card_link;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public PaytmData getPaytm() {
        return paytm;
    }

    public void setPaytm(PaytmData paytm) {
        this.paytm = paytm;
    }

    public Data() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.paytm, flags);
        dest.writeString(this.add_card_link);
        dest.writeTypedList(this.data);
    }

    protected Data(Parcel in) {
        this.paytm = in.readParcelable(PaytmData.class.getClassLoader());
        this.add_card_link = in.readString();
        this.data = in.createTypedArrayList(Datum.CREATOR);
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
