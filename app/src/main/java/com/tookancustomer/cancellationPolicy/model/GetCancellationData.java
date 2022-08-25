package com.tookancustomer.cancellationPolicy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCancellationData {
    @SerializedName("cancellationCharge")
    @Expose
    private double cancellationCharges;
    @SerializedName("refundAmount")
    @Expose
    private double refundAmount;
    @SerializedName("loyaltyPoint")
    @Expose
    private int loyaltyPoint;
    @SerializedName("refundLoyaltyPoint")
    @Expose
    private int refundedLoyaltyPoint;

    public GetCancellationData(double cancellationCharges, double refundAmount, int refundedLoyaltyPoint) {
        this.cancellationCharges = cancellationCharges;
        this.refundAmount = refundAmount;
        this.refundedLoyaltyPoint = refundedLoyaltyPoint;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public int getLoyaltyPoint() {
        return loyaltyPoint;
    }

    public int getRefundedLoyaltyPoint() {
        return refundedLoyaltyPoint;
    }

    public double getCancellationCharges() {
        return cancellationCharges;
    }
}
