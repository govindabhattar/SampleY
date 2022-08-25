
package com.tookancustomer.models.createBookingData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

public class CreateBookingResponse extends BaseModel implements Parcelable {


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

    public CreateBookingResponse() {
    }

    protected CreateBookingResponse(Parcel in) {
        this.data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Parcelable.Creator<CreateBookingResponse> CREATOR = new Parcelable.Creator<CreateBookingResponse>() {
        @Override
        public CreateBookingResponse createFromParcel(Parcel source) {
            return new CreateBookingResponse(source);
        }

        @Override
        public CreateBookingResponse[] newArray(int size) {
            return new CreateBookingResponse[size];
        }
    };
}
