
package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.PromosModel;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendorPromos implements Serializable {

    @SerializedName("NET_PAYABLE_AMOUNT")
    @Expose
    public BigDecimal netPayableAmount;
    @SerializedName("DISCOUNT")
    @Expose
    public BigDecimal discount;
    @SerializedName("PROMO_ID")
    @Expose
    private Integer promoId;
    @SerializedName("REFERRAL")
    @Expose
    private PromosModel referrral;

    public PromosModel getReferrral() {
        return referrral;
    }

    public void setReferrral(PromosModel referrral) {
        this.referrral = referrral;
    }

    public String getNetPayableAmount() {
        return netPayableAmount != null ? Utils.getDoubleTwoDigits(netPayableAmount) : "0";
    }

    public String getDiscount() {
        return discount != null ? Utils.getDoubleTwoDigits(discount) : "0";
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }


    @SerializedName("promos")
    @Expose
    private List<Promos> promos = new ArrayList<>();
    @SerializedName("coupons")
    @Expose
    private List<Promos> coupons = new ArrayList<>();

    public List<Promos> getPromos() {
        return promos;
    }

    public void setPromos(List<Promos> promos) {
        this.promos = promos;
    }

    public List<Promos> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Promos> coupons) {
        this.coupons = coupons;
    }

}
