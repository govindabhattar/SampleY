package com.tookancustomer.models.appConfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomOrderPage implements Serializable {
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("page_id")
    @Expose
    private int pageId;
    @SerializedName("is_active")
    @Expose
    private int isActive;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("route")
    @Expose
    private String route;

    public int getUserId() {
        return userId;
    }

    public int getPageId() {
        return pageId;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public String getRoute() {
        return route;
    }
}
