package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class LoyaltyPoints implements Serializable{

    @SerializedName("loyalty_points_redeem")
    @Expose
    private int pointsRedeemed;
    @SerializedName("discount_amount")
    @Expose
    private BigDecimal discountAmount;
    @SerializedName("loyalty_points_earned")
    @Expose
    private int loyaltyPointsEarned;
    @SerializedName("expiry_date")
    @Expose
    private String  expiryDate;

    public int getPointsRedeemed() {
        return pointsRedeemed;
    }

    public void setPointsRedeemed(int pointsRedeemed) {
        this.pointsRedeemed = pointsRedeemed;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getLoyaltyPointsEarned() {
        return loyaltyPointsEarned;
    }

    public void setLoyaltyPointsEarned(int loyaltyPointsEarned) {
        this.loyaltyPointsEarned = loyaltyPointsEarned;
    }

    public String  getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
