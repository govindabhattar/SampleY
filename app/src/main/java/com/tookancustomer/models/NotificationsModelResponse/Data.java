package com.tookancustomer.models.NotificationsModelResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Data {
    @SerializedName("count")
    private Integer count;
    @SerializedName("unread_count")
    private Integer unreadCount;
    @SerializedName("notifications")
    @Expose
    private List<Datum> notifications = new ArrayList<>();


    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public List<Datum> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Datum> notifications) {
        this.notifications = notifications;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
