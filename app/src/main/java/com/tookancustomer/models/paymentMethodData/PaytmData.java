
package com.tookancustomer.models.paymentMethodData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaytmData implements Parcelable {

    @SerializedName("wallet_balance")
    @Expose
    private Double walletBalance = 0.0;
    @SerializedName("paytm_verified")
    @Expose
    private Integer paytmVerified;
    @SerializedName("paytm_add_money_url")
    @Expose
    private String paytmAddMoneyUrl="";

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public Integer getPaytmVerified() {
        return paytmVerified;
    }

    public void setPaytmVerified(Integer paytmVerified) {
        this.paytmVerified = paytmVerified;
    }

    public PaytmData() {
    }

    public String getPaytmAddMoneyUrl() {
        return paytmAddMoneyUrl;
    }

    public void setPaytmAddMoneyUrl(String paytmAddMoneyUrl) {
        this.paytmAddMoneyUrl = paytmAddMoneyUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.walletBalance);
        dest.writeValue(this.paytmVerified);
        dest.writeString(this.paytmAddMoneyUrl);
    }

    protected PaytmData(Parcel in) {
        this.walletBalance = (Double) in.readValue(Double.class.getClassLoader());
        this.paytmVerified = (Integer) in.readValue(Integer.class.getClassLoader());
        this.paytmAddMoneyUrl = in.readString();
    }

    public static final Creator<PaytmData> CREATOR = new Creator<PaytmData>() {
        @Override
        public PaytmData createFromParcel(Parcel source) {
            return new PaytmData(source);
        }

        @Override
        public PaytmData[] newArray(int size) {
            return new PaytmData[size];
        }
    };
}
