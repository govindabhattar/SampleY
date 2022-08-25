package com.tookancustomer.models.billbreakdown;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionalChangesInfo implements Serializable {
    @SerializedName("STRIPE")
    @Expose
    private StripePayment stripe;

    @SerializedName("PAYLATER")
    @Expose
    private StripePayment payLater;



    @SerializedName("MPAISA")
    @Expose
    private Mpaisa mPAISA;


    public StripePayment getPayLater() {
        return payLater;
    }


    public Mpaisa getmPAISA() {
        return mPAISA;
    }

    public void setmPAISA(Mpaisa mPAISA) {
        this.mPAISA = mPAISA;
    }

    public StripePayment getStripe() {
        return stripe;
    }

    public void setStripe(StripePayment stripe) {
        this.stripe = stripe;
    }


}
