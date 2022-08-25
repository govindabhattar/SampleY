package com.tookancustomer.models.alltaskdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderHistory implements Serializable {

    @SerializedName("job_id")
    @Expose
    private Integer jobId;

    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("statusCompleted")
    @Expose
    private Integer statusCompleted;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getStatusCompleted() {
        return statusCompleted;
    }

    public void setStatusCompleted(Integer statusCompleted) {
        this.statusCompleted = statusCompleted;
    }


}

