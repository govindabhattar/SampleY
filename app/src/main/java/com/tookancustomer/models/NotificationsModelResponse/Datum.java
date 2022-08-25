
package com.tookancustomer.models.NotificationsModelResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("app_notif_id")
    @Expose
    private Integer appNotifId;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("is_read")
    @Expose
    private Integer isRead;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("push_type")
    @Expose
    private Integer pushType;

    public Integer getAppNotifId() {
        return appNotifId;
    }

    public void setAppNotifId(Integer appNotifId) {
        this.appNotifId = appNotifId;
    }

    public Integer getJobId() {
        return jobId != null ? jobId : 0;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

}
