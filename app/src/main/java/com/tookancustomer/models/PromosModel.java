package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cl-macmini-83 on 10/10/16.
 */

public class PromosModel implements Serializable {
    public boolean isSelected;

    @SerializedName("id")
    @Expose
    private Integer promoId;
    @SerializedName(value = "code", alternate = "promo_code")
    @Expose
    private String promoCode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("isPromo")
    @Expose
    private Integer isPromo;
    @SerializedName("promo_mode")
    @Expose
    private int promoMode = 0;
    @SerializedName("promo_on")
    @Expose
    private int promoOn = 0;

    public PromosModel(Integer promoId, String promoCode, String description, Integer isPromo, boolean isSelected) {
        this.promoId = promoId;
        this.promoCode = promoCode;
        this.description = description;
        this.isPromo = isPromo;
        this.isSelected = isSelected;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getDescription() {
        return description != null && !description.isEmpty() ? description : promoCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsPromo() {
        return isPromo;
    }

    public void setIsPromo(Integer isPromo) {
        this.isPromo = isPromo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPromoMode() {
        return promoMode;
    }

    public void setPromoMode(int promoMode) {
        this.promoMode = promoMode;
    }

    public int getPromoOn() {
        return promoOn;
    }

    public void setPromoOn(int promoOn) {
        this.promoOn = promoOn;
    }
}