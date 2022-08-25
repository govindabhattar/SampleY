package com.tookancustomer.models.userdata;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.StorefrontCommonData;

import java.io.Serializable;


public class ReferralShareData implements Serializable {
    @SerializedName("smarturl_enabled")
    @Expose
    private int smarturlEnabled;
    @SerializedName("smart_url")
    @Expose
    private String smartUrl;
    @SerializedName("status")
    @Expose
    private Integer status = 0;
    @SerializedName("receiver_description")
    @Expose
    private String receiverDescription = "";
    @SerializedName("sender_description")
    @Expose
    private String senderDescription;

    public int getSmarturlEnabled() {
        return smarturlEnabled;
    }

    public void setSmarturlEnabled(int smarturlEnabled) {
        this.smarturlEnabled = smarturlEnabled;
    }

    public String getSmartUrl() {
        return smartUrl;
    }

    public void setSmartUrl(String smartUrl) {
        this.smartUrl = smartUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReceiverDescription() {
        return receiverDescription != null ? receiverDescription.replace("<referral>", StorefrontCommonData.getUserData().getData().getVendorDetails().getReferralCode()) : "";
    }

    public void setReceiverDescription(String receiverDescription) {
        this.receiverDescription = receiverDescription;
    }

    public String getSenderDescription() {
        return senderDescription != null ? senderDescription.replace("<referral>", StorefrontCommonData.getUserData().getData().getVendorDetails().getReferralCode()) : "";
    }

    public void setSenderDescription(String senderDescription) {
        this.senderDescription = senderDescription;
    }
}
