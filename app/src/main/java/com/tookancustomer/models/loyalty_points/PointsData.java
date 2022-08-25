
package com.tookancustomer.models.loyalty_points;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PointsData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("marketplace_user_id")
    @Expose
    private Integer marketplaceUserId;
    @SerializedName("rate_criteria")
    @Expose
    private Integer rateCriteria;
    @SerializedName("rate_point")
    @Expose
    private Integer ratePoint;
    @SerializedName("max_usable")
    @Expose
    private Integer maxUsable;
    @SerializedName("min_amount")
    @Expose
    private Integer minAmount;
    @SerializedName("expiry_limit")
    @Expose
    private Integer expiryLimit;
    @SerializedName("value_criteria")
    @Expose
    private Integer valueCriteria;
    @SerializedName("value_point")
    @Expose
    private Integer valuePoint;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("max_earning")
    @Expose
    private Integer maxEarning;
    @SerializedName("min_earning_criteria")
    @Expose
    private Integer minEarningCriteria;

    @SerializedName("loyalty_point_history")
    @Expose
    private ArrayList<LoyaltyPointHistory> loyaltyPointHistory = null;

    public ArrayList<LoyaltyPointHistory> getLoyaltyPointHistory() {
        return loyaltyPointHistory;
    }

    public void setLoyaltyPointHistory(ArrayList<LoyaltyPointHistory> loyaltyPointHistory) {
        this.loyaltyPointHistory = loyaltyPointHistory;
    }

    public Integer getMinEarningCriteria() {
        return minEarningCriteria;
    }

    public void setMinEarningCriteria(Integer minEarningCriteria) {
        this.minEarningCriteria = minEarningCriteria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(Integer marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public Integer getRateCriteria() {
        return rateCriteria;
    }

    public void setRateCriteria(Integer rateCriteria) {
        this.rateCriteria = rateCriteria;
    }

    public Integer getRatePoint() {
        return ratePoint;
    }

    public void setRatePoint(Integer ratePoint) {
        this.ratePoint = ratePoint;
    }

    public Integer getMaxUsable() {
        return maxUsable;
    }

    public void setMaxUsable(Integer maxUsable) {
        this.maxUsable = maxUsable;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getExpiryLimit() {
        return expiryLimit;
    }

    public void setExpiryLimit(Integer expiryLimit) {
        this.expiryLimit = expiryLimit;
    }

    public Integer getValueCriteria() {
        return valueCriteria;
    }

    public void setValueCriteria(Integer valueCriteria) {
        this.valueCriteria = valueCriteria;
    }

    public Integer getValuePoint() {
        return valuePoint;
    }

    public void setValuePoint(Integer valuePoint) {
        this.valuePoint = valuePoint;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getMaxEarning() {
        return maxEarning;
    }

    public void setMaxEarning(Integer maxEarning) {
        this.maxEarning = maxEarning;
    }
}
