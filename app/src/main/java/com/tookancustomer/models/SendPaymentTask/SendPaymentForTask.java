
package com.tookancustomer.models.SendPaymentTask;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

public class SendPaymentForTask extends BaseModel implements Parcelable {

    @SerializedName("data")
    @Expose
    private Data data;
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

    public SendPaymentForTask() {
    }

    protected SendPaymentForTask(Parcel in) {
        this.data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Parcelable.Creator<SendPaymentForTask> CREATOR = new Parcelable.Creator<SendPaymentForTask>() {
        @Override
        public SendPaymentForTask createFromParcel(Parcel source) {
            return new SendPaymentForTask(source);
        }

        @Override
        public SendPaymentForTask[] newArray(int size) {
            return new SendPaymentForTask[size];
        }
    };
}
