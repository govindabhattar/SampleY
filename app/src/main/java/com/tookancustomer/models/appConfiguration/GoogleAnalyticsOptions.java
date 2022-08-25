package com.tookancustomer.models.appConfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GoogleAnalyticsOptions implements Serializable {

    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("action_name")
    @Expose
    private String actionName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("ga_master_option_id")
    @Expose
    private int gaMasterOptionId;
    @SerializedName("google_event_id")
    @Expose
    private int googleEventId;
    @SerializedName("is_active")
    @Expose
    private int isActive;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGaMasterOptionId() {
        return gaMasterOptionId;
    }

    public void setGaMasterOptionId(int gaMasterOptionId) {
        this.gaMasterOptionId = gaMasterOptionId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getGoogleEventId() {
        return googleEventId;
    }

    public void setGoogleEventId(int googleEventId) {
        this.googleEventId = googleEventId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
