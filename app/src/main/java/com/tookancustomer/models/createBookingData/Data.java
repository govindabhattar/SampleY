
package com.tookancustomer.models.createBookingData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("job_hash")
    @Expose
    private String jobHash;
    @SerializedName("job_pickup_name")
    @Expose
    private String jobPickupName;
    @SerializedName("job_pickup_address")
    @Expose
    private String jobPickupAddress;
    @SerializedName("job_token")
    @Expose
    private String jobToken;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobHash() {
        return jobHash;
    }

    public void setJobHash(String jobHash) {
        this.jobHash = jobHash;
    }

    public String getJobPickupName() {
        return jobPickupName;
    }

    public void setJobPickupName(String jobPickupName) {
        this.jobPickupName = jobPickupName;
    }

    public String getJobPickupAddress() {
        return jobPickupAddress;
    }

    public void setJobPickupAddress(String jobPickupAddress) {
        this.jobPickupAddress = jobPickupAddress;
    }

    public String getJobToken() {
        return jobToken;
    }

    public void setJobToken(String jobToken) {
        this.jobToken = jobToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.jobId);
        dest.writeString(this.jobHash);
        dest.writeString(this.jobPickupName);
        dest.writeString(this.jobPickupAddress);
        dest.writeString(this.jobToken);
    }

    public Data() {
    }

    protected Data(Parcel in) {
        this.jobId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jobHash = in.readString();
        this.jobPickupName = in.readString();
        this.jobPickupAddress = in.readString();
        this.jobToken = in.readString();
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
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
