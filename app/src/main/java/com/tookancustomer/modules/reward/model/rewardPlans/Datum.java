
package com.tookancustomer.modules.reward.model.rewardPlans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("is_purchased")
    @Expose
    private int isPurchased;
    @SerializedName("reward_name")
    @Expose
    private String rewardName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("percentage_cashback")
    @Expose
    private String percentageCashback;
    @SerializedName("fixed_cashback")
    @Expose
    private String fixedCashback;
    @SerializedName("order_count")
    @Expose
    private int orderCount;
    @SerializedName("total_count")
    @Expose
    private int totalCount;
    @SerializedName("is_expired")
    @Expose
    private int isExpired;
    @SerializedName("plan_fees")
    @Expose
    private String planFees;
    @SerializedName("valid_upto")
    @Expose
    private String validUpto;
    @SerializedName("plan_upto")
    @Expose
    private String planUpto;

    public int getId() {
        return id;
    }

    public String getRewardName() {
        return rewardName;
    }

    public String getDescription() {
        return description;
    }

    public String getPercentageCashback() {
        return percentageCashback;

    }

    public String getFixedCashback() {
        return fixedCashback;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public String getPlanFees() {
        return planFees;
    }

    public String getValidUpto() {
        return validUpto;
    }

    public String getPlanUpto() {
        return planUpto;
    }

    public boolean getIsPurchased() {
        return isPurchased == 1;
    }

    public void setIsPurchased(boolean bool) {
        if (bool) {
            this.isPurchased = 1;
        } else {
            this.isPurchased = 0;
        }

    }

    public int getTotalCount() {
        return totalCount;
    }

    public boolean getIsExpired() {
        return isExpired == 1;
    }
}
