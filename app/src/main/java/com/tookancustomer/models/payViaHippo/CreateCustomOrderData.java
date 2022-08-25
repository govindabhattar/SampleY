package com.tookancustomer.models.payViaHippo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCustomOrderData {

    @SerializedName("channel_id")
    @Expose
    private String channelId;
    @SerializedName("job_id")
    @Expose
    private String jobId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
