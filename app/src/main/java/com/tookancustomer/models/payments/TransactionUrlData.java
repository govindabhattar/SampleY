package com.tookancustomer.models.payments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.BILLPLZ;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.INSTAPAY;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFAST;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFORT;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYPAL;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYU_LATAM;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.RAZORPAY;

public class TransactionUrlData {

    @SerializedName("paymentUrl")
    @Expose
    private String paymentUrl;  /* Url is used for FAC */
    @SerializedName("url")
    @Expose
    private String url;  /* Url is used for INSTAPAY, BILLPLZ, RAZORPAY */
    @SerializedName("transaction_url")
    @Expose
    private String transactionUrl;  /* Transaction url is used for PAYFAST, PAYPAL */
    @SerializedName("3ds_url")
    @Expose
    private String _3dsUrl;  /* _3dsUrl is used for PAYFORT */

    public String getUrl(long paymentMethod) {
        if (paymentMethod == PAYFORT.intValue) return _3dsUrl;
       // else if (paymentMethod == FAC.intValue) return paymentUrl;
        else if (paymentMethod == INSTAPAY.intValue || paymentMethod == BILLPLZ.intValue || paymentMethod == RAZORPAY.intValue)
            return url;
        else if (paymentMethod == PAYFAST.intValue || paymentMethod == PAYPAL.intValue || paymentMethod == PAYU_LATAM.intValue)
            return transactionUrl;
        else return url;
    }
}