
package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.CASH;

public class PaymentMethod implements Serializable {


    @SerializedName("lock_enabled")
    @Expose
    public int lockEnabled;


    @SerializedName("enabled")
    @Expose
    public int enabled;
    @SerializedName("value")
    @Expose
    public long value;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("label")
    @Expose
    public String label;


    @SerializedName("payment_process_type")
    @Expose
    public int paymentProcessType = 0;


    @SerializedName("payment_mode")
    @Expose
    public int paymentMode;

    public int getPaymentMode() {
        return paymentMode;

        //1 for add card
        //0 for webview
    }

    public int getPaymentProcessType() {

        //0 pre
        //1 post
        //2 automatic

        return paymentProcessType;
    }

    public void setPaymentProcessType(int paymentProcessType) {
        this.paymentProcessType = paymentProcessType;
    }

    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getLabel() {
        if (value == CASH.intValue && (label == null || label.isEmpty()))
            return "Cash";
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public long getValue() {
        return value;
    }

    public String getName() {
        if (value == CASH.intValue && (label == null || label.isEmpty()))
            return "Cash";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getLockEnabled() {
        return lockEnabled;
    }

    public void setLockEnabled(int lockEnabled) {
        this.lockEnabled = lockEnabled;
    }

}
