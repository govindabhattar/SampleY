
package com.tookancustomer.models.paymentMethodData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

public class PaymentMethods extends BaseModel implements Parcelable {

    @SerializedName("data")
    @Expose
    private Data data;

    public PaymentMethods() {
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
    }

    protected PaymentMethods(Parcel in) {
        this.data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Parcelable.Creator<PaymentMethods> CREATOR = new Parcelable.Creator<PaymentMethods>() {
        @Override
        public PaymentMethods createFromParcel(Parcel source) {
            return new PaymentMethods(source);
        }

        @Override
        public PaymentMethods[] newArray(int size) {
            return new PaymentMethods[size];
        }
    };
}
