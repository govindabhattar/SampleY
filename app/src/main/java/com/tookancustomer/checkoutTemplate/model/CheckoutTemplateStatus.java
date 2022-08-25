package com.tookancustomer.checkoutTemplate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutTemplateStatus {

    @SerializedName("is_checkout_template_enabled")
    @Expose
    private int isCheckoutTemplateEnabled;

    public int getIsCheckoutTemplateEnabled() {
        return isCheckoutTemplateEnabled;
    }

    public void setIsCheckoutTemplateEnabled(int isCheckoutTemplateEnabled) {
        this.isCheckoutTemplateEnabled = isCheckoutTemplateEnabled;
    }
}
