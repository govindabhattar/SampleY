package com.tookancustomer.models.billbreakdown;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StripePayment implements Serializable {

    @SerializedName("charge_type")
    @Expose
    private int chargeType;
    @SerializedName("transaction_charges")
    @Expose
    private double transactionCharges;
    @SerializedName("total_charges")
    @Expose
    private double totalCharges;


    @SerializedName("PAYLATER_TOTAL_RECURRING_AMOUNT")
    @Expose
    private double payLaterRecuuringCharges;


    public double getPayLaterRecuuringCharges() {
        return payLaterRecuuringCharges;
    }



    public int getChargeType() {
        return chargeType;
    }


    public double getTransactionCharges() {
        return transactionCharges;
    }


    public double getTotalCharges() {
        return totalCharges;
    }

}
