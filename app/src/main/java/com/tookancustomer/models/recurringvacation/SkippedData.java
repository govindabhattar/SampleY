package com.tookancustomer.models.recurringvacation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ashutosh Ojha on 2/25/19.
 */
public class SkippedData {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("rule_id")
    @Expose
    private Integer ruleId;
    @SerializedName("skip_dates")
    @Expose
    private List<String> skipDates = null;
    @SerializedName("vendor_id")
    @Expose
    private Integer vendorId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("marketplace_user_id")
    @Expose
    private Integer marketplaceUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public List<String> getSkipDates() {
        return skipDates;
    }

    public void setSkipDates(List<String> skipDates) {
        this.skipDates = skipDates;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(Integer marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

}
