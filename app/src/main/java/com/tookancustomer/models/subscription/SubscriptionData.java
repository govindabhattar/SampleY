package com.tookancustomer.models.subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ashutosh Ojha on 2/15/19.
 */
public class SubscriptionData {



    @SerializedName("marketplace_user_id")
    @Expose
    private String marketplaceUserId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("active_days")
    @Expose
    private List<ActiveDay> activeDays = null;
    @SerializedName("slots_array")
    @Expose
    private List<String> slotsArray = null;
    @SerializedName("slot_interval")
    @Expose
    private Integer slotInterval;

    public String getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(String marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ActiveDay> getActiveDays() {
        return activeDays;
    }

    public void setActiveDays(List<ActiveDay> activeDays) {
        this.activeDays = activeDays;
    }

    public List<String> getSlotsArray() {
        return slotsArray;
    }

    public void setSlotsArray(List<String> slotsArray) {
        this.slotsArray = slotsArray;
    }

    public Integer getSlotInterval() {
        return slotInterval;
    }

    public void setSlotInterval(Integer slotInterval) {
        this.slotInterval = slotInterval;
    }


}
