package com.tookancustomer.modules.reward.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateChargeData {
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("debt_amount")
    @Expose
    private double debtAmount;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getDebtAmount() {
        return debtAmount;
    }
}
