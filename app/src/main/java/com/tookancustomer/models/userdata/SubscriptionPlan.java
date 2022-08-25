
package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;

public class SubscriptionPlan implements Serializable {

    @SerializedName("plan_name")
    @Expose
    private String planName;
    @SerializedName("plan_amount")
    @Expose
    private double planAmount;
    @SerializedName("plan_type")
    @Expose
    private String planType;
    @SerializedName("plan_description")
    @Expose
    private String planDescription;
    @SerializedName("paid")
    @Expose
    private int paid;

    public SubscriptionPlan(double planAmount, int paid) {
        this.planAmount = planAmount;
        this.paid = paid;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanAmount() {
        return Utils.getDoubleTwoDigits(planAmount);
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }
}
